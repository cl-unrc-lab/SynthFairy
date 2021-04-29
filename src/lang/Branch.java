package lang;

import java.util.*;

public class Branch extends ProgramNode {
	    //BoolExp guard; // the guard of the branch
	    Expression guard; // the guard of the branch
	    LinkedList<Code> assignList;
	    //Code code; // the code of the branch
	    boolean isFaulty; // is the branch faulty?
	    boolean isTau;  // is the branch internal?
	    boolean isProb;
	    String label;
	    int reward;
	    
	    public Branch(Expression guard,  LinkedList<Code> assignList, boolean isFaulty, boolean isTau){
	    	
	    	this.guard=guard;
	    	this.assignList = assignList;
	    	this.isFaulty = isFaulty;
	    	this.isTau = isTau;
	    	this.label = "";
	    	reward = 0;
	    	
	    }

	    public Branch(Expression guard,  LinkedList<Code> assignList, boolean isFaulty, boolean isTau, String label, int reward, boolean isProb){
	    	
	    	this.guard = guard;
	    	this.assignList = assignList;
	    	this.isFaulty = isFaulty;
	    	this.isTau = isTau;
	    	this.label = label;
	    	this.isProb = isProb;
	    	this.reward = reward;
	    	
	    }
	    
	    public Expression getGuard(){
	    	return this.guard;
	    }
	    
	    public LinkedList<Code> getAssignList(){
	    	return this.assignList;
	    }
	
	    public boolean getIsFaulty(){
	    	return this.isFaulty;
	    }

	    public boolean getIsTau(){
	    	return this.isTau;
	    }

	    public boolean getIsProb(){
	    	return this.isProb;
	    }

	    public String getLabel(){
	    	return this.label;
	    }

	    public int getReward(){
	    	return this.reward;
	    }
	    
		public void accept(LangVisitor v){
		     v.visit(this);			
		}
}
