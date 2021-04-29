package parserProgram;

import java_cup.runtime.*;
%%

%public
%cup
%line
%column
%char
%full

%eofval{
return  new Symbol(symLang.EOF,yyline,yycolumn,"");
%eofval}

%class scannerLang
%{
	public scannerLang(java.io.InputStream r, SymbolFactory sf){
		this(r);
		this.sf=sf;
	}
	private SymbolFactory sf;
%}



digit=[0-9]
char=[a-zA-Z]
id = {char}({char}|{digit}|"_")*	
integer = {digit}{digit}*	
double =  1|(0\.({digit}+))
comment=("//"(.)*)|"/*"(((([^*/])*"*"[^/])*)|((([^*])*"/"[^*/])*)|([^*/])*)*"*/"
/* To ignore */
end_of_line   = \r|\n|\r\n
white_space     = {end_of_line} | [ \t\f]

%%


 
{comment} {}

"+" {   //System.out.println("SUMA:"+ yytext()); 
        return new Symbol(symLang.PLUS,yyline,yycolumn,yytext());
    }
"++" {   //System.out.println("SUMASUMA:"+ yytext()); 
        return new Symbol(symLang.PLUSPLUS,yyline,yycolumn,yytext());
    }
    
"-" {    //System.out.println("RESTA:"+ yytext()); 
        return new Symbol(symLang.MINUS,yyline,yycolumn, yytext());
    }
"*" {    //System.out.println("PROD:"+ yytext());
        return new Symbol(symLang.ASTERISK,yyline,yycolumn,yytext());
    }
    
"/" {    //System.out.println("DIV:"+ yytext());
        return new Symbol(symLang.SLASH,yyline,yycolumn,yytext());
    }
    
"(" {   //System.out.println("LPARENT:"+ yytext());
        return new Symbol(symLang.LPARENT,yyline,yycolumn,yytext());
    }
    
")" {   //System.out.println("RPARENT:"+ yytext());
        return new Symbol(symLang.RPARENT,yyline,yycolumn,yytext());
    }
    
";" {  //System.out.println("PUNTOYCOMA: "+ yytext());
        return new Symbol(symLang.SEMICOLON,yyline,yycolumn,yytext());
    }
    
":" {    //System.out.println(" DOSPUNTOS: "+ yytext());
        return new Symbol(symLang.COLON,yyline,yycolumn,yytext());
    }
    
"->" {  //System.out.println("FLECHA: "+ yytext());
        return new Symbol(symLang.RIGHTARROW,yyline,yycolumn,yytext());
     }
"=" {    //System.out.println(" ASIG:"+ yytext());
        return new Symbol(symLang.EQUAL,yyline,yycolumn,yytext());
    }
    
"==" { //System.out.println(" IGUAL:"+ yytext());
       return new Symbol(symLang.DOUBLE_EQUAL,yyline,yycolumn,yytext());
     }
     
"," {   //System.out.println(" COMA:"+ yytext());
        return new Symbol(symLang.COMMA,yyline,yycolumn,yytext());
    }
    
"||" {   //System.out.println(" OR:"+ yytext());
        return new Symbol(symLang.OR,yyline,yycolumn,yytext());
     }
     
"&&" {   //System.out.println(" AND:"+ yytext());
        return new Symbol(symLang.AND,yyline,yycolumn,yytext());
     }
     
">" {    //System.out.println("MAYOR :"+ yytext());
        return new Symbol(symLang.GT,yyline,yycolumn,yytext());
    }
    
"<" {    //System.out.println(" MENOR :"+ yytext());
        return new Symbol(symLang.LT,yyline,yycolumn,yytext());
    }

">=" {    //System.out.println("MAYORIGUAL :"+ yytext());
        return new Symbol(symLang.GEQ,yyline,yycolumn,yytext());
    }
    
"<=" {    //System.out.println(" MENORIGUAL :"+ yytext());
        return new Symbol(symLang.LEQ,yyline,yycolumn,yytext());
    }
    
