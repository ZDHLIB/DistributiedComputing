package BlackVirusFinding;

import CommonBean.NodeBean.BasicNode;
import CommonBean.StatisticInfo;
import jbotsim.Node;
import jbotsim.Topology;
import utils.Output;

import java.util.List;

public class ManagerExp {
//    private final static Logger logger = LoggerFactory.getLogger(ManagerExp.class);
    private static StatisticInfo statisticInfo = StatisticInfo.getInstance();
    private static String filePath = "./src/main/resources/Results/res.csv";

    public static void start(Integer nodeNo, String path, String type){
        filePath = path;
        statisticInfo.setType(type);
        while( !startRound(nodeNo) ){}
    }

    private static boolean startRound(Integer NodesNo){
        AlgorithmFacade algorithmFacade = new AlgorithmFacade();
        Topology tp = Initiator.initTopology(NodesNo,600,600,true, BasicNode.class);
        // initial success;
        if( tp != null ){
            BasicNode sink = getSinkNode(tp);
            AbstractAgentOperatioin.sendVisited2Neighbours(sink, sink.getNeighbors());
            algorithmFacade.startExplorer();
            return true;
        } else {
//            logger.info("Initial topology failed as there are some disconnected nodes");
            return false;
        }
    }

    public static void writeResult(){
        String[] res = statisticInfo.getResults();
        Output.CSVWriteOneLine(res, filePath);
    }

    private static BasicNode getSinkNode(Topology tp){
        List<Node> nodeList = tp.getNodes();
        for(Node node : nodeList){
            if( node.getID() == 0 ){
                return (BasicNode)node;
            }
        }
        return null;
    }

    public static void Terminate(){
        System.exit(0);
    }
}
