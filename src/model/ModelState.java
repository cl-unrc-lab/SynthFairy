package model;

import java.util.*;
import lang.*;


public class ModelState implements Comparable{
	HashMap<String,Boolean> state; // global state bool vars
	HashMap<String,String> stateEnums; // global state enum vars
	HashMap<String,Integer> stateInts; // global state int vars
	Model model; // Global model whose this state belongs to
	boolean visited;

	public ModelState(){

	}

	public ModelState(Model m){
		model = m;
		state = new HashMap<String,Boolean>();
		stateEnums = new HashMap<String,String>();
		stateInts = new HashMap<String,Integer>();
		for (Var v : model.getSharedVars()){
			if (v.getType().isEnumerated())
				stateEnums.put(v.getName(),"");
			else
				if (v.getType().isInt())
					stateInts.put(v.getName(),0);
				else
					state.put(v.getName(),false);
		}
		for (int i=0; i < model.getProcDecls().size(); i++){
			for (Var v : model.getProcs().get(i).getVarBool()){
				state.put(model.getProcDecls().get(i)+v.getName(),false);
			}
			for (Var v : model.getProcs().get(i).getVarEnum()){
				stateEnums.put(model.getProcDecls().get(i)+v.getName(),"");
			}
			for (Var v : model.getProcs().get(i).getVarInt()){
				stateInts.put(model.getProcDecls().get(i)+v.getName(),0);
			}
		}
	}

	public void setVisited(boolean b){
		visited = b;
	}

	public boolean getVisited(){
		return visited;
	}

	@Override
	public int compareTo(Object u) {
		if (u instanceof ModelState)
			if (this.equals((ModelState)u))
				return 0;
		return -1;
	}

	@Override
	public int hashCode(){
	    return Objects.hash(state, stateEnums, stateInts);
	}

	public HashMap<String,Boolean> getState(){
		return state;
	}

	public HashMap<String,String> getStateEnums(){
		return stateEnums;
	}

	public HashMap<String,Integer> getStateInts(){
		return stateInts;
	}

	public Model getModel(){
		return model;
	}

	public boolean satisfies(Expression e, int procIndex){
		return evalBoolExpr(e, procIndex);
	}

	public String toString(){
		String res = "<";
		for (Var v : model.getSharedVars()){
			if (state.get(v.getName()) != null && state.get(v.getName()))
				res += v.getName() + ",";
			if (v.hasEnumType() && stateEnums.get(v.getName())!=null)
				res += v.getName() + "=" +stateEnums.get(v.getName())+ ",";
			if (stateInts.get(v.getName())!=null)
				res += v.getName() + "=" +stateInts.get(v.getName())+ ",";
		}
		for (int i=0; i < model.getProcDecls().size(); i++){
			for (Var v : model.getProcs().get(i).getVarBool()){
				if (state.get(model.getProcDecls().get(i)+v.getName())!=null && state.get(model.getProcDecls().get(i)+v.getName()))
					res += model.getProcDecls().get(i)+v.getName() + ",";
			}
			for (Var v : model.getProcs().get(i).getVarEnum()){
				if (v.hasEnumType() && stateEnums.get(model.getProcDecls().get(i)+v.getName())!=null)
					res += model.getProcDecls().get(i)+v.getName()+ "=" +stateEnums.get(model.getProcDecls().get(i)+v.getName()) + ",";
			}
			for (Var v : model.getProcs().get(i).getVarInt()){
				if (stateInts.get(model.getProcDecls().get(i)+v.getName())!=null)
					res += model.getProcDecls().get(i)+v.getName()+ "=" +stateInts.get(model.getProcDecls().get(i)+v.getName()) + ",";
			}
		}
		return res+">";
	}

