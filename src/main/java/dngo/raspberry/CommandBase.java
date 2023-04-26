package dngo.raspberry;

import java.math.BigInteger;

public abstract class CommandBase {

    public String cond;


    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public abstract String returnCommand();

    public abstract void buildCommand(String assemblyLine);

    public String formatCommand(String numericValueCommand){
        String binary = new BigInteger(numericValueCommand, 2).toString(2);

        binary = String.format("%32s", binary);
        binary = binary.replaceAll(" ", "0");
        return binary;
    }

}
