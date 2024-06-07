package pl.rsww.touroperator.events;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.rsww.tour_operator.api.BusRequests;
import pl.rsww.tour_operator.api.FlightRequests;
import pl.rsww.tour_operator.api.HotelRequests;
import pl.rsww.tour_operator.api.UpdatePriceListRequests;

import java.util.Properties;

import static pl.rsww.tour_operator.api.TourOperatorTopics.*;

public class EventSender {
    private Producer<String, HotelRequests.CreateHotel> hotelProducer;
    private Producer<String, FlightRequests.CreateFlight> flightProducer;
    private Producer<String, BusRequests.CreateBus> busProducer;
    private Producer<String, UpdatePriceListRequests.UpdateBusPrices> updatePriceBusProducer;
    private Producer<String, UpdatePriceListRequests.UpdateFlightPrices> updatePriceFlightProducer;
    private Producer<String, UpdatePriceListRequests.UpdateHotelRoomPrices> updatePriceHotelRoomProducer;
    private Properties producerProps;
    private static EventSender thisEventSender;

    public static EventSender getEventSender(){
        if(thisEventSender == null){
            thisEventSender = new EventSender(1);
        }
        return thisEventSender;
    }

    private void configure() {
        String bootstrapServer = System.getenv("BOOTSTRAP_SERVER");

        producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    }

    public void sendHotel(HotelRequests.CreateHotel request, String key){
        if(hotelProducer == null){
            hotelProducer = new KafkaProducer<>(producerProps);
        }
        ProducerRecord<String, HotelRequests.CreateHotel> record = new ProducerRecord<>(HOTEL_INTEGRATION_TOPIC, key, request);
        hotelProducer.send(record);
    }

    public void sendFlight(FlightRequests.CreateFlight request, String key){
        if(flightProducer == null){
            flightProducer = new KafkaProducer<>(producerProps);
        }
        ProducerRecord<String, FlightRequests.CreateFlight> record = new ProducerRecord<>(FLIGHT_INTEGRATION_TOPIC, key, request);
        flightProducer.send(record);
    }

    public void sendBus(BusRequests.CreateBus request, String key){
        if(busProducer == null){
            busProducer = new KafkaProducer<>(producerProps);
        }
        ProducerRecord<String, BusRequests.CreateBus> record = new ProducerRecord<>(BUS_INTEGRATION_TOPIC, key, request);
        busProducer.send(record);
    }

    public void sendPriceListUpdateBus(UpdatePriceListRequests.UpdateBusPrices request, String key){
        if(updatePriceBusProducer == null){
            updatePriceBusProducer = new KafkaProducer<>(producerProps);
        }
        ProducerRecord<String, UpdatePriceListRequests.UpdateBusPrices> record = new ProducerRecord<>(PRICES_INTEGRATION_TOPIC, key, request);
        updatePriceBusProducer.send(record);
    }

    public void sendPriceListUpdateFlight(UpdatePriceListRequests.UpdateFlightPrices request, String key){
        if(updatePriceFlightProducer == null){
            updatePriceFlightProducer = new KafkaProducer<>(producerProps);
        }
        ProducerRecord<String, UpdatePriceListRequests.UpdateFlightPrices> record = new ProducerRecord<>(PRICES_INTEGRATION_TOPIC, key, request);
        updatePriceFlightProducer.send(record);
    }

    public void sendPriceListUpdateHotelRoom(UpdatePriceListRequests.UpdateHotelRoomPrices request, String key){
        if(updatePriceHotelRoomProducer == null){
            updatePriceHotelRoomProducer = new KafkaProducer<>(producerProps);
        }
        ProducerRecord<String, UpdatePriceListRequests.UpdateHotelRoomPrices> record = new ProducerRecord<>(PRICES_INTEGRATION_TOPIC, key, request);
        updatePriceHotelRoomProducer.send(record);
    }

    public EventSender(int useAsSingleton){
        configure();
    }
}
