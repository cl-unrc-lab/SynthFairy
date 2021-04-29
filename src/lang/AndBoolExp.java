package lang;



/**
 *  A class to represent the "and" of two boolean expressions
 */
public class AndBoolExp extends BoolExp{
	Expression exp1; // the left expression
	Expression exp2; // the right expression
	

    
    /**
     * Basic constructor for the class
     * @param 	exp1	the left expression
     * @param	epx2	the right expression
     */
    public AndBoolExp(Expression exp1, Expression exp2){
    	
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
