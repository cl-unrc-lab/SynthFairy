package lang;

import java.util.*;




public class TypeCheckerVisitor implements LangVisitor{
	
	private Type type;
	private LinkedList<Error> listError;
	private SymbolsTable table;
    private EnumType currentEnumeratedType;
    
	/**
	 * Basic constructor
	 */
	public TypeCheckerVisitor(){
		type = Type.UNDEFINED;
	    listError = new LinkedList<Error>();
	    table = new SymbolsTable();
        currentEnumeratedType=null;
	}
	
	
	public void visit(Program a){
		
		 
		//Type eType = Type.ERROR;
		Type eType = Type.UNDEFINED;
		EnumType enumT;
        for(int i=0;i< a.enumTypes.size();i++){
            
			enumT =a.enumTypes.get(i);
			enumT.accept(this);
		    eType= this.getType();
        }
        
       
        
		a.globalVars.accept(this);
		Type globalT = this.getType();
		
						
		a.process.accept(this);		
		Type procT= this.getType();
		
		a.mainProgram.accept(this);
		Type mainT = this.getType();
		
		if(globalT!= Type.ERROR  && procT!=Type.ERROR && mainT!= Type.ERROR && eType!=Type.ERROR){
            
            calculatesEnumVars(a); // Calculates and set in each enum type the number of instances of this type of the program.
            
            
		}
		else{
			type =Type.ERROR;			
		}
		
	}
    
    public void visit(EnumType a){
        
        boolean added =table.addSymbol(a);
        //System.out.println("----------------> enum declaration " + a.getName() + " const 0 "+ a.getCons(0) + " - size = "+ a.getSize());
        
        if(!added){
            type = Type.ERROR;
            listError.add(new Error("ErrorType - The Enumerated Type " + a.getName() + ", already exist."));
        }
        
    }
    
	public void visit(GlobalVarCollection a){
		
		LinkedList<Var> boolVars = a.getBoolVars();
		LinkedList<Var> intVars = a.getIntVars();
        LinkedList<Var> enumVars = a.getEnumVars();
		
		Var var;
		
		boolean correct =true;
		
		for(int i=0;i<boolVars.size();i++){
			var =boolVars.get(i);
			var.accept(this);
			if (this.getType() == Type.ERROR){
				correct=false;
			}
		}
		for(int i=0;i<intVars.size();i++){
			var =intVars.get(i);
			var.accept(this);
			if (this.getType() == Type.ERROR){
				correct=false;
			}
		}
        
        for(int i=0;i<enumVars.size();i++){
			var =enumVars.get(i);
            var.accept(this);
			if (this.getType() == Type.ERROR){
				correct=false;
			}
		}
		
		
		if (correct){
			this.type = Type.INT; 	// by default type global vars is int, otherwise ERROR.
		}
		else{
			this.type=Type.ERROR;
		}
	    
	    
        
    }
	
	public void visit(ProcessCollection a){		
		LinkedList<Proc> processList = a.getProcessList();
	
		boolean correctType = true; 
		
		for(int i=0; i<processList.size(); i++){
		
			processList.get(i).accept(this);			
			if (this.getType()==Type.ERROR){
				correctType = false;
			}
		}
		
        
		if (correctType){
			this.type = Type.INT; 	// by default type correct process list is int, otherwise ERROR.
        
        }
		else{
			this.type=Type.ERROR;
		}
	}
	
	
	public void visit(Proc a){
		
		LinkedList<Branch> branches = a.getBranches();
		Expression ini=a.getInitialCond();
		LinkedList<Var> varB = a.getVarBool();
		LinkedList<Var> varI = a.getVarInt();
        LinkedList<Var> varE = a.getVarEnum();
        
        LinkedList<Param> paramList = a.getParamList();

		boolean correct =true;
		table.incrementLevel();
        
        
        //Parameters
		for(int i=0;i<paramList.size();i++){
			paramList.get(i).accept(this);
			Type parT= this.getType();
			if ( parT == Type.ERROR){
				correct=false;
			}
		}
		
		
		
		//BOOL variables
		for(int i=0;i<varB.size();i++){
			varB.get(i).accept(this);
			Type varT= this.getType();
			if ( varT == Type.ERROR){
				correct=false;
			}
		}
		
		//INT variables
		for(int i=0;i<varI.size();i++){
			varI.get(i).accept(this);
			Type varT= this.getType();
			if ( varT == Type.ERROR){
				correct=false;
			}
		}
        
        //ENUM variables
		for(int i=0;i<varE.size();i++){
			varE.get(i).accept(this);
			Type varT= this.getType();
			if ( varT == Type.ERROR){
				correct=false;
			}
		}
        
		
       
		//initial Condition
		ini.accept(this);
		Type iniT = this.getType();
		if ( iniT == Type.ERROR){
			correct = false;
		}
		
		
		// Branches of te process
		for(int i=0;i<branches.size();i++){
			branches.get(i).accept(this);
			Type varBr= this.getType();
			if ( varBr == Type.ERROR){
				correct=false;
			}
		}
		
		if(!correct){
			this.type =Type.ERROR;
		}
		else{
			this.type = Type.INT;
		}
		
		TableLevel initialLevel = table.getLevelSymbols(0);
		if (initialLevel!=null){ //add the declared process to the fisrt level
			initialLevel.addProcess(a);
		}
		
		table.decrementLevel();
		
	}
	
	
	public void visit(Branch a){
		LinkedList<Code> aList = a.assignList;
		Expression expr = a.guard;
		Type exprT;
		expr.accept(this);
		exprT = this.getType();
		
		//Type assigListT = Type.BOOL; //By default all branches are BOOL type 
		boolean correct =true;
		for(int i=0;i<aList.size();i++){
			aList.get(i).accept(this);
			if ( this.getType() == Type.ERROR){
				correct= false;
			}
		}
		
		
		if(exprT!=Type.ERROR && correct ){
			this.type = Type.BOOL;
		}
		else{
			this.type =Type.ERROR;
		}
	}

