package lang;


import java.util.*;



public class Proc extends ProgramNode {

    LinkedList<Param> paramList;

	String processName;
	LinkedList<Var> intVars; // it has a collection of local variables of type int
	LinkedList<Var> boolVars; // it has a collections of local variables of type boolean
    LinkedList<Var> enumVars; // it has a collections of local variables of type enumerated
    
	LinkedList<Branch> branches; // a collection of branches consisting of a guard and a command
	Expression initialCond; // the initial valuations of the local variables.
	LinkedList<String> processInstanceNames; // Names of the differents process Instances
	LinkedList<InvkProcess> processInvkParameters; // Invocation Parameters of each process instance.
    LinkedList<Reward> rewards;
    Expression controllerCond;
    Expression goalCond;
	
	
	public Proc(String name){
		this.processName = name;
		paramList = new LinkedList<Param>();

        intVars = new LinkedList<Var>();
		boolVars = new LinkedList<Var>();
        enumVars = new LinkedList<Var>();
		branches = new LinkedList<Branch>();
	    initialCond = null;
        controllerCond = null;
        goalCond = null;
	    processInstanceNames= new LinkedList<String>();
        processInvkParameters = new LinkedList<InvkProcess>();
		
	}
	
	
	public Proc(String name,Expression iniC,LinkedList<Var> varList,LinkedList<Branch> branchList){
		this.processName = name;
		paramList = new LinkedList<Param>();
        initialCond = iniC;
        controllerCond = null;
        goalCond = null;
		branches = branchList;
		intVars = new LinkedList<Var>();
		boolVars = new LinkedList<Var>();
        enumVars = new LinkedList<Var>();
		processInstanceNames= new LinkedList<String>();
		processInvkParameters= new LinkedList<InvkProcess>();
		
		 /* --- Adding the Declarated Vbles according the type ---*/
        for (int i = 0; i < varList.size(); i++){
            
            if ( varList.get(i).getType().isBoolean() ){
            	boolVars.add(varList.get(i));
            }
            else{
                if ( varList.get(i).getType().isInt() ){
                    intVars.add(varList.get(i));
                }else{
                    enumVars.add(varList.get(i));
                }
            }
        }
		
		
	}
	
	public Proc(Expression iniC,LinkedList<Var> varList,LinkedList<Branch> branchList){
		this.processName = new String();
        paramList = new LinkedList<Param>();
		initialCond = iniC;
        controllerCond = null;
        goalCond = null;
		branches = branchList;
		intVars = new LinkedList<Var>();
		boolVars = new LinkedList<Var>();
        enumVars = new LinkedList<Var>();
		processInstanceNames= new LinkedList<String>();
        processInvkParameters= new LinkedList<InvkProcess>();
        rewards = new LinkedList<Reward>();
		
		
		/* --- Adding the Declarated Vbles according the type ---*/
        for (int i = 0; i < varList.size(); i++){
            
            if ( varList.get(i).getType().isBoolean() ){
            	boolVars.add(varList.get(i));
            }
            else{
                if ( varList.get(i).getType().isInt() ){
                    intVars.add(varList.get(i));
                }else{
                    enumVars.add(varList.get(i));
                }
            }
        }
		
		
	}
	
	public Proc(Expression iniC,LinkedList<Branch> branchList){
		this.processName = new String();
        paramList = new LinkedList<Param>();
		initialCond = iniC;
        controllerCond = null;
        goalCond = null;
		branches = branchList;
		intVars = new LinkedList<Var>();
		boolVars = new LinkedList<Var>();
        enumVars = new LinkedList<Var>();
		processInstanceNames= new LinkedList<String>();
        processInvkParameters= new LinkedList<InvkProcess>();
        rewards = new LinkedList<Reward>();
		
	}

