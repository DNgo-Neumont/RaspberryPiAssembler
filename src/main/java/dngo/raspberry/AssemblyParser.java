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

    static List<CommandBase> branchesCalculated = new ArrayList<>();

    public static void parseFile(Path inputPath, Path outputPath, BufferedReader fileBufferedReader){
        List<String> fileContents = fileBufferedReader.lines().collect(Collectors.toList());
        List<CommandBase> commandList = new ArrayList<>();
        
        fileContents = fileContents.stream().filter(line -> !line.contains("//")).toList();

        for(String line: fileContents){
            line = line.replace("\n", "");
            line = line.strip();

            String[] lineArray = line.split(" ");

            String commandName = lineArray[0].substring(0, lineArray[0].indexOf("["));
            
            System.out.println(commandName);

            CommandBase command = CommandFactory.createCommand(commandName);

            System.out.println(command.getClass().getName());

            command.buildCommand(line);

            commandList.add(command);
        } 

        //Done so branch commands do not reference eachother.
        List<CommandBase> bcommandsWithLabels = commandList.stream().filter(predicate -> predicate.getLabel() != null && predicate.getClass().isAssignableFrom(BCommand.class)).toList();

        branchesCalculated.addAll(bcommandsWithLabels);

        StringBuilder commandBuilder = new StringBuilder();
        StringBuilder commandBinaryBuilder = new StringBuilder();

        for(CommandBase command: commandList){
            
            if(command.getClass().isAssignableFrom(BCommand.class)){
                System.out.println("Assigned branch command");
                BCommand bcommand = (BCommand) command;

                if(bcommand.getLinkBit().equals("1")){
                    //branch with link conf, find label attached and calc offset
                    //Wanted behavior
                    //We walk through all the lines in assembly
                    //we grab the first command with the supplied label attached to this branch command
                    //unless that command's already been calculated then we continue
                    //
                    int result = recurseForLabel(bcommand.getLabel(), commandList, commandList.indexOf(command), 0);

                    System.out.println("offset: " + result);

                    String binaryString = Integer.toBinaryString(result);
                    System.out.println(binaryString);
                    if(result < 0){
                        //Done to fix the issue with integer.tobinaryString returning a 32 bit long string
                        //Branch commands devote 24 bits to an offset - the first 8 bits control functionality
                        binaryString = binaryString.substring(8, binaryString.length());
                    }else{
                        binaryString = String.format("%24s", binaryString).replaceAll(" ", "0");
                    }

                    System.out.println(binaryString);
                    bcommand.setOffset(binaryString);

                    System.out.println(bcommand.returnCommand());
                }else{
                    System.out.println("Link bit unset");
                }

            }

            System.out.println(command.getClass());

            System.out.println(command.returnCommand());

            System.out.println(convBinarytoHex(command.returnCommand()));

            System.out.println(littleEndianFormatHex(convBinarytoHex(command.returnCommand())));

            writeCommand(command, outputPath);

            commandBuilder.append(littleEndianFormatHex(convBinarytoHex(command.returnCommand()))).append("\n");
            commandBinaryBuilder.append(command.returnCommand()).append("\n");
        }

        System.out.println(commandBuilder.toString());
        System.out.println(commandBinaryBuilder.toString());

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
    

    //Variable padding length - when needed just specify a power of two to make sure the end result is padded correctly
    public static String convDecToBinary(String decimalString, int paddingLength) throws NumberFormatException{
        String result = "";
        try{
            result = new BigInteger(decimalString, 10).toString(2);
            // result = String.format("%32s", Integer.toBinaryString(result).replace(" ", "0"));
            result = String.format(("%" + paddingLength + "s"), result);
            result = result.replaceAll(" ", "0");


        }catch(NumberFormatException NFE){
            System.out.println("FATAL: Input decimal is not valid!");
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
                fileStream.flush();
            }

            fileStream.close();

        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }


    //needs refactoring - doesn't look forwards
    public static int recurseForLabel(String label, List<CommandBase> commands, int currentIndex, int lineIndex){
        // lineIndex = 0;
        CommandBase lineToCheck = commands.get(lineIndex);
        
        if(lineToCheck.getLabel() != null && lineToCheck.getLabel().equals(label) && !(branchesCalculated.contains(lineToCheck))){

            System.out.println("Checked command: ");
            System.out.println(lineToCheck.getClass().getName());
            System.out.println("Command signature: ");
            System.out.println(lineToCheck.returnCommand());
            
            int calculatedIndex = Math.abs(currentIndex - lineIndex); 
            System.out.println("Current index: " + currentIndex);
            System.out.println("Line index: " + lineIndex);
            // int calculatedIndex = currentIndex - lineIndex;
            System.out.println("Calculated index: " + calculatedIndex);

            if(lineIndex < currentIndex){
                calculatedIndex = (0-calculatedIndex) - 2;
            }else{
                calculatedIndex -= 2;
            }

            System.out.println("Calculated index (aft transform): " + calculatedIndex);

            return calculatedIndex;
        }else if(!((lineIndex + 1) > commands.size()-1)){
            return recurseForLabel(label, commands, currentIndex, lineIndex + 1);
        }
        return -1;
    }


}
