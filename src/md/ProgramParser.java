package md;

import java_cup.runtime.*;
import lang.*;

import java.io.*;
import java.util.LinkedList;

import parserProgram.*;

/**
 * This class represents the compiler.
 */
public class ProgramParser {
	
	private parserLang parser;	// Parser
	private static LinkedList<lang.Error> errorList; // Errors
    private static SymbolsTable symbolsTable;
    private static FileReader programFile;
    
    
    public ProgramParser(){
    	this.programFile=null;
    	this.symbolsTable =null;
    	this.errorList= null;
    }
    
    


    public Program parseAux(String NameFile){
        try {
            programFile = new FileReader(NameFile);
        
            // Read file
            parser = new parserLang(new scannerLang(programFile));
            Program program = (Program)parser.parse().value;
            String [] splits = NameFile.split("/"); 
            program.setName(splits[splits.length-1]);
            
            // Check Types
            Type result = checkTypes(program);
            if(result == Type.ERROR){
                for(int i=0; i<errorList.size(); i++){
                    System.out.println(errorList.get(i).getErrorMsg());
                }               
            }
            else
                return program; 
        } catch (Exception e) {         
            System.out.println("Program Error." + e.getMessage());
            e.printStackTrace(System.out);
        }
        
        return null;
        
    }

    /**
     * 
     * @return Return the symbols table of the model.
     */
    public static SymbolsTable getSymbolsTable(){
    	return symbolsTable;
    }
    
 	

 	/**
 	 * Check types
 	 */
 	private static Type checkTypes(Program prog) {
        TypeCheckerVisitor typeV = new TypeCheckerVisitor();
        prog.accept(typeV);
        Type result = typeV.getType();
        errorList = typeV.getErrorList();
        symbolsTable =typeV.getSymbolTable();
        return result;
 	}
 	
}