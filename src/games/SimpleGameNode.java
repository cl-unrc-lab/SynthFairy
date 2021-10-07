package games;

import java.util.*;
import model.*;
import lang.Proc;
import lang.Reward;

public class SimpleGameNode implements Comparable<Object>{

	ModelState state; // Current state of the Specification
	Action symbol; // The action that lead to this state
	int id;
	private static int idCounter = 0;
	//EXPANSION SET
	Double[] values; // for value iteration
	Double[] expectedDecisions; // for robot Example experiments
	int reward;
	int playerControl;
	boolean isGoal;
	SimpleGameNode strategy;
	boolean visited;

	public SimpleGameNode(){
		values = new Double[2];
		expectedDecisions = new Double[2];
		id = idCounter++;
		reward = 0;
		playerControl = 2;
		isGoal = false;
		strategy = null;
	}

	public SimpleGameNode(ModelState s, Action sym, boolean probabilistic){
		state = s;
		symbol = sym;
		values = new Double[2];
		expectedDecisions = new Double[2];
		id = idCounter++;
		reward = calculateReward();
		if (probabilistic)
			playerControl = 3;
		else
			playerControl = whoControlsThis();
		isGoal = isThisTheGoal();
		strategy = null;
	}

	public ModelState getState(){
		return state;
	}

	public Action getSymbol(){
		return symbol;
	}

	public int getPlayerControl(){
		return playerControl;
	}

	public boolean getIsGoal(){
		return isGoal;
	}

	public int getId(){
		return id;
	}

	public SimpleGameNode getStrategy(){
		return strategy;
	}

	public void setStrategy(SimpleGameNode strat){
		strategy = strat;
	}

	private int calculateReward(){
		int rwd = 0;
		for (int i=0; i < state.getModel().getProcs().size(); i++){
			for (Reward r : state.getModel().getProcs().get(i).getRewards()){
				if (state.satisfies(r.getExp(),i)){
					rwd += r.getValue();
				}
			}
		}
		return rwd;
	}

	private int whoControlsThis(){
		boolean c = true;
		for (int i=0; i < state.getModel().getProcs().size(); i++){
			Proc p = state.getModel().getProcs().get(i);
			c = c && state.satisfies(p.getControllerCond(),i);
		}
		if (c)
			return 1;
		else
			return 2;
	}

	private boolean isThisTheGoal(){
		boolean c = true;
		for (int i=0; i < state.getModel().getProcs().size(); i++){
			Proc p = state.getModel().getProcs().get(i);
			c = c && state.satisfies(p.getGoalCond(),i);
		}
		return c;
	}

	public int getReward(){
		return reward;
	}

	public boolean isController(){
		return playerControl == 1;
	}

	public boolean isEnvironment(){
		return playerControl == 2;
	}

	//EXPANSION SET
	public boolean isProbabilistic(){
		return playerControl == 3;
	}
	//EXPANSION SET
  	public void setValue(int i, double val){
  		values[i] = val;
  	}
  	//EXPANSION SET
  	public void setExpectedDecisions(int i, double val){
  		expectedDecisions[i] = val;
  	}
  	//EXPANSION SET
  	public Double[] getValues(){
	    return values;
	}
	//EXPANSION SET
  	public Double[] getExpectedDecisions(){
	    return expectedDecisions;
	}

	public boolean getVisited(){
		return visited;
	}

	public void setVisited(boolean v){
		visited = v;
	}

	@Override
	public int compareTo(Object u) {
		if (u instanceof SimpleGameNode)
			if (this.equals((SimpleGameNode)u))
				return 0;
		return -1;
	}

	@Override
	public int hashCode(){
	    return Objects.hash(state, symbol, playerControl);
	}


	public String toString(){
		String res;
		res = "[STATE: "+state.toString()+", SYMBOL: "+symbol.getLabel()+", PLAYER: "+playerControl+"]";
		return res;
	}

	public String toStringDot(){
		String res;
		String s = symbol.getLabel();
		res = "State: "+state.toStringDot()+"\nSymbol: "+s+"\nPlayer: "+playerControl;
		return res;
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof SimpleGameNode){
			SimpleGameNode n = (SimpleGameNode)o;
			if (state == n.getState() && symbol.equals(n.getSymbol()) && playerControl == n.getPlayerControl()){
				//System.out.println(this.hashCode()==n.hashCode());
				return true;
			}
			//System.out.println(this.hashCode()==n.hashCode());
		}
		return false;
	}
	
}