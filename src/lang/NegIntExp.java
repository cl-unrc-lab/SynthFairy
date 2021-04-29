package lang;




/**
 * Class representing  the subtraction of two integers: exp1 - exp2
 * 
 */
public class NegIntExp extends NumExp{
	Expression exp1; // the first integer
	Expression exp2; // the second integer
    

    
    /**
     * Basic constructor of the class
     * @param exp1	the left int 
     * @param exp2	the right int
     */
    public NegIntExp(Expression exp1, Expression exp2){
    	
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
