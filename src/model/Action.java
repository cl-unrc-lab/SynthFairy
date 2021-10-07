package model;

import java.util.*;


public class Action {

	private String label;
	private Boolean isFaulty;
	private Boolean isTau;
	private Boolean isFromSpec;
	private Boolean isMask;
	private int reward;
	private boolean isProbabilistic;

	public Action(String l, Boolean isF, Boolean isT, int r, Boolean ifs){
		label = l;
		isFaulty = isF;
		isTau = isT;
		isFromSpec = ifs;
		isMask = false;
		reward = r;
	}

	public Action(String l, Boolean isF, Boolean isT, int r, Boolean ifs, Boolean isM){
		label = l;
		isFaulty = isF;
		isTau = isT;
		isFromSpec = ifs;
		isMask = isM;
		reward = r;
	}

	public Action(String l, Boolean isF, Boolean isT, Boolean ifs){
		label = l;
		isFaulty = isF;
		isTau = isT;
		isFromSpec = ifs;
		isMask = false;
		reward = 0;
	}

	public Action(String l, Boolean isF, Boolean isT, Boolean ifs, Boolean isM){
		label = l;
		isFaulty = isF;
		isTau = isT;
		isFromSpec = ifs;
		isMask = isM;
		reward = 0;
	}

	public String getLabel(){
		return label;
	}

	public void setLabel(String l){
		label = l;
	}

	public int getReward(){
		return reward;
	}

	public void setReward(int r){
		reward = r;
	}

	public void setIsMask(Boolean m){
		isMask = m;
	}

	public Boolean isMask(){
		return isMask;
	}

	public Boolean isFaulty(){
		return isFaulty;
	}

	public Boolean isTau(){
		return isTau;
	}

	public Boolean isFromSpec(){
		return isFromSpec;
	}

	public void setIsProbabilistic(boolean b){
		isProbabilistic = true;
	}

	public boolean getIsProbabilistic(){
		return isProbabilistic;
	}

	public Action cloneForSpec(boolean forSpec){ // this is an utility for the game graph creation
		Action a = new Action (label, isFaulty, isTau, reward, forSpec, isMask);
		return a;
	}

	@Override
	public int hashCode(){
	    return Objects.hash(label, isFaulty, isTau, isFromSpec);
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof Action){
			Action a = (Action)o;
			if ( label.equals(a.getLabel()) && isFaulty.equals(a.isFaulty()) && isTau.equals(a.isTau()) && isFromSpec.equals(a.isFromSpec())){
				//System.out.println(this.hashCode()==a.hashCode());
				return true;
			}
		}
		return false;
	}

	public String toString(){
		return label;
	}

}