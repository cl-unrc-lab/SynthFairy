package lang;

import java.util.*;

import model.*;

import java.io.*;


/**
* This class defines a Faulty program, it provides the basic structures: a list of enumTypes,  
* a collection of global variables( internally separating in bool and int variables),
* a collection of channels( internally separating in bool and int channels),
* and a collection of all processes defined and invocated.
* @author Ceci
**/

public class Program extends ProgramNode{
    LinkedList<EnumType> enumTypes;
    GlobalVarCollection globalVars;
    ProcessCollection process;
    Main mainProgram;
    int maxEnumSize;
    String name;
    
    /**  GlobalVars
     * @param gVars: Collection of all global variables classified by their type.
     * @param channels: Collection of all global channels classified by their type.
     * @param process: Collection of all processes defined.
     * @param mainProgram: Collection of all process intances with their respective parameters.
     **/
    public Program(GlobalVarCollection gVars, ProcessCollection process, Main mainProgram){
        this.enumTypes = new LinkedList<EnumType>();
        this.globalVars = gVars;
        this.process = process;
        this.mainProgram = mainProgram;
        this.maxEnumSize = 0;
    }
    
    /** !EnumTypes & !GlobalVars & !Channels
     * @param process: Collection of all processes defined.
     * @param mainProgram: Collection of all process intances with their respective parameters.
     **/
    public Program( ProcessCollection process, Main mainProgram){
        this.enumTypes = new LinkedList<EnumType>();
        this.globalVars = new GlobalVarCollection();
        this.process = process;
        this.mainProgram = mainProgram;
    }
    
    
    /** EnumTypes + GlobalVars 
     * @param enumList: List of EnumTypes
     * @param gVars: Collection of all global variables classified by their type.
     * @param process: Collection of all processes defined.
     * @param mainProgram: Collection of all process intances with their respective parameters.
     **/
    public Program(LinkedList<EnumType> enumList, GlobalVarCollection gVars, ProcessCollection process, Main mainProgram){
        this.enumTypes = enumList;
        this.globalVars = gVars;
        this.process = process;
        this.mainProgram = mainProgram;
        
        this.maxEnumSize = 0;
        int currentEnumsize=0;
        for(int i=0; i<enumList.size();i++){
            currentEnumsize = enumList.get(i).getSize();
            if(currentEnumsize > this.maxEnumSize){
                this.maxEnumSize = currentEnumsize;
            }
        }
    }
    
    
    /** EnumTypes 
     * @param enumList: List of EnumTypes
     * @param process: Collection of all processes defined.
     * @param mainProgram: Collection of all process intances with their respective parameters.
     **/
    public Program(LinkedList<EnumType> enumList,ProcessCollection process, Main mainProgram){
        this.enumTypes = enumList;
        this.globalVars = new GlobalVarCollection();
        this.process = process;
        this.mainProgram = mainProgram;
        this.maxEnumSize = 0;
        int currentEnumsize=0;
        for(int i=0; i<enumList.size();i++){
            currentEnumsize = enumList.get(i).getSize();
            if(currentEnumsize > this.maxEnumSize){
                this.maxEnumSize = currentEnumsize;
            }
        }
    }
    
    public GlobalVarCollection getGlobalVars(){
        return this.globalVars;
    }
    
    public LinkedList<EnumType> getEnumList(){
        return this.enumTypes;
    }
    
    public int getMaxEnumSize(){
        return this.maxEnumSize;
    }
    
    public void accept(LangVisitor v){
         v.visit(this);         
    }

    
    public Model toGraph(boolean isSpec){
        Model m = new Model(globalVars, isSpec);

        //states in m are lists of states (from processes)
        //calculate initial state
        ModelState init = new ModelState(m);
        for (int j=0; j < mainProgram.getProcessDecl().size(); j++){
            ProcessDecl pDecl = mainProgram.getProcessDecl().get(j);
            for (int i = 0; i < process.getProcessList().size(); i++){
                Proc proc = process.getProcessList().get(i);
                if (pDecl.getType().equals(proc.getName())){
                    init.getModel().getProcs().add(proc);
                    init.getModel().getProcDecls().add(pDecl.getName());
                    init.evalInit(proc.getInitialCond(),j);
                }
            }
        }
        
        m.addNode(init);
        m.setInitial(init);

        TreeSet<ModelState> iterSet = new TreeSet<ModelState>();
        iterSet.add(m.getInitial());

        //build the whole model
        while(!iterSet.isEmpty()){
            ModelState curr = iterSet.pollFirst();
            for (int i = 0; i < m.getProcDecls().size(); i++){ // for each process in current global state
                for (Branch b : m.getProcs().get(i).getBranches()){
                    if (b.getIsTau())
                        m.setIsWeak(true);
                    if (curr.satisfies(b.getGuard(),i)){
                        //create global successor curr_
                        if (b.getIsProb()){ //its a probabilistic branch
                            //ProbabilisticAction act = new ProbabilisticAction(m.getProcDecls().get(i)+b.getLabel(),b.getIsFaulty(),b.getIsTau(),isSpec, null);
                            Action act = new Action(m.getProcDecls().get(i)+b.getLabel(),b.getIsFaulty(),b.getIsTau(),b.getReward(),isSpec);
                            act.setIsProbabilistic(true);
                            //System.out.println(act.toString() + b.getReward());
                            //LinkedList<Double> probs = new LinkedList<Double>();
                            for (Code c : b.getAssignList()){ // each prob. of a single action
                                ProbAssign pa = (ProbAssign)c;
                                ModelState curr_ = curr.createSuccessor(pa.getAssigns(),i);
                                ModelState toOld = m.search(curr_);
                                //probs.add(pa.getProbability());
                                //System.out.println(b.getLabel()+":"+pa.getProbability());
                                if (toOld == null){
                                    m.addNode(curr_);
                                    iterSet.add(curr_);

                                    m.addProbEdge(curr, curr_, act, pa.getProbability());
                                }
                                else{
                                    m.addProbEdge(curr, toOld, act, pa.getProbability());
                                }
                            }
                        }
                        else{ // its a pure branch
                            ModelState curr_ = curr.createSuccessor(b.getAssignList(),i);
                            ModelState toOld = m.search(curr_);
                            Action act = new Action(m.getProcDecls().get(i)+b.getLabel(),b.getIsFaulty(),b.getIsTau(),b.getReward(),isSpec);
                            //System.out.println(act.toString() + b.getReward());
                            //System.out.println(b.getLabel());
                            if (toOld == null){
                                m.addNode(curr_);
                                iterSet.add(curr_);
                                m.addProbEdge(curr, curr_, act,1.0);
                            }
                            else{
                                m.addProbEdge(curr, toOld, act,1.0);
                            }
                        }
                        
                    }
                }
            }
        }
        return m;
    }

    public void setName(String n){
        name = n;
    }

    public String getName(){
        return name;
    }

}
