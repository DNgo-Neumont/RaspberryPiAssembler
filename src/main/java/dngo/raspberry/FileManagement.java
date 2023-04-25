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

public class FileManagement {
    private static Path loadedFile = null;

    public static Path getLoadedFile() {
        return loadedFile;
    }

    public static void initAssemblyDirectory(){ 
        if(Files.notExists(FileSystems.getDefault().getPath("assembly_storage"))){
            try {
                Files.createDirectories(FileSystems.getDefault().getPath("assembly_storage"));
            } catch (IOException e) {
                System.out.println("Error creating local directory");
                e.printStackTrace();
            }
        }
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

}