"!" {    //System.out.println(" NEG:"+ yytext());
        return new Symbol(symLang.EXCLAMATION,yyline,yycolumn,yytext());
    }
    
"{" {   //System.out.println(" LLAVEABRE:"+ yytext());
        return new Symbol(symLang.LBRACE,yyline,yycolumn,yytext());
    }

"}" {    //System.out.println(" LLAVECIERRA:"+ yytext());
        return new Symbol(symLang.RBRACE,yyline,yycolumn,yytext());
    }

"[" {  //System.out.println(" CORCHETE ABRE:"+ yytext());
        return new Symbol(symLang.LBRACKET,yyline,yycolumn,yytext());
    }

"]" {    //System.out.println(" CORCHETE CIERRA:"+ yytext());
        return new Symbol(symLang.RBRACKET,yyline,yycolumn,yytext());
    }
    
"true"  {   //System.out.println(" TRUE :"+ yytext()); 
           return new Symbol(symLang.TRUE,yyline,yycolumn, yytext());
         }
         
"false"  {  //System.out.println(" FALSE :"+ yytext());
           return new Symbol(symLang.FALSE,yyline,yycolumn, yytext());
         }
         
"BOOL"  { //System.out.println(" BOOL:"+ yytext());
           return new Symbol(symLang.BOOL,yyline,yycolumn,yytext());
        }
        
"INT" {  //System.out.println(" INT:"+ yytext());
         return new Symbol(symLang.INT,yyline,yycolumn,yytext());
      }

      
"Global" { //System.out.println(" GLOBAL :"+ yytext());
           return new Symbol(symLang.GLOBAL,yyline,yycolumn,yytext());
        }

      
"Enum" { //System.out.println(" ENUM :"+ yytext());
           return new Symbol(symLang.ENUM,yyline,yycolumn,yytext());
        }


"Initial" { //System.out.println(" INIT:"+ yytext());
            return new Symbol(symLang.INIT,yyline,yycolumn,yytext());
          }

"Process" { //System.out.println(" PROCESS :"+ yytext());
            return new Symbol(symLang.PROCESS,yyline,yycolumn,yytext());
          }
       

"Main" {  //System.out.println(" MAIN :"+ yytext());
          return new Symbol(symLang.MAIN,yyline,yycolumn,yytext());
       }

"Rewards" {  //System.out.println(" REWARDS :"+ yytext());
          return new Symbol(symLang.REWARDS,yyline,yycolumn,yytext());
       }

"Controller" {  //System.out.println(" CONTROLLER :"+ yytext());
          return new Symbol(symLang.CONTROLLER,yyline,yycolumn,yytext());
       }

"Goal" {  //System.out.println(" GOAL :"+ yytext());
          return new Symbol(symLang.GOAL,yyline,yycolumn,yytext());
       }


"run" {   //System.out.println(" RUN:"+ yytext());
          return new Symbol(symLang.RUN,yyline,yycolumn,yytext());
       }
          
"faulty"  {  //System.out.println("FAULTY :  "+ yytext());
         return new Symbol(symLang.FAULTY,yyline,yycolumn,yytext());
      }

"internal"  {  //System.out.println("INTERNAL :  "+ yytext());
         return new Symbol(symLang.INTERNAL,yyline,yycolumn,yytext());
      } 
          
{id}  {  //System.out.println("ID :  "+ yytext());
         return new Symbol(symLang.ID,yyline,yycolumn,yytext());
      }	
 
       
{integer} {  //System.out.println(" VALORINT:"+ yytext());
            Integer value = new Integer(yytext());
            return new Symbol(symLang.INTEGER,yyline,yycolumn,value);
          }

{double} {  //System.out.println(" VALORPROB:"+ yytext());
            Double value = new Double(yytext());
            return new Symbol(symLang.DOUBLE,yyline,yycolumn,value);
          }
          
{white_space} {/* Ignore */} 

.     {   //return new Symbol(symLang.LEXICAL_ERROR,yyline,yycolumn,yytext());
       }