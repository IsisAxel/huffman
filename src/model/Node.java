package model;

public class Node 
{
    private Node left;    
    private Node rigth;
    private int probability;
    private char symbol;
    private String code;

    public Node(int probability, char symbol) {
        this.probability = probability;
        this.symbol = symbol;
    }

    public Node(Node left, Node rigth, int probability) {
        this.left = left;
        this.rigth = rigth;
        this.probability = probability;
    }

    public Node(Node left, Node rigth, int probability, char symbol, String code) {
        this.left = left;
        this.rigth = rigth;
        this.probability = probability;
        this.symbol = symbol;
        this.code = code;
    }

    public Node() {
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRigth() {
        return rigth;
    }

    public void setRigth(Node rigth) {
        this.rigth = rigth;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }    
}
