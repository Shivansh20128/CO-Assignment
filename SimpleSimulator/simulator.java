import java.util.HashMap;
import java.util.Scanner;
import java.lang.Math;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException; // Import the IOException class to handle errors

public class simu { // gives what code is it and sends it respective fn
    static Scanner scan = new Scanner(System.in);
    static int ProgramCounter = 0;// Program Counter
    static int cycle = 0;
    static String x_cycles = "";
    static String y_memAddr = "";
    // Hashmaps
    // contains codes of regs and their value as integer
    // FINAL?
    static final HashMap<String, Integer> regFile = new HashMap<String, Integer>();// Register File
    // wont mem_addr as a 256 array be better?
    static final HashMap<String, String> memAddr = new HashMap<String, String>();// Memory
    // string1-key= 8 bit addr/PC string2-value= 16 bit instruction
    // Flags
    // overflow(V)=8 less than(L)=4 greater than(G)=2 Equal (E)=1
    // should be .replace() instead of .put()

    static void resetFlag() {
        regFile.put("111", 0);
    }

    static void setV() {
        regFile.put("111", 8);
    }

    static void setL() {
        regFile.put("111", 4);
    }

    static void setG() {
        regFile.put("111", 2);
    }

    static void setE() {
        regFile.put("111", 1);
    }

    // decides what command the binary stands for
    static void commandIns(String s) {// Excecution Engine
        if (s.substring(0, 5).equals("00000")) {// add
            // calls add fn // add fn has input as string//input is complete string
            addc(s);
        }
        if (s.substring(0, 5).equals("00001")) {// sub
            subc(s);
        }
        if (s.substring(0, 5).equals("00010")) {// mov imm
            movic(s);
        }
        if (s.substring(0, 5).equals("00011")) {// mov reg
            movrc(s);
        }
        if (s.substring(0, 5).equals("00100")) {
            loadc(s);
        }
        if (s.substring(0, 5).equals("00101")) {
            storec(s);
        }
        if (s.substring(0, 5).equals("00110")) {
            mulc(s);
        }
        if (s.substring(0, 5).equals("00111")) {
            divc(s);
        }
        if (s.substring(0, 5).equals("01000")) {
            rightShift(s);
        }
        if (s.substring(0, 5).equals("01001")) {
            leftShift(s);
        }
        if (s.substring(0, 5).equals("01010")) {
            xor(s);
        }
        if (s.substring(0, 5).equals("01011")) {
            or(s);
        }
        if (s.substring(0, 5).equals("01100")) {
            and(s);
        }
        if (s.substring(0, 5).equals("01101")) {
            not(s);
        }
        if (s.substring(0, 5).equals("01110")) {
            cmp(s);
        }
        if (s.substring(0, 5).equals("01111")) {
            jmp(s);
        }
        if (s.substring(0, 5).equals("10000")) {
            jlt(s);
        }
        if (s.substring(0, 5).equals("10001")) {
            jgt(s);
        }
        if (s.substring(0, 5).equals("10010")) {
            je(s);
        }
    }

