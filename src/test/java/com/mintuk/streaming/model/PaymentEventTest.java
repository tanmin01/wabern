package com.mintuk.streaming.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for PaymentEvent model
 */
public class PaymentEventTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testConstructorAndGetters() {
        String id = "payment-123";
        long timestamp = System.currentTimeMillis();
        double value = 99.99;

        PaymentEvent event = new PaymentEvent(id, timestamp, value);

        assertEquals(id, event.getId());
        assertEquals(timestamp, event.getTimestamp());
        assertEquals(value, event.getValue(), 0.01);
    }

    @Test
    public void testDefaultConstructor() {
        PaymentEvent event = new PaymentEvent();

        assertNull(event.getId());
        assertEquals(0, event.getTimestamp());
        assertEquals(0.0, event.getValue(), 0.0);
    }

    @Test
    public void testSetters() {
        PaymentEvent event = new PaymentEvent();

        event.setId("payment-456");
        event.setTimestamp(987654321L);
        event.setValue(149.99);

        assertEquals("payment-456", event.getId());
        assertEquals(987654321L, event.getTimestamp());
        assertEquals(149.99, event.getValue(), 0.01);
    }

    @Test
    public void testToString() {
        PaymentEvent event = new PaymentEvent("order-789", 999888777L, 250.50);

        String str = event.toString();

        assertTrue(str.contains("PaymentEvent"));
        assertTrue(str.contains("order-789"));
        assertTrue(str.contains("250.5"));
    }

    @Test
    public void testJsonSerialization() throws Exception {
        PaymentEvent event = new PaymentEvent("test-id", 555666777L, 75.25);

        String json = mapper.writeValueAsString(event);
        PaymentEvent deserialized = mapper.readValue(json, PaymentEvent.class);

        assertEquals(event.getId(), deserialized.getId());
        assertEquals(event.getTimestamp(), deserialized.getTimestamp());
        assertEquals(event.getValue(), deserialized.getValue(), 0.01);
    }

    @Test
    public void testLargeValue() {
        PaymentEvent event = new PaymentEvent("large-payment", 111222333L, 999999.99);

        assertEquals(999999.99, event.getValue(), 0.01);
    }

    @Test
    public void testZeroValue() {
        PaymentEvent event = new PaymentEvent("zero-payment", 111222333L, 0.0);

        assertEquals(0.0, event.getValue(), 0.0);
    }
}
