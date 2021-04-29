package lang;

import java.util.*;


public class Main extends ProgramNode{
	
	LinkedList<ProcessDecl> processDecl;
	LinkedList<InvkProcess> processInvk;
	
	public Main(){
		processDecl = new LinkedList<ProcessDecl>();
		processInvk = new LinkedList<InvkProcess>();
		
	}
	

	public Main(LinkedList<ProcessDecl> pDecl,LinkedList<InvkProcess> pInvk){
		processDecl = pDecl;
		processInvk = pInvk;
		
	}
	
	/**
	 * 
	 * @param name
	 * @return the type of the process with that name, null otherwise.
	 */
	public String getProcessType(String name){
		boolean found =false;
		int i = 0;
		
		
		while ( i < processDecl.size() && !found ){
			
			if (processDecl.get(i).getName().equals(name)){
				found =true;
				return processDecl.get(i).getType();
			}
			i++;
	    }
		
		return null;
		
	} 
	
	public boolean existProcessDecl(String name){
		boolean found =false;
		int i = 0;
		
		while ( i < processDecl.size() && !found ){
			if (processDecl.get(i).getName().equals(name)){
				found =true;
			}
			i++;
	    }
		
		return found;
		
	} 
	
	public boolean isCorrectInvk(){
		boolean correct =true;
		int i = 0;
		
		while ( i < processInvk.size() ){
			
			correct = correct && existProcessDecl(processInvk.get(i).getInstanceName());
			i++;
	    }
		
		return correct;
		
	} 
	
	public boolean isCorrectDeclProcess(ProcessCollection processCollection){
		boolean correct =true;
		int i = 0;
		
		while ( i < processDecl.size() ){
			
			correct = correct && processCollection.existProcess(processDecl.get(i).getType());
			i++;
	    }
		
		return correct;
		
	}
	
	public LinkedList<InvkProcess> getProcessInvk(){
		return processInvk;	
	}
	
	public LinkedList<ProcessDecl> getProcessDecl(){
		return processDecl;
	}
	
	public void accept(LangVisitor v){
	     v.visit(this);			
	}
	
	
}
