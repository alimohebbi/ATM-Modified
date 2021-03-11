package app.test.migrator.matching.server;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.Pair;
import app.test.migrator.matching.util.uiautomator.UiNode;

public class ServerSemanticMatchingPairs extends ServerSemanticMatchingABS<Pair<Event, List<Double>>, Pair<Event, List<Double>>> {
    public ServerSemanticMatchingPairs(List<Pair<Event, List<Double>>> objectToScored, Event sourceEvent) {
        super(objectToScored, sourceEvent);
    }

    @Override
    UiNode getNode(int i) throws IOException {
        Pair<Event, List<Double>> clickableNode = this.objectToScored.get(i);
        UiNode node = clickableNode.first.getTargetElement();
        AttributesAdder.addAttributes(node);
        return node;
    }

    @Override
    ScoredObject<Pair<Event, List<Double>>> getWrappedScoredObject(Map<String, Double> orderedIndex, String index) {
        Pair<Event, List<Double>> pair = this.objectToScored.get(Integer.parseInt(index));
        return new ScoredObject<>(pair, orderedIndex.get(index));
    }


}
