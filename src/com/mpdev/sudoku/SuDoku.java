package com.mpdev.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class SuDoku
 * defines a SuDoku gave as a 9x9 array of integers
 * numbers 1-9 are allowed as array elements (the puzzle squares) number 0 means that the square is blank
 * can be initialised either from a file or from a 9x9 integer array (the contents of Class Init)
 * solves the puzzle by "brute force" i.e. by trying every allowed number in each empty square
 * 
 * @author Marinos Pappas
 *
 */
public class SuDoku {
  
    public static final int ROWS = 9;
    public static final int COLS = 9;
  
    private static int count = 0;   // the count of the attempts

    //
    // private class variables
    //
    private final int[][] data = new int[ROWS][COLS];  // our initial SuDoku puzzle
    
    private boolean dataComplete;   // the state of our SuDoku puzzle (used when initialising from file)
    private final int[][] solution = new int[ROWS][COLS];  // our solution to the puzzle (or attempted solution)
    private final int[] nextEmpty = new int[2];    // the coordinates of the next empty square (top left is 0,0)
    private boolean debug = false;

    /**
     * Constructor from file
     * 9 lines, 9 numbers (1-9) or 'x' or '-' for empty square) in each line
     * empty lines and spaces to improve the look of the file are allowed
     * 
     * @param filename		the String filename containing the SuDoku puzzle
     */
    SuDoku (String filename) {

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
                    for (int charIndx=0; j < COLS && charIndx < charsRead.length; ++charIndx) {
                        if (charsRead[charIndx] == 'x' || charsRead[charIndx] == 'X' || charsRead[charIndx] == '-')
                            data[i][j++] = 0;
                        else
                            if (charsRead[charIndx] >= '0' && charsRead[charIndx] <= '9')
                                data[i][j++] = charsRead[charIndx]-'0';
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

    /**
     * Constructor form integer 2xdimensional array
     * numbers 1-9 or 0 for empty square
     * 
     * @param inputData		the 9x9 int[][] array containing the SuDoku puzzle
     */
    SuDoku (int[][] inputData) {
        for (int i=0; i < ROWS; ++i) 
            System.arraycopy (inputData[i], 0, data[i], 0, COLS);
    } // SuDoku (int[][] inputData)
 
    /**
     * Returns the state of the SuDoku data (used when initialising from file)
     * 
     * @return		True if SuDoku data is loaded (all 9x9 squares with 1-9 or 0), false otherwise
     */
    boolean isDataComplete() {
        return dataComplete;
    } // boolean isDataComplete() 
 
    /**
     * Prints the SuDoku contents
     */
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

    /**
     * Prints the SuDoku solution
     */
    void printSolution() {
        for (int i = 0; i < ROWS; ++i) {
            if (i > 0 && i%3 == 0)
                System.out.println();
                for (int j = 0; j < COLS; ++ j) {
                    if (j > 0 && j%3 == 0)
                        System.out.print("   ");
                    System.out.print(solution[i][j]+" ");
                }
            System.out.println();
        } 
    } // printSolution()

    /**
     * Returns the count of the attempts to solve the puzzle
     * @return		count of attempts
     */
    static int getCount() {
        return count;
    } // int getCount()

    /**
     * Sets the debug flag so that the methods in this class print debugging information
     */
    void setDebug() {
        debug = true;
    } // void setDebug()

    /**
     * Tries to solve the SuDoku puzzle
     * 
     * @return		the SuDoku object that contains the solution or null if unsolvable
     */
    SuDoku tryToSolve () {

    	// get a new SuDoku instance and copy this data into it
		SuDoku s = new SuDoku(data);

		// find the next empty square in the 9x9 matrix
        if (s.findNextEmpty(nextEmpty)) { 
        	// increase the attempt count and print a '.'
        	++count;
        	System.out.print(".");
        	if (count%100 == 0) System.out.println();
        	
        	//try all possible numbers 1-9 in the empty square
        	for (int num=1; num <= 9; ++num) {	

        		// if number is acceptable
        		if (okInRow(num, nextEmpty[0]) &&	okInCol(num, nextEmpty[1]) &&	okInSquare(num, nextEmpty)) {    
        			// set the empty square to this number
        			s.setSquare(nextEmpty, num);    
        			// and try to solve this and keep the result in a new object
        			SuDoku s1;
        			if ((s1 = s.tryToSolve()) != null) {   // if solved
        				// Point *A*
        				// this is where we will get the solved SuDoku from the recursively called this same method
        				// we just have to pass the solution to the caller so that eventually will be returned to the method which called us in the first place
        	        	// pass the solution from the solved SuDoku we just got to this instance so that it can be passed upwards
        	            for (int i=0; i < ROWS; ++i) 
        	                System.arraycopy (s1.solution[i], 0, this.solution[i], 0, COLS); 
        				// and finally return the solved object to the top level caller - this where effectively we return to the calling method from
        				return this;                 
        			}
        			// else just continue with the next number
        		}
        		
        	}
        	// all numbers failed - return fail
        	return null;
        }
        else {
        	// if no more empty squares, we have solved it - this is where the recursion ENDS
        	// save the solution from the solved SuDoku data to this instance
            for (int i=0; i < ROWS; ++i) 
                System.arraycopy (s.data[i], 0, this.solution[i], 0, COLS); 
            // and return the solved sudoku object 
            // this method reaches this point only once when the recursion ends and the solved SuDOku is returned for the first time to the caller 
            // which is the same method itself Point *A*
            return this;
        }
        
                
    } // SuDoku tryToSolve()
  
    /**
     * Set a SuDoku square to a number
     * 
     * @param coord		the coordinates of the SuDoku square
     * @param num		the number to set it to
     */
    void setSquare (int[] coord, int num) {
        data[coord[0]][coord[1]] = num;
    } // void setSquare (int num, int[] coord)

    /**
     * Finds the next empty square in the 9x9 matrix
     * 
     * @param coord		returns the coordinates of the next empty square
     * @return			true if an empty square was found, false otherwise
     */
    boolean findNextEmpty (int[] coord) {
        for (int i=0; i < ROWS; ++i)
            for (int j=0; j < COLS; ++j)
                if (data[i][j] == 0) {
                    // found empty square
                    coord[0] = i;
                    coord[1] = j;
                    return true;
                }
        // no empty square found
        return false;
    } // boolean findNextEmpty (int[] coord)

    /**
     * Checks if a specific number is acceptable within a specific row
     * 
     * @param number	the number to be checked
     * @param row		the row against which the number will be checked
     * @return			true if the number does not already exist in the row
     */
    private boolean okInRow (int number, int row) {
        for (int i=0; i < COLS; ++i)
            if (number == data[row][i])
                // if number found in the row return not ok
                return false;
        // otherwise return ok
        return true;
    } // private boolean okInRow (int number, int row)

    /**
     * Checks if a specific number is acceptable within a specific column
     * 
     * @param number	the number to be checked
     * @param col		the column against which the number will be checked
     * @return			true if the number does not already exist in the column
     */
    private boolean okInCol (int number, int col) {
        for (int i=0; i < ROWS; ++i)
            if (number == data[i][col])
                // if number found in the column return not ok
                    return false;
        // otherwise return ok
        return true;
    } // private boolean okInCol (int number, int col)

    /**
     * Checks if a specific number is acceptable within the 3x3 square
     * 
     * @param number	the number to be checked
     * @param coord		the coordinates of the number in question in the 9x9 square
     * @return			true if the number does not already exist in the 3x3 square
     */
    private boolean okInSquare (int number, int[] coord) {
    	// calculate the co-ordinates of the top-left of the 3x3 square where the input co-ordinates fall into
        int startX = (coord[0] / 3) * 3;
        int startY = (coord[1] / 3) * 3;
        // check the input number against all the existing numbers in the 3x3 square
        for (int i=0; i < 3; ++i)
            for (int j=0; j < 3; ++j)
                if (number == data[startX+i][startY+j])
                    // if number found in the square return not ok
                	return false;
        // otherwise return ok
        return true;
    } // private boolean okInSquare (int number, int[] coord)

} // class SuDoku

