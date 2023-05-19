package dngo.raspberry;

public class CommandFactory {
    enum CommandType {MOVT, MOVW, ADD, LDR, ORR, STR, SUB, B, BX, LDM, STM}

    public static CommandBase createCommand(String commandName){
        CommandBase commandResult;

        CommandType command = CommandType.valueOf(commandName);

        switch(command){
            case MOVT:
                commandResult = new MOVTCommand();
                break;
            case MOVW:
                commandResult = new MOVWCommand();
                break;
            case ADD:
                commandResult = new ADDCommand();
                break;
            case LDR:
                LDRSTRCommand ldrCommand = new LDRSTRCommand();
                ldrCommand.setCOMMAND_NAME("LDR");
                commandResult = ldrCommand;
                break;
            case ORR:
                commandResult = new ORRCommand();
                break;
            case STR:
                LDRSTRCommand strCommand = new LDRSTRCommand();
                strCommand.setCOMMAND_NAME("STR");
                commandResult = strCommand;
                break;
            case SUB:
                commandResult = new SUBCommand();
                break;
            case B:
                commandResult = new BCommand();
                break;
            case BX:
                commandResult = new BXCommand();
                break;
            case LDM:
                LDMSTMCommand ldmCommand = new LDMSTMCommand();
                ldmCommand.setCOMMAND_NAME("LDM");
                commandResult = ldmCommand;
                break;
            case STM:
                LDMSTMCommand stmCommand = new LDMSTMCommand();
                stmCommand.setCOMMAND_NAME("STM");
                commandResult = stmCommand;
                break;
            default:
                throw new IllegalArgumentException("Illegal command passed: " + commandName);
        }
        
        return commandResult;
    }
    
}
