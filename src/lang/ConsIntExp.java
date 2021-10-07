package lang;


/**
 * A class representing an integer constant
 */
// Intcons it represents a constant of type Int
public class ConsIntExp extends NumExp{
    
	Integer value;
    /**
     * Basic constructor for the class, it constructs the number expressed as a BDD
     * @param	i	the integer
     */
    public ConsIntExp(Integer i){
      super();
      value = i;
    }
    
    @Override
	public void accept(LangVisitor v){
	     v.visit(this);			
	}

    public Integer getValue(){
        return value;
    }
}