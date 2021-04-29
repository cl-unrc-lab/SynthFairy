package lang;




/**
 * A class that implements the division between two integers: int1 / int2
 * TO BE IMPLEMENTED, DO NOT USE 
 */
public class DivIntExp extends NumExp{
	Expression exp1; // left integer
	Expression exp2; // right integer
     
	/**
     * Constructor of the class, it takes as paramenter the two integers
     * @param	int1	the left integer
     * @param	int2	the right integer
     */
    public DivIntExp(Expression int1, Expression int2){
    	
    	this.exp1 = int1;
    	this.exp2 = int2;
    }
    
    
    @Override
	public void accept(LangVisitor v){
	     v.visit(this);			
	}

    public Expression getExp1(){
        return exp1;
    }

    public Expression getExp2(){
        return exp2;
    }
   
    
}
