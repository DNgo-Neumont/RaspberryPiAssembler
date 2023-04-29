package dngo.raspberry;

public class BCommand extends CommandBase {

    private final String COMMAND_BITS = "101";
    
    private String linkBit;

    private String offsetString;

    @Override
    public String returnCommand() {

        String result = (getCond() + COMMAND_BITS + linkBit + offsetString);

        return result;
    }

    @Override
    public void buildCommand(String assemblyLine) {
        String[] stringArray = assemblyLine.split(" ");
        String condition = stringArray[0];
        condition = condition.replaceAll("[B]", "");
        condition = condition.replaceAll("[\\[\\]]", "");
        setCond(AssemblyParser.convHexToBinary(condition, 4));

        String linkBit = stringArray[1].replaceAll("[L\\{\\}]", "");
        if(!isZeroOrOneBit(linkBit)){
            throw new IllegalArgumentException("Invalid param given for immediateOperandBit!");
        }

        this.linkBit = linkBit;

        this.offsetString = AssemblyParser.convHexToBinary(stringArray[2], 24);

    }
    
}
