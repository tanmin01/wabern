package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Data model for matched events from both streams
 */
public class MatchedEvents implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("event1")
    private ExpectedEvent event1;

    @JsonProperty("event2")
    private PaymentEvent event2;

    @JsonProperty("matchedAt")
    private long matchedAt;

    public MatchedEvents() {
    }

    public MatchedEvents(String id, ExpectedEvent event1, PaymentEvent event2, long matchedAt) {
        this.id = id;
        this.event1 = event1;
        this.event2 = event2;
        this.matchedAt = matchedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExpectedEvent getEvent1() {
        return event1;
    }

    public void setEvent1(ExpectedEvent event1) {
        this.event1 = event1;
    }

    public PaymentEvent getEvent2() {
        return event2;
    }

    public void setEvent2(PaymentEvent event2) {
        this.event2 = event2;
    }

    public long getMatchedAt() {
        return matchedAt;
    }

    public void setMatchedAt(long matchedAt) {
        this.matchedAt = matchedAt;
    }

    @Override
    public String toString() {
        return "MatchedEvents{" +
                "id='" + id + '\'' +
                ", event1=" + event1 +
                ", event2=" + event2 +
                ", matchedAt=" + matchedAt +
                '}';
    }
}
