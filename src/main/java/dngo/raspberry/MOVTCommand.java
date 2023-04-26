package dngo.raspberry;


public class MOVTCommand extends CommandBase{


    private final String COMMAND_DEF = "00110100";

    private String immFour;

    private String destRegister;

    private String immTwelve;

    
    public String getImmFour() {
        return immFour;
    }
    
    public void setImmFour(String immFour) {
        this.immFour = immFour;
    }
    
    public String getDestRegister() {
        return destRegister;
    }
    
    public void setDestRegister(String destRegister) {
        this.destRegister = destRegister;
    }
    
    public String getImmTwelve() {
        return immTwelve;
    }

    public void setImmTwelve(String immTwelve) {
        this.immTwelve = immTwelve;
    }

    @Override
    public String returnCommand() {
        String numericValueCommand = (getCond() + COMMAND_DEF + immFour + destRegister + immTwelve);
        String binary = formatCommand(numericValueCommand);

        return binary;
    }

    @Override
    public void buildCommand(String assemblyLine){

    }

    
}
