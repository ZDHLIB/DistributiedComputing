package CommonBean;

public class StatisticInfo {
    private static StatisticInfo ourInstance = null;

    //Data fields
    private static Integer NO_MOVES = 0;
    private static Integer NO_MOVES_LEADER = 0;
    private static Integer NO_SHADOWS = 0;
    private static Integer NO_EDGES = 0;
    private static Integer NO_NODES = 0;


    public static StatisticInfo getInstance() {
        if( ourInstance == null ){
            synchronized (StatisticInfo.class){
                if( ourInstance == null ){
                    ourInstance = new StatisticInfo();
                    return ourInstance;
                }
            }
        }
        return ourInstance;
    }

    private StatisticInfo() {
    }

    public synchronized void addNO_MOVES(){
        NO_MOVES++;
    }

    public synchronized void addNO_MOVES_LEADER(){
        NO_MOVES_LEADER++;
    }

    public static Integer getNoEdges() {
        return NO_EDGES;
    }

    public static void setNoEdges(Integer noEdges) {
        NO_EDGES = noEdges;
    }

    public static Integer getNoNodes() {
        return NO_NODES;
    }

    public static void setNoNodes(Integer noNodes) {
        NO_NODES = noNodes;
    }

    public static Integer getNoShadows() {
        return NO_SHADOWS;
    }

    public static void setNoShadows(Integer noShadows) {
        NO_SHADOWS = noShadows;
    }
}
