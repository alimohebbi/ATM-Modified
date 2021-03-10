package app.test.migrator.matching.server;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.Pair;
import app.test.migrator.matching.util.uiautomator.UiNode;

public class ServerSemanticMatchingPairs extends ServerSemanticMatchingABS<Pair<Event, List<Double>>, Event> {
    public ServerSemanticMatchingPairs(List<Pair<Event, List<Double>>> objectToScored, Event sourceEvent) {
        super(objectToScored, sourceEvent);
    }

    @Override
    UiNode getNode(int i) throws IOException {
        Pair<Event, List<Double>> clickableNode = this.objectToScored.get(i);
        UiNode node = clickableNode.first.getTargetElement();
//        AttributesAdder.addAttributes(node);
        return node;
    }

    @Override
    ScoredObject<Event> getWrappedScoredObject(Map<String, Double> orderedIndex, String index) {
        Event event = this.objectToScored.get(Integer.parseInt(index)).first;
        return new ScoredObject<>(event, orderedIndex.get(index));
    }


}
