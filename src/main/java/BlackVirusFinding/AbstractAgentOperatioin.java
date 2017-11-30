package BlackVirusFinding;

import CommonBean.NodeBean.BasicNode;
import jbotsim.Node;

import java.util.HashMap;

public abstract class AbstractAgentOperatioin {

    public abstract BasicNode chooseTarget(HashMap<Integer, Node> frontiersMap);
}
