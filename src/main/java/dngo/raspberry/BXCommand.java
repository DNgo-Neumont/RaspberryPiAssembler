package dngo.raspberry;

public class BXCommand extends CommandBase{

    private final String COMMAND_BITS = "000100101111111111110001";

    private String sourceRegister;

    @Override
    public String returnCommand() {
        return (getCond() + COMMAND_BITS + sourceRegister);
    }

    @Override
    public void buildCommand(String assemblyLine) {
        String[] stringArray = assemblyLine.split(" ");
        String condition = stringArray[0];
        condition = condition.replaceAll("[BX]", "");
        condition = condition.replaceAll("[\\[\\]]", "");
        setCond(AssemblyParser.convHexToBinary(condition, 4));
        
        this.sourceRegister = AssemblyParser.convHexToBinary(stringArray[1], 4);
    }
    
}
