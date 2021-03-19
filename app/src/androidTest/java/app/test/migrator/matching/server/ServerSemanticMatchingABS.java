package app.test.migrator.matching.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.uiautomator.UiNode;

import static app.test.migrator.matching.server.ObjectSender.sendDescriptors;

public abstract class ServerSemanticMatchingABS<T, M> {
    List<T> objectToScored = null;
    Map<String, String> sourceEvent = null;

    public ServerSemanticMatchingABS(List<T> objectsToScored, UiNode sourceNode) {
        this.objectToScored = objectsToScored;
        this.sourceEvent = sourceNode.getAttributes();
    }

    public List<ScoredObject<M>> getScoredObjects() throws IOException {
        Map<Integer, Map<String, String>> nodesDict = new HashMap<>();
        for (int i = 0; i < objectToScored.size(); i++) {
            UiNode node = getNode(i);
            Map<String, String> attributes = node.getAttributes();
            nodesDict.put(i, attributes);
        }
        MatchObject matchObject = new MatchObject(nodesDict, sourceEvent);
        String jsonString = new ObjectMapper().writeValueAsString(matchObject);
        Map<String, Double> orderedIndex = sendDescriptors(jsonString);
        return getOrderedObject(orderedIndex);
    }


    abstract UiNode getNode(int i) throws IOException;

    abstract ScoredObject<M> getWrappedScoredObject(Map<String, Double> orderedIndex, String index);

    private List<ScoredObject<M>> getOrderedObject(Map<String, Double> orderedIndex) {
        ArrayList<ScoredObject<M>> orderedObjects = new ArrayList<>();
        for (String index : orderedIndex.keySet()) {
            ScoredObject<M> scoredObject = getWrappedScoredObject(orderedIndex, index);
            orderedObjects.add(scoredObject);
        }
        orderedObjects.sort(Comparator.comparing(ScoredObject<M>::getScore).reversed());
        return orderedObjects;
    }

}
