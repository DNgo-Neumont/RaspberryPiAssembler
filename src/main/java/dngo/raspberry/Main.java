package dngo.raspberry;

import java.io.BufferedReader;
// import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
// import java.nio.ByteBuffer;
// import java.nio.channels.ByteChannel;
// import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args){
        String menu = "1. Select file\n2. List images\n3. Exit";
        
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        boolean quit = false;
        try{
            while(!quit){
                System.out.println(menu);
                int input = 0;
                try {
                    input = Integer.parseInt(userInput.readLine());
                    
                } catch (NumberFormatException NFE) {
                    System.out.println("enter a number only.");
                }
                switch(input) {
                    case 1:
                        // System.out.println("not yet implemented");
                        // select and read assembler file for compilation
                        System.out.println("Enter the name of the file you want to load: ");
                        try {
                            FileManagement.initAssemblyDirectory();
                            FileManagement.initExportDirectory();
                        } catch (Exception e) {
                            System.out.println("Error creating assembly file directory; please retry");
                            e.printStackTrace();
                            continue;
                        }
                        int i = 1;

                        List<Path> pathList =  Files.list(FileManagement.getImportpath()).collect(Collectors.toList());
                        for(Path path : pathList){
                            System.out.println(i + ". " + path.toString());
                            i++;
                        }
                        int fileIndex = Integer.parseInt(userInput.readLine()) - 1;
                        try {
                            FileManagement.loadAssemblyFile(pathList.get(fileIndex).toString());
                            AssemblyParser.parseFile(FileManagement.getLoadedAssemblyFile(), FileManagement.createImageFile(), 
                            FileManagement.getFileBufferedReader());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                            System.out.println("Error loading assembly file for writing; please retry");
                            continue;
                        }

                        break;
                    case 2:
                        System.out.println("not yet implemented");
                        //list contents of folder in console
                        break;
                    case 3:
                        //break enclosing loop
                        quit = true;
                        break;
                    default:
                        break;
                }
            }


        }catch(IOException exception) {
            exception.printStackTrace();
        }



    }
}
