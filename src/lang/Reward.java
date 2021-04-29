package lang;


/**
 * A class representing a reward on a expression
 */
public class Reward extends Code{

    Integer value; // the reward corresponfing to exp
    Expression exp; 
    
    
    public Reward(Expression exp, Integer i){
    	this.value = i;
    	this.exp = exp;
    	
    }
    
        
    @Override
	public void accept(LangVisitor v){
	     v.visit(this);			
	}
    
    public Expression getExp(){
        return exp;
    }

    public Integer getValue(){
        return value;
    }
}
