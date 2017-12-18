import BlackVirusFinding.Initiator;
import BlackVirusFinding.ManagerExp;
import CommonBean.NodeBean.BasicNode;
import CommonBean.StatisticInfo;
import Events.TopologyEvents;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

import java.util.Date;

public class main {
    private static StatisticInfo statisticInfo = StatisticInfo.getInstance();
    public static void main(String[] orgs) throws InterruptedException {
        //Use GUI to start algorithm
        Integer nodeNO = Integer.valueOf(orgs[0]);
        String filePath = orgs[1];
        String type = orgs[2];  // "0": greedy based, "1": threshold based
        statisticInfo.setNoNodes(nodeNO);
        statisticInfo.setType(type);
        statisticInfo.setStartTime(new Date().getTime());
        ManagerExp.setFilePath(filePath);
        ManagerExp.start(nodeNO, filePath, type);

        return;
    }
}
