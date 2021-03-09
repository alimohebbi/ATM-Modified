package app.test.migrator.matching.server;

import org.json.JSONObject;
import app.test.migrator.matching.util.uiautomator.BasicTreeNode;
import app.test.migrator.matching.util.uiautomator.UiNode;

import java.io.IOException;
import java.util.List;


class AttributesAdder {

    static void addAttributes(UiNode node) throws IOException {
        UiNode parent = (UiNode) node.getParent();
        String parentText = parent.getAttribute("text");
        List<BasicTreeNode> childrenList = parent.getChildrenList();
        UiNode sibling = null;
        for (BasicTreeNode child : childrenList) {
            sibling = (UiNode) child;
            if (!sibling.equals(node))
                break;
        }
        String siblingText = sibling.getAttribute("text");
        node.addAtrribute("parentText", parentText);
        node.addAtrribute("siblingText", siblingText);
    }
}
