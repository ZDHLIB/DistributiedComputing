import BlackVirusFinding.ManagerExp;
import CommonBean.StatisticInfo;

public class main {

    public static void main(String[] orgs) throws InterruptedException {
        //Use GUI to start algorithm
//        Topology topology = Initiator.initTopology(6,600,600,true, BasicNode.class);
//        new TopologyEvents(topology);
//        new JViewer(topology);

        //Auto running
        Integer nodeNO = Integer.valueOf(orgs[0]);
        String filePath = orgs[1];
        ManagerExp.start(nodeNO, filePath);

        return;
    }
}
