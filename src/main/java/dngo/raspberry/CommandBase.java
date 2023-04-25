package dngo.raspberry;

public abstract class CommandBase {

    public String cond;


    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public abstract String returnCommand();

}
