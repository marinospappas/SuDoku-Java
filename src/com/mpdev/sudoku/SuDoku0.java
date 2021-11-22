package com.mpdev.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SuDoku0 {

	    public static final int ROWS = 9;
	    public static final int COLS = 9;
	  
	    static int count = 0;

	    //
	    // private class variables
	    //
	    private final int[][] data = new int[ROWS][COLS];
	    private boolean dataComplete;
	    static int[][] solution = new int[ROWS][COLS];
	    private final int[] nextEmpty = new int[2];
	    private boolean debug = false;

	    //
	    // constructor methods
	    //
	    // 1. constructor from file
	    //
	    SuDoku0 (String filename) {

	        dataComplete = false;

	        try {
	            // open file
	            FileReader infile = new FileReader (filename);
	            BufferedReader textLine = new BufferedReader(infile);			

	            // read 9 non-empty lines
	            int i=0;
	            String s;
	            do {
	                s = textLine.readLine();
	                if (s != null) {
	                    char[] charsRead = s.toCharArray();
	                    int j = 0;
	                    for (int cj=0; j < COLS && cj < charsRead.length; ++cj) {
	                        if (charsRead[cj] == 'x' || charsRead[cj] == 'X' || charsRead[cj] == '-')
	                            data[i][j++] = 0;
	                        else
	                            if (charsRead[cj] >= '0' && charsRead[cj] <= '9')
	                                data[i][j++] = charsRead[cj]-'0';
	                    }
	                    if (j == COLS)
	                        ++i; 
	                }
	            } while (s != null && i < ROWS);
	            if (i == ROWS)
	                dataComplete = true;
	            // close file and return
	            textLine.close();
	        }
	        catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
	    } // SuDoku (String filename)

	    //
	    // 2. constructor from int array
	    //
	    SuDoku0 (int[][] inputData) {
	        for (int i=0; i < ROWS; ++i) 
	            System.arraycopy (inputData[i], 0, data[i], 0, COLS);
	    } // SuDoku (int[][] inputData)
	 
	    // return data complete flag
	    boolean isDataComplete() {
	        return dataComplete;
	    } // boolean isDataComplete() 
	 
	    // print SuDoku contents
	    void printData() {
	        for (int i = 0; i < ROWS; ++i) {
	            if (i > 0 && i%3 == 0)
	                System.out.println();
	            for (int j = 0; j < COLS; ++ j) {
	                if (j > 0 && j%3 == 0)
	                    System.out.print("   ");
	                if (data[i][j] == 0)
	                    System.out.print("x ");
	                else
	                    System.out.print(data[i][j]+" ");
	            }
	            System.out.println();
	        } 
	    } // void printData()

	    // print SuDoku solution
	    void printSolution() {
	        for (int i = 0; i < ROWS; ++i) {
	            if (i > 0 && i%3 == 0)
	                System.out.println("");
	                for (int j = 0; j < COLS; ++ j) {
	                    if (j > 0 && j%3 == 0)
	                        System.out.print("   ");
	                    System.out.print(solution[i][j]+" ");
	                }
	            System.out.println();
	        } 
	    } // printSolution()

	    // get count of solve attempt
	    int getCount() {
	        return count;
	    } // int getCount()

	    // set the debug flag
	    void setDebug() {
	        debug = true;
	    } // void setDebug()

	    //
	    // main method that solves the sudoku puzzle
	    // tries each empty square with all valid numbers and calls itself recurssively until 
	    //     there are no more empty squares
	    // solve SuDoku
	    //
	    boolean tryToSolve () {
	        if (debug) System.out.println("trying to solve");

	        if (!findNextEmpty(nextEmpty)) {        // if no more enpty squares
	            if (debug) System.out.println("no empty square found - returning true");
	            for (int i=0; i < ROWS; ++i) 
	                System.arraycopy (data[i], 0, solution[i], 0, COLS); // save solution
	            System.out.println("attempt: "+count);
	            return true;        // we have solved it!
	        }

	        ++count;
	        System.out.print(".");
	        if (count%100 == 0) System.out.println();
	        if (debug) {
	            System.out.println();
	            System.out.println("found empty square in position "+nextEmpty[0]+","+nextEmpty[1]);
	        }
	        for (int num=1; num <= 9; ++num) {	//try all numbers 1-9 in the empty square
	            if (debug) System.out.println("trying number "+num);
	            if (debug && !okInRow(num, nextEmpty[0])) System.out.println("number not ok in row");
	            if (debug && !okInCol(num, nextEmpty[1])) System.out.println("number not ok in column");
	            if (debug && !okInSquare(num, nextEmpty)) System.out.println("number not ok in square");
	            if (okInRow(num, nextEmpty[0]) &&
	                okInCol(num, nextEmpty[1]) &&
	                okInSquare(num, nextEmpty)) {        // if number is acceptable
	                    SuDoku0 s = new SuDoku0(data);     // get a new sudou instance
	                    s.setSquare(num, nextEmpty);     // set the square to this number
	                    if (debug) {
	                        s.setDebug();
	                        System.out.println("number ok - trying to solve this one");
	                        s.printData();
	                    }
	                    boolean result = s.tryToSolve(); // and try to solve it
	                    if (result)
	                        return true;                 // if solved, return success!
	            }
	        }
	        // all numbers failed - return fail
	        if (debug) System.out.println("all numbers failed - returning false");
	        return false;
	    } // booloean tryToSolve()
	  
	    // set specific square to number
	    void setSquare (int num, int[] coord) {
	        data[coord[0]][coord[1]] = num;
	    } // void setSquare (int num, int[] coord)

	    // find next empty square
	    boolean findNextEmpty (int[] coord) {
	        for (int i=0; i < ROWS; ++i)
	            for (int j=0; j < COLS; ++j)
	                if (data[i][j] == 0) {
	                    // found empty square
	                    coord[0] = i;
	                    coord[1] = j;
	                    return true;
	                }
	        // no emtpy square found
	        return false;
	    } // boolean findNextEmpty (int[] coord)

	    // checks to see if a given number is valid
	    //
	    // 1. check if number is ok along the row
	    //
	    private boolean okInRow (int number, int row) {
	        for (int i=0; i < COLS; ++i)
	            if (number == data[row][i])
	                // if number found in the row return not ok
	                return false;
	        // otherwise return ok
	        return true;
	    } // private boolean okInRow (int number, int row)

	    //
	    // 2. check if number is ok along the column
	    //
	    private boolean okInCol (int number, int col) {
	        for (int i=0; i < ROWS; ++i)
	            if (number == data[i][col])
	                // if number found in the column return not ok
	                    return false;
	        // otherwise return ok
	        return true;
	    } // private boolean okInCol (int number, int col)

	    //
	    // 3. check if number is ok in the square
	    //
	    private boolean okInSquare (int number, int[] coord) {
	        int startX = (coord[0] / 3) * 3;
	        int startY = (coord[1] / 3) * 3;
	        for (int i=0; i < 3; ++i)
	            for (int j=0; j < 3; ++j)
	                if (number == data[startX+i][startY+j])
	                    // if number found in the square return not ok
	                        return false;
	        // otherwise return ok
	        return true;
	    } // private boolean okInSquare (int number, int[] coord)
	    
} // class SuDoku0
