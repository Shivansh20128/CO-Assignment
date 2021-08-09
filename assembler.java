import java.lang.reflect.Array;
import java.util.Arrays;

public class assembler {
    static String instrucOpcode(String p) {// returns 5 bit opcode for instruction
        if (p.equals("add")) {
            return "00000";
        }
        if (p.equals("sub")) {
            return "00001";
        }
        if (p.equals("movi")) {// the program reads instruc if not move
            return "00010"; // but if the instruc is move it further checks if its mov reg
        } // or move imm then it asks fn a diff opcode for string
        if (p.equals("movr")) { // like movi for mov imm and movr for mov reg
            return "00011"; // basicaly for mov caller fn will call with a modified value
        }
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

    static void printarr(int[] a) {// just to print array asap for verification;
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        // variables
        String R0, R1, R2, R3, R4, R5, R6, FLAGS;// registers are 16 bit so strings..
        int[] arr = new int[16];
        System.out.println(binaryConv("2"));
        // int[] m = fill(arr, 15, 3);
        // String p = "";
        // for (int i = 0; i < m.length; i++) {
        // p = p + Integer.toString(m[i]);
        // }
        // System.out.println(p);
    }
}