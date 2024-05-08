package pl.rsww.touroperator.initialization;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.dominik.api.HotelRequests;

import java.util.Properties;

import static pl.rsww.dominik.api.TourOperatorTopics.FLIGHT_INTEGRATION_TOPIC;
import static pl.rsww.dominik.api.TourOperatorTopics.HOTEL_INTEGRATION_TOPIC;

public class EventSender {
    private Producer<String, HotelRequests.CreateHotel> hotelProducer;
    private Producer<String, FlightRequests.CreateFlight> flightProducer;
    private static EventSender thisEventSender;

    public static EventSender getEventSender(){
        if(thisEventSender == null){
            thisEventSender = new EventSender(1);
        }
        return thisEventSender;
    }

    private void configure() {
        String bootstrapServer = System.getenv("BOOTSTRAP_SERVER");

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        hotelProducer = new KafkaProducer<>(producerProps);
        flightProducer = new KafkaProducer<>(producerProps);
    }

    public void sendHotel(HotelRequests.CreateHotel request, String key){
//        System.out.println("DEBUG sending hotel: " + request.name());
        ProducerRecord<String, HotelRequests.CreateHotel> record = new ProducerRecord<>(HOTEL_INTEGRATION_TOPIC, key, request);
        hotelProducer.send(record);
    }

    public void sendFlight(FlightRequests.CreateFlight request, String key){
//        System.out.println("DEBUG sending flight: " + request.flightNumber());
        ProducerRecord<String, FlightRequests.CreateFlight> record = new ProducerRecord<>(FLIGHT_INTEGRATION_TOPIC, key, request);
        flightProducer.send(record);
    }

    public EventSender(int useAsSingleton){
        configure();
    }
}
