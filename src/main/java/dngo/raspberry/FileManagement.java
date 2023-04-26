package dngo.raspberry;


import java.io.BufferedReader;
import java.io.IOException;
// import java.io.BufferedOutputStream;
// import java.io.OutputStream;
import java.nio.channels.ByteChannel;
// import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

public class FileManagement {
    private static Path loadedFile = null;

    private final static Path exportPath = FileSystems.getDefault().getPath("image_exports");
    private final static Path importPath = FileSystems.getDefault().getPath("assembly_storage");


    public static void initAssemblyDirectory(){ 
        if(Files.notExists(importPath)){
            try {
                Files.createDirectories(importPath);
            } catch (IOException e) {
                System.out.println("Error creating local directory");
                e.printStackTrace();
            }
        }
    }

    public static void initExportDirectory(){
        if(Files.notExists(exportPath)){
            try{
                Files.createDirectories(exportPath);
            }catch(IOException e){
                System.out.println("Error creating local directory");
                e.printStackTrace();
            }
        }
    }

    public static Path createImageFile(){
        if(Files.notExists(Path.of(exportPath.toString(), "image7.img"))){
            try {
                Files.createFile(Path.of(exportPath.toString(), "image7.img"));
                return Path.of(exportPath.toString(), "image7.img");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{

            try {
                int numOfFiles = Files.list(exportPath).collect(Collectors.toList()).size();
                Files.createFile(Path.of(exportPath.toString(), ("image7_(" + numOfFiles + ")_.img")));
                return Path.of(exportPath.toString(), ("image7_(" + numOfFiles + ")_.img"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void loadAssemblyFile(String fileName) throws Exception{

        Path reqPath = FileSystems.getDefault().getPath(fileName);
        if(Files.exists(reqPath)){
            loadedFile = reqPath;
        }else{
            throw new Exception("File doesn't exist!");
        }
    }

    public static Path getLoadedAssemblyFile(){
        return loadedFile;
    }
    
    //Deprecated
    public static ByteChannel getFileByteWriter() throws Exception{
        if(loadedFile != null){
            return Files.newByteChannel(loadedFile, StandardOpenOption.WRITE);
        }else{
            throw new Exception("No loaded file!");
        }
    }

    public static BufferedReader getFileBufferedReader() throws Exception{
        if(loadedFile != null){
            return Files.newBufferedReader(loadedFile);
        }else{
            throw new Exception("No loaded file!");
        }
    }

    public static Path getExportpath() {
        return exportPath;
    }

    public static Path getImportpath() {
        return importPath;
    }

}