    // errors like; FLAGS being used with instructions other than mov, decode the
    // bin
    static void loadc(String s) {/// type D opc=5 reg=3 mem=8
        // put reg code and value at a particular addr
        // loads from memory into reg file
        if (s.substring(5, 8).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        regFile.put(s.substring(5, 8), bin_to_int(memAddr.get(s.substring(8, 16)))); // variable
        x_cycles = x_cycles + cycle + " ";
        y_memAddr = y_memAddr + bin_to_int(s.substring(8, 16)) + " ";
        resetFlag();
        printcommand();
    }

    static void storec(String s) {// type D
        // stores from reg into memory at that addr
        // put mem_Address, value at that register(as bin str)
        //
        if (s.substring(5, 8).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS reg");
            // exit code
        }
        memAddr.put(s.substring(8, 16), Int_to_Bin16bit(regFile.get(s.substring(5, 8))));
        x_cycles = x_cycles + cycle + " ";
        y_memAddr = y_memAddr + bin_to_int(s.substring(8, 16)) + " ";
        resetFlag();
        printcommand();
    }

    static int power(int m) {// gives powers of 2
        // generate error if reg value is -ve or there is an overflow, the valuen of reg
        // becomes 0?
        int result = 1;
        for (int i = 0; i < m; i++) {
            result = result * 2;
        }
        return result;
    }

    static int bin_to_int(String s) {// bin to int convertor
        int retnum = 0;
        int count = 0;
        for (int i = s.length() - 1; i >= 0; i--) {

            int k = Integer.parseInt(s.substring(i, i + 1));
            retnum = retnum + k * (power(count));
            // Math.pow()
            count++;
        }
        return retnum;
    }

    static void movic(String s) {// opcode//type B
        if (s.substring(5, 8).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }

        regFile.put(s.substring(5, 8), bin_to_int(s.substring(8, 16)));
        resetFlag();
        printcommand();

    }

    static void movrc(String s) {// Type C
        if (s.substring(10, 13).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        regFile.put(s.substring(10, 13), regFile.get(s.substring(13, 16)));
        resetFlag();
        printcommand();
    }

    static String Int_to_Bin8bit(int num) {

        String p = "";
        int binary[] = new int[8];
        int index = 0;
        while (num > 0) {
            binary[index++] = num % 2;
            num = num / 2;
        }
        for (int i = index - 1; i >= 0; i--) {
            p = p + Integer.toString(binary[i]);
        }
        for (int j = index; j <= 7; j++) {
            p = "0" + p;
        }
        return p;
    }

    static String Int_to_Bin16bit(int num) {
        String p = "";
        int binary[] = new int[16];
        int index = 0;
        while (num > 0) {
            binary[index++] = num % 2;
            num = num / 2;
        }
        for (int i = index - 1; i >= 0; i--) {
            p = p + Integer.toString(binary[i]);
        }
        for (int j = index; j <= 15; j++) {
            p = "0" + p;
        }
        return p;
    }

    // printing instructions as they come
    static void printcommand() {
        String[] keyinorder = { "000", "001", "010", "011", "100", "101", "110", "111" };
        System.out.print(Int_to_Bin8bit(ProgramCounter) + " ");
        for (String ele : keyinorder) {
            System.out.print(Int_to_Bin16bit(regFile.get(ele)) + " ");
            if (ele.equals("111")) {
                System.out.println(); // new line
            }
        }
    }

    // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
    static void addc(String s) {// add r1 r2 r3 //5 2 unsused 3 3 3
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }

        int r2 = regFile.get(s.substring(10, 13));
        int r3 = regFile.get(s.substring(13, 16));
        int r1 = r2 + r3;
        if (r1 < 65536) {
            regFile.put(s.substring(7, 10), r1);
            resetFlag();
        } else {
            regFile.put(s.substring(7, 10), 0);
            setV();
        }
        // resetFlag();
        printcommand();

    }

    static void subc(String s) {// add r1 r2 r3 //5 2 unsused 3 3 3
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }

        int r2 = regFile.get(s.substring(10, 13));
        int r3 = regFile.get(s.substring(13, 16));
        int r1 = r2 - r3;
        if (r1 >= 0 && r1 <= 65535) {
            r1 = regFile.put(s.substring(7, 10), r1);
            resetFlag();
        } else {
            regFile.put(s.substring(7, 10), 0);
            setV();
        }
        // resetFlag();
        printcommand();

    }

    static void mulc(String s) {
        // resetFlag();
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }

