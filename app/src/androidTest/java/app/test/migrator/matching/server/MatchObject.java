package app.test.migrator.matching.server;
import java.util.Map;

public class MatchObject {
    private Map<Integer, Map<String, String>> candidates;
    private Map<Integer, Map<String, String>> labels;
    private Map<String, String> sourceEvent;

    public MatchObject(Map<Integer, Map<String, String>> candidates,
                       Map<Integer, Map<String, String>> labels,
                       Map<String, String> sourceEvent) {
        this.candidates = candidates;
        this.labels = labels;
        this.sourceEvent = sourceEvent;
    }

    public Map<Integer, Map<String, String>> getLabels() {
        return labels;
    }

    public void setLabels(Map<Integer, Map<String, String>> labels) {
        this.labels = labels;
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
}
