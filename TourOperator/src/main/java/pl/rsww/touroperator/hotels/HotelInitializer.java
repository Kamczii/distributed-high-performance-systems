package pl.rsww.touroperator.hotels;


import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.data.ModesOfTransportSetting;
import pl.rsww.touroperator.price.*;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.data.HotelInfo;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import pl.rsww.touroperator.hotels.rooms.HotelRoomRepository;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.math.BigDecimal;
import java.util.*;

public class HotelInitializer {

    private HotelRepository hotelRepository;
    private HotelRoomRepository hotelRoomRepository;
    private AirportLocationRepository airportLocationRepository;
    private PriceRepository priceRepository;

    private HotelInfo info;
    private Hotel hotel;
    private String country;
    private AirportLocation location;
    private List<FlightLine> flightLines;
    private PriceListGenerator priceListGenerator;
    private Set<AgeRangePriceItem> priceList;
    private Random random;
    @Setter
    @Getter
    private Map<PlaneConnectionHolder, HashSet<String>> planeConnections;
    private ModesOfTransportSetting modeOfTransport;

    private void initLocation(){
        location = airportLocationRepository.findByCityAndCountry(info.getCity(), country);
        if(location == null){
            location = new AirportLocation();
            location.setCountry(country);
            location.setCity(info.getCity());
            airportLocationRepository.save(location);
        }
        airportLocationRepository.save(location);
        hotel.setLocation(location);
    }

    private List<Integer> generateRandomPersonNumbers(){
        List<Integer> numbers = new ArrayList<>(2);
        int c = random.nextInt() % 3;
        if(c == 0){
            numbers.add(2);
            numbers.add(2);
        } else if (c == 1) {
            numbers.add(2);
            numbers.add(4);
        }else{
            numbers.add(3);
            numbers.add(3);
        }
        return numbers;
    }

    private void initRooms(){
        List<HotelRoom> rooms = new LinkedList<>();
        int numRooms = 2;
        List<Integer> generatedNumbers = generateRandomPersonNumbers();
        for(String roomDesc: info.getRooms()){
            int numBeds = generatedNumbers.get(0);
            int capacity = generatedNumbers.get(1);
            HotelRoom r = new HotelRoom();
            r.setHotel(hotel);
            r.setDescription(roomDesc);
            r.setNumberInHotel(numRooms);
            r.setNumberOfBeds(numBeds);
            r.setMaxPeople(capacity);
            hotelRoomRepository.save(r);
            rooms.add(r);
        }
        hotel.setRooms(rooms);
        hotelRepository.save(hotel);
    }

    private void collectConnections(){
        PlaneConnectionHolder pc = new PlaneConnectionHolder(location.getCity(), location.getCountry());
        HashSet<String> departures = planeConnections.get(pc);

        if(departures == null){
            planeConnections.put(pc, new HashSet<>(info.getDepartures()));
        }else{
            departures.addAll(info.getDepartures());
            planeConnections.put(pc, departures);
        }

    }

    private void setPriceListForRoom(HotelRoom room){
        BigDecimal priceNumber = priceListGenerator.getNextRoomPrice();
        Price price = new Price();
        price.setPrice(priceNumber);
        price.setHotelRoom(room);
        priceRepository.save(price);
        room.setPrice(price);
        hotelRoomRepository.save(room);
    }

    private void initPriceList(){
        priceListGenerator.getPrice(this);
        for(HotelRoom room: hotel.getRooms()){
            setPriceListForRoom(room);
        }

    }

    public void clear(){
        info = null;
        hotel = null;
        country = null;
        location = null;
        if(flightLines != null){
            flightLines.clear();
        }
    }

    public void initialize(HotelInfo info){
        this.info = info;
        hotel = new Hotel();
        hotel.setName(info.getName());
        country = info.getCountry();
        hotelRepository.save(hotel);
        initRooms();
        initPriceList();

        modeOfTransport = info.getModeOfTransport();
        hotel.setModeOfTransport(modeOfTransport);
        initLocation();
        collectConnections();

        hotelRepository.save(hotel);
    }

    public HotelInitializer(HotelRepository hotelRepository, HotelRoomRepository hotelRoomRepository,
                            AirportLocationRepository airportLocationRepository, PriceRepository priceRepository)
    {
        random = new Random();
        this.hotelRepository = hotelRepository;
        this.hotelRoomRepository = hotelRoomRepository;
        this.airportLocationRepository = airportLocationRepository;
        this.priceRepository = priceRepository;
        priceListGenerator = new PriceListGenerator();
    }

}
