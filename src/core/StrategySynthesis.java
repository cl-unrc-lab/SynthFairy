package core;

import java.util.*;

import lang.*;
import model.*;
import games.*;

import java.io.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class StrategySynthesis{

	private SimpleGameGraph g; // The masking distance game graph, undefined until buildGraph is called
  private Program p;
  private boolean verbose;

    public StrategySynthesis(Program prog, boolean verb) throws InterruptedException{
        p = prog;
        verbose = verb;
        buildGraph();
    }

    public SimpleGameGraph getG(){
        return g;
    }

    public void buildGraph() throws InterruptedException{
        Model m;
        System.out.println("Building Model...");
        m = p.toGraph(true);
        if (verbose){
          System.out.println("Model states: "+m.getNumNodes());
          System.out.println("Model edges: "+m.getNumEdges());
        }
        //m.createDot(true);
        System.out.println("Building Game Graph...");
        g = new SimpleGameGraph();

        //calculate initial state
        SimpleGameNode init = new SimpleGameNode(m.getInitial(),new Action("", false, false, false) , false);
        g.addNode(init);
        g.setInitial(init);

        TreeSet<SimpleGameNode> iterSet = new TreeSet<SimpleGameNode>();
        iterSet.add(init);

        //build the game graph
        while(!iterSet.isEmpty()){
            SimpleGameNode curr = iterSet.pollFirst();
            if (!curr.isProbabilistic()){ // player C or E turn
              for (ModelState succ : m.getSuccessors(curr.getState())){
                  Pair p = new Pair(curr.getState(),succ);
                  if (m.getActions().get(p) != null){
                      for (int i=0; i < m.getActions().get(p).size(); i++){
                          SimpleGameNode curr_ = null;
                          if (m.getActions().get(p).get(i).getIsProbabilistic()){ //then we create a probabilistic node (has same data as curr)
                              curr_ = new SimpleGameNode(curr.getState(),m.getActions().get(p).get(i), true);
                          }
                          else{ //non probabilistic, so curr_ is C or E
                              curr_ = new SimpleGameNode(succ,m.getActions().get(p).get(i), false);
                          }
                          
                          SimpleGameNode toOld = g.search(curr_);
                          if (toOld == null){
                              g.addNode(curr_);
                              g.addEdge(curr,curr_, new Action("", false, false, false)); 
                              iterSet.add(curr_);
                          }
                          else{
                              g.addEdge(curr,toOld, new Action("", false, false, false));
                          }
                      }
                  }
              }
            }
            else{ // player P turn
                for (ModelState succ : m.getSuccessors(curr.getState())){
                    if (curr.getState().getModel().getProb(curr.getState(),succ,curr.getSymbol()) != null){
                        SimpleGameNode curr_ = null;
                        curr_ = new SimpleGameNode(succ,new Action("", false, false, false), false);
                        SimpleGameNode toOld = g.search(curr_);
                        if (toOld == null){
                            g.addNode(curr_);
                            g.addEdge(curr,curr_, new Action("", false, false, false)); 
                            iterSet.add(curr_);
                        }
                        else{
                            g.addEdge(curr,toOld, new Action("", false, false, false));
                        }
                    }
                }
            }
            

            
        }
        if (verbose)
          System.out.println("Game graph states: "+g.getNumNodes());
    }


  public double synthesizeStrategy(int precision, double upperBound) throws Exception{
      SimpleGameNode init =  g.getInitial();
      boolean forceExit = false;
      int i = 0;
      for (SimpleGameNode v : g.getNodes()) {
          if (v.getIsGoal()) {
              v.setValue(1,0);
          }
          else {
              v.setValue(1,upperBound); 
          }
      }
      do {
          i++;
          for (SimpleGameNode v : g.getNodes()) {
              v.setValue(0,v.getValues()[1]);
          }
          for (SimpleGameNode v : g.getNodes()) {
              if (forceExit)
                    break;
              double val = 0;
              switch (v.getPlayerControl()){
                case 2:  double minVal = minValue(v,g.getSuccessors(v));
                         //val = v.getReward() + (minVal < v.getValues()[0]?minVal:v.getValues()[0]); //Environment
                         val = v.getReward() + minVal;
                         break;
                case 1:  double maxVal = maxValue(v,g.getSuccessors(v));
                         //val = v.getReward() + (maxVal > v.getValues()[0]?maxVal:v.getValues()[0]); //Controller
                         val = v.getReward() + maxVal;
                         break;
                case 3:  try{
                            val = sumProbs(v,g.getSuccessors(v)); //Probabilistic
                            } 
                            catch (Exception e){
                              System.out.println("error en P");
                            }
                            break;
                default: break;
              } 
              v.setValue(1,val);
              if (v.equals(init) && verbose) {
                    System.out.println("Initial state value at iteration "+i+": "+v.getValues()[1]);
              }
          }   
      }  while (thereIsNoFixPoint(precision) && !forceExit);
      resolveStrategyConflicts();
      createControllerStrategy();
      //createEnvironmentStrategy();
      return init.getValues()[1];
  }
  
  private boolean thereIsNoFixPoint(int precision) {
      String decimalFormat = "#.";
      for (int i = 0; i < precision; i++){
        decimalFormat += "#";
      }
      DecimalFormat newFormat = new DecimalFormat(decimalFormat);
      for (SimpleGameNode v : g.getNodes()){  
          double currValue =  Double.valueOf(newFormat.format(v.getValues()[1]));
          double oldValue = Double.valueOf(newFormat.format(v.getValues()[0]));
         if (currValue != oldValue) {
             return true;
         }
      }
      return false;
  }

  private boolean thereIsNoFixPoint2(int precision) {
      String decimalFormat = "#.";
      for (int i = 0; i < precision; i++){
        decimalFormat += "#";
      }
      DecimalFormat newFormat = new DecimalFormat(decimalFormat);
      for (SimpleGameNode v : g.getNodes()){  
          double currValue =  Double.valueOf(newFormat.format(v.getExpectedDecisions()[1]));
          double oldValue = Double.valueOf(newFormat.format(v.getExpectedDecisions()[0]));
         if (currValue != oldValue) {
             return true;
         }
      }
      return false;
  }

  private double minValue(SimpleGameNode current, Set<SimpleGameNode> vs) throws Exception{
    double min = Double.POSITIVE_INFINITY;
    for (SimpleGameNode v : vs){
      double val = v.getValues()[0];
      if (val < min){
        min = val;
        current.setStrategy(v);
      }
    }
    return min;
  }


  private double maxValue(SimpleGameNode current, Set<SimpleGameNode> vs) throws Exception{
    double max = Double.NEGATIVE_INFINITY;
    for (SimpleGameNode v : vs){
        double val = v.getValues()[0];
        if (val > max){
          max = val;
          current.setStrategy(v);
        }
    }
    return max;
  }

  private double sumProbs(SimpleGameNode v, Set<SimpleGameNode> vs) throws Exception{
    double sum = 0;
    for (SimpleGameNode v_ : vs){
      sum += v.getState().getModel().getProb(v.getState(),v_.getState(),v.getSymbol()) * v_.getValues()[0];
    }
    return sum;
  }

  private double sumProbs2(SimpleGameNode v, Set<SimpleGameNode> vs) throws Exception{
    double sum = 0;
    for (SimpleGameNode v_ : vs){
      sum += v.getState().getModel().getProb(v.getState(),v_.getState(),v.getSymbol()) * v_.getExpectedDecisions()[0];
    }
    return sum;
  }

  public String createControllerStrategy(){
    String res = "";
    for (SimpleGameNode v : g.getNodes()){
      if (v.isController() && !v.getState().getVisited()){
        v.getState().setVisited(true);
        res += "In state: " + v.getState() + "\n\n";
        //res += "        move to : " + v.getStrategy().getState() + "\n\n";
        res += "        move by action : " + v.getStrategy().getSymbol() + "\n\n\n";
      }
    }
    try{
        File file = new File("out/" + p.getName() +".strat");
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

  public void resolveStrategyConflicts(){
      LinkedList<SimpleGameNode> q = new LinkedList<SimpleGameNode>();
      for (SimpleGameNode v : g.getNodes()){
        v.setVisited(false);
        if (v.getIsGoal())
          q.add(v);
      }
      while (!q.isEmpty()){
        SimpleGameNode current = q.removeFirst();
        current.setVisited(true);
        //System.out.println(current);
        for (SimpleGameNode v : g.getPredecessors(current)){
          //System.out.println("----------------------"+v);
          if (!v.getVisited()){
            //System.out.println("preStrat:"+v.getStrategy());
            //System.out.println("curr:"+current);
            if (!v.isProbabilistic()){
              if (v.getStrategy() != current && v.getStrategy().getValues()[1].equals(current.getValues()[1])){
                //System.out.println("AAAAAAAA");
                v.setStrategy(current);    
              }
            }
            if (!q.contains(v))
              q.add(v);
          }
        }
      }
  }

  public String createEnvironmentStrategy(){
    String res = "";
    for (SimpleGameNode v : g.getNodes()){
      if (v.isEnvironment() && !v.getState().getVisited()){
        v.getState().setVisited(true);
        res += "In state: " + v.getState() + "\n\n";
        //res += "        move to : " + v.getStrategy().getState() + "\n\n";
        res += "        move by action : " + v.getStrategy().getSymbol() + "\n\n\n";
      }
    }
    try{
        File file = new File("../out/" + p.getName() +".astrat");
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

  private boolean canMoveBothWays(SimpleGameNode v){
    boolean right = false;
    boolean left = false;
    for (SimpleGameNode v_ : g.getSuccessors(v)){
      if (v_.getSymbol().getLabel().endsWith("r_r"))
        right = true;
      if (v_.getSymbol().getLabel().endsWith("r_l"))
        left = true;
    }
    return right && left;
  }
  
  public Double expectedNumberOfDecisions(int precision){
    SimpleGameNode init =  g.getInitial();
    for (SimpleGameNode v : g.getNodes()) {
        v.setExpectedDecisions(1,0); 
    }
    do {
        for (SimpleGameNode v : g.getNodes()) {
            v.setVisited(false); 
            v.setExpectedDecisions(0,v.getExpectedDecisions()[1]);
        }
        LinkedList<SimpleGameNode> q = new LinkedList<SimpleGameNode>();
        q.add(init);
        while (!q.isEmpty()){
          SimpleGameNode v = q.removeFirst();
          v.setVisited(true);
          double val = 0;
          switch (v.getPlayerControl()){
                case 1:  //Controller
                         if (v.getStrategy() != null){
                            val = ((canMoveBothWays(v))?1:0)+v.getStrategy().getExpectedDecisions()[0];
                            if (!v.getStrategy().getVisited() && !q.contains(v.getStrategy()))
                                q.add(v.getStrategy());
                          }
                         else
                            val = 0;
                         break;
                case 2:  //Environment
                         if (v.getStrategy() != null){
                            val = v.getStrategy().getExpectedDecisions()[0];
                            if (!v.getStrategy().getVisited() && !q.contains(v.getStrategy()))
                                q.add(v.getStrategy());
                          }
                          else
                            val = 0;
                         break;
                case 3:  try{
                            val = sumProbs2(v,g.getSuccessors(v)); //Probabilistic
                            for (SimpleGameNode v_ : g.getSuccessors(v)){
                                  if (!v_.getVisited() && !q.contains(v_))
                                      q.add(v_);
                              }
                            } 
                            catch (Exception e){
                              System.out.println("error en P");
                            }
                            break;
                default: break;
          }
          v.setExpectedDecisions(1,val);
          //if (v.equals(init)) {
          //          System.out.println(v.getExpectedDecisions()[1]);
          //    }
        }  
    }  while (thereIsNoFixPoint2(precision));
    return init.getExpectedDecisions()[1];
  }



    public void createDot(int lineLimit){
        g.createDot(lineLimit, p.getName());
    }

}