	public void visit(ProbAssign a){
		LinkedList<Code> aList = a.getAssigns();
		//Expression expr = a.getExp();
		//Type exprT;
		//expr.accept(this);
		//exprT = this.getType();
		boolean correct =true;
		for(int i=0;i<aList.size();i++){
			aList.get(i).accept(this);
			if ( this.getType() == Type.ERROR){
				correct= false;
			}
		}
		
		
		//if( correct && exprT == Type.DOUBLE ){
		if( correct ){
			type = Type.BOOL;
		}
		else{
		    type = Type.ERROR;
		}	
	}
	
	
	
	public void visit(VarAssign a){
		Var var = a.var;
		var.accept(this);
		Type varT = this.getType();
        String enumExp1 = null;
        String enumExp2 = null;
        //System.out.println(" -------------------Visiting VarAssign------ Var= " + var.getName() + "-- Type:"  + var.getEnumName() + " --------- \n");
        
        
        Type exprT;
        Expression expr = a.exp;
		if(varT.isEnumerated()){ //if is enumerated search in the first level de types declarated.
            
            TableLevel initialLevel = table.getLevelSymbols(0);
            currentEnumeratedType = initialLevel.getEnumeratedType(var.getEnumName());//set the enumerated type information of the current assignation
            enumExp1 = var.getEnumName();
        }
        expr.accept(this);
        exprT = this.getType();
        if(exprT.isEnumerated()){
            enumExp2 = currentEnumeratedType.getName();
        }
            
        //System.out.println(" ------------------ Visiting VarAssign------ expr= " + "-- TypEEEEEE: "  + exprT + " --------- \n");
            
        
		
        
		if( (varT.isBoolean() && exprT.isBoolean()) || (varT.isInt() && exprT.isInt() ) || ((varT.isEnumerated() && exprT.isEnumerated() && ( enumExp1.equals(enumExp2)) )) ){
			type = varT;
		}
		else{
		    type = Type.ERROR;
		    listError.add(new Error("ErrorType: Assignation of var :" + a.var.getName()));
		}	
	}

