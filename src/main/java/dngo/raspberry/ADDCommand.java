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
        
        if(stringArray.length < 5){
            String condition = stringArray[0];
            condition = condition.replaceAll("ADD", "");
            condition = condition.replaceAll("[\\[\\]]", "");
            System.out.println(condition);
            String[] registers = stringArray[1].strip().split(",");
            String operand = stringArray[2].strip();
            System.out.println(condition + " raw condition");
            String binaryCondition = AssemblyParser.convHexToBinary(condition, 4);
            System.out.println(binaryCondition + " formatted condition");
            String registerOne = AssemblyParser.convHexToBinary(registers[0], 4);
    
            String registerTwo = AssemblyParser.convHexToBinary(registers[1], 4);
            firstRegister = registerOne;
            secondRegister = registerTwo;
            immediateOperandBit = "1";
            conditionCodeBit = "0";
            this.operand = AssemblyParser.convHexToBinary(operand, 12);
    
            setCond(binaryCondition);
        }

        



    }

    @Override
    public String returnCommand() {
        String result = (getCond() + DATA_COMMAND_NIBBLE + immediateOperandBit + COMMAND_DEF + conditionCodeBit + firstRegister + secondRegister + operand);
        
        String binary = formatCommand(result);

        return binary;
    }
    
}