        int r2 = regFile.get(s.substring(10, 13));
        int r3 = regFile.get(s.substring(13, 16));
        int r1 = r2 * r3;
        if (r1 >= 0 && r1 <= 65535) {
            regFile.put(s.substring(7, 10), r1);
            resetFlag();
        } else {
            regFile.put(s.substring(7, 10), 0);
            setV();
        }
        // resetFlag();
        printcommand();

    }

    static void divc(String s) { // TYPE C
        // resetFlag();
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        int r3 = regFile.get(s.substring(10, 13));
        int r4 = regFile.get(s.substring(13, 16));
        int r0 = r3 / r4;
        int r1 = r3 % r4;
        if (r1 >= 0 && r1 <= 65535) {
            regFile.put("000", r0);
            regFile.put("001", r1);
            resetFlag();
        } else {
            regFile.put("000", 0);
            regFile.put("001", 0);
            setV();
        }
        // resetFlag();
        printcommand();

    }

    static void rightShift(String s) {
        if (s.substring(5, 8).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        int r3 = regFile.get(s.substring(5, 8));
        int r4 = bin_to_int(s.substring(8, 16));
        r3 = r3 * (int) (Math.pow(2, r4));
        if (r3 >= 0 && r3 <= 65535) {
            regFile.put(s.substring(5, 8), r3);
        } else {
            regFile.put(s.substring(5, 8), 0);
        }
        resetFlag();
        printcommand();

    }

    static void leftShift(String s) {
        if (s.substring(5, 8).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        int r3 = regFile.get(s.substring(5, 8));
        int r4 = bin_to_int(s.substring(8, 16));
        r3 = r3 / (int) Math.pow(2, r4);
        if (r3 >= 0 && r3 <= 65535) {
            regFile.put(s.substring(5, 8), r3);
        } else {
            regFile.put(s.substring(5, 8), 0);
        }
        resetFlag();
        printcommand();

    }

    static void xor(String s) {
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")) {
            // print line num.

            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        int r2 = regFile.get(s.substring(10, 13));
        int r3 = regFile.get(s.substring(13, 16));
        int r1 = r2 ^ r3; // ?
        if (r1 >= 0 && r1 <= 65535) {
            regFile.put(s.substring(7, 10), r1);
            resetFlag();
        } else {
            regFile.put(s.substring(7, 10), 0);
            setV();
        }
        // resetFlag();
        printcommand();

    }

    static void or(String s) {
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")
                || s.substring(7, 10).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        int r2 = regFile.get(s.substring(10, 13));
        int r3 = regFile.get(s.substring(13, 16));
        int r1 = r2 | r3; // ?
        if (r1 >= 0 && r1 <= 65535) {
            regFile.put(s.substring(7, 10), r1);
            resetFlag();
        } else {
            regFile.put(s.substring(7, 10), 0);
            setV();
        }
        // resetFlag();
        printcommand();

    }

    static void not(String s) {
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        int r2 = regFile.get(s.substring(13, 16));
        int r1 = ~r2;
        if (r1 >= 0 && r1 <= 65535) {
            regFile.put(s.substring(10, 13), r1);
            resetFlag();
        } else {
            regFile.put(s.substring(10, 13), 0);
            setV();
        }
        // resetFlag();
        printcommand();

    }

    static void and(String s) {
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")
                || s.substring(7, 10).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        int r2 = regFile.get(s.substring(10, 13));
        int r3 = regFile.get(s.substring(13, 16));
        int r1 = r2 & r3;
        if (r1 >= 0 && r1 <= 65535) {
            regFile.put(s.substring(7, 10), r1);
            resetFlag();
        } else {
            regFile.put(s.substring(7, 10), 0);
            setV();
        }
        // resetFlag();
        printcommand();

    }

    static void cmp(String s) {
        if (s.substring(10, 13).equals("111") || s.substring(13, 16).equals("111")) {
            // print line num.
            System.out.print("At line " + ProgramCounter);
            System.out.println(" ERROR: Illegal use of FLAGS register");
            // exit code
        }
        int r2 = regFile.get(s.substring(10, 13));
        int r3 = regFile.get(s.substring(13, 16));
        if (r2 > r3) {
            setG();
        } else if (r2 == r3) {
            setE();
        } else if (r2 < r3) {
            setL();
        }
        printcommand();
    }

    // update PC
    // jump to that mem-addr and execute the instruction stored there
    // handle ERROR misuse of vars as labels and vice versa
    static void jmp(String s) {
        String label = s.substring(8, 16); // mem_addr/PC
        ProgramCounter = bin_to_int(label);
        commandIns(memAddr.get(label)); //
        resetFlag();
        printcommand();
    }

    static void jlt(String s) {
        if (regFile.get("111") == 4) {
            String label = s.substring(8, 16);
            ProgramCounter = bin_to_int(label);
            commandIns(memAddr.get(label));
        }
        resetFlag();
        printcommand();
    }

    static void jgt(String s) {
        if (regFile.get("111") == 2) {
            String label = s.substring(8, 16);
            ProgramCounter = bin_to_int(label);
            commandIns(memAddr.get(label));
        }
        resetFlag();
        printcommand();
    }

    static void je(String s) {
        if (regFile.get("111") == 1) {
            String label = s.substring(8, 16);
            ProgramCounter = bin_to_int(label);
            commandIns(memAddr.get(label));
        }
        resetFlag();
        printcommand();
    }

    public static void main(String[] args) {
        // hashmap init, initialised reg file
        regFile.put("000", 0);
        regFile.put("001", 0);
        regFile.put("010", 0);
        regFile.put("011", 0);
        regFile.put("100", 0);
        regFile.put("101", 0);
        regFile.put("110", 0);// Opcode vs decimal base me stored except for flags ig
        regFile.put("111", 0);// flag

        for (int i = 0; i < 256; i++) {// initialized Memory
            memAddr.put(Int_to_Bin8bit(i), "0000000000000000");
        }

        String p; // = "0";
        int count = 0;
        // storing instructions in memory
        // reading in the binary input file
        while (scan.hasNextLine()) {
            p = scan.nextLine();
            memAddr.put(Int_to_Bin8bit(count), p);
            count++;
        }
        // till hlt instruction's bin is encountered
        while (!(memAddr.get(Int_to_Bin8bit(ProgramCounter))).equals("1001100000000000")) {
            commandIns(memAddr.get(Int_to_Bin8bit(ProgramCounter)));
            // plot
            // generate a scatter plot with the cycle number on the x-axis and the memory
            // address on
            // the y-axis. You need to plot which memory address is accessed at what time.
            x_cycles = x_cycles + cycle + " ";
            y_memAddr = y_memAddr + ProgramCounter + " ";
            cycle++;
            ProgramCounter++;
        }
        if ((memAddr.get(Int_to_Bin8bit(ProgramCounter))).equals("1001100000000000")) {
            x_cycles = x_cycles + cycle + " ";
            y_memAddr = y_memAddr + ProgramCounter + " ";
            printcommand();
        }

        // mem dump
        for (int i = 0; i < 256; i++) {
            System.out.println(memAddr.get(Int_to_Bin8bit(i)));
        }

        try {
            File myObj = new File("ScatterPlot.txt");
            if (myObj.createNewFile()) {
                // System.out.println("File created: " + myObj.getName());
            } else {
                // System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("ScatterPlot.txt");
            myWriter.write(x_cycles + "\n" + y_memAddr);
            myWriter.close();
            // System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}