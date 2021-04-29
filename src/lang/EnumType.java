package lang;
import java.util.*;


/**
 * This class contains all the information about an enumerable type
 * this is achieved by using hashmap that relates enumtypes with their size, and 
 * identifiers with their number
 *
 */
public class EnumType extends ProgramNode{
	String name; // the name of the enum type
	String[] cons; // the constants belonging to the type
	int size; // the size of the enumerable type
    int numVars; // the number of variables of this type in the program

	
	/**
	 * A basic constructor
	 * @param name
	 * @param size
	 */
	public EnumType(String name, int size){
		this.name = name;
		this.size = size;
		cons = new String[size];
        numVars = 0;
	}
	
	/**
	 * 
	 * @return the name of the type
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return the size
	 */
	public int getSize(){
		return size;
	}
	
    /**
	 *
	 * @return the numVars
	 */
	public int getNumVars(){
		return numVars;
	}
	
    
    
    /**
	 *
	 * @param name
	 * @return the true is the name corresponding to a given constant, false otherwise.
	 *
	 */
	public boolean existConstant(String name){
		for (int i=0; i < size; i++){
			if (cons[i].equals(name))
				return true;
		}
		// no id for the name
		return false;
	}
	
	
	/**
	 * 
	 * @param name
	 * @return the id (integer) corresponding to a given constant in the case of a wrong constant it returns -1
	 * 
	 */
	public int getConsId(String name){
		for (int i=0; i < size; i++){
			if (cons[i].equals(name))
				return i;
		}
		// no id for the name
		return -1;
	}
    
    /**
	 *
	 * 
     * Increments in 1 the number of variables of this type in the program	 
	 */
    public void incrementNumVar(){
        numVars = numVars + 1;
    }
    
    /**
	 *
	 * @param num : number of variables of this type in the program
	 *
	 */
    public void setNumVars(int num){
        numVars = num;
    }
    
    /**
	 *
	 * @param name
	 * @return the name corresponding to a given position constant in the case of a wrong constant it returns null
	 *
	 */
	public String getCons(int pos){
		for (int i=0; i < size; i++){
			if (i==pos)
				return cons[i];
		}
		// no pos for the name
		return null;
	}
	

	
	
	/**
	 * Adds a constant in the enum type
	 * @param name	the name of the constant
	 * @param pos the position ( 0 .. size-1)
	 * @pre pos < size and pos >=0
	 */
	public void addCons(String name, int pos){
		cons[pos] = name;
	}
    
    @Override
	public void accept(LangVisitor v){
        v.visit(this);
	}

	
	
}
