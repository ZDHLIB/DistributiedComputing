package CommonBean.Agents;

public enum AgentTypeEnum {
    BLACKVIRUS("BLACKVIRUS",1),
    CLONESVIRUS("CLONESVIRUS",2),
    LEADER("LEADER",3),
    EXPLORER("EXPLORER",4),
    CLEANER("CLEANER",5);

    private String name;
    private int index;

    AgentTypeEnum(String name, int index){
        this.name = name;
        this.index = index;
    }

    public String getName(){
        return this.name;
    }

    public int getIndex(){
        return this.index;
    }
}
