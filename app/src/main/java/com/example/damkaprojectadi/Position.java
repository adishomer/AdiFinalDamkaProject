package com.example.damkaprojectadi;

public class Position {
    private int newLine;
    private int newCol;
    private int lastLine;
    private int lastCol;

    public Position() {
        // חייבים ליצור פעולה בונה ריקה בשביל הפיירבייס
    }

    public Position(int newLine, int newCol, int lastLine, int lastCol) {
        this.newLine = newLine;
        this.newCol = newCol;
        this.lastLine = lastLine;
        this.lastCol = lastCol;
    }

    public int getNewLine() {
        return newLine;
    }

    public void setNewLine(int newLine) {
        this.newLine = newLine;
    }

    public int getNewCol() {
        return newCol;
    }

    public void setNewCol(int newCol) {
        this.newCol = newCol;
    }

    public int getLastLine() {
        return lastLine;
    }

    public void setLastLine(int lastLine) {
        this.lastLine = lastLine;
    }

    public int getLastCol() {
        return lastCol;
    }

    public void setLastCol(int lastCol) {
        this.lastCol = lastCol;
    }
}
