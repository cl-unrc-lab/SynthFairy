package lang;


import java.util.*;

/**
 * A class representing an integer constant
 */
// Intcons it represents a constant of type Int
public class ConsDoubleExp extends NumExp{
    
	Double value;
    /**
     * Basic constructor for the class, it constructs the number expressed as a BDD
     * @param	i	the integer
     */
    public ConsDoubleExp(Double i){
      super();
      value = i;
    }
    
    @Override
	public void accept(LangVisitor v){
	     v.visit(this);			
	}

    public Double getValue(){
        return value;
    }
}