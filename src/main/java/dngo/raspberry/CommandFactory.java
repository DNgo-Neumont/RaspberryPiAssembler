package dngo.raspberry;

public class CommandFactory {
    enum CommandType {MOVT, MOVW, ADD, LDR, ORR, STR, SUB, B}

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
            default:
                throw new IllegalArgumentException("Illegal command passed: " + commandName);
        }
        
        return commandResult;
    }
    
}