	public String toStringDot(){
		String res = "";
		for (Var v : model.getSharedVars()){
			if (state.get(v.getName()) != null && state.get(v.getName()))
				res += v.getName() + "·";
			if (stateEnums.get(v.getName())!=null)
				res += v.getName() + "" +stateEnums.get(v.getName())+ "·";
			if (stateInts.get(v.getName())!=null)
				res += v.getName() + "" +stateInts.get(v.getName())+ "·";
		}
		for (int i=0; i < model.getProcDecls().size(); i++){
			for (Var v : model.getProcs().get(i).getVarBool()){
				if (state.get(model.getProcDecls().get(i)+v.getName())!=null && state.get(model.getProcDecls().get(i)+v.getName()))
					res += model.getProcDecls().get(i)+v.getName() + "·";
			}
			for (Var v : model.getProcs().get(i).getVarEnum()){
				if (v.hasEnumType() && stateEnums.get(model.getProcDecls().get(i)+v.getName())!=null)
					res += model.getProcDecls().get(i)+v.getName()+ "" +stateEnums.get(model.getProcDecls().get(i)+v.getName()) + "·";
			}
			for (Var v : model.getProcs().get(i).getVarInt()){
				if (stateInts.get(model.getProcDecls().get(i)+v.getName())!=null)
					res += model.getProcDecls().get(i)+v.getName()+ "" +stateInts.get(model.getProcDecls().get(i)+v.getName()) + "·";
			}
		}
		return res;
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof ModelState){
			ModelState n = (ModelState)o;
			for (Var var: model.getSharedVars()){
				if (state.get(var.getName()) != null && !state.get(var.getName()).equals(n.getState().get(var.getName())))
					return false;
				if (stateEnums.get(var.getName()) != null && !stateEnums.get(var.getName()).equals(n.getStateEnums().get(var.getName())))
					return false;
				if (stateInts.get(var.getName()) != null && !stateInts.get(var.getName()).equals(n.getStateInts().get(var.getName())))
					return false;
			}
			for (int i=0; i < model.getProcDecls().size(); i++){
				for (Var v : model.getProcs().get(i).getVarBool()){
					if (state.get(model.getProcDecls().get(i)+v.getName()) != null && !n.getState().get(model.getProcDecls().get(i)+v.getName()).equals(state.get(model.getProcDecls().get(i)+v.getName())))
						return false;
				}
				for (Var v : model.getProcs().get(i).getVarEnum()){
					if (stateEnums.get(model.getProcDecls().get(i)+v.getName()) != null && !n.getStateEnums().get(model.getProcDecls().get(i)+v.getName()).equals(stateEnums.get(model.getProcDecls().get(i)+v.getName())))
						return false;
				}
				for (Var v : model.getProcs().get(i).getVarInt()){
					if (stateInts.get(model.getProcDecls().get(i)+v.getName()) != null && !n.getStateInts().get(model.getProcDecls().get(i)+v.getName()).equals(stateInts.get(model.getProcDecls().get(i)+v.getName())))
						return false;
				}
			}
			//System.out.println(this.hashCode()==n.hashCode());
			return true;
		}
		return false;
	}

	public ModelState clone(){
		ModelState n = new ModelState();
		n.model = model;
		n.state = new HashMap<String,Boolean>();
		n.stateEnums = new HashMap<String,String>();
		n.stateInts = new HashMap<String,Integer>();
		//n.isFaulty = isFaulty;
		for (Var v : model.getSharedVars()){
			if (v.getType().isEnumerated())
				n.getStateEnums().put(v.getName(),stateEnums.get(v.getName()));
			else
				if (v.getType().isInt())
					n.getStateInts().put(v.getName(),stateInts.get(v.getName()));
				else
					n.getState().put(v.getName(),state.get(v.getName()));
		}
		for (int i=0; i < model.getProcDecls().size(); i++){
			for (Var v : model.getProcs().get(i).getVarBool()){
				n.getState().put(model.getProcDecls().get(i)+v.getName(),state.get(model.getProcDecls().get(i)+v.getName()));
			}
			for (Var v : model.getProcs().get(i).getVarEnum()){
				n.getStateEnums().put(model.getProcDecls().get(i)+v.getName(),stateEnums.get(model.getProcDecls().get(i)+v.getName()));
			}
			for (Var v : model.getProcs().get(i).getVarInt()){
				n.getStateInts().put(model.getProcDecls().get(i)+v.getName(),stateInts.get(model.getProcDecls().get(i)+v.getName()));
			}
		}
		return n;
	}

	public void evalInit(Expression e, int procIndex){
		HashMap<String,Boolean> st = new HashMap<String,Boolean>();
		HashMap<String,String> stEnum = new HashMap<String,String>();
		HashMap<String,Integer> stInt = new HashMap<String,Integer>();
		evalExprInit(e, false, st, stEnum, stInt, procIndex);
		stateEnums.putAll(stEnum);
		state.putAll(st);
		stateInts.putAll(stInt);
	}

	private Var instanciateIfParam(Var v, int procIndex){
		Proc proc = model.getProcs().get(procIndex);
		String processName = model.getProcDecls().get(procIndex);
		for (int i=0;i < proc.getParamList().size();i++){
			if (v.getName().equals(proc.getParamList().get(i).getDeclarationName()) && proc.getInvkParametersList(processName).get(i) instanceof Var){
				v = (Var)proc.getInvkParametersList(processName).get(i);
				break;
			}
		}
		return v;
	}

	private boolean checkGlobalVar(Var v){
		for (Var gv : model.getSharedVars()){
			if (v.getName().equals(gv.getName()))
				return true;
		}
		return false;
	}

	private void evalExprInit(Expression e, boolean neg, HashMap<String,Boolean> st, HashMap<String,String> stEnum, HashMap<String,Integer> stInt, int procIndex){
		if (e instanceof Var){
			Var e_ = (Var)e;
			e_ = instanciateIfParam(e_,procIndex);
			if (checkGlobalVar(e_)){ //global
				if (neg)
					st.put(e_.getName(),false);
				else
					st.put(e_.getName(),true);
			}
			else{ //local
				if (neg)
					st.put(model.getProcDecls().get(procIndex)+e_.getName(),false);
				else
					st.put(model.getProcDecls().get(procIndex)+e_.getName(),true);
			}
		}
		if (e instanceof NegBoolExp){
			NegBoolExp e_ = (NegBoolExp)e;
			evalExprInit(e_.getExp(),!neg,st,stEnum,stInt,procIndex);
		}
		if (e instanceof AndBoolExp){
			AndBoolExp e_ = (AndBoolExp)e;
			evalExprInit(e_.getExp1(),neg,st,stEnum,stInt,procIndex);
			evalExprInit(e_.getExp2(),neg,st,stEnum,stInt,procIndex);
		}
		if (e instanceof EqBoolExp){
			EqBoolExp e_ = (EqBoolExp)e;
			Var v = (Var)e_.getExp1();
			String procName = model.getProcDecls().get(procIndex);
			if (e_.getExp2() instanceof Var) //then its an enum
				stEnum.put(checkGlobalVar(v)?v.getName():procName+v.getName(), ((Var)e_.getExp2()).getName());
			else
				if (((Var)e_.getExp1()).getType().isInt()){ //its an int
					stInt.put(checkGlobalVar(v)?v.getName():procName+v.getName(), evalIntExpr(e_.getExp2(), procIndex));
				}
				else //its a bool*/
					st.put(checkGlobalVar(v)?v.getName():procName+v.getName(), ((ConsBoolExp)e_.getExp2()).getValue());
		}
	}

	private Integer evalIntExpr(Expression e, int procIndex){
		if (e instanceof ConsIntExp){
			ConsIntExp e_ = (ConsIntExp)e;
			return e_.getValue();
		}
		if (e instanceof Var){
			Var e_ = (Var)e;
			e_ = instanciateIfParam(e_,procIndex);
			if (checkGlobalVar(e_)){
				return stateInts.get(e_.getName());
			}
			else
				return stateInts.get(model.getProcDecls().get(procIndex)+(e_.getName()));
		}
		if (e instanceof NegIntExp){
			NegIntExp e_ = (NegIntExp)e;
			//return evalIntExpr(e_.getExp(),procIndex) * (-1);
			return evalIntExpr(e_.getExp1(),procIndex) - evalIntExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof MultIntExp){
			MultIntExp e_ = (MultIntExp)e;
			return evalIntExpr(e_.getExp1(),procIndex) * evalIntExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof SumIntExp){
			SumIntExp e_ = (SumIntExp)e;
			return evalIntExpr(e_.getExp1(),procIndex) + evalIntExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof DivIntExp){
			SumIntExp e_ = (SumIntExp)e;
			return evalIntExpr(e_.getExp1(),procIndex) / evalIntExpr(e_.getExp2(),procIndex);
		}
		return 0;
	}

	/*private Double evalDoubleExpr(Expression e, int procIndex){
		if (e instanceof ConsDoubleExp){
			ConsDoubleExp e_ = (ConsDoubleExp)e;
			return e_.getValue();
		}
		if (e instanceof Var){
			Var e_ = (Var)e;
			return model.getConstants().get(e_.getName());
		}
		if (e instanceof NegIntExp){
			NegIntExp e_ = (NegIntExp)e;
			//return evalIntExpr(e_.getExp(),procIndex) * (-1);
			return evalDoubleExpr(e_.getExp1(),procIndex) - evalDoubleExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof MultIntExp){
			MultIntExp e_ = (MultIntExp)e;
			return evalDoubleExpr(e_.getExp1(),procIndex) * evalDoubleExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof SumIntExp){
			SumIntExp e_ = (SumIntExp)e;
			return evalDoubleExpr(e_.getExp1(),procIndex) + evalDoubleExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof DivIntExp){
			SumIntExp e_ = (SumIntExp)e;
			return evalDoubleExpr(e_.getExp1(),procIndex) / evalDoubleExpr(e_.getExp2(),procIndex);
		}
		return 0.0;
	}*/

	private boolean evalBoolExpr(Expression e, int procIndex){
		if (e instanceof ConsBoolExp){
			ConsBoolExp e_ = (ConsBoolExp)e;
			return e_.getValue();
		}
		if (e instanceof Var){
			Var e_ = (Var)e;
			e_ = instanciateIfParam(e_,procIndex);
			if (checkGlobalVar(e_)){
				//System.out.println(e_.getName());
				//System.out.println(state.get(e_.getName()));
				return state.get(e_.getName());
			}
			else{
				return state.get(model.getProcDecls().get(procIndex)+(e_.getName()));
			}
		}
		if (e instanceof NegBoolExp){
			NegBoolExp e_ = (NegBoolExp)e;
			return !evalBoolExpr(e_.getExp(),procIndex);
		}
		if (e instanceof AndBoolExp){
			AndBoolExp e_ = (AndBoolExp)e;
			return evalBoolExpr(e_.getExp1(),procIndex) && evalBoolExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof OrBoolExp){
			OrBoolExp e_ = (OrBoolExp)e;
			return evalBoolExpr(e_.getExp1(),procIndex) || evalBoolExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof EqBoolExp){
			EqBoolExp e_ = (EqBoolExp)e;
			if (e_.getExp2() instanceof Var){ //check if its an Enum Eq
				Var value = (Var)e_.getExp2();
				if (value.isEnumValue() && e_.getExp1() instanceof Var){
					Var var = instanciateIfParam((Var)e_.getExp1(),procIndex);
					if (checkGlobalVar(var)){
						return stateEnums.get(var.getName()).equals(value.getName());
					}
					else{
						return stateEnums.get(model.getProcDecls().get(procIndex)+var.getName()).equals(value.getName());
					}
				}
			}
			if (e_.getExp2() instanceof NumExp || (e_.getExp2() instanceof Var && ((Var)e_.getExp2()).getType().isInt())){
				return evalIntExpr(e_.getExp1(),procIndex).equals(evalIntExpr(e_.getExp2(),procIndex));
			}
			return evalBoolExpr(e_.getExp1(),procIndex) == evalBoolExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof GreaterBoolExp){
			GreaterBoolExp e_ = (GreaterBoolExp)e;
			return evalIntExpr(e_.getExp1(),procIndex) > evalIntExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof LessBoolExp){
			LessBoolExp e_ = (LessBoolExp)e;
			return evalIntExpr(e_.getExp1(),procIndex) < evalIntExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof GreaterOrEqualBoolExp){
			GreaterOrEqualBoolExp e_ = (GreaterOrEqualBoolExp)e;
			return evalIntExpr(e_.getExp1(),procIndex) >= evalIntExpr(e_.getExp2(),procIndex);
		}
		if (e instanceof LessOrEqualBoolExp){
			LessOrEqualBoolExp e_ = (LessOrEqualBoolExp)e;
			return evalIntExpr(e_.getExp1(),procIndex) <= evalIntExpr(e_.getExp2(),procIndex);
		}
		return false;
	}

	public ModelState createSuccessor(LinkedList<Code> assigns, int procIndex){
		ModelState succ = clone();
		for (Code c : assigns){
			if (c instanceof VarAssign){
				VarAssign assign = (VarAssign)c;
				Var var = instanciateIfParam(assign.getVar(),procIndex);
				String name = var.getName();
				if (var.getType().isBoolean()){
					Boolean value = evalBoolExpr(assign.getExp(),procIndex);
					if (checkGlobalVar(var))
						succ.getState().put(name,value);
					else
						succ.getState().put(model.getProcDecls().get(procIndex)+name,value);
				}
				if (var.getType().isInt()){
					Integer value = evalIntExpr(assign.getExp(),procIndex);
					if (checkGlobalVar(var))
						succ.getStateInts().put(name,value);
					else
						succ.getStateInts().put(model.getProcDecls().get(procIndex)+name,value);
				}
				if (var.getType().isEnumerated()){
					Var exp = (Var) assign.getExp();
					String value = exp.getName();
					if (checkGlobalVar(var))
						succ.getStateEnums().put(name,value);
					else
						succ.getStateEnums().put(model.getProcDecls().get(procIndex)+name,value);
				}			
			}
		}
		return succ;
	}

	public void update(LinkedList<Code> assigns, int procIndex){
		for (Code c : assigns){
			this.updateSingleVar(c,procIndex);
		}
	}

	private void updateSingleVar(Code c, int procIndex){
		if (c instanceof VarAssign){
			VarAssign assign = (VarAssign)c;
			Var var = instanciateIfParam(assign.getVar(),procIndex);
			String name = var.getName();
			if (var.getType().isBoolean()){
				Boolean value = evalBoolExpr(assign.getExp(),procIndex);
				if (checkGlobalVar(var))
					this.getState().put(name,value);
				else
					this.getState().put(model.getProcDecls().get(procIndex)+name,value);
			}
			if (var.getType().isInt()){
				Integer value = evalIntExpr(assign.getExp(),procIndex);
				if (checkGlobalVar(var))
					this.getStateInts().put(name,value);
				else
					this.getStateInts().put(model.getProcDecls().get(procIndex)+name,value);
			}
			if (var.getType().isEnumerated()){
				Var exp = (Var) assign.getExp();
				String value = exp.getName();
				if (checkGlobalVar(var))
					this.getStateEnums().put(name,value);
				else
					this.getStateEnums().put(model.getProcDecls().get(procIndex)+name,value);
			}
			
		}
	}

}