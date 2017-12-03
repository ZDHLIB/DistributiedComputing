package CommonBean;

import CommonBean.NodeBean.BasicNode;

import java.util.Queue;

public class MyMessage {

    private Object obj;
    private Queue<BasicNode> path;

    public MyMessage(Object obj, Queue<BasicNode> path) {
        this.obj = obj;
        this.path = path;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Queue<BasicNode> getPath() {
        return path;
    }

    public void setPath(Queue<BasicNode> path) {
        this.path = path;
    }
}
