package CommonBean;

import BlackVirusFinding.ManagerExp;

import java.util.Map;
import java.util.UUID;

public class StatisticInfo {
    private static StatisticInfo ourInstance = null;

    // "0": greedy based, "1": threshold based
    private static String type = "0";

    //Data fields
    private static Integer NO_MOVES = 0;
    private static Integer NO_MOVES_LEADER = 0;
    private static Integer NO_SHADOWS = 0;
    private static Integer NO_EDGES = 0;
    private static Integer NO_NODES = 0;
    private static Integer TERMINATE = 0;

    private static Long startTime = new Long(0);
    private static Long endTime = new Long(0);

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

    public static boolean checkTerminate(){
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

    public static Integer getNoMoves() {
        return NO_MOVES;
    }

    public static void setNoMoves(Integer noMoves) {
        NO_MOVES = noMoves;
    }

    public static Integer getNoMovesLeader() {
        return NO_MOVES_LEADER;
    }

    public static void setNoMovesLeader(Integer noMovesLeader) {
        NO_MOVES_LEADER = noMovesLeader;
    }

    public static Integer getTERMINATE() {
        return TERMINATE;
    }

    public static void setTERMINATE(Integer TERMINATE) {
        StatisticInfo.TERMINATE = TERMINATE;
    }

    public static Long getStartTime() {
        return startTime;
    }

    public static void setStartTime(Long startTime) {
        StatisticInfo.startTime = startTime;
    }

    public static Long getEndTime() {
        return endTime;
    }

    public static void setEndTime(Long endTime) {
        StatisticInfo.endTime = endTime;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        StatisticInfo.type = type;
    }
}
