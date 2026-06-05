package com.mintuk.streaming.service;

import com.mintuk.streaming.model.ExpectedEvent;
import com.mintuk.streaming.model.PaymentEvent;
import com.mintuk.streaming.model.MatchedEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyedCoprocessor for matching events from two Kafka streams by ID.
 * Maintains state for both streams and outputs matched pairs.
 */
public class StreamMatchingOperator {
    private static final Logger logger = LoggerFactory.getLogger(StreamMatchingOperator.class);
    
    // State variables to hold pending events
    private ExpectedEvent pendingEvent1;
    private PaymentEvent pendingEvent2;
    
    // Timeout in milliseconds for matching window
    private final long matchingTimeoutMs;

    public StreamMatchingOperator(long matchingTimeoutMs) {
        this.matchingTimeoutMs = matchingTimeoutMs;
    }

    public StreamMatchingOperator() {
        this(5000); // Default 5 second timeout
    }

    /**
     * Process an event from Stream 1
     * @param event1 Event from stream 1
     * @return MatchedEvents if both streams have events for this ID, null otherwise
     */
    public MatchedEvents processEvent1(ExpectedEvent event1) {
        logger.debug("Processing ExpectedEvent: {}", event1);
        
        // Check if we have a matching Event2
        if (pendingEvent2 != null && isWithinTimeWindow(event1.getTimestamp(), pendingEvent2.getTimestamp())) {
            MatchedEvents matched = createMatchedEvents(event1, pendingEvent2);
            pendingEvent2 = null; // Clear the matched state
            logger.info("Events matched: {}", matched);
            return matched;
        }
        
        // Store Event1 for future matching
        pendingEvent1 = event1;
        return null;
    }

    /**
     * Process an event from Stream 2
     * @param event2 Payment event from stream 2
     * @return MatchedEvents if both streams have events for this ID, null otherwise
     */
    public MatchedEvents processEvent2(PaymentEvent event2) {
        logger.debug("Processing PaymentEvent: {}", event2);
        
        // Check if we have a matching Event1
        if (pendingEvent1 != null && isWithinTimeWindow(pendingEvent1.getTimestamp(), event2.getTimestamp())) {
            MatchedEvents matched = createMatchedEvents(pendingEvent1, event2);
            pendingEvent1 = null; // Clear the matched state
            logger.info("Events matched: {}", matched);
            return matched;
        }
        
        // Store Event2 for future matching
        pendingEvent2 = event2;
        return null;
    }

    /**
     * Check if two timestamps are within the matching window
     */
    private boolean isWithinTimeWindow(long ts1, long ts2) {
        long timeDiff = Math.abs(ts1 - ts2);
        return timeDiff <= matchingTimeoutMs;
    }

    /**
     * Create a matched events object from both streams
     */
    private MatchedEvents createMatchedEvents(ExpectedEvent event1, PaymentEvent event2) {
        String matchedId = event1.getId(); // Use ExpectedEvent's ID as the primary key
        long matchedAt = System.currentTimeMillis();
        return new MatchedEvents(matchedId, event1, event2, matchedAt);
    }

    /**
     * Clear pending state
     */
    public void clearPendingState() {
        pendingEvent1 = null;
        pendingEvent2 = null;
        logger.debug("Cleared pending state");
    }

    /**
     * Get current state for debugging
     */
    public String getStateInfo() {
        return String.format("State[event1=%s, event2=%s]", 
            pendingEvent1 != null ? pendingEvent1.getId() : "null",
            pendingEvent2 != null ? pendingEvent2.getId() : "null");
    }
}
