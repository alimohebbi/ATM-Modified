package app.test.migrator.matching.server;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.Pair;
import app.test.migrator.matching.util.uiautomator.UiNode;

import static app.test.migrator.matching.CommonMatchingOps.addAttributes;

public class ServerSemanticMatchingPairs extends ServerSemanticMatchingABS<Pair<Event, List<Double>>, Pair<Event, List<Double>>> {
    public ServerSemanticMatchingPairs(
            List<Pair<Event, List<Double>>> objectToScored,
            List<Pair<Event, List<Double>>> labelNodes,
            UiNode sourceNode) {
        super(objectToScored,labelNodes, sourceNode);
    }

    @Override
    UiNode getNode(int i, List<Pair<Event, List<Double>>> objects) throws IOException {
        Pair<Event, List<Double>> clickableNode = objects.get(i);
        UiNode node = clickableNode.first.getTargetElement();
        addAttributes(node);
        return node;
    }

    @Override
    ScoredObject<Pair<Event, List<Double>>> getWrappedScoredObject(Map<String, Double> orderedIndex, String index) {
        Pair<Event, List<Double>> pair = this.objectToScored.get(Integer.parseInt(index));
        return new ScoredObject<>(pair, orderedIndex.get(index));
    }


}