	public void visit(Reward r){
        Type exprT;
        Expression expr = r.exp;
        expr.accept(this);
        exprT = this.getType();
		if( exprT.isBoolean()){
			type = exprT;
		}
		else{
		    type = Type.ERROR;
		    listError.add(new Error("ErrorType: Expression of reward must be boolean"));
		}	
	}
	public void visit(Var a){
		Type origT = a.getType();
        if(a.isDeclaration()){
            
            
            if(origT.isEnumerated()){
                
                TableLevel initialLevel = table.getLevelSymbols(0);
                EnumType enumT = initialLevel.getEnumeratedType(a.getEnumName()); //search the enumerated type.
                if(enumT!=null){
                    a.setEnumType(enumT); //set the complete information of the type.
                    //System.out.println("VISITING var  declaration " + a.getName() + " type "+ a.getEnumName() + " -- etype " + enumT.getName() + " - size = "+ enumT.getSize());
                }
                else{
                    type = Type.ERROR;
                    listError.add(new Error("ErrorType - Var Declaration " + a.getName() + ", Type " + a.getEnumName() + " not found."));
                }
                
            }
            
            
		    boolean added =table.addSymbol(a);
            if(!added){
                type = Type.ERROR;
                listError.add(new Error("ErrorType - Var Declaration " + a.getName() + " already exist."));
            }
            else{
                type=origT;
            }
		}
		else{ //type is undefined or error
			if (origT.isUndefined() ){
				
                int i= table.getLevel();
                boolean found=false;
			    
                //Checks that not be a parameter.
                Param param = table.searchParam(a.getName(), i);
                if(param!=null){
                	
                    found = true;
                    type = param.getType();
                    a.setType(param.getType());
                    a.setEnumName(param.getEnumName());
                    TableLevel initialLevel = table.getLevelSymbols(0);
                    this.currentEnumeratedType = initialLevel.getEnumeratedType(param.getEnumName()); // bug fixed, CECI: CHECK THIS OUT                                                
                }
                else{
                
			        Var var=null;			       
			        //search the variable in the symbol table
			        while(i >= 0 && !found){
                        var = table.searchVar(a.getName(), i);
                        
			    	    if(var!=null){
                            found = true;
					        type = var.getType();
					        a.setType(var.getType());
                            a.setEnumName(var.getEnumName());
                            //System.out.println("----------------> Var Founded = " + a.getName() + " -- in Enumerated Type " + var.getEnumName() + "Type: " + var.getType().getStringValue() );
                            
                            TableLevel initialLevel = table.getLevelSymbols(0);
                            this.currentEnumeratedType = initialLevel.getEnumeratedType(var.getEnumName());//set the enumerated type information of the current assignation o comparison
                            a = var;
				        }
				        i--;
			        }
                    
                    if(!found){//search if its a constant of the current enumerated type
                        //System.out.println("----------------> VAR Not found -----Searching constant = " + this.currentEnumeratedType );

                        if(this.currentEnumeratedType!=null){
                            //System.out.println("----------------> Searching constant = " + a.getName() + " -- in Enumerated Type " + this.currentEnumeratedType.getName() );

                            found = this.currentEnumeratedType.existConstant(a.getName());
                            if(found){
                                //System.out.println("----------------> Exist constant!!" + a.getName()  );

                                type= Type.ENUMERATED;
                                a.setType(type);
                                a.setEnumName(this.currentEnumeratedType.getName());
                                a.setEnumType(this.currentEnumeratedType);//set the complete information if the enumerated type.
                                
                            }
                        }
                    }
                    
                    if(!found){
                        type = Type.ERROR;
                        listError.add(new Error("ErrorType - Variable or Parameter or Constant = " + a.getName() + ", not found"));
                    }

                }
			    
	        }
			else{// type is ERROR
			    type = Type.ERROR;
			}
		}
		
		//System.out.println("----------------> FINAL vble" + a.getName() + " -- type: " + a.getType().toString() );
        
        
        
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
                               /* System.out.println("Parameter declaration " + declName + " type "+ declT.toString() );
                                
                                if(par.getEnumType()!=null && declT.isEnumerated()){
                                   System.out.println(" enumerated: " +  par.getEnumName()+ " -- etype: " + par.getEnumType().getName() + " - size = "+ par.getEnumType().getSize());
                                }*/
                                
                                Var gVar = (Var)invkParametersList.get(j);
                                /*System.out.println("Parameter invok: " + gVar.getName() + " type: "+ gVar.getType().toString() );
                                
                                if(gVar.getEnumType()!=null && gVar.getType().isEnumerated()){
                                     System.out.println( " enumerated = " + gVar.getEnumName() + " -- etype " + gVar.getEnumType().getName() + " - size = "+ gVar.getEnumType().getSize()+ " \n\n\n");
                                }*/
                                
                                
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
	
	/**
	 * Return the type involved in the object.
	 * @return type 
	 */
    public Type getType(){
    	return type;
    }
    
    /**
	 * Return the SymbolsTable involved in the program
	 * @return type 
	 */
    public SymbolsTable getSymbolTable(){
    	return table;
    }

    /**
	 * Return the List of errors
	 * @return type 
	 */
    public LinkedList<Error> getErrorList(){
    	return listError;
    }
    
    
    /**
	 * Calculates and set in each enumType the number of instances of this type of the program.
	 * 
	 */
    
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
    

}
