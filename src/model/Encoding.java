package model;

import java.io.Serializable;

public class Encoding implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private String code;
    private char symbol;
    
    
    public Encoding(String code, char symbol) {
        this.code = code;
        this.symbol = symbol;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public char getSymbol() {
        return symbol;
    }
    
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    } 
}
