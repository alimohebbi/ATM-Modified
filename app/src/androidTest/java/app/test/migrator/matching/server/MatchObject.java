package app.test.migrator.matching.server;
import java.util.Map;

public class MatchObject {
    private Map<Integer, Map<String, String>> candidates;
    private Map<Integer, Map<String, String>> targetLabels;
    private Map<String, String> sourceEvent;

    public MatchObject(Map<Integer, Map<String, String>> candidates,
                       Map<Integer, Map<String, String>> targetLabels,
                       Map<String, String> sourceEvent) {
        this.candidates = candidates;
        this.targetLabels = targetLabels;
        this.sourceEvent = sourceEvent;
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
}