    public Proc(Expression iniC,LinkedList<Var> varList,LinkedList<Branch> branchList, LinkedList<Reward> rewardList, Expression player1, Expression goal){
        this.processName = new String();
        paramList = new LinkedList<Param>();
        initialCond = iniC;
        branches = branchList;
        intVars = new LinkedList<Var>();
        boolVars = new LinkedList<Var>();
        enumVars = new LinkedList<Var>();
        processInstanceNames= new LinkedList<String>();
        processInvkParameters= new LinkedList<InvkProcess>();
        rewards = rewardList;
        controllerCond = player1;
        goalCond = goal;
        
        
        /* --- Adding the Declarated Vbles according the type ---*/
        for (int i = 0; i < varList.size(); i++){
            
            if ( varList.get(i).getType().isBoolean() ){
                boolVars.add(varList.get(i));
            }
            else{
                if ( varList.get(i).getType().isInt() ){
                    intVars.add(varList.get(i));
                }else{
                    enumVars.add(varList.get(i));
                }
            }
        }
        
        
    }
    
    public Proc(Expression iniC,LinkedList<Branch> branchList, LinkedList<Reward> rewardList, Expression player1, Expression goal){
        this.processName = new String();
        paramList = new LinkedList<Param>();
        initialCond =iniC;
        branches = branchList;
        intVars = new LinkedList<Var>();
        boolVars = new LinkedList<Var>();
        enumVars = new LinkedList<Var>();
        processInstanceNames= new LinkedList<String>();
        processInvkParameters= new LinkedList<InvkProcess>();
        rewards = rewardList;
        controllerCond = player1;
        goalCond = goal;
    }
	
	public String getName(){
		
		return processName;
	}
	
    
    public LinkedList<Param> getParamList(){
		
		return paramList;
	}
    
    public LinkedList<Branch> getBranches(){
		
		return branches;
	}
    
    public LinkedList<Var> getVarInt(){
		
		return intVars;
	}
 
    public LinkedList<Var> getVarBool(){
        
		return boolVars;
	}
    
    public LinkedList<Var> getVarEnum(){
        
		return enumVars;
	}


    
    public Expression getInitialCond(){
    	return initialCond;
    	
    }

    public Expression getControllerCond(){
        return controllerCond;    
    }

    public Expression getGoalCond(){
        return goalCond;    
    }

    public LinkedList<Reward> getRewards(){
        return rewards;
    }

    
    /**
     *
     * @param processName Name of the process.
     * @return return the list with the invocation parameters of the process with that name, an empty list otherwise.-
     */
    
    public LinkedList<Expression> getInvkParametersList(String processName){
        
        
        for(int i=0;i<processInvkParameters.size();i++){
            String pName = processInvkParameters.get(i).getInstanceName();
            
            if(pName.equals(processName)){
                return processInvkParameters.get(i).getInvkValues();
            }
            
        }
        return new LinkedList<Expression>();
        
	}
    
    
    /**
     *
     * @param processName Name of the process.
     * @return return the list with the boolean parameters of the process with that name, an empty list otherwise.-
     */
    
    public LinkedList<Expression> getInvkBoolParamList(String processName){
        
        LinkedList<Expression> boolPar = new LinkedList<Expression>();
        LinkedList<Expression> invkPar = this.getInvkParametersList(processName);
        
        for(int i=0;i<invkPar.size();i++){
            Var var = (Var)invkPar.get(i);
            if(var.getType().isBoolean()){
                boolPar.add(invkPar.get(i));
            }
        }
        
        return boolPar;
	}
    
    
    /**
     *
     * @param processName Name of the process.
     * @return return the list with the integer parameters of the process with that name, an empty list otherwise.-
     */
    
    public LinkedList<Expression> getInvkIntParamList(String processName){
        
        LinkedList<Expression> intPar = new LinkedList<Expression>();
        LinkedList<Expression> invkPar = this.getInvkParametersList(processName);
        
        for(int i=0;i<invkPar.size();i++){
            Var var = (Var)invkPar.get(i);
            
            if(var.getType().isInt()){
                intPar.add(invkPar.get(i));
            }
        }
        
        return intPar;
	}
    
    
    
    /**
     *
     * @param i ith intance position
     * @return return the name of the ith instance, null if not exist.-
     */
    public String getProcessInstanceName(int i){
    	
    	if ( i < processInstanceNames.size()){
    		return processInstanceNames.get(i);
    	}
    	else{
    		return null;
    	}
    	
    }
    
    public int getNumVar(){
    	
        return ( boolVars.size() + intVars.size() + enumVars.size());
    }
    
    public int getNumVarInt(){
    	
        return ( intVars.size());
    }
   
