import java.util.HashMap;
import java.util.Scanner;
import java.lang.Math;

public class simu { // gives what code is it and sends it respective fn
    static Scanner scan = new Scanner(System.in);
    static int ProgramCounter = 0;// Program Counter
    // Hashmaps
    static final HashMap<String, Integer> r = new HashMap<>();// Register File
    static final HashMap<String, String> maddr = new HashMap<>();// Memory
    // Flags
    // int values to set flags:
    /*
     * overflow(V)=8 less than(L)=4 greater than(G)=2 Equal (E)=1
     * 
     */

    static void resetFlag() {
        r.put("111", 0);
    }

    static void setV() {
        r.put("111", 8);
    }

    static void setL() {
        r.put("111", 4);
    }

    static void setG() {
        r.put("111", 2);
    }

    static void setE() {
        r.put("111", 1);
    }

    // decides what command the binary stands for
    static void commandIns(String s) {// Excecution Engine
        // String result = "";
        // System.out.println("Test 1: " + s.substring(0, 5));
        if (s.substring(0, 5).equals("00000")) {// add
            // calls add fn // add fn has input as string//input is complete string

            addc(s);
        }
        if (s.substring(0, 5).equals("00001")) {// sub
            // calls add fn // add fn has input as string//input is complete string

            subc(s);
        }
        if (s.substring(0, 5).equals("00010")) {// mov imm
            // calls add fn // add fn has input as string//input is complete string

            movic(s);
        }
        if (s.substring(0, 5).equals("00011")) {// mov imm
            // calls add fn // add fn has input as string//input is complete string

            movrc(s);
        }
        if (s.substring(0, 5).equals("00100")) {
            loadc(s);

        }
        if (s.substring(0, 5).equals("00101")) {
            storec(s);
        }

        // return result;
    }

    static void loadc(String s) {/// type D 5 3 8
        r.put(s.substring(5, 8), bin_to_int(maddr.get(s.substring(8, 16))));
        printcommand();
    }

    static void storec(String s) {// type D
        maddr.put(s.substring(8, 16), Int_to_Bin16bit(r.get(s.substring(5, 8))));
    }

