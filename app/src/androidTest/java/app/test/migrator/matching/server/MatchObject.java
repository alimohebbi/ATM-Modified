package app.test.migrator.matching.server;

import java.util.List;
import java.util.Map;

public class MatchObject {
    private Map<Integer, Map<String, String>> candidates;
    private  Map<String, String> sourceEvent;

    public MatchObject(Map<Integer, Map<String, String>> candidates, Map<String, String> sourceEvent) {
        this.candidates = candidates;
        this.sourceEvent = sourceEvent;
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
