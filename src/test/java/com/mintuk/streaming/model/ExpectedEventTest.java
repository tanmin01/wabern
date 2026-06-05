package com.mintuk.streaming.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for ExpectedEvent model
 */
public class ExpectedEventTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testConstructorAndGetters() {
        String id = "order-123";
        long timestamp = System.currentTimeMillis();
        String data = "Test data";

        ExpectedEvent event = new ExpectedEvent(id, timestamp, data);

        assertEquals(id, event.getId());
        assertEquals(timestamp, event.getTimestamp());
        assertEquals(data, event.getData());
    }

    @Test
    public void testDefaultConstructor() {
        ExpectedEvent event = new ExpectedEvent();

        assertNull(event.getId());
        assertEquals(0, event.getTimestamp());
        assertNull(event.getData());
    }

    @Test
    public void testSetters() {
        ExpectedEvent event = new ExpectedEvent();

        event.setId("event-456");
        event.setTimestamp(123456789L);
        event.setData("New data");

        assertEquals("event-456", event.getId());
        assertEquals(123456789L, event.getTimestamp());
        assertEquals("New data", event.getData());
    }

    @Test
    public void testToString() {
        ExpectedEvent event = new ExpectedEvent("order-789", 999888777L, "Payment processed");

        String str = event.toString();

        assertTrue(str.contains("ExpectedEvent"));
        assertTrue(str.contains("order-789"));
        assertTrue(str.contains("Payment processed"));
    }

    @Test
    public void testJsonSerialization() throws Exception {
        ExpectedEvent event = new ExpectedEvent("test-id", 555666777L, "Test payload");

        String json = mapper.writeValueAsString(event);
        ExpectedEvent deserialized = mapper.readValue(json, ExpectedEvent.class);

        assertEquals(event.getId(), deserialized.getId());
        assertEquals(event.getTimestamp(), deserialized.getTimestamp());
        assertEquals(event.getData(), deserialized.getData());
    }

    @Test
    public void testIsSerializable() throws Exception {
        // Verify that the class is serializable
        ExpectedEvent event = new ExpectedEvent("test", 123L, "data");
        assertNotNull(event);
        assertTrue(event instanceof java.io.Serializable);
    }
}
