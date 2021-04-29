package lang;




/**
 * Class implementing the comparation int1 > int2
 */

public class GreaterBoolExp extends BoolExp{
	Expression exp1; // left int
	Expression exp2; // right int

    /**
     * Constructor, it takes the two integer in the comparison as parameters
     * @param	int1	left integer
     * @param	int2	right integer
     */
    public GreaterBoolExp(Expression int1, Expression int2){
    	super();
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