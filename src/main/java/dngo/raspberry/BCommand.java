package dngo.raspberry;

public class BCommand extends CommandBase {

    private final String COMMAND_BITS = "101";
    
    private String linkBit;

    private String offsetString;

    
    /** 
     * @return String
     */
    public String getLinkBit(){
        return linkBit;
    }

    /**
    * TO ONLY BE USED IN CASES OF LINK BIT BEING SET TO ON
    @param calculatedOffset Computer generated offset for line to branch back to
     **/
    public void setOffset(String calculatedOffset){
        offsetString = calculatedOffset;
    }

    public int getOffsetAsInt(){
        if(!offsetString.isBlank()){
            return Integer.valueOf(offsetString, 2);
        }
        return 0;
    }

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

        if(stringArray.length > 3){
            setLabel(stringArray[3]);
        }

    }
    
}
