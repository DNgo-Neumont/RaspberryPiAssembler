package dngo.raspberry;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AssemblyParser {
    public static void parseFile(Path inputPath, Path outputPath, BufferedReader fileBufferedReader){
        List<String> fileContents = fileBufferedReader.lines().collect(Collectors.toList());

        for(String line: fileContents){
            line = line.replace("\n", "");
            line = line.strip();

            
            MOVTCommand commandTest = new MOVTCommand();

            commandTest.setCond("1110");
            commandTest.setImmFour("0011");
            commandTest.setDestRegister("0100");
            commandTest.setImmTwelve("111100100000");
            System.out.println(commandTest.returnCommand());

            System.out.println(convBinarytoHex(commandTest.returnCommand()));

            System.out.println(littleEndianFormatHex(convBinarytoHex(commandTest.returnCommand())));

            
            String testAddAssembly = "ADD[E] IM{1} 3,4 SCODE{0} 001C";
            String testSubAssembly = "SUB[E] IM{1} 6,6 SCODE{1} 0001";

            ADDCommand testAdd = new ADDCommand();

            testAdd.buildCommand(testAddAssembly);

            System.out.println(convBinarytoHex(testAdd.returnCommand()));
            System.out.println(testAdd.returnCommand());
            System.out.println(littleEndianFormatHex(convBinarytoHex(testAdd.returnCommand())));

            SUBCommand testSUB = new SUBCommand();

            testSUB.buildCommand(testSubAssembly);

            System.out.println(convBinarytoHex(testSUB.returnCommand()));
            System.out.println(testSUB.returnCommand());
            System.out.println(littleEndianFormatHex(convBinarytoHex(testSUB.returnCommand())));

            // try{ example conversions of hex and binary
            // splice strings as needed to create full commands
            //     System.out.println(convHexToBinary("0042"));
            //     System.out.println(convBinarytoHex("11100011010000110100111100100000"));
            // }catch(NumberFormatException NFE){
            //     NFE.printStackTrace();
            // }

        } 

    }

    //Should only be used to parse full instructions for writing - hardlocked to 8 chars for result
    public static String convBinarytoHex(String binaryString) throws NumberFormatException{
        String result = "";

        try{
            result = new BigInteger(binaryString, 2).toString(16);
            result = String.format("%8s", result);
            result = result.replaceAll(" ", "0");
        }catch (NumberFormatException NFE){
            System.out.println("FATAL: Input binary is invalid!");
            throw NFE;
        }

        return result;
    }


    //Variable padding length - when needed just specify a power of two to make sure the end result is padded correctly
    public static String convHexToBinary(String hexadecimalString, int paddingLength) throws NumberFormatException{
        String result = "";
        try{
            result = new BigInteger(hexadecimalString, 16).toString(2);
            // result = String.format("%32s", Integer.toBinaryString(result).replace(" ", "0"));
            result = String.format(("%" + paddingLength + "s"), result);
            result = result.replaceAll(" ", "0");


        }catch(NumberFormatException NFE){
            System.out.println("FATAL: Input hexadecimal is not valid!");
            throw NFE;
        }
        
        return result;
    }
    
    public static String littleEndianFormatHex(String hexToFormat){
        
        List<String> partitions = new ArrayList<>();

        int prevIndex = 0;
        for(int i = 2; i < 9; i+=2){
            partitions.add(hexToFormat.substring(prevIndex, i));
            prevIndex = i;
        }
        Collections.reverse(partitions);

        String result = "";

        for(String part : partitions){
            result += part;
        }
        return result;
    }

    public static short[] parseToShortArray(String hexString){
        
        short[] arrayToReturn = new short[hexString.length()/2];
        
        int prevIndex = 0;
        int arrayToReturnIndex = 0;
        for(int i = 2; i < 9; i+=2){
            String charAtPosition = hexString.substring(prevIndex, i);
            prevIndex = i;
            arrayToReturn[arrayToReturnIndex] = Short.valueOf(charAtPosition, 16);
            arrayToReturnIndex++;
        }
        System.out.println(Arrays.toString(arrayToReturn));
        return arrayToReturn;

    }

    public static byte[] parseToByteArray(String hexString){
        
        byte[] arrayToReturn = new byte[hexString.length()];
        
        int prevIndex = 0;
        // int arrayToReturnIndex = 0;
        for(int i = 1; i < 8; i++){
            String charAtPosition = hexString.substring(prevIndex, i);
            prevIndex = i;
            arrayToReturn[i] = (byte) (Byte.valueOf(charAtPosition, 16) & 0xff);
        }
        System.out.println(Arrays.toString(arrayToReturn));

        for(int i = 0; i < arrayToReturn.length; i++){
            System.out.println(Byte.toString(arrayToReturn[i]));
        }
        return arrayToReturn;

    }

    public static void writeCommand(CommandBase command, Path outputPath){
        try {

            FileOutputStream fileStream = new FileOutputStream(outputPath.toFile(), true);

            short[] arrayToWrite = parseToShortArray(littleEndianFormatHex(convBinarytoHex(command.returnCommand())));

            for(int i = 0; i < arrayToWrite.length; i++){
                fileStream.write(arrayToWrite[i]);
            }

        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
