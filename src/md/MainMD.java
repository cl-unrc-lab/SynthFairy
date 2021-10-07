package md;


import lang.*;
import core.*;


public class MainMD {
     
    
    public static void main(String[] args) throws InterruptedException
    {
       ProgramParser prog = new ProgramParser();
       boolean printTrace = false;
       boolean toDot = false;
       boolean startSimulation = false;
       boolean deadlockIsError = false;
       boolean noBisim = false;
       boolean deterministic = false;
       boolean verbose = false;
       int precision = 10;
       int bound = Integer.MAX_VALUE;
       int algorithm = 0;
       
      if (args.length < 1){
        printHelp();
      }
       else{
            Program test = prog.parseAux(args[args.length - 1]);
            for (int i = 0; i < args.length; i++){
              if (args[i].equals("-d")){
                toDot = true;
              }
              if (args[i].equals("-v")){
                verbose = true;
              }
              if (args[i].startsWith("p=")){
                String[] splits = args[i].split("=");
                precision = Integer.parseInt(splits[1]);
              }
              if (args[i].startsWith("b=")){
                String[] splits = args[i].split("=");
                bound = Integer.parseInt(splits[1]);
              }
            }
            StrategySynthesis md = new StrategySynthesis(test,verbose);
            try{
              if (toDot)
                md.createDot(200);
                System.out.println("Expected Total Reward: "+md.synthesizeStrategy(precision,bound));
                System.out.println("A strategy for the Controller has been synthesized! --> out/<filename>.strat");
            }
            catch(Exception e){
              System.out.println(e);
            }
         
                   
          
      }
   }

   private static void printHelp(){
    System.out.println("SynthFairy: Controller Synthesis Tool\n");
    System.out.println("Usage: ./synthFairy <options> <model path>\n");
    System.out.println("Options:");
    System.out.println("            -d : create dot file");
    System.out.println("            -v : turn verbosity on");
    System.out.println("            p=<num> : use precision <num> for real numbers");
    System.out.println("            b=<num> : set upper bound for value iteration");
   }
}