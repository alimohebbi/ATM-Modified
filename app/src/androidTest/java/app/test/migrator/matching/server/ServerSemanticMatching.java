package app.test.migrator.matching.server;


import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.Pair;
import app.test.migrator.matching.util.uiautomator.UiNode;

import static app.test.migrator.matching.server.SendObject.sendDescriptors;


public class ServerSemanticMatching {

    public static List<Event> rankEvents(List<Pair<Event, List<Double>>> pairList) throws IOException {
        Map<Integer, Map<String, String>> nodesDict = new HashMap<>();
        for (int i = 0; i < pairList.size(); i++) {
            Pair<Event, List<Double>> clickableNode = pairList.get(i);
            UiNode node = clickableNode.first.getTargetElement();
            // AttributesAdder.addAttributes(node);
            Map<String, String> attributes = node.getAttributes();
            nodesDict.put(i, attributes);
        }
        JSONObject jsonObject = new JSONObject(nodesDict);
        List<Integer> orderedIndex = sendDescriptors(jsonObject);
        return getOrderedEvents(pairList, orderedIndex);
    }

    private static List<Event> getOrderedEvents(List<Pair<Event, List<Double>>> pairList, List<Integer> orderedIndex) {
        ArrayList<Event> orderedEvents = new ArrayList<>();
        for (int index : orderedIndex) {
            Event event = pairList.get(index).first;
            orderedEvents.add(event);
        }
        return orderedEvents;
    }

}