    static int power(int m) {// gives powers of 2
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

            count++;
        }
        return retnum;
    }

    static void movic(String s) {// opcode//type B
        r.put(s.substring(5, 8), bin_to_int(s.substring(8, 16)));
        printcommand();

    }

    static void movrc(String s) {// Type C
        r.put(s.substring(10, 13), r.get(s.substring(13, 16)));
        printcommand();
    }

    // We need hashmaps for
    // reg values and opcode
    // memory address

    // binary to int convertor
    // int to binary convertot
    // we would need to store Rehgisters memory address and item at address too

    // or we can make a hashmap with key value pair with key being the opcode for
    // registers
    // and value to be the value at that key.

    // we will store value at hashmap location in base 10 only
    // but at the time t print we may convert back to binary reducig fn calls
    // fn to print the entire array but converts numbers to binary beore printing

    // also we need a memory array of 256 which could also be a hashmap with
    // 256 as keys and rest as values as per need.

    // fn to print mem addr

    // we need a hashmap for storing Rvalues.
    // also a program counter is needed.
    // static String Rval(String s) {// gives value at register at opcode given
    // return "";// basically returns hashmap keys for rval.
    // }
    static String Int_to_Bin8bit(int num) {
        // if (num > 255 || num < 0) {
        // // Error_flag = true;
        // // print line no.
        // System.out.print("At line ");
        // // System.out.print(count);
        // System.out.println(" ERROR: invalid immediate value");
        // return "-1"; // generate error and exit code
        // }
        String p = "";
        int binary[] = new int[8];
        int index = 0;
        while (num > 0) {
            binary[index++] = num % 2;
            num = num / 2;
        }
        for (int i = index - 1; i >= 0; i--) {
            p = p + Integer.toString(binary[i]);
            // System.out.print(binary[i]);
        }
        for (int j = index; j <= 7; j++) {
            p = "0" + p;
        }
        return p;
    }

    static void printmem() {
        // prints mem after hlt is found
    }

    static String Int_to_Bin16bit(int num) {
        // if (num > || num < 0) {
        // // Error_flag = true;
        // // print line no.
        // System.out.print("At line ");
        // // System.out.print(count);
        // System.out.println(" ERROR: invalid immediate value");
        // return "-1"; // generate error and exit code
        // }
        String p = "";
        int binary[] = new int[16];
        int index = 0;
        while (num > 0) {
            binary[index++] = num % 2;
            num = num / 2;
        }
        for (int i = index - 1; i >= 0; i--) {
            p = p + Integer.toString(binary[i]);
            // System.out.print(binary[i]);
        }
        for (int j = index; j <= 15; j++) {
            p = "0" + p;
        }
        return p;
    }

    static void printcommand() {
        String[] keyinorder = { "000", "001", "010", "011", "100", "101", "110", "111" };
        System.out.print(Int_to_Bin8bit(ProgramCounter) + " ");
        for (String ele : keyinorder) {
            System.out.print(Int_to_Bin16bit(r.get(ele)) + " ");
            if (ele.equals("111")) {
                System.out.print("\n");
            }
            // System.out.println("ggg");
            // System.out.print(" ");
        }
    }

    // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
    static void addc(String s) {// add r1 r2 r3 //5 2 unsused 3 3 3
        // System.out.println("Test 2: Inside addc(String) Success");
        int r2 = r.get(s.substring(10, 13));
        int r3 = r.get(s.substring(13, 16));
        int r1 = r2 + r3;
        if (r1 < 65536) {
            r.put(s.substring(7, 10), r1);
        } else {
            r.put(s.substring(7, 10), 0);
            setV();
        }
        printcommand();

    }

    static void subc(String s) {// add r1 r2 r3 //5 2 unsused 3 3 3
        // System.out.println("Test 2: Inside addc(String) Success");
        int r2 = r.get(s.substring(10, 13));
        int r3 = r.get(s.substring(13, 16));
        int r1 = r2 - r3;
        if (r1 > 0 && r1 < 65535) {
            r1 = r.put(s.substring(7, 10), r1);
        } else {
            r.put(s.substring(7, 10), 0);
            setV();
        }
        printcommand();

    }

    public static void main(String[] args) {
        // hashmap init
        r.put("000", 0);
        r.put("001", 0);
        r.put("010", 0);
        r.put("011", 0);
        r.put("100", 0);
        r.put("101", 0);
        r.put("110", 0);// Opcode vs decimal base me stored except for flags ig
        r.put("111", 0);// flag

        for (int i = 0; i < 256; i++) {// initialized maddr

            maddr.put(Int_to_Bin8bit(i), "0000000000000000");
        }
        String p = "0";
        int count = 0;
        while (scan.hasNextLine()) {
            p = scan.nextLine();
            maddr.put(Int_to_Bin8bit(count), p);
            count++;
        }

        // mem addr initialized form input
        // for (int i = 0; i < 256; i++) {
        // String s = scan.nextLine();
        // if (s.equals("")) {
        // s = "0000000000000000";// empty memaddrop
        // }
        // maddr.put(Int_to_Bin8bit(i), s);
        // }
        while (!(maddr.get(Int_to_Bin8bit(ProgramCounter))).equals("1001100000000000")) {// basically ! halt instruc
            commandIns(maddr.get(Int_to_Bin8bit(ProgramCounter)));
            ProgramCounter++;
        }
        if ((maddr.get(Int_to_Bin8bit(ProgramCounter))).equals("1001100000000000")) {
            printcommand();
        }
        for (int i = 0; i < 256; i++) {
            System.out.println(maddr.get(Int_to_Bin8bit(i)));
        }

        // while loop approach wont work this time
        // while (true) {
        // resetFlag();
        // String s = scan.nextLine();
        // if (s.equals("")) {
        // continue;
        // }else{
        // maddr.put()//idk what happens if jmp is done
        // }
        // // System.out.println(r);
        // if (s.substring(0, 5).equals("10011")) {// hlt
        // // calls add fn // add fn has input as string//input is complete string
        // printcommand();// prinnts last command wale stuff
        // // printmem();// pritn mem
        // break;// just to test

        // }
        // commandIns(s);
        // ProgramCounter++;

        // }

        // System.out.println(r);
        // printcommand();
        // trial run s=add r0 r1 r2//00000000001010 and r=0,r1=1,r2=2
        // feed input continuos string of 16 bit as substrings or
    }
}
