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
        String[] stringArray = assemblyLine.split(" ");
        
        String condition = stringArray[0];
        condition = condition.replaceAll("[MOVT]", "");
        condition = condition.replaceAll("[\\[\\]]", "");

        condition = AssemblyParser.convHexToBinary(condition, 4);
        condition = formatCommand(condition);
        setCond(condition);

        String immFour = stringArray[1].replaceAll("[\\{\\}]", "");

        immFour = AssemblyParser.convHexToBinary(immFour, 4);
        this.immFour = immFour;

        String destRegister = stringArray[2].replaceAll("[R\\{\\}]", "");

        destRegister = AssemblyParser.convHexToBinary(destRegister, 4);

        this.destRegister = destRegister;

        this.immTwelve = AssemblyParser.convHexToBinary(stringArray[3], 12);

    }

    
}
