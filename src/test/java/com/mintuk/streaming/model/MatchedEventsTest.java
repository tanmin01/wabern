package com.mintuk.streaming.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MatchedEvents model
 */
public class MatchedEventsTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testConstructorAndGetters() {
        String id = "order-123";
        ExpectedEvent event1 = new ExpectedEvent("order-123", 1000L, "Order placed");
        PaymentEvent event2 = new PaymentEvent("order-123", 1500L, 99.99);
        long matchedAt = System.currentTimeMillis();

        MatchedEvents matched = new MatchedEvents(id, event1, event2, matchedAt);

        assertEquals(id, matched.getId());
        assertEquals(event1, matched.getEvent1());
        assertEquals(event2, matched.getEvent2());
        assertEquals(matchedAt, matched.getMatchedAt());
    }

    @Test
    public void testDefaultConstructor() {
        MatchedEvents matched = new MatchedEvents();

        assertNull(matched.getId());
        assertNull(matched.getEvent1());
        assertNull(matched.getEvent2());
        assertEquals(0, matched.getMatchedAt());
    }

    @Test
    public void testSetters() {
        MatchedEvents matched = new MatchedEvents();

        ExpectedEvent event1 = new ExpectedEvent("order-456", 2000L, "Payment confirmed");
        PaymentEvent event2 = new PaymentEvent("order-456", 2500L, 150.50);

        matched.setId("order-456");
        matched.setEvent1(event1);
        matched.setEvent2(event2);
        matched.setMatchedAt(999888777L);

        assertEquals("order-456", matched.getId());
        assertEquals(event1, matched.getEvent1());
        assertEquals(event2, matched.getEvent2());
        assertEquals(999888777L, matched.getMatchedAt());
    }

    @Test
    public void testToString() {
        ExpectedEvent event1 = new ExpectedEvent("order-789", 3000L, "Order created");
        PaymentEvent event2 = new PaymentEvent("order-789", 3500L, 200.00);
        MatchedEvents matched = new MatchedEvents("order-789", event1, event2, 555666777L);

        String str = matched.toString();

        assertTrue(str.contains("MatchedEvents"));
        assertTrue(str.contains("order-789"));
    }

    @Test
    public void testJsonSerialization() throws Exception {
        ExpectedEvent event1 = new ExpectedEvent("test-id", 4000L, "Test event 1");
        PaymentEvent event2 = new PaymentEvent("test-id", 4500L, 123.45);
        MatchedEvents matched = new MatchedEvents("test-id", event1, event2, 777888999L);

        String json = mapper.writeValueAsString(matched);
        MatchedEvents deserialized = mapper.readValue(json, MatchedEvents.class);

        assertEquals(matched.getId(), deserialized.getId());
        assertEquals(matched.getEvent1().getId(), deserialized.getEvent1().getId());
        assertEquals(matched.getEvent2().getId(), deserialized.getEvent2().getId());
        assertEquals(matched.getMatchedAt(), deserialized.getMatchedAt());
    }

    @Test
    public void testMatchedEventsWithDifferentIds() {
        // This is a valid scenario - testing event matching
        ExpectedEvent event1 = new ExpectedEvent("order-100", 5000L, "Order");
        PaymentEvent event2 = new PaymentEvent("order-100", 5500L, 50.00);
        MatchedEvents matched = new MatchedEvents("order-100", event1, event2, 888999000L);

        assertEquals("order-100", matched.getId());
        assertEquals(event1.getId(), event2.getId());
    }
}
