
package com.mpdev.sudoku;

import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SolveSudoku {

  public static void main(String[] args) {
	  (new SolveSudoku()).run(args);
  }
  
  public void run(String[] args) {

    SuDoku mySuDoku;
    Calendar tStart, tEnd;

    if (args.length > 0) {              // filename specified
        mySuDoku = new SuDoku(args[0]);	// initialise SuDoku from file
        if (!mySuDoku.isDataComplete()) {
            System.out.println("sudoku data is not complete");
                return;
        }
    }
    else
        mySuDoku = new SuDoku(Init.initData()); // else initialise from init int array

    // mySuDoku.setDebug(); // set debug mode

    System.out.println("Trying to solve:");
    mySuDoku.printData();

    tStart = Calendar.getInstance();
    System.out.println(tStart.getTime().toString());

    SuDoku solvedSuDoku;
    if ((solvedSuDoku = mySuDoku.tryToSolve()) != null) {
        System.out.println("");
        System.out.println(SuDoku.getCount()+" attempts");
        System.out.println("SuDoku was solved successfully");
        System.out.println("-----------------------");
        solvedSuDoku.printSolution();
        System.out.println("-----------------------");
    }
    else {
        System.out.println("");
        System.out.println(mySuDoku.getCount()+" attempts");
        System.out.println("No sulotion was found");
    }
    tEnd = Calendar.getInstance();
    long duration = tEnd.getTimeInMillis() - tStart.getTimeInMillis();
    System.out.println(tEnd.getTime().toString());
    System.out.println("Solution took: " + duration + " msec");
  }
}
