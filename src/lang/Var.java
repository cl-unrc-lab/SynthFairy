package lang;


public class Var extends Expression {

	String name;
	Type type;
    String enumName;
	boolean isDeclaration;
    private EnumType enumType;

		
	/**
	 * 
	 * @param name
	 * @param t
	 */
	public Var(String name, Type t){
		this.name = name;
        Type tVar;
        if(t.isEnumerated()){
            this.enumName = new String(t.getStringValue());
            tVar= Type.ENUMERATED;
            tVar.setStringValue(t.getStringValue());
            this.type = tVar;
    //System.out.println("in Var ---->  declaration " + this.name + " type "+ this.type.toString() + " enum " + this.type.getStringValue()+ " -- enumName: " + this.enumName);
            
        }
        else{
            this.enumName = null;
            this.type = t;

        }
		this.isDeclaration=true;
        this.enumType=null;
	}
	
	/**
	 * 
	 * @param name  
	 */
	public Var(String name){
		this.name = name;
		this.type = Type.UNDEFINED;
		this.isDeclaration=false;
        this.enumType=null;
        this.enumName = null;


	}

	/**
	 * 
	 * @return Return the type of the variable.
	 */
	public Type getType(){
		
		return this.type;
	}
	
    /**
	 *
	 * @return Return the enumerated type of the variable.
	 */
	public EnumType getEnumType(){
		
		return this.enumType;
	}
           
    /**
     *
     * @return Return the enumerated name of the variable.
     */
    public String getEnumName(){
        return enumName;
    }

	public boolean isDeclaration(){
    	return this.isDeclaration;
    }

	
	/**
	 * 
	 * @return Return the name of the variable.
	 */
    public String getName(){
		
		return name;
	}
    
    
    /**
	 * 
	 * @return Set the type of the variable.
	 */
	public void setType(Type t){
		Type tVar;
        if(t.isEnumerated()){
            //this.enumName = new String(t.getStringValue());
            
            tVar= Type.ENUMERATED;
            tVar.setStringValue(t.getStringValue());
            this.type = tVar;
            
        }else{
            this.type = t;
        }
	}
	
    
    
    /**
	 *
	 * Set the enumerated Type of the variable.
	 */
	public void setEnumType(EnumType t){
		
		this.enumType = t;
	}
    
	/**
	 * 
	 * @return Set the name of the variable.
	 */
    public void setName(String n){
		
		this.name = n;
	}
    
    /**
	 *
	 * Set the enumerated name of the variable.
	 */
    public void setEnumName(String n){
		
		this.enumName = n;
	}

	public boolean isEnumValue(){

		return this.enumName != null;
	}
    
    public boolean hasEnumType(){
    	return type.isEnumerated();
    }
    
    public String toStringComplete(){
        
    	String varInfo = new String("Var:   ");
    	varInfo = varInfo.concat("name: "+this.name + " - ");
    	varInfo = varInfo.concat("type: "+this.type.toString()+ " - ");
    	varInfo = varInfo.concat("isDeclaration: " +this.isDeclaration +"\n");
    	return varInfo;
    }

    
    @Override
	public void accept(LangVisitor v){
	     v.visit(this);			
	}
	
}

