package app.test.migrator.matching.server;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.uiautomator.UiNode;

public class ServerSemanticMatchingNodes extends ServerSemanticMatchingABS<UiNode, UiNode> {
    public ServerSemanticMatchingNodes(List<UiNode> objectToScored, Event sourceEvent) {
        super(objectToScored, sourceEvent);
    }

    @Override
    UiNode getNode(int i) throws IOException {
        return this.objectToScored.get(i);
    }

    @Override
    ScoredObject<UiNode> getWrappedScoredObject(Map<String, Double> orderedIndex, String index) {
        UiNode node = this.objectToScored.get(Integer.parseInt(index));
        return new ScoredObject<>(node, orderedIndex.get(index));
    }

}
