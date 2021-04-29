package model;
import java.util.*;

//Utility class
//TODO: make it generic
public class Triple {
	
	private Object fst;
	private Object snd;
	private Object thrd;

	public Triple(Object f, Object s, Object t){
	  fst = f;
	  snd = s;
	  thrd = t;
	}

	public Object getFst(){
		return fst;
	}

	public Object getSnd(){
		return snd;
	}
	public Object getThrd(){
		return thrd;
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof Triple){
			Triple p = (Triple)o;
			return p.getFst().equals(fst) && p.getSnd().equals(snd) && p.getThrd().equals(thrd);
		}
		return false;
	}

	public String toString(){
		return "("+fst.toString()+","+snd.toString()+","+thrd.toString()+")";
	}


	@Override
	public int hashCode(){
	    return Objects.hash(fst, snd, thrd);
	}

}