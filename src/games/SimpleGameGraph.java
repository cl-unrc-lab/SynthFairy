package games;

import java.util.*;
import model.*;
import java.io.*;

public class SimpleGameGraph{

	private HashMap<SimpleGameNode, HashSet<SimpleGameNode>> succList; // Successors adjacency list
	private HashMap<SimpleGameNode, HashSet<SimpleGameNode>> preList; // Predecessors adjacency list
	private HashMap<Pair, LinkedList<Action>> actions; // actions for edges
	private SimpleGameNode initial; // Initial state
	private LinkedList<SimpleGameNode> nodes; // States
	private int numNodes;
	private int numEdges;
	private int numTransitions;


	public SimpleGameGraph() {
		succList = new HashMap<SimpleGameNode, HashSet<SimpleGameNode>>();
		preList = new HashMap<SimpleGameNode, HashSet<SimpleGameNode>>();
		actions = new HashMap<Pair, LinkedList<Action>>();
		numNodes = numEdges = numTransitions = 0;
		nodes = new LinkedList<SimpleGameNode>();
	}

	public int getNumNodes(){
		return numNodes;
	}

	public int getNumEdges(){
		return numEdges;
	}

	public int getNumTransitions(){
		return numEdges;
	}

	public void setInitial(SimpleGameNode v){
		initial = v;
	}

	public SimpleGameNode getInitial(){
		return initial;
	}

	public HashMap<Pair, LinkedList<Action>> getActions(){
		return actions;
	}

	public void addNode(SimpleGameNode v) {
		nodes.add(v);
		succList.put(v, new HashSet<SimpleGameNode>());
		preList.put(v, new HashSet<SimpleGameNode>());
		numNodes += 1;
	}

	public SimpleGameNode search(SimpleGameNode v) {
		for (SimpleGameNode node : nodes){
			if (node.equals(v))
				return node;
		}
		return null;
	}

	public boolean hasNode(SimpleGameNode v) {
		return nodes.contains(v);
	}


	public boolean hasEdge(SimpleGameNode from, SimpleGameNode to, Action a) {
		if (!hasNode(from) || !hasNode(to))
			return false;
		Pair transition = new Pair(from,to);
		if (actions.get(transition) == null)
			return false;
		return actions.get(transition).contains(a);
	}


	public void addEdge(SimpleGameNode from, SimpleGameNode to, Action a) {
		if (to != null){
			if (hasEdge(from, to, a))
				return;
			numEdges += 1;
			if (!from.isProbabilistic())
				numTransitions += 1;
			succList.get(from).add(to);
			preList.get(to).add(from);
			Pair transition = new Pair(from,to);
			if (actions.get(transition) == null){
				actions.put(transition, new LinkedList<Action>());
			}
			actions.get(transition).add(a);
		}
	}

	public LinkedList<SimpleGameNode> getNodes(){
		return nodes;
	}

	public HashSet<SimpleGameNode> getSuccessors(SimpleGameNode v){
		return succList.get(v);
	}

	public HashSet<SimpleGameNode> getPredecessors(SimpleGameNode v){
		return preList.get(v);
	}

	public String createDot(int lineLimit, String name){
		String res = "digraph model {\n\n";
		res += "    node [style=filled];\n";
		int lineCount = 0;
		for (SimpleGameNode v : nodes){
			//if (lineCount > lineLimit)
			//	break;
			lineCount++;
			if (v.getPlayerControl()==1)
				res += "    "+ v.getId() +" [label=\""+v.toStringDot()+"\",color=\"lightblue\"];\n";
			if (v.getPlayerControl()==3)
				res += "    "+ v.getId() +" [label=\""+v.toStringDot()+"\",color=\"orange\"];\n";
			if (v.getPlayerControl()==2)
				if (v == initial)
					res += "    "+ v.getId() +" [label=\""+v.toStringDot()+"\",color=\"pink\"];\n";
				else
					res += "    "+ v.getId() +" [label=\""+v.toStringDot()+"\",color=\"grey\"];\n";
			if (v.getIsGoal())
				res += "    "+v.getId()+" [label=\""+v.toStringDot()+"\",color=\"red\"];\n";
			for (SimpleGameNode u : succList.get(v)){
				Pair edge = new Pair(v,u);
				if (actions.get(edge) != null)
					for (int i=0; i < actions.get(edge).size(); i++){
						if (actions.get(edge).get(i).isMask())
							res += "    "+v.getId()+" -> "+ u.getId() +" [color=\"green\"]"+";\n";
						else
							if (actions.get(edge).get(i).isFaulty())
								res += "    "+v.getId()+" -> "+ u.getId() +" [color=\"red\"]"+";\n";
							else
								if (actions.get(edge).get(i).isTau())
									res += "    "+v.getId()+" -> "+ u.getId() + " [style=dashed]"+";\n";
								else
									res += "    "+v.getId()+" -> "+ u.getId() + ";\n";
					}			
			}
		}
		res += "\n}";
		try{
            File file = new File("../out/" + name +".dot");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(res);
            bw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
		return res;
	}

}