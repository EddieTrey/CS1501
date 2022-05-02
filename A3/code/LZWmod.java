/*************************************************************************
 *  Compilation:  javac LZWmod.java
 *  Execution:    java LZWmod - < input.txt   (compress)
 *  Execution:    java LZWmod + < input.txt   (expand)
 *  Dependencies: BinaryStdIn.java BinaryStdOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/

public class LZWmod {
    private static final int R = 256;           // number of input chars
    //no longer final because they have to be dynamic
    private static int L = 512;                 // number of codewords = 2^W
    private static int W = 9;                   // codeword width
    private static final int max_W = 16;        //max codeword width
    private static final int max_L = 65536;     //max num of codeword, 2^W aka 2^16
    private static char reset = 'n';            //reset value that is taken from arg[1]
                                                //is written to lzw by compress() then read in by expand()
    private static boolean flag = false;        //flag for when we run out of codewords

    public static void compress() {
        TSTmod<Integer> st = new TSTmod<Integer>();
        for (int i = 0; i < R; i++)
            st.put(new StringBuilder("" + (char) i), i);
        int codewordNum = R+1;  // R is codeword for EOF

        //write the reset value to the file 
        //Note to Self - MUST CAST TO BYTE or the ascii will get written in instead
        BinaryStdOut.write((byte)reset);
        StringBuilder current = new StringBuilder();
        char c = BinaryStdIn.readChar();
        current.append(c);
        Integer codeword = st.get(current);

        while (!BinaryStdIn.isEmpty()) 
        {
            codeword = st.get(current);
            c = BinaryStdIn.readChar(); 
            //TODO: read and append the next char to current
            current.append(c);

            if(!st.contains(current))
            {
                BinaryStdOut.write(codeword, W);

                //increment codewordnum if it isn't at max and if we still have code words left <- based on flag boolean
                if (codewordNum == L && flag == false) 
                {
                    //increments codeword width and if width is max, sets boolean so that we know codewords are used up
                    //if width is not at max, we double number of codewords
                    W++;
                    if (W == max_W)
                        flag = true;
                    else
                        L = L * 2;
                }
                //if codewords are used up and 'r' was given as an arguement, we reset our dictionary
                if (flag == true && reset == 'r')
                {
                    flag = false; //reset flag
                    L = 512;
                    W = 9;
                    codewordNum = R + 1;
                    st = new TSTmod<Integer>();
                    for (int i = 0; i < R; i++)                     
                        st.put(new StringBuilder("" + (char) i), i);
                }
                if (codewordNum < L)    // Add to symbol table if not full
                    st.put(current, codewordNum++);
              //TODO: reset current
              current = new StringBuilder();
              current.append(c);
            }
        }

        //TODO: Write the codeword of whatever remains
        //in current
        BinaryStdOut.write(st.get(current), W); //new 
        BinaryStdOut.write(R, W); //Write EOF
        BinaryStdOut.close();
    }


    public static void expand() {
        String[] st = new String[max_L];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        i = R + 1;
        reset = BinaryStdIn.readChar();
        //decompression first byte
        //Note to self: This test saved us because we didn't cast the 'n'/'r' char to byte in compress()
        //and expand() didn't know what to do.
        if (reset != 'r' && reset != 'n') 
            throw new IllegalArgumentException("Not valid reset arguement");

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);

            //same logic as compress: if codewords is at max
            //we set flag boolean to true
            //if not at max, we double the number of codewords
            if (i == L && flag == false)
            {
                //increments codeword width and if width is max, sets boolean so that we know codewords are used up
                //if width is not at max, we double number of codewords
                W++;
                if (W == max_W)
                    flag = true;
                else
                    L = L * 2;
            }
            //flag = true means we're out of codewords
            //reset has to be r. If it is n, we don't reset the dictionary
            if (flag == true && reset == 'r')
            {
                flag = false;
                L = 512;
                W = 9;
                i = R;
                
                st = new String[max_L];
                //for (int i = 0; i < R; i++)                     
                //  st.put(new StringBuilder("" + (char) i), i);
                for (i = 0; i < R; i++)
                    st[i] = ("" + (char) i);
                i++;
                st[i] = "";
            }

            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;

        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        /*java:148: error: incompatible types: 
          String cannot be converted to char
          reset = args[1];
        */
        //Note to self: (args.length != 0) could mean 1,5,or even 10 arguements
        if (args.length > 1)
            reset = args[1].charAt(0);
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argumentzzzz");
    }

}
