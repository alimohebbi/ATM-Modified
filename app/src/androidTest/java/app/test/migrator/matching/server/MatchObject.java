package app.test.migrator.matching.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import app.test.migrator.matching.EventMatching;

public class MatchObject {
    private Map<Integer, Map<String, String>> candidates;
    private Map<Integer, Map<String, String>> targetLabels;
    private Map<Integer, Map<String, String>> sourceLabels;
    private Map<String, String> sourceEvent;
    private Map<String, String> smConfig;

    public Map<String, String> getSmConfig() {
        return smConfig;
    }

    public void setSmConfig(Map<String, String> smConfig) {
        this.smConfig = smConfig;
    }

    public MatchObject(Map<Integer, Map<String, String>> candidates,
                       Map<Integer, Map<String, String>> targetLabels,
                       Map<String, String> sourceEvent,
                       Map<Integer, Map<String, String>> sourceLabels) throws IOException {
        this.candidates = candidates;
        this.targetLabels = targetLabels;
        this.sourceEvent = sourceEvent;
        this.sourceLabels = sourceLabels;
        this.smConfig = new HashMap<>();
        this.smConfig.put("smConfig", EventMatching.getSemanticConfig());
    }

    public Map<Integer, Map<String, String>> getTargetLabels() {
        return targetLabels;
    }

    public void setTargetLabels(Map<Integer, Map<String, String>> targetLabels) {
        this.targetLabels = targetLabels;
    }

    public Map<Integer, Map<String, String>> getCandidates() {
        return candidates;
    }

    public void setCandidates(Map<Integer, Map<String, String>> candidates) {
        this.candidates = candidates;
    }

    public Map<String, String> getSourceEvent() {
        return sourceEvent;
    }

    public void setSourceEvent(Map<String, String> sourceEvent) {
        this.sourceEvent = sourceEvent;
    }

    public Map<Integer, Map<String, String>> getSourceLabels() {
        return sourceLabels;
    }

    public void setSourceLabels(Map<Integer, Map<String, String>> sourceLabels) {
        this.sourceLabels = sourceLabels;
    }
}
