package lang;

/**
 * Provides the basic behavior of an expression
 * adding two integers
*/
public class SumIntExp extends NumExp{
	Expression exp1; // the left integer
	Expression exp2; // the right integer

    /**
     * basic constructor of the class
     * @param	exp1	the left integer
     * @param	exp2	the right integer
     */
    public SumIntExp(Expression exp1, Expression exp2){
    	this.exp1 = exp1;
        this.exp2 = exp2;
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