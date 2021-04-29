package lang;




/**
 * A class that implements the boolean expresion int1 = int2
 */

public class EqBoolExp extends BoolExp{

Expression exp1; // the left int
Expression exp2; // the right int
boolean createBiimp; // is true in case of comparates two boolean expression,
                     // indicates to the buildervisitor if is necesary to creates a biimp for represent its.
    
boolean isEnumerated; // is true in case of comparates two enumerated expression,
String enumType; // Name of the enumerated type
    
/**
 * Constructor, it takes the two integers composing hte equality
 * @param	int1	the left integer
 * @param	int2	the right integer
 */
public EqBoolExp(Expression int1, Expression int2){
	super();
    this.exp1 = int1;
    this.exp2 = int2;
    createBiimp = false;
    isEnumerated = false;
    enumType = null;
}

public void setCreateBiimp(boolean value){
	this.createBiimp = value;
}

public void setIsEnumerated(boolean value){
        this.isEnumerated = value;
}
    
    
public boolean getCreateBiimp(){
	return this.createBiimp;
}

public boolean isEnumerated(){
        return this.isEnumerated;
}
    
public void setEnumType(String eName){
    this.enumType = eName;
}
    
public String getEnumType(){
    return this.enumType;
}

public Expression getExp1(){
    return exp1;
}

public Expression getExp2(){
    return exp2;
}
    
    
@Override
public void accept(LangVisitor v){
     v.visit(this);			
}

}