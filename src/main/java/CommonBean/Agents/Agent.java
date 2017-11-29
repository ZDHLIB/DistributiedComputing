package CommonBean.Agents;

public abstract class Agent{

    protected boolean activate = true;
    protected AgentTypeEnum type;

    public AgentTypeEnum getType(){
        return this.type;
    }

    public boolean getActivate(){
        return this.activate;
    }

    public void setActivate(boolean b){
        this.activate = b;
    }
}
