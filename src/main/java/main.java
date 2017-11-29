import BlackVirusFinding.Initiator;
import CommonBean.NodeBean.BasicNode;
import jbotsim.Topology;
import jbotsim.ui.JViewer;

public class main {

    public static void main(String[] orgs){

        Topology topology = Initiator.initTopology(5,600,600,true, BasicNode.class);

        JViewer jViewer = new JViewer(topology);
        return;
    }
}
