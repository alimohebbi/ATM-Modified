package app.test.migrator.matching.server;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import app.test.migrator.matching.util.uiautomator.UiNode;

public class ServerSemanticMatchingNodes extends ServerSemanticMatchingABS<UiNode, UiNode> {
    public ServerSemanticMatchingNodes(List<UiNode> objectToScored, UiNode sourceNode) throws IOException {
        super(objectToScored, null, sourceNode);
    }

    @Override
    UiNode getNode(int i, List<UiNode> objects) throws IOException {
        return objects.get(i);
    }

    @Override
    ScoredObject<UiNode> getWrappedScoredObject(Map<String, Double> orderedIndex, String index) {
        UiNode node = this.objectToScored.get(Integer.parseInt(index));
        return new ScoredObject<>(node, orderedIndex.get(index));
    }

}
