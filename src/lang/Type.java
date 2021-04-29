package lang;

public enum Type {
	INT,
	BOOL,
  ENUMERATED,
  DOUBLE,
	UNDEFINED,
	ERROR;

    private String stringValue;

	@Override
	public String toString() {
        switch(this) {
            case INT:
                return new String("INT");
                
            case BOOL:
                return new String("BOOL");

            case DOUBLE:
                return new String("DOUBLE");

            case UNDEFINED:
                return new String("UNDEFINED");
                
            case ERROR:
                return new String("ERROR");
                
            case ENUMERATED:
                return new String("ENUMERATED");
                
        }

        return new String("NONE");
    }


    public boolean isBoolean() {
		if (this == Type.BOOL) {
			return true;
		}

		return false;
	}

	public boolean isInt() {
		if (this == Type.INT) {
			return true;
		}
		
		return false;
	}

  public boolean isDouble() {
    if (this == Type.DOUBLE) {
      return true;
    }
    
    return false;
  }
	
	public boolean isUndefined() {
		if (this == Type.UNDEFINED) {
			return true;
		}
		
		return false;
	}


    public boolean isEnumerated() {
       if (this == Type.ENUMERATED) {
           return true;
       }
       return false;
    }

    /**
     * @return The name of the type. 
     *  e.g : Type.INT --> "INT", 
     *        Type.ENUMERATED --> "<Name of the enumerated>",etc
     **/
    public String getStringValue(){
        return stringValue;
    }

    /**
    * Set the name of the type only if is an Enumerated, if not rewrites the original types.
    **/
    public void setStringValue(String val){
        switch(this) {
            case INT:
                      stringValue= new String("INT");
                      break;
            case BOOL:
                      stringValue= new String("BOOL");
                      break;
            case DOUBLE:
                      stringValue= new String("DOUBLE");
                      break;
            case UNDEFINED:
                      stringValue= new String("UNDEFINED");
                      break;
                      
            case ERROR:
                      stringValue= new String("ERROR");
                      break;
            case ENUMERATED:
                      stringValue= new String(val); //copy el value of the enumerated type.
                      break;
        }
        
    }

}