    public int getNumVarEnumerated(){
    	
        return ( enumVars.size());
    }
    
    
    public int getNumVarBool(){
    	return ( boolVars.size());
    }
    
    public int getNumBranches(){
    	
        return branches.size();
    }

    public int getNumInstances(){
    	
        return processInstanceNames.size();
    }
	
    public void addInstanceName(String instanceName){
    	processInstanceNames.add(instanceName);
    }
    
    public void addParam(String parId){
    	paramList.add(new Param(parId));
    	
    }
    
    public void addInvkParam(InvkProcess invP){
    	processInvkParameters.add(invP);
    }
    
    
    public void addBranchList(LinkedList<Branch> list){
    	branches = list;
    	
    }
    
    public void setInitialCond(Expression ini){
    	initialCond = ini;
    	
    }
    
    public void setName(String name){
    	processName = name;
    	
    }
        
    public void setParamList(LinkedList<Param> parL){
    	paramList = parL;
    }
    
    
    /**
     * Return the list of all names of the boolean variables involved of this
     *  process (according the instance name of the process) :
     * "Process_instanceName" + "." + "VariableName"
     * @return
     */                       
    public LinkedList<String> getBoolVarNamesProcessInstances(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	
    	for(int j=0; j< processInstanceNames.size() ; j++){
    	   for(int i = 0; i< boolVars.size(); i++){
    		   String nameB = new String (processInstanceNames.get(j) + "." + boolVars.get(i).getName());
    		   varNames.add(nameB);
    	   }
   	    }
    	return varNames;
    }
    
    
    
    /**
     * Return the list of all names of the integer variables involved of this
     *  process (according the instance name of the process) :
     * "Process_instanceName" + "." + "VariableName"
     * @return
     */
    public LinkedList<String> getIntVarNamesProcessInstances(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	
    	for(int j=0; j< processInstanceNames.size() ; j++){
    	   for(int i = 0; i< intVars.size(); i++){
    		   String nameI = new String (processInstanceNames.get(j) + "." + intVars.get(i).getName());
    		   varNames.add(nameI);

    	   }
   	    }
    	return varNames;
    }
    
    /**
     * Return the list of all names of the enumerated variables involved of this
     *  process (according the instance name of the process) :
     * "Process_instanceName" + "." + "VariableName"
     * @return
     */
    public LinkedList<String> getEnumVarNamesProcessInstances(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	
    	for(int j=0; j< processInstanceNames.size() ; j++){
            for(int i = 0; i< enumVars.size(); i++){
                String nameI = new String (processInstanceNames.get(j) + "." + enumVars.get(i).getName());
                varNames.add(nameI);
                
            }
   	    }
    	return varNames;
    }
    
    /**
     * @return Return the total number of enumVars of a single Process.
     * 
     */
    public int getNumEnumVarsProcess(String enumName){
        
        int numberVars =0;
        int numberParam = 0;
        
        
        for(int i = 0; i< enumVars.size(); i++){
            if(enumVars.get(i).getEnumName().equals(enumName) ){
                numberVars++;
            }
        }
        
        for(int i = 0; i< paramList.size(); i++){
            if(paramList.get(i).getEnumName().equals(enumName) ){
                numberParam++;
            }
        }
        
        
    	return (numberVars+numberParam);
    }
    
    
    /**
     * @return Return the total number of enumVars according the number of process instances.
     *
     */
    public int getNumEnumProcessInstances(String enumName){
        
        
        int numberVars =0;
        int numberInstances=0;
        int numberParam = 0;
        
        for(int i = 0; i< enumVars.size(); i++){
            if(enumVars.get(i).getEnumName().equals(enumName) ){
                numberVars++;
            }
        }
        
        
        for(int i = 0; i< paramList.size(); i++){
            if (paramList.get(i).getType().isEnumerated()){//Only compare if the type of parameter is Enum.
                if(paramList.get(i).getEnumName().equals(enumName) ){
                    numberParam++;
                }
            }
        }
        
        numberInstances = processInstanceNames.size();
        
       return ((numberParam + numberVars) * numberInstances);
    }
    
    
	public void accept(LangVisitor v){
	     v.visit(this);			
	}
    
    
}
