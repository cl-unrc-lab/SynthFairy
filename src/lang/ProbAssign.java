package lang;
import java.util.LinkedList;


/**
 * A class representing an assignation to a var: var := Expr
 * it implements the interface code
 */
public class ProbAssign extends Code{

    //Expression exp;
    LinkedList<Code> assigns; 
    Double probability;
    
    
    /**
     * Basic constructor of the class
     * @param		var 	the var in the assignment
     * @param		exp		the expression in the (right part of the) assignment
     */
    public ProbAssign(Double exp, LinkedList<Code> assigns){
    	//this.exp = exp;
        this.probability = exp;
    	this.assigns = assigns;
    	
    }
    
        
    @Override
	public void accept(LangVisitor v){
	     v.visit(this);			
	}
    
    public Double getProbability(){
        return probability;
    }

    /*public Expression getExp(){
        return exp;
    }*/

    public LinkedList<Code> getAssigns(){
        return assigns;
    }
}
