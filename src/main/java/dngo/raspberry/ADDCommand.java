package dngo.raspberry;

public class ADDCommand extends CommandBase{

    private final String COMMAND_DEF = "0100";
    private final String DATA_COMMAND_NIBBLE = "00";

    private String immediateOperandBit;
    private String conditionCodeBit;

    private String firstRegister;
    private String secondRegister;

    private String operand;

    @Override
    public void buildCommand(String assemblyLine) {
        String[] stringArray = assemblyLine.split(" ");
        
        String condition = stringArray[0].replaceAll("ADD\\[\\]", "");

        String[] registers = stringArray[1].strip().split(",");
        String operand = stringArray[2].strip();


    }

    @Override
    public String returnCommand() {
        String result = (getCond() + DATA_COMMAND_NIBBLE + immediateOperandBit + COMMAND_DEF + conditionCodeBit + firstRegister + secondRegister + operand);
        
        String binary = formatCommand(result);

        return binary;
    }
    
}
