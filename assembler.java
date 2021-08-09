import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

public class assembler {
    static String instrucOpcode(String p) {// returns 5 bit opcode for instruction
        if (p.equals("add")) {
            return "0000000";// Type A so 2 unused hence i set unused bits here only
        } // concept hai abhi bas ~for rest too//finlized
        if (p.equals("sub")) {
            return "0000100";// Type A
        }
        if (p.equals("movi")) {// the program reads instruc if not move
            return "00010"; // but if the instruc is move it further checks if its mov reg
        } // or move imm then it asks fn a diff opcode for string
        if (p.equals("movr")) { // like movi for mov imm and movr for mov reg
            return "0001100000"; // basicaly for mov caller fn will call with a modified value
        } // Type C
        if (p.equals("ld")) {
            return "00100";
        }
        if (p.equals("st")) {
            return "00101";
        }
        if (p.equals("mul")) {// not mine but wrote it
            return "00110";
        } else {
            return "Error";// or whatever you want to write
        }
    }

    static String registerAddress(String s) {// returns 3 bit
        if (s.equals("R0")) {
            return "000";
        }
        if (s.equals("R1")) {
            return "001";
        }
        if (s.equals("R2")) {
            return "010";
        }
        if (s.equals("R3")) {
            return "011";
        }
        if (s.equals("R4")) {
            return "100";
        }
        if (s.equals("R5")) {
            return "101";
        }
        if (s.equals("R6")) {
            return "110";
        }
        if (s.equals("FLAGS")) {
            return "111";
        } else {
            return "-1";
        }

    }

    static int[] fill(int[] n, int arrIndex, int num) {// array index should be size of arr
        if (num == 1) { // converts decimal to binary
            n[arrIndex] = 1;
            return n;
        } else {
            int q = num / 2;
            int r = num % 2;
            num = q;
            n[arrIndex] = r;
            arrIndex = arrIndex - 1;
            return fill(n, arrIndex, num);
        }

    }

    static String binaryConv(String st) { // return string for binary representation
        int n = Integer.parseInt(st);
        int[] bin = new int[16];

        if (n > 65536) {
            return "-1";
        } else {
            int[] m = fill(bin, 15, n);
            String p = "";
            for (int i = 0; i < m.length; i++) {
                p = p + Integer.toString(m[i]);
            }
            return p;
        }
    }

    static String binaryConvImm(String st) { // return string for binary representation
        int n = Integer.parseInt(st);
        int[] bin = new int[8];

        if (n > 255) {
            return "-1";
        } else {
            int[] m = fill(bin, 7, n);
            String p = "";
            for (int i = 0; i < m.length; i++) {
                p = p + Integer.toString(m[i]);
            }
            return p;
        }
    }

    static void printarr(int[] a) {// just to print array asap for verification;
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    static int checkImm(String s) {
        if (s.charAt(0) == '$') {
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        // variables
        String R0, R1, R2, R3, R4, R5, R6, FLAGS;// registers are 16 bit so strings..//will be used in simulator ig
        Scanner scan = new Scanner(System.in);
        // for hault basically do exit
        while (true) {
            String set = "";// complete assemply coded string
            String str = scan.next();

            if (str.equals("mov")) {// mov wale completed
                String str2 = scan.next();
                String str3 = scan.next();
                if (checkImm(str3) == 1) {
                    set = instrucOpcode("movi") + registerAddress(str2) + binaryConvImm(str3.substring(1));
                    System.out.println(set);

                } else {
                    set = instrucOpcode("movr") + registerAddress(str2) + registerAddress(str3);
                    System.out.println(set);
                }

            }
            // then check command and send it to
            // fn of that command where complete assm code is returned

            set = instrucOpcode(str);
            String str4 = scan.next();
            set = set + registerAddress(str4);
            String str5 = scan.next();// will continue from here later..
            // will need to make if statement for add and sub also..
        }

    }
}