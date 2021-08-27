import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class Assembler {
    static boolean Error_flag = false;
    static int count = 0;
    static int hlt_enc = 0;

    static String instrucOpcode(String p) {// returns 5 bit opcode for instruction
        count++;
        
        if (p.equals("add")) {
            return "0000000";
        } 
        if (p.equals("sub")) {
            return "0000100";// Type A
        }
        if (p.equals("movi")) {// the program reads instruc if not move
            return "00010"; // but if the instruc is move it further checks if its mov reg
        } // or move imm then it asks fn a diff opcode for string
        if (p.equals("movr")) { // like movi for mov imm and movr for mov reg
            return "0001100000"; // for mov caller fn will call with a modified value
        } // Type C
        if (p.equals("ld")) {
            return "00100";
        }
        if (p.equals("st")) {
            return "00101";
        }
        if (p.equals("mul")) {
            return "00110";
        }
        if (p.equals("div")) {
            return "00111";
        }
        if(p.equals("rs")) {
            return "01000";
        }
        if(p.equals("ls")) {
            return "01001";
        }
        if (p.equals("xor")) {
            return "01010";
        }
        if (p.equals("or")) {
            return "01011";
        }
        if (p.equals("and")){
            return "01100";
        }
        if (p.equals("not")) {
            return "01101";
        }
        if (p.equals("cmp")) {
            return "01110";
        }
        if (p.equals("jmp")) {
            return "01111";
        }
        if (p.equals("jlt")) {
            return "10000";
        }
        if (p.equals("jgt")) {
            return "10001";
        }
        if (p.equals("je")) {
            return "10010";
        }
        if (p.equals("hlt")) {
            return "10011";
        }
        else {
            return "-1";
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
        } 
        else {
            Error_flag = true;
            System.out.print("At line ");
            System.out.print(count);
            System.out.println(" ERROR: wrong register name");
            return "-1"; // generate error and exit 
        }

    }

    //throws a NumberFormatException if the string cannot be converted to an int type
    static String binaryConvImm(String st) { 
        int n = Integer.parseInt(st);
        return Int_to_Bin(n);
    }

    static String Int_to_Bin(int num){
        if (num > 255 || num <0) {
            Error_flag = true;
            //print line no.
            System.out.print("At line ");
            System.out.print(count);
            System.out.println(" ERROR: invalid immediate value");
            return "-1"; //generate error and exit code
        }
        String p = "";
        int binary[] = new int[8];
        int index = 0;
        while(num > 0){
            binary[index++] = num%2;
            num = num/2;
        }
        for(int i = index-1;i >= 0;i--){
            p = p + Integer.toString(binary[i]);
            
        }
        for(int j=index; j <= 7; j++){
            p = "0" + p;
        }
        return p;
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
    static int[] regValues= new int[6];
    static String FLAG = "";

    public static void main(String[] args) {

        String[] registers = {"R0","R1","R2", "R3", "R4","R5","R6","FLAGS"};
        String[] opcodes = {"add","sub","ld","mov","st","mul","div","rs","ls","xor","or","and","not","cmp","jmp","jlt","jgt","je","hlt"};
        
        //hashmap for storing label-address pair in jump instructions
        //synchronizing the hashmap
        //Map myhash = Collections.synchronizedMap(hashMap);
        HashMap<String, Integer> labels = new HashMap<String, Integer>();

        //array list of initially incomplete jump instructions
        ArrayList<Integer> incomp = new ArrayList<Integer>();

        //array list for storing binaries of each instruction
        //at indexes corresponding to address no.s in memory
        //need to check instructions dont exceed 256
        ArrayList<String> instr_bin = new ArrayList<String>();
        //easier to add variables in arraylist after hlt 

        //for storing variables in order
        ArrayList<String> vars_inorder = new ArrayList<String>();

        //for storing label names in order
        ArrayList<String> labelNamesArr = new ArrayList<String>();

        HashMap<String, Integer> undefined = new HashMap<String, Integer>();

        
        String R0, R1, R2, R3, R4, R5, R6, FLAGS;
        String specialCharacters = " !#$%&'()*+,-./:;<=>?@[]^`{|}";
        
        Scanner scan = new Scanner(System.in);
        String str;
        // for hault do exit
        while (count<=256 && !Error_flag) {
            String set = "";// complete assemply coded string
            str = scan.next();
            if(count==255 && !str.equals("hlt")){
                Error_flag = true;
                System.out.println(" ERROR: HLT NOT THE LAST INSTRUCTION");
                break;
            }

            // variables
            if(str.equals("var")){
                //var should only be there if instr_bin.size() = 0
                //else exit code
                if(instr_bin.size() != 0){
                    Error_flag = true;
                    //also print the line of code where error occurs
                    System.out.print("At line ");
                    System.out.print(count);
                    System.out.println(" ERROR: variable declared in the middle of the program");
                    break;
                }
                

                String variable = scan.next();
                
                //variable name should be valid
                boolean invalid_var = false;

                for (String element3 : registers) {
                    if (variable.equals(element3)) {
                        //error
                        //exit
                        invalid_var = true;
                        System.out.print("At line ");
                        System.out.print(count);
                        System.out.println(" ERROR: variable name can't be a mneomonic");
                        break;
                    }
                }

                for (String element4 : opcodes) {
                    if (variable.equals(element4)) {
                        //error
                        //exit
                        invalid_var = true;
                        System.out.print("At line ");
                        System.out.print(count);
                        System.out.println(" ERROR: variable name can't be a mneomonic");
                        break;
                    }
                }
                
                for(int i=0; i< specialCharacters.length() ; i++){
                        
                    //Checking if the input string contain any of the specified Characters
                    if(variable.contains(Character.toString(specialCharacters.charAt(i)))){
                        invalid_var = true;
                        //also print the line number where error occured
                        System.out.print("At line ");
                        System.out.print(count);
                        System.out.println(" ERROR: Invalid vaiable name ");
                        break;
                    }
                }
                //exit code
                if(invalid_var){
                    Error_flag = true;
                    break;
                }
                else if(!invalid_var){
                    //put the variable in the hashmap, set address no. to -1
                    //after hlt instruction, address no. of 1st var = instr_bin.size() + 1
                    labels.put(variable,-1);
                    //add it to vars_inorder
                    vars_inorder.add(variable);
                    count++;
                    
                    continue;

                }    
            }

            //handle instr starting with "<label_name>:"
            //label_name should be valid
            if(str.charAt(str.length() - 1) == ':'){
                boolean invalid_label = false;
                String label_name = str.substring(0,str.length()-1);
                
                for (String element : registers) {
                    if (label_name.equals(element)) {
                        //error and exit
                        invalid_label = true;
                        System.out.print("At line ");
                        System.out.print(count + 1);
                        System.out.println(" ERROR: label name can't be a mneomonic");
                        break;
                    }
                }

                for (String element2 : opcodes){
                    if (label_name.equals(element2)){
                        System.out.print("At line ");
                        System.out.print(count + 1);
                        System.out.println(" ERROR: label name can't be a mneomonic");
                        //error and exit
                        invalid_label = true;
                        break;
                    }
                }
                //redeclaration of a label is an error
                if(labels.get(label_name) != null &&  labels.get(label_name) != -1){
                    invalid_label = true;
                    //also print the line number where error occured
                    System.out.print("At line ");
                    System.out.print(count + 1);
                    System.out.println(" ERROR: redeclaration of a label ");
                    //break;
                }

                for(int i=0; i< specialCharacters.length() ; i++){    
                    //Checking if the input string contain any of the specified Characters
                    if(label_name.contains(Character.toString(specialCharacters.charAt(i)))){
                        invalid_label = true;
                        //also print the line number where error occured
                        System.out.print("At line ");
                        System.out.print(count + 1);
                        System.out.println(" ERROR: Invalid Label name ");
                        break;
                    }
                }
                //exit code
                if(invalid_label){
                    Error_flag = true;
                    break;
                }

                if(!invalid_label){
                    for(String e : vars_inorder){
                        if(e.equals(label_name)){
                            Error_flag = true;
                            System.out.print("At line ");
                            System.out.print(count + 1);
                            System.out.println(" ERROR: Cant declare a label having the same name as a variable");
                            break;
                        }
                    }
                    if(Error_flag){
                        break;
                    }
                    //add to labelNamesArr
                    labelNamesArr.add(label_name);
                    //add to hashmap with value as its address no. 
                    if(labels.get(label_name) == null){
                        labels.put(label_name, instr_bin.size());
                    }
                    else{
                        labels.replace(label_name, instr_bin.size());
                    }
                    // read the instruction ahead and convert to binary
                    continue;
                }
            }

            if (str.equals("mov")) {
                String str2 = scan.next();
                String str3 = scan.next();
                if (checkImm(str3) == 1) {
                    if(str2.equals("FLAGS")){
                        System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                        Error_flag = true;
                        break;
                    }
                    set = instrucOpcode("movi") + registerAddress(str2) + binaryConvImm(str3.substring(1));
                    instr_bin.add(set);
                } else {
                    set = instrucOpcode("movr") + registerAddress(str2) + registerAddress(str3);
                    instr_bin.add(set);
                }
                continue;
            }

            else if (str.equals("add") || str.equals("sub")) {
                String str2 = scan.next();
                String str3 = scan.next();
                String str4 = scan.next();
                if(str2.equals("FLAGS") || str3.equals("FLAGS") || str4.equals("FLAGS") ){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode(str) + registerAddress(str2) + registerAddress(str3) + registerAddress(str4);
                instr_bin.add(set);
                continue;
            }
            else if (str.equals("ld") || str.equals("st")) {
                String str2 = scan.next();
                String str3 = scan.next();// this is mem addr/ variable 
                if(str2.equals("FLAGS") || str3.equals("FLAGS")){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }

                for(String e : labelNamesArr){
                    if(e.equals(str3)){
                        Error_flag = true;
                        System.out.print("At line");
                        System.out.print(count);
                        System.out.println("ERROR: Misuse of labels as variables or vice-versa");
                        break;
                    }
                }
                if(Error_flag){
                    break;
                }
                //incomplete instr stored in instr_bin
                //will be completed after halt
                set = instrucOpcode(str) + registerAddress(str2);
                undefined.put(str3,count);
                instr_bin.add(set + " " + str3);
                incomp.add(instr_bin.indexOf(set + " " + str3));
                continue;
            }

            else if(str.equals("mul")){
                String str2 = scan.next();
                String str3 = scan.next();
                String str4 = scan.next();
                if(str2.equals("FLAGS") || str3.equals("FLAGS") || str4.equals("FLAGS") ){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }

                set = instrucOpcode("mul") + "00" + registerAddress(str2) + registerAddress(str3) + registerAddress(str4);
                instr_bin.add(set);
                continue;    
            }
            else if(str.equals("div")){
                String str2 = scan.next();
                String str3 = scan.next();
                if(str2.equals("FLAGS") || str3.equals("FLAGS")){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode("div") + "00000" + registerAddress(str2) + registerAddress(str3);
                instr_bin.add(set);
                continue;
            }
            else if(str.equals("rs")){
                String str2 = scan.next();
                String str3 = scan.next();

                if(str2.equals("FLAGS") || str3.equals("FLAGS") ){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode("rs") + registerAddress(str2) + binaryConvImm(str3.substring(1));
                instr_bin.add(set);
                continue;
            }
            else if(str.equals("ls")){
                String str2 = scan.next();
                String str3 = scan.next();

                if(str2.equals("FLAGS") || str3.equals("FLAGS")){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode("ls") + registerAddress(str2) + binaryConvImm(str3.substring(1));
                instr_bin.add(set);
                continue;
            }
            else if(str.equals("xor")){
                String str2 = scan.next();
                String str3 = scan.next();
                String str4 = scan.next();

                if(str2.equals("FLAGS") || str3.equals("FLAGS") || str4.equals("FLAGS") ){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode("xor") + "00" + registerAddress(str2) + registerAddress(str3) + registerAddress(str4);
                instr_bin.add(set);
                continue;
            }
            else if(str.equals("or")){
                String str2 = scan.next();
                String str3 = scan.next();
                String str4 = scan.next();
                if(str2.equals("FLAGS") || str3.equals("FLAGS") || str4.equals("FLAGS") ){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode("or") + "00" + registerAddress(str2) + registerAddress(str3) + registerAddress(str4);
                instr_bin.add(set);
                continue;    
            }
            else if(str.equals("and")){
                String str2 = scan.next();
                String str3 = scan.next();
                String str4 = scan.next();
                if(str2.equals("FLAGS") || str3.equals("FLAGS") || str4.equals("FLAGS") ){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode("and") + "00" + registerAddress(str2) + registerAddress(str3) + registerAddress(str4);
                instr_bin.add(set);
                continue;   
            }
            else if(str.equals("not")){
                String str2 = scan.next();
                String str3 = scan.next();
                if(str2.equals("FLAGS") || str3.equals("FLAGS") ){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode("not") + "00000" + registerAddress(str2) + registerAddress(str3);
                instr_bin.add(set);
                continue;
            }
            else if(str.equals("cmp")){
                String str2 = scan.next();
                String str3 = scan.next();
                if(str2.equals("FLAGS") || str3.equals("FLAGS")){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }
                set = instrucOpcode("cmp") + "00000" + registerAddress(str2) + registerAddress(str3);
                instr_bin.add(set);
                if(Integer.parseInt(str2.substring(1))<Integer.parseInt(str3.substring(1))){
                    FLAG = "L";
                }
                else if(Integer.parseInt(str2.substring(1))==Integer.parseInt(str3.substring(1))){
                    FLAG = "E";
                }
                else if(Integer.parseInt(str2.substring(1))>Integer.parseInt(str3.substring(1))){
                    FLAG = "G";
                }
                continue;
            }
            
            //must reset FLAGS reg after every instruction

            //jmp label_name
            else if(str.equals("jmp")){
                String str2 = scan.next();
                if(str2.equals("FLAGS")){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }

                for(String e : vars_inorder){
                    if(e.equals(str2)){
                        Error_flag = true;
                        System.out.print("At line");
                        System.out.print(count);
                        System.out.println("ERROR: Misuse of labels as variables or vice-versa");
                        break;
                    }
                }
                if(Error_flag){
                    break;
                }

                //if the label hasnt come before the jmp instr
                // i.e the label is not in hashmap
                if(labels.get(str2) == null){
                    labels.put(str2,-1); //add the label to map
                    //add to labelNamesArr
                    labelNamesArr.add(str2);
                    //write the instruc just containing the opcode and unused bits
                    // white space + label_name: in the bin array
                    set = instrucOpcode("jmp") + "000";
                    undefined.put(str2,count);
                    instr_bin.add(set + " " + str2);
                    //store this index in incomp array
                    incomp.add(instr_bin.indexOf(set + " " + str2));
                }

                //label was encountered before the jmp instr
                //label is in hashmap, value is not -1
                //its value stores the address no.
                if(labels.get(str2) != -1){
                    set = instrucOpcode("jmp") + "000" + Int_to_Bin(labels.get(str2));
                    instr_bin.add(set);
                }
                continue;
            }
            
            //jlt label_name
            else if(str.equals("jlt")){
                String str2 = scan.next();
                //can be defined as a separate function
                if(str2.equals("FLAGS")){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }

                for(String e : vars_inorder){
                    if(e.equals(str2)){
                        Error_flag = true;
                        System.out.print("At line");
                        System.out.print(count);
                        System.out.println("ERROR: Misuse of labels as variables or vice-versa");
                        break;
                    }
                }
                if(Error_flag){
                    break;
                }

                if(labels.get(str2) == null){
                    labelNamesArr.add(str2);
                    labels.put(str2,-1);
                    set = instrucOpcode("jlt") + "000";
                    undefined.put(str2,count);
                    instr_bin.add(set + " " + str2);
                    incomp.add(instr_bin.indexOf(set + " " + str2));
                }
                if(labels.get(str2) != -1){
                    set = instrucOpcode("jlt") + "000" + Int_to_Bin(labels.get(str2));
                    instr_bin.add(set);
                }
                continue;
            }

            else if(str.equals("jgt")){
                String str2 = scan.next();
                if(str2.equals("FLAGS")){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }

                for(String e : vars_inorder){
                    if(e.equals(str2)){
                        Error_flag = true;
                        System.out.print("At line");
                        System.out.print(count);
                        System.out.println(" ERROR: Misuse of labels as variables or vice-versa");
                        break;
                    }
                }
                if(Error_flag){
                    break;
                }

                if(labels.get(str2) == null){
                    labelNamesArr.add(str2);
                    labels.put(str2,-1);
                    set = instrucOpcode("jgt") + "000";
                    undefined.put(str2,count);
                    instr_bin.add(set + " " + str2);
                    incomp.add(instr_bin.indexOf(set + " " + str2));
                }
                if(labels.get(str2) != -1){
                    set = instrucOpcode("jgt") + "000" + Int_to_Bin(labels.get(str2));
                    instr_bin.add(set);
                }
                continue;
            }
            else if(str.equals("je")){
                String str2 = scan.next();
                if(str2.equals("FLAGS")){
                    System.out.println("At Line " + count + " ERROR: illegal use of FLAGS register");
                    Error_flag = true;
                    break;
                }

                for(String e : vars_inorder){
                    if(e.equals(str2)){
                        Error_flag = true;
                        System.out.print("At line");
                        System.out.print(count);
                        System.out.println(" ERROR: Misuse of labels as variables or vice-versa");
                        break;
                    }
                }
                if(Error_flag){
                    break;
                }

                if(labels.get(str2) == null){
                    labelNamesArr.add(str2);
                    labels.put(str2,-1);
                    set = instrucOpcode("je") + "000";
                    undefined.put(str2,count);
                    instr_bin.add(set + " " + str2);
                    incomp.add(instr_bin.indexOf(set + " " + str2));
                }
                if(labels.get(str2) != -1){
                    set = instrucOpcode("je") + "000" + Int_to_Bin(labels.get(str2));
                    instr_bin.add(set);
                }
                continue;
            }
            else if(str.equals("hlt")){
                hlt_enc = 1;
                set = instrucOpcode("hlt") + "00000000000";
                instr_bin.add(set);
                //instr_bin.add(set);
                break;
            }
            else{
                System.out.print("At line ");
                System.out.print(count + 1);
                System.out.println(" ERROR: Invalid instruction or general syntax error");
                Error_flag = true;
                break;
            }
        }
        
        /* 
        hlt
        .
        .
        . (256)
        */

        //--------
        if(!Error_flag && count > 256 && hlt_enc != 1){
            Error_flag = true;
            System.out.print("At line " + (count));
            System.out.println(" ERROR: no hlt instruction present");
        }
        //---------
        //str = scan.next(); 
        if(scan.hasNextLine() && !Error_flag) { 
            Error_flag = true;
            System.out.print("At line " + (count+1));
            System.out.println(" ERROR: instruction should not be present after hlt");
        }

        // if(!Error_flag && count > 256){
        //     Error_flag = true;
        //     System.out.println("ERROR: number of instructions exceeded 256 ");
        //     System.out.println("and hlt instruction missing before or at line 256");
        // }

        if(!Error_flag){
            //setting address no.s of variables
            for(String vars : vars_inorder){
                labels.replace(vars, instr_bin.size() + vars_inorder.indexOf(vars));
            }

            String[] arrOfStr;
            String incomp_instr;
            String comp_instr; //complete instruction
            if (incomp.size() != 0){
                for(int item : incomp){
                    
                    incomp_instr = instr_bin.get(item);
                    arrOfStr = incomp_instr.split(" ",2);
                    //System.out.println(arrOfStr[1]);
                    if(labels.get(arrOfStr[1]) == null || labels.get(arrOfStr[1]) == -1){
                        int i = undefined.get(arrOfStr[1]);
                        System.out.print("At line " + i);
                        System.out.println(" ERROR: " + arrOfStr[1] + " is an Undefined name");
                        Error_flag=true;
                        break;
                    }
                    comp_instr = arrOfStr[0] + Int_to_Bin(labels.get(arrOfStr[1]));
                    instr_bin.set(item,comp_instr);   
                }
            }
            
            if(!Error_flag){
                for(String binary : instr_bin){
                System.out.println(binary);   
                }
            }
            
        }
    }
}