package CommonBean;

import BlackVirusFinding.ManagerExp;

import java.util.Map;
import java.util.UUID;

public class StatisticInfo {
    private static StatisticInfo ourInstance = null;

    //Data fields
    private static Integer NO_MOVES = 0;
    private static Integer NO_MOVES_LEADER = 0;
    private static Integer NO_SHADOWS = 0;
    private static Integer NO_EDGES = 0;
    private static Integer NO_NODES = 0;
    private static Integer TERMINATE = 0;


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

    public synchronized void reduceTERMINATE(){
        TERMINATE--;
        if(checkTerminate()){
            ManagerExp.writeResult();
            ManagerExp.Terminate();
        }

    }

    public String[] getResults(){
        String[] res = new String[7];
        res[0] = String.valueOf(UUID.randomUUID());
        res[1] = String.valueOf(NO_NODES);
        res[2] = String.valueOf(NO_EDGES);
        res[3] = String.valueOf(NO_MOVES);
        res[4] = String.valueOf(NO_MOVES_LEADER);
        res[5] = String.valueOf(NO_SHADOWS);
        return res;
    }

    private boolean checkTerminate(){
        if(TERMINATE <= 0){
            return true;
        }
        return false;
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

    public static Integer getTERMINATE() {
        return TERMINATE;
    }

    public static void setTERMINATE(Integer TERMINATE) {
        StatisticInfo.TERMINATE = TERMINATE;
    }
}
