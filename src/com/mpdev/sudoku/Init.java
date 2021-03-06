package com.mpdev.sudoku;

public class Init {
  
   static int[][] sudokuArray = {
     // put your SuDoku data here
      { 0,0,7,  0,0,0,  0,0,4 },
      { 0,0,0,  0,2,1,  5,0,0 },
      { 8,6,0,  0,0,0,  0,0,0 },

      { 0,0,0,  6,0,9,  0,7,1 },
      { 0,0,3,  7,0,4,  8,0,0 },
      { 4,7,0,  2,0,8,  0,0,0 },

      { 0,0,0,  0,0,0,  0,3,5 },
      { 0,0,2,  9,6,0,  0,0,0 },
      { 3,0,0,  0,0,0,  7,0,0 }
   };
  
   static int [][] initData() {
     return sudokuArray;
   }

} // class Init
