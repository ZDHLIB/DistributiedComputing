import CommonBean.*;
import jbotsim.Topology;
import jbotsim.ui.JViewer;
import org.junit.Test;

public class TestFunctions {

    @Test
    public void testHelloWorld() throws InterruptedException {
        new JViewer(new Topology());
        Thread.sleep(50000);
    }

    @Test
    public void testCreatNode() throws InterruptedException {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(BasicNode.class);
        tp.addNode(50,50, new BasicNode());
        tp.addNode(100,100, new BasicNode());
        tp.addNode(150,150);
        new JViewer(tp);
        Thread.sleep(50000);
    }

    /**
     * Moving node to one direction
     * @throws InterruptedException
     */
    @Test
    public void testMovingTimingNode() throws InterruptedException {
        Topology tp = new Topology(400, 300);
        tp.setDefaultNodeModel(MovingNode.class);
        new JViewer(tp);
        Thread.sleep(50000);
    }

    /**
     * When the node has neighbors, change color from green to red
     * @throws InterruptedException
     */
    @Test
    public void testLink() throws InterruptedException {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(LonerGraphBased.class);
        new JViewer(tp);
        Thread.sleep(50000);
    }

    /**
     * Use the interface of TopologyListener, ConnectivityListener
     * @throws InterruptedException
     */
    @Test
    public void testTopology() throws InterruptedException {
        Topology tp = new Topology();
        new LonerCentralized(tp);
        new JViewer(tp);
        Thread.sleep(50000);
    }


    @Test
    public void testNodeModel() throws InterruptedException {
        Topology tp = new Topology(400,300);
        tp.setNodeModel("car", HighwayCar.class);
        new JViewer(tp);
        Thread.sleep(50000);
    }
}
