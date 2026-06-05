package com.mintuk.streaming;

import com.mintuk.streaming.model.ExpectedEvent;
import com.mintuk.streaming.model.PaymentEvent;
import com.mintuk.streaming.model.MatchedEvents;
import com.mintuk.streaming.service.StreamMatchingOperator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Flink Job for matching events from two Kafka streams based on ID
 * 
 * This job demonstrates:
 * - Reading from two separate Kafka topics
 * - Using a KeyedCoprocessor (via StreamMatchingOperator) to match events by ID
 * - Writing matched events to an output Kafka topic
 * 
 * Note: Flink and Kafka dependencies can be added when SSL/TLS issues are resolved
 */
public class StreamMatchingJob {
    private static final Logger logger = LoggerFactory.getLogger(StreamMatchingJob.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        logger.info("Starting Stream Matching Job");
        
        // Configuration
        String kafkaBrokers = System.getProperty("kafka.brokers", "localhost:9092");
        String topic1 = System.getProperty("kafka.topic1", "events-stream-1");
        String topic2 = System.getProperty("kafka.topic2", "events-stream-2");
        String outputTopic = System.getProperty("kafka.output.topic", "matched-events");
        long matchingTimeoutMs = Long.parseLong(System.getProperty("matching.timeout.ms", "5000"));
        
        logger.info("Configuration: brokers={}, topic1={}, topic2={}, output={}, timeout={}ms",
            kafkaBrokers, topic1, topic2, outputTopic, matchingTimeoutMs);
        
        // Example usage of StreamMatchingOperator
        demonstrateStreamMatchingOperator(matchingTimeoutMs);
        
        /*
         * Full Flink job implementation (uncomment when Flink dependencies are available):
         * 
         * final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
         * env.setParallelism(1);
         * 
         * // Source 1: Read from Kafka topic
         * DataStream<ExpectedEvent> stream1 = env
         *     .addSource(new FlinkKafkaConsumer<>(topic1, new ExpectedEventDeserializer(), kafkaProps))
         *     .keyBy(ExpectedEvent::getId)
         *     .name("ExpectedEvent-Source");
         * 
         * // Source 2: Read from Kafka topic
        * DataStream<PaymentEvent> stream2 = env
        *     .addSource(new FlinkKafkaConsumer<>(topic2, new PaymentEventDeserializer(), kafkaProps))
        *     .keyBy(PaymentEvent::getId)
        *     .name("PaymentEvent-Source");
         * 
         * // Join streams using KeyedCoprocessor
         * DataStream<MatchedEvents> matchedEvents = stream1
         *     .connect(stream2)
        *     .keyBy(ExpectedEvent::getId, PaymentEvent::getId)
         *     .process(new StreamMatchingCoprocessor(matchingTimeoutMs))
         *     .name("Stream-Matcher");
         * 
         * // Sink: Write matched events to output Kafka topic
         * matchedEvents
         *     .addSink(new FlinkKafkaProducer<>(outputTopic, new MatchedEventSerializer(), kafkaProps))
         *     .name("Matched-Events-Sink");
         * 
         * env.execute("Stream Matching Job");
         */
    }

    /**
     * Demonstrate the StreamMatchingOperator functionality
     */
    private static void demonstrateStreamMatchingOperator(long timeoutMs) {
        logger.info("=== Demonstrating StreamMatchingOperator ===");
        
        StreamMatchingOperator operator = new StreamMatchingOperator(timeoutMs);
        
        // Create test events
        ExpectedEvent event1 = new ExpectedEvent("order-123", System.currentTimeMillis(), "Order placed");
        PaymentEvent event2 = new PaymentEvent("order-123", System.currentTimeMillis() + 1000, 99.99);
        
        // Process events
        MatchedEvents result1 = operator.processEvent1(event1);
        logger.info("After Event1: result = {}, operator state = {}", result1, operator.getStateInfo());
        
        MatchedEvents result2 = operator.processEvent2(event2);
        logger.info("After Event2: result = {}, operator state = {}", result2, operator.getStateInfo());
        
        if (result2 != null) {
            logger.info("Successfully matched events: {}", result2);
        }
    }

    /**
     * Configuration for Kafka properties (placeholder)
     */
    public static java.util.Properties getKafkaProperties(String brokers) {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("bootstrap.servers", brokers);
        props.setProperty("group.id", "stream-matching-job");
        props.setProperty("auto.offset.reset", "earliest");
        return props;
    }
}
