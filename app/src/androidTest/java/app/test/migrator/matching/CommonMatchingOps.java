package app.test.migrator.matching;

import java.io.IOException;
import java.util.List;

import app.test.migrator.matching.server.ScoredObject;
import app.test.migrator.matching.server.ServerSemanticMatchingPairs;
import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.Pair;
import app.test.migrator.matching.util.State;
import app.test.migrator.matching.util.uiautomator.UiNode;

class CommonMatchingOps {

    static List<ScoredObject<Pair<Event, List<Double>>>> getDynamicCandidates(State currState, Event event) throws IOException {
        UiNode node = event.getTargetElement();
        List<Pair<Event, List<Double>>> dynamicPairList = currState.getActionables();
        return new ServerSemanticMatchingPairs(dynamicPairList,
                node).getScoredObjects();
    }


    static List<ScoredObject<Pair<Event, List<Double>>>> getDynamicCandidates(State currState, UiNode event) throws IOException {
        List<Pair<Event, List<Double>>> dynamicPairList = currState.getActionables();
        return new ServerSemanticMatchingPairs(dynamicPairList,
                event).getScoredObjects();
    }



}
