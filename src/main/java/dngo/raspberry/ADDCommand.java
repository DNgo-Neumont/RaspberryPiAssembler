package dngo.raspberry;

public class ADDCommand extends CommandBase{

    private String COMMAND_DEF;
    private String COMMAND_NAME;
    
    private final String DATA_COMMAND_NIBBLE = "00";
    
    private String immediateOperandBit;
    private String conditionCodeBit;
    
    private String firstRegister;
    private String secondRegister;
    
    private String operand;
    
    public void setCOMMAND_DEF(String COMMAND_DEF) {
        this.COMMAND_DEF = COMMAND_DEF;
    }
    
    public void setCOMMAND_NAME(String COMMAND_NAME) {
        this.COMMAND_NAME = COMMAND_NAME;
    }

    public ADDCommand(){
        COMMAND_DEF = "0100";
        COMMAND_NAME = "ADD";
    }

    @Override
    public void buildCommand(String assemblyLine) {
        String[] stringArray = assemblyLine.split(" ");
        
        
        String condition = stringArray[0];
        condition = condition.replaceAll("["+ COMMAND_NAME +"]", "");
        condition = condition.replaceAll("[\\[\\]]", "");

        String[] registers = stringArray[2].strip().split(",");

        String operand = stringArray[4].strip();
        String binaryCondition = AssemblyParser.convHexToBinary(condition, 4);
        String registerOne = AssemblyParser.convHexToBinary(registers[0], 4);

        String registerTwo = AssemblyParser.convHexToBinary(registers[1], 4);
        firstRegister = registerOne;
        secondRegister = registerTwo;
        
        String immediateOperandBit = stringArray[1];
        immediateOperandBit = immediateOperandBit.replaceAll("[IM\\{\\}]", "");
        if(!isZeroOrOneBit(immediateOperandBit)){
            throw new IllegalArgumentException("Invalid param given for immediateOperandBit!");
        }

        this.immediateOperandBit = immediateOperandBit;
        

        String conditionCodeBit = stringArray[3];
        conditionCodeBit = conditionCodeBit.replaceAll("[SCODE\\{\\}]", "");
        if(!isZeroOrOneBit(conditionCodeBit)){
            throw new IllegalArgumentException("Invalid param given for immediateOperandBit!");
        }
        this.conditionCodeBit = conditionCodeBit;
    
        this.operand = AssemblyParser.convHexToBinary(operand, 12);

        setCond(binaryCondition);
        



    }

    @Override
    public String returnCommand() {
        String result = (getCond() + DATA_COMMAND_NIBBLE + immediateOperandBit + COMMAND_DEF + conditionCodeBit + firstRegister + secondRegister + operand);
        
        String binary = formatCommand(result);

        return binary;
    }
    
}
