package com.example.damkaprojectadi;

public class Position {
    private int startRow;
    private int startCol;
    private int lastRow;
    private int lastCol;

    public Position() {
        // חייבים ליצור פעולה בונה ריקה בשביל הפיירבייס
    }

    public Position(int startRow, int startCol, int lastRow, int lastCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.lastRow = lastRow;
        this.lastCol = lastCol;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getLastCol() {
        return lastCol;
    }

    public void setLastCol(int lastCol) {
        this.lastCol = lastCol;
    }
}
