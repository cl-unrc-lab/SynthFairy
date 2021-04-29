package lang;

import java.util.*;

public class TableLevel {
	
	private LinkedList<Var> varList;
    private LinkedList<Param> parList;
    private LinkedList<EnumType> enumList;
 
	private LinkedList<Proc> processList;
	
	public TableLevel(){
		
		varList = new  LinkedList<Var>();
		parList = new  LinkedList<Param>();
        processList = new LinkedList<Proc>();
        enumList = new LinkedList<EnumType>();
		
	}
	
	
	
	public boolean addVar(Var var){
		boolean result = false;
		if (!existVar(var.getName())){
			result =true;
			varList.add(var);
		}
		return result;
	}
	
    public boolean addParam(Param par){
		boolean result = false;
		if (!existParam(par.getDeclarationName())){
			result =true;
			parList.add(par);
		}
		return result;
	}
    
	public boolean addProcess(Proc proc){
		boolean result = false;
		if (!existProcess(proc.getName())){
			result =true;
			processList.add(proc);
		}
		return result;
	}
	
    
    public boolean addEnumeratedType(EnumType eType){
		boolean result = false;
		if (!existEnumeratedType(eType.getName())){
			result =true; //added
			enumList.add(eType);
		}
		return result;
	}
    

    public Var getVar(String name){
		for(int i=0; i<varList.size();i++){
			if (varList.get(i).getName().equals(name)){
				return varList.get(i);
			}
		}
		return null;
	}

    
    public Param getParam(String name){
		for(int i=0; i<parList.size();i++){
			if (parList.get(i).getDeclarationName().equals(name)){
				return parList.get(i);
			}
		}
		return null;
	}

    
    public Proc getProcess(String name){
		for(int i=0; i<processList.size();i++){
			if (processList.get(i).getName().equals(name)){
				return processList.get(i);
			}
		}
		return null;
	}
    
    
    public EnumType getEnumeratedType(String name){
		for(int i=0; i<enumList.size();i++){
			if (enumList.get(i).getName().equals(name)){
				return enumList.get(i);
			}
		}
		return null;
	}

    
    /**
     * 
     * @return Return a list with all names of boolean variables in the current level.
     */
    public LinkedList<String> getBoolVarNames(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	// Obtain all names of the global variables of type bool. 
        for(int i=0; i<varList.size();i++){
        	if(varList.get(i).getType().isBoolean()){
			    varNames.add(varList.get(i).getName());
			}
		}
        return varNames;
	}

    
    /**
     *
     * @return Return a list with all names of enumerated variables in the current level.
     */
    public LinkedList<String> getEnumVarNames(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	// Obtain all names of the global variables of type bool.
        for(int i=0; i<varList.size();i++){
        	if(varList.get(i).getType().isEnumerated()){
			    varNames.add(varList.get(i).getName());
			}
		}
        return varNames;
	}

    /**
     * 
     * @return Return a list with all names of integer variables in the current level.
     */
    public LinkedList<String> getIntVarNames(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	// Obtain all names of the global variables of type bool. 
        for(int i=0; i<varList.size();i++){
        	if(varList.get(i).getType().isInt()){
			    varNames.add(varList.get(i).getName());
			}
		}
        return varNames;
	}

    /**
     * 
     * @return Return a list with all names of the boolean variables the process current level.
     */
    public LinkedList<String> getBoolVarNamesProcesses(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	Proc proc =null;
    	// Obtain all names of the variables according the instances names 
    	// of the process ( "Process_instanceName" + "." + "VariableName")
        for(int i=0; i<processList.size();i++){
        	proc =processList.get(i);
        	if (proc !=null){
        		LinkedList<String> processVars = proc.getBoolVarNamesProcessInstances();
        		for(int j=0; j<processVars.size();j++){
        			varNames.add(processVars.get(j)); 
        		}
        	}
			
		}
        return varNames;
	}
    
    
    /**
     * 
     * @return Return a list with all names of the integer variables the process current level.
     */
    public LinkedList<String> getIntVarNamesProcesses(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	Proc proc =null;
    	// Obtain all names of the variables according the instances names 
    	// of the process ( "Process_instanceName" + "." + "VariableName")
        for(int i=0; i<processList.size();i++){
        	proc =processList.get(i);
        	if (proc !=null){
        		LinkedList<String> processVars = proc.getIntVarNamesProcessInstances();
        		for(int j=0; j<processVars.size();j++){
        			varNames.add(processVars.get(j)); 
        		}
        	}
			
		}
        return varNames;
	}
    
	
    /**
     *
     * @return Return a list with all names of the enumerated variables the process current level.
     */
    public LinkedList<String> getEnumVarNamesProcesses(){
    	LinkedList<String> varNames = new LinkedList<String>();
    	Proc proc =null;
    	// Obtain all names of the variables according the instances names
    	// of the process ( "Process_instanceName" + "." + "VariableName")
        for(int i=0; i<processList.size();i++){
        	proc =processList.get(i);
        	if (proc !=null){
        		LinkedList<String> processVars = proc.getEnumVarNamesProcessInstances();
        		for(int j=0; j<processVars.size();j++){
        			varNames.add(processVars.get(j));
        		}
        	}
			
		}
        return varNames;
	}
    
   
	
	public boolean existVar(String varName){
		for(int i=0;i<varList.size();i++){
			if(varList.get(i).getName().equals(varName)){
				return true;
			}
		}
		return false;
	}
	
	
    public boolean existParam(String parName){
		for(int i=0;i<parList.size();i++){
			if(parList.get(i).getDeclarationName().equals(parName)){
				return true;
			}
		}
		return false;
	}
	
    
    public boolean existProcess(String procName){
		for(int i=0;i<processList.size();i++){
			if(processList.get(i).getName().equals(procName)){
				return true;
			}
		}
		return false;
	}
    
    public boolean existEnumeratedType(String eName){
		for(int i=0;i<enumList.size();i++){
			if(enumList.get(i).getName().equals(eName)){
				return true;
			}
		}
		return false;
	}
	
	
	public boolean existNameLevel(String name){
		
		if (!existVar(name) && !existParam(name) && !existProcess(name)&& !existEnumeratedType(name)) {
			return false;
		}
		return true;
	}
	
	public void deleteLevel(){
		
		
	}



}
