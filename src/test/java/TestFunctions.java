import CommonBean.BasicNode;
import CommonBean.LonerMessageBased;
import CommonBean.MovingNode;
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

    @Test
    public void testMovingTimingNode() throws InterruptedException {
        Topology tp = new Topology(400, 300);
        tp.setDefaultNodeModel(MovingNode.class);
        new JViewer(tp);
        Thread.sleep(50000);
    }

    @Test
    public void testMessagePassing() throws InterruptedException {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(LonerMessageBased.class);
        new JViewer(tp);
        Thread.sleep(50000);
    }
}
