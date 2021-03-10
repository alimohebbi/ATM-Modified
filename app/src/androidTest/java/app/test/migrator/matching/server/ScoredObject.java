package app.test.migrator.matching.server;

public class  ScoredObject <T> {

    public ScoredObject(T event, Double score) {
        this.event = event;
        this.score = score;
    }

    private T event;
    private Double score;

    public T getObject() {
        return event;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ScoredEvent{" + "event=" + event + ", score=" + score + '}';
    }
}

