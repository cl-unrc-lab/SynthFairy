package lang;

import java.util.*;




public class EvalVisitor {
	/*
	private LinkedList<Error> listError;
    

	public TypeCheckerVisitor(){
	    listError = new LinkedList<Error>();
	}
	
	
	public void visit(Program a){	 	
	}
    
    public void visit(EnumType a){
    }
    
	public void visit(GlobalVarCollection a){
    }
	
	public void visit(ProcessCollection a){		
	}
		
	public void visit(Proc a){
	}
	
	public void visit(Branch a){
	}

	public void visit(ProbAssign a){
	}
		
	public void visit(VarAssign a){
	}

	public void visit(Var a){
	}
	
	public void visit(AndBoolExp a){
		
		a.exp1.accept(this);	
		Type typeExp1 = this.getType();
		a.exp2.accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isBoolean() && typeExp2.isBoolean() ){
	    	type= Type.BOOL;
	    }
	    else{
	    	type = Type.ERROR;
	    	listError.add(new Error("ErrorType - && operation: Expected types Boolean."));
	    }	
	}
	
	public void visit(OrBoolExp a){
		a.exp1.accept(this);
		Type typeExp1 = this.getType();
		a.exp2.accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isBoolean() && typeExp2.isBoolean() ){
	    	type= Type.BOOL;
	    }
	    else{
	    	type = Type.ERROR;
	    	listError.add(new Error("ErrorType - || operation: Expected types Boolean."));
	    }
		
		
	}
	public void visit(NegBoolExp a){
		
		a.exp.accept(this);
		Type typeExp = this.getType();
	    if(typeExp.isBoolean()  ){
	    	type= Type.BOOL;
	    }
	    else{
	    	type = Type.ERROR;
	    	listError.add(new Error("ErrorType - ! operation: Expected types Boolean."));
	    }

		
	}
	public void visit(GreaterBoolExp a){
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	type= Type.BOOL;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	type= Type.DOUBLE;
		    }
		    else{
		    	type = Type.ERROR;
		    	listError.add(new Error("ErrorType - > operation: Expected types Integer or Double."));
		    }
	    }
		
	}
	public void visit(LessBoolExp a){
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	type= Type.BOOL;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	type= Type.DOUBLE;
		    }
		    else{
		    	type = Type.ERROR;
		    	listError.add(new Error("ErrorType - < operation: Expected types Integer or Double."));
		    }
	    }
	}
	public void visit(GreaterOrEqualBoolExp a){
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	type= Type.BOOL;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	type= Type.DOUBLE;
		    }
		    else{
		    	type = Type.ERROR;
		    	listError.add(new Error("ErrorType - >= operation: Expected types Integer or Double."));
		    }
	    }
		
	}
	public void visit(LessOrEqualBoolExp a){
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	type= Type.BOOL;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	type= Type.DOUBLE;
		    }
		    else{
		    	type = Type.ERROR;
		    	listError.add(new Error("ErrorType - <= operation: Expected types Integer or Double."));
		    }
	    }
		
	}
	public void visit(EqBoolExp a){
       		
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
			
        String tEnum1 = null;
        String tEnum2 = null;
        if(typeExp1.isEnumerated()){ //if is enumerated search in the first level of declarated types.   
        	tEnum1 = this.currentEnumeratedType.getName();
        }
        
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
        if(typeExp2.isEnumerated()){ //if is enumerated search in the first level of declarated types.
            tEnum2 = this.currentEnumeratedType.getName();
        }
       
        //System.out.println("operation type First Op " + typeExp1.toString() + "type second op " +  typeExp2.toString() );
		
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	//System.out.println(" ------> INT first op" + typeExp1.toString() + "type second op " +  typeExp2.toString() );
    		
	    	type= Type.BOOL;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	//System.out.println(" ------> INT first op" + typeExp1.toString() + "type second op " +  typeExp2.toString() );
	    		
		    	type= Type.BOOL;
		    }
		    else{
		    	if(typeExp1.isBoolean() && typeExp2.isBoolean() ){
		    		//System.out.println(" ------> BOOLEAN first op" + typeExp1.toString() + "type second op " +  typeExp2.toString() );
		    		a.setCreateBiimp(true); //marks to create the biimplication for boolean expression 
			    	type= Type.BOOL;
			    }
			    else{
	                
	                if(typeExp1.isEnumerated() && typeExp2.isEnumerated() ){
	                    
	                    if(tEnum1.equals(tEnum2)){
	                       // System.out.println(" ------> ENUMERATED first op" + typeExp1.toString() + "type second op " +  typeExp2.toString() + "EnumType:" + tEnum2);
	                        a.setIsEnumerated(true); //mark that comparation involves 2 enum expressions.
	                        a.setEnumType(tEnum2);
	                        type= Type.BOOL;
	                    }else{
	                        type = Type.ERROR;
	                        listError.add(new Error("ErrorType - == operator: Expected the same enumerated types for Comparation."));
	                    }
	                }
	                else{
	                    //System.out.println(" ------> ERROR first op" + typeExp1.toString() + "type second op " +  typeExp2.toString() );
	                    type = Type.ERROR;
	                    listError.add(new Error("ErrorType - == operator: Expected the same types for Comparation."));
	                }
			    }	 
		    }
	    	
	    }
	}
	
	
	
	public void visit(ConsBoolExp a){
        this.type = Type.BOOL;
	}
	public void visit(NegIntExp a){
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	type= Type.INT;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	type= Type.DOUBLE;
		    }
		    else{
		    	type = Type.ERROR;
		    	listError.add(new Error("ErrorType - - operation: Expected types Integer or Double."));
		    }
	    }
	}
	public void visit(SumIntExp a){
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	type= Type.INT;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	type= Type.DOUBLE;
		    }
		    else{
		    	type = Type.ERROR;
		    	listError.add(new Error("ErrorType - + operation: Expected types Integer or Double."));
		    }
	    }
		
	}
	public void visit(DivIntExp a){
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	type= Type.INT;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	type= Type.DOUBLE;
		    }
		    else{
		    	type = Type.ERROR;
		    	listError.add(new Error("ErrorType - / operation: Expected types Integer or Double."));
		    }
	    }
	}
	public void visit(MultIntExp a){
		a.getExp1().accept(this);
		Type typeExp1 = this.getType();
		a.getExp2().accept(this);
		Type typeExp2 = this.getType();
	    if(typeExp1.isInt() && typeExp2.isInt() ){
	    	type= Type.INT;
	    }
	    else{
	    	if(typeExp1.isDouble() && typeExp2.isDouble() ){
		    	type= Type.DOUBLE;
		    }
		    else{
		    	type = Type.ERROR;
		    	listError.add(new Error("ErrorType - * operation: Expected types Integer or Double."));
		    }
	    }
	}
	
	public void visit(ConsIntExp a){
		this.type = Type.INT;
	}

	public void visit(ConsDoubleExp a){
		this.type = Type.DOUBLE;
	}
	
	
    public void visit(Param a){
        	
        Type origT = a.getType();
        String paramName = a.getDeclarationName();
		
		if(a.isDeclaration()){			
            if(origT.isEnumerated()){
               
                TableLevel initialLevel = table.getLevelSymbols(0);
                EnumType enumT = initialLevel.getEnumeratedType(a.getEnumName()); //search the enumerated type.
                
                if(enumT!=null){
                    a.setEnumType(enumT); //set the complete information of the type.
                    //System.out.println("Parameter declaration " + paramName + " type "+ a.getEnumName() + " -- etype " + enumT.getName() + " - size = "+ enumT.getSize());
                    
                }
                else{
                    
                    type = Type.ERROR;
                    listError.add(new Error("ErrorType - Parameter " + paramName + " type :"+ a.getEnumName()+  " not found."));
                
                }
            }
		    boolean added = table.addSymbol(a);
            
            if (!added){ //Already exist an element with the same id at this level.
                type = Type.ERROR;
                listError.add(new Error("ErrorType - Parameter " + paramName + " : Ambiguous statement - already exist a Channel or Variable with the same name."));
            }
            else{
			    type=origT;
            }
		}		
        
    }
    
    
	
    public void visit(InvkProcess a){
        
        Type procT;
		TableLevel initialLevel = table.getLevelSymbols(0);
        Var gVar =null;
        String varName;
        
        LinkedList<Expression> invkParam = a.getInvkValues();
        for(int i=0; i< invkParam.size();i++){
            gVar = (Var)invkParam.get(i); //Obtain the name of the variable used in the process invocation.
            varName = gVar.getName();
            gVar = initialLevel.getVar(varName); // Search the global vble with that name
            
            if (gVar==null) {
                type = Type.ERROR;
                listError.add(new Error("ErrorType - Main Program - Invocation of process: "+ a.getInstanceName() +"  - Variable: " + varName +" - not found ."));
            }
            else{
                invkParam.set(i,gVar); //replace the parameter by the reference of the global variable.
            }
        }
        
    }
    
    
    
    public void visit(Main a){

		LinkedList<ProcessDecl> pDecl = a.processDecl;
		LinkedList<InvkProcess> pInvk = a.getProcessInvk();
        
		Type procT;
		this.type = Type.INT; //By default all correct process are INT type, otherwise ERROR.
		
		for(int i=0; i< pDecl.size();i++){
			pDecl.get(i).accept(this);
			procT = this.getType();
        }
		
		if(!a.isCorrectInvk()){
			
			listError.add(new Error("ErrorType -  Process Invocation - Process instance not Found ."));
		}
		else{
		    TableLevel initialLevel = table.getLevelSymbols(0);
		    Proc proc = null;
            InvkProcess infoInvoc;
            String procNam;
		    for(int i=0; i<pInvk.size(); i++ ){
                procNam = pInvk.get(i).getInstanceName();
			    proc = initialLevel.getProcess(a.getProcessType(procNam));
			    if(proc !=null){
                    infoInvoc = pInvk.get(i);
                    infoInvoc.accept(this);
                    procT = this.getType();
                    
                    if (procT!=Type.ERROR){
                        
                        //Parameter vs InvocationsParameters - Check number of parameters
                        LinkedList<Param> paramList = proc.getParamList();
                        LinkedList<Expression>  invkParametersList= infoInvoc.getInvkValues();
                        
                        if(invkParametersList.size() != paramList.size()){ //TODO: Pensar chequear esto antes de llamar al Visitor de AusiliarinvkProcess.
                            listError.add(new Error("ErrorType - Invocation of process: "+ procNam +" - The number of parameters does not match its definition."));
                            
                        }
                        else{
                            
                            for(int j=0;j<paramList.size();j++){
                                Param par= paramList.get(j);
                                String declName = par.getDeclarationName();
                                
                                Type declT= par.getType();

                                
                                Var gVar = (Var)invkParametersList.get(j);

                                
                                
                                if( gVar == null){
                                    listError.add(new Error("ErrorType1 - Invocation of process : "+ procNam +" - Type of parameters does not match its definition."));
                                }else{
                                    
                                    if( gVar.getType().isEnumerated()){
                                        if(gVar.getEnumType()== null){
                                            listError.add(new Error("ErrorType2 - Invocation of process : "+ procNam +" - Type of parameters does not match its definition."));
                                            
                                        }
                                        
                                        if(!gVar.getEnumName().equals(par.getEnumName())){
                                            listError.add(new Error("ErrorType3 - Invocation of process : "+ procNam +" - Type of parameters does not match its definition."));
                                            
                                        }
                                            
                                    
                                    }else{
                                       if( gVar.getType()!= declT){
                                          listError.add(new Error("ErrorType4 - Invocation of process : "+ procNam +" - Type of parameters does not match its definition."));
                                       }
                                    }
                                }
                                
                            }
                        }
				        proc.addInstanceName(infoInvoc.getInstanceName()); //add the instance name to the Process for later generation of instances.
                        proc.addInvkParam(infoInvoc); //Add the invocation parameters
                    }
                }
		    }
        }
		
        if(listError.size()>0){
            this.type = Type.ERROR;
        }
        
	}
	
	public void visit(ProcessDecl a){
		TableLevel level = table.getLevelSymbols(0);
		Proc proc = level.getProcess(a.getType());
        if(proc!=null){
        	type = Type.INT;
        }
        else{
        	type = Type.ERROR;
			listError.add(new Error("ErrorType - Main Program - Process Declaration - Process name not Found ."));        	
        }
	}
	

    public Type getType(){
    	return type;
    }
    

    public SymbolsTable getSymbolTable(){
    	return table;
    }


    public LinkedList<Error> getErrorList(){
    	return listError;
    }
    
    

    
    private void calculatesEnumVars(Program p){
    
        EnumType enumT;
        String eName;
        int numEnumT;
        for(int i=0;i< p.enumTypes.size();i++){
			enumT =p.enumTypes.get(i);
            eName = enumT.getName();
            numEnumT= getNumInstances(eName, p.globalVars.getEnumVars(), p.process.getProcessList());
            enumT.setNumVars(numEnumT);
        }
        
	}
    
    
    private int getNumInstances(String eName, LinkedList<Var> globalVarList , LinkedList<Proc> processList){
        
        int numEnumInstances=0;
       
        //calculates the number of global var of this enumType
        for(int i=0;i<globalVarList.size();i++){
			Var var =globalVarList.get(i);
            if(var.getEnumName().equals(eName)){
                numEnumInstances++;
            }
		}
       
        
        //calculates the number of var and parameters of this enumType by each process
        for(int i=0;i<processList.size();i++){
			Proc proc =processList.get(i);
            numEnumInstances= numEnumInstances + proc.getNumEnumProcessInstances(eName);
            
		}
        
        return numEnumInstances;
    }
    
*/
}
