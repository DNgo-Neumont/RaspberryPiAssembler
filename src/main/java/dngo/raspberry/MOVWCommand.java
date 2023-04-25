package dngo.raspberry;

import java.math.BigInteger;

public class MOVWCommand extends CommandBase {
    
    private final String COMMAND_DEF = "00110000";

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
        String binary = new BigInteger(numericValueCommand, 2).toString(2);

        binary = String.format("%32s", binary);
        binary = binary.replaceAll(" ", "0");

        return binary;

    }
    
}
