package app.test.migrator.matching.server;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

import app.test.migrator.matching.util.uiautomator.UiNode;

import static app.test.migrator.matching.CommonMatchingOps.addFamilyAttributes;
import static app.test.migrator.matching.server.ObjectSender.sendDescriptors;

public abstract class ServerSemanticMatchingABS<T, M> {
    List<T> objectToScored = null;
    List<T> labelsNodes = null;
    Map<String, String> sourceEvent = null;

    public ServerSemanticMatchingABS(List<T> objectsToScored, List<T> labelNodes, UiNode sourceNode) throws IOException {
        this.objectToScored = objectsToScored;
        this.sourceEvent = sourceNode.getAttributes();
        this.labelsNodes = labelNodes;
        addFamilyAttributes(sourceNode);
    }

    public List<ScoredObject<M>> getScoredObjects() throws IOException {
        Map<Integer, Map<String, String>> candidates = convertListToMap(this.objectToScored);
        Map<Integer, Map<String, String>> labelNodes = convertListToMap(this.labelsNodes);
        MatchObject matchObject = new MatchObject(candidates, labelNodes, sourceEvent);
        String jsonString = new ObjectMapper().writeValueAsString(matchObject);
        Map<String, Double> orderedIndex = sendDescriptors(jsonString);
        return getOrderedObject(orderedIndex);
    }

    private Map<Integer, Map<String, String>> convertListToMap(List<T> objects) throws IOException {
        if (objects == null)
            return null;
        Map<Integer, Map<String, String>> nodesDict = new HashMap<>();
        for (int i = 0; i < objects.size(); i++) {
            UiNode node = getNode(i, objects);
            Map<String, String> attributes = node.getAttributes();
            nodesDict.put(i, attributes);
        }
        return nodesDict;
    }


    abstract UiNode getNode(int i, List<T> objectList) throws IOException;

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
