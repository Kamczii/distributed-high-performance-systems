package pl.rsww.touroperator.initialization;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.rsww.touroperator.api.requests.FlightRequests;
import pl.rsww.touroperator.api.requests.HotelRequests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EventSender {
    private static final String TOPIC_HOTELS = "pl.rsww.hotel";
    private static final String TOPIC_FLIGHTS = "pl.rsww.flight";

    private Producer<String, HotelRequests.CreateHotel> hotelProducer;
    private Producer<String, FlightRequests.CreateFlight> flightProducer;


    private void conf() {
        String bootstrapServer = System.getenv("BOOTSTRAP_SERVER");

        // Kafka producer configuration
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        hotelProducer = new KafkaProducer<>(producerProps);
        flightProducer = new KafkaProducer<>(producerProps);
    }

    public void sendHotel(HotelRequests.CreateHotel request, String key){
//        System.out.println("DEBUG sending hotel: " + request.name());
        ProducerRecord<String, HotelRequests.CreateHotel> record = new ProducerRecord<>(TOPIC_HOTELS, key, request);
        hotelProducer.send(record);
    }

    public void sendFlight(FlightRequests.CreateFlight request, String key){
//        System.out.println("DEBUG sending flight: " + request.flightNumber());
        ProducerRecord<String, FlightRequests.CreateFlight> record = new ProducerRecord<>(TOPIC_FLIGHTS, key, request);
        flightProducer.send(record);
    }

    public EventSender(){
        conf();
    }
}
