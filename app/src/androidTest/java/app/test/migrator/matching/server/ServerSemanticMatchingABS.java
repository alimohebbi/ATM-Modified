package app.test.migrator.matching.server;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

import app.test.migrator.matching.EventMatching;
import app.test.migrator.matching.util.uiautomator.UiNode;

import static app.test.migrator.matching.CommonMatchingOps.addFamilyAttributes;
import static app.test.migrator.matching.server.ObjectSender.sendDescriptors;

public abstract class ServerSemanticMatchingABS<T, M> {
    List<T> objectToScored = null;
    List<T> targetLabels = null;
    List<T> sourceLabels = null;
    Map<String, String> sourceEvent = null;

    public ServerSemanticMatchingABS(List<T> objectsToScored, List<T> targetLabels, UiNode sourceNode, List<T> sourceLabels) throws IOException {
        this.objectToScored = objectsToScored;
        this.sourceEvent = sourceNode.getAttributes();
        this.targetLabels = targetLabels;
        this.sourceLabels = sourceLabels;
        addFamilyAttributes(sourceNode);
    }

    public List<ScoredObject<M>> getScoredObjects() throws IOException {
        Map<Integer, Map<String, String>> candidates = convertListToMap(this.objectToScored);
        Map<Integer, Map<String, String>> targetLabelsMap = convertListToMap(this.targetLabels);
        Map<Integer, Map<String, String>> sourceLabelsMap = convertListToMap(this.sourceLabels);
        MatchObject matchObject = new MatchObject(candidates, targetLabelsMap, sourceEvent, sourceLabelsMap);
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
        Collections.sort(orderedObjects, new Comparator<ScoredObject<M>>() {
            @Override
            public int compare(ScoredObject<M> o1, ScoredObject<M> o2) {
                return -1 * o1.getScore().compareTo(o2.getScore());
            }
        });
        return orderedObjects;
    }

}
