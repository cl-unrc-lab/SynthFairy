package lang;



/**
 * A class that represents a boolean constant, it could be true or false, represented by a 
 * constant BDD
 */
public class ConsBoolExp extends BoolExp{

    boolean value; // the value
    
    /**
     * Constructor of this class
     * @param value		the value of the constant: true or false
     */
    public ConsBoolExp(boolean value){
    	super();
        this.value = value;
    }
    
    
    
    @Override
	public void accept(LangVisitor v){
	     v.visit(this);			
	}

    public boolean getValue(){
        return value;
    }
}
