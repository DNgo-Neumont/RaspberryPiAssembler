package dngo.raspberry;

public class LDRSTRCommand extends CommandBase{

    private String COMMAND_NAME;

    private final String DATA_COMMAND_NIBBLE = "01";
    private String immediateOperandBit;
    private String preIndexBit;
    private String upDownBit;
    private String byteWordBit;
    private String writeBackBit;
    private String loadStoreBit;

    private String firstRegister;
    private String secondRegister;
    
    private String offsetBits;

    public void setCOMMAND_NAME(String COMMAND_NAME) {
        this.COMMAND_NAME = COMMAND_NAME;
    }

    @Override
    public String returnCommand() {

        String command = (getCond() + DATA_COMMAND_NIBBLE + immediateOperandBit + preIndexBit + upDownBit + byteWordBit + writeBackBit + loadStoreBit + firstRegister + secondRegister + offsetBits);

        return command;
    }

    @Override
    public void buildCommand(String assemblyLine) {
        String[] stringArray = assemblyLine.split(" ");
        String condition = stringArray[0];
        condition = condition.replaceAll("["+ COMMAND_NAME +"]", "");
        condition = condition.replaceAll("[\\[\\]]", "");
        setCond(AssemblyParser.convHexToBinary(condition, 4));

        String immediateOperandBit = stringArray[1].replaceAll("[IM\\{\\}]", "");
        if(!isZeroOrOneBit(immediateOperandBit)){
            throw new IllegalArgumentException("Invalid param given for immediateOperandBit!");
        }

        this.immediateOperandBit = immediateOperandBit;

        String preIndexBit = stringArray[2].replaceAll("[PRI\\{\\}]", "");

        if(!isZeroOrOneBit(immediateOperandBit)){
            throw new IllegalArgumentException("Invalid param given for preIndexBit!");
        }

        this.preIndexBit = preIndexBit;

        String upDownBit = stringArray[3].replaceAll("[UD\\{\\}]", "");
        if(!isZeroOrOneBit(upDownBit)){
            throw new IllegalArgumentException("Invalid param given for upDownBit!");
        }

        this.upDownBit = upDownBit;
    
        String byteWordBit = stringArray[4].replaceAll("[BW\\{\\}]", "");
        if(!isZeroOrOneBit(byteWordBit)){
            throw new IllegalArgumentException("Invalid param given for byteWordBit!");
        }
        this.byteWordBit = byteWordBit;

        String writeBackBit = stringArray[5].replaceAll("[WRB\\{\\}]", "");
        if(!isZeroOrOneBit(writeBackBit)){
            throw new IllegalArgumentException("Invalid param given for writeBackBit!");
        }

        this.writeBackBit = writeBackBit;

        String loadStoreBit = stringArray[6].replaceAll("[LDST\\{\\}]", "");
        if(!isZeroOrOneBit(loadStoreBit)){
            throw new IllegalArgumentException("Invalid param given for loadStoreBit!");
        }

        this.loadStoreBit = loadStoreBit;

        String[] registers = stringArray[7].split(",");

        this.firstRegister = AssemblyParser.convHexToBinary(registers[0],4);
        this.secondRegister = AssemblyParser.convHexToBinary(registers[1],4);

        this.offsetBits = AssemblyParser.convHexToBinary(stringArray[8], 12);

        if(stringArray.length > 8){
            setLabel(stringArray[8]);
        }


    }
    
}
