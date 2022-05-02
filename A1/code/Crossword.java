import java.io.*;
import java.util.*;


public class Crossword 
{
    public static void main(String [] args) throws IOException
    {
        MyDictionary dict = new MyDictionary();
        
        Scanner fileScan = new Scanner(new FileInputStream(args[0]));
		String st;
		StringBuilder sb;
		
		while (fileScan.hasNext())
		{
			st = fileScan.nextLine();
			dict.add(st);
            // testing if function reads in dict8.txt correctly
            // System.out.println(st);
		}
        Scanner fileScan1 = new Scanner(new FileInputStream(args[1]));
        // for how big the board is, read from txt file
        // data structure for holding the board
        int boardNum = Integer.parseInt(fileScan1.nextLine()); 
      
        char[][] board = new char[boardNum][boardNum];
        for (int i = 0; i < boardNum; i++)
        {
            String temp = fileScan1.next();
            for (int p = 0; p < temp.length(); p++)
            {
                board[i][p] = temp.charAt(p); 
            }
        } 
        /*
        for (int i = 0; i < boardNum; i++)
        {
            for (int o = 0; o < boardNum; o++)
            {
                System.out.print(board[i][o]);
                
            }
            System.out.println();
        }*/

        // size is from boardnum
        StringBuilder[] colStr = new StringBuilder[]{};
        StringBuilder[] rowStr = new StringBuilder[]{};
    }
}