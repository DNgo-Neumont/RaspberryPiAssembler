package dngo.raspberry;

public class LDMSTMCommand extends CommandBase{

    private String COMMAND_NAME;

    
    private final String DATA_COMMAND_BITS = "100";

    private String preIndexBit;
    private String upDownBit;
    private String byteWordBit;
    private String writeBackBit;
    private String loadStoreBit;
    
    private String baseRegister;
    
    private String registerBits;
    
    public void setCOMMAND_NAME(String COMMAND_NAME) {
        this.COMMAND_NAME = COMMAND_NAME;
    }


    @Override
    public String returnCommand() {
        String command = (getCond() + DATA_COMMAND_BITS + preIndexBit + upDownBit + byteWordBit + writeBackBit + loadStoreBit + baseRegister + registerBits);

        return command;
        
        
    }

    @Override
    public void buildCommand(String assemblyLine) {
        
        String[] stringArray = assemblyLine.split(" ");
        String condition = stringArray[0];
        condition = condition.replaceAll("["+ COMMAND_NAME +"]", "");
        condition = condition.replaceAll("[\\[\\]]", "");
        setCond(AssemblyParser.convHexToBinary(condition, 4));


        String preIndexBit = stringArray[1].replaceAll("[PRI\\{\\}]", "");

        if(!isZeroOrOneBit(preIndexBit)){
            throw new IllegalArgumentException("Invalid param given for preIndexBit!");
        }

        this.preIndexBit = preIndexBit;

        String upDownBit = stringArray[2].replaceAll("[UD\\{\\}]", "");
        if(!isZeroOrOneBit(upDownBit)){
            throw new IllegalArgumentException("Invalid param given for upDownBit!");
        }

        this.upDownBit = upDownBit;
    
        String byteWordBit = stringArray[3].replaceAll("[BW\\{\\}]", "");
        if(!isZeroOrOneBit(byteWordBit)){
            throw new IllegalArgumentException("Invalid param given for byteWordBit!");
        }
        this.byteWordBit = byteWordBit;

        String writeBackBit = stringArray[4].replaceAll("[WRB\\{\\}]", "");
        if(!isZeroOrOneBit(writeBackBit)){
            throw new IllegalArgumentException("Invalid param given for writeBackBit!");
        }

        this.writeBackBit = writeBackBit;

        String loadStoreBit = stringArray[5].replaceAll("[LDST\\{\\}]", "");
        if(!isZeroOrOneBit(loadStoreBit)){
            throw new IllegalArgumentException("Invalid param given for loadStoreBit!");
        }

        this.loadStoreBit = loadStoreBit;

        this.baseRegister = AssemblyParser.convDecToBinary(stringArray[6],4);

        this.registerBits = parseArrayRange(stringArray[7]);

        if(stringArray.length > 8){
            setLabel(stringArray[8]);
        }
    }

    private String parseArrayRange(String registers){
        
        String registersFormatted = registers.replaceAll("[\\{\\}R]", "");

        String[] registersSplit = registersFormatted.split(",");

        StringBuilder builder = new StringBuilder("0000000000000000");

        for(int i = Integer.valueOf(registersSplit[0]); i < Integer.valueOf(registersSplit[1]); i++ ){
            //yeah this is cursed. fixed! - before was a jank conversion of a string literal to char array
            builder.setCharAt(i, '1');
        }

        builder = builder.reverse();
        return builder.toString();
    }
    
}
