package pl.rsww.touroperator.hotels;


import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.data.HotelInfo;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItemRepository;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import pl.rsww.touroperator.hotels.rooms.HotelRoomRepository;
import pl.rsww.touroperator.initialization.PriceListGenerator;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.util.*;

public class HotelInitializer {

    private HotelRepository hotelRepository;
    private HotelRoomRepository hotelRoomRepository;
    private AirportLocationRepository airportLocationRepository;
    private AgeRangePriceItemRepository ageRangePriceItemRepository;

    private HotelInfo info;
    private Hotel hotel;
    private String country;
    private AirportLocation location;
    private List<FlightLine> flightLines;
    private PriceListGenerator priceListGenerator;
    private Set<AgeRangePriceItem> priceList;
    private Random random;
    private Map<PlaneConnectionHolder, HashSet<String>> planeConnections;
    private ModesOfTransport modeOfTransport;

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
        int c = random.nextInt() % 2;
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
        List<AgeRangePriceItem> ranges = priceListGenerator.getNextRoomRanges();
        priceList = new HashSet<>(ranges);
        for(AgeRangePriceItem item: priceList){
            item.setRoom(room);
        }
        ageRangePriceItemRepository.saveAll(priceList);
    }

    private void initPriceList(){
        priceListGenerator.startHotel();
        for(HotelRoom room: hotel.getRooms()){
            setPriceListForRoom(room);
            room.setPriceList(priceList);
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
                            AirportLocationRepository airportLocationRepository, AgeRangePriceItemRepository ageRangePriceItemRepository)
    {
        random = new Random();
        this.hotelRepository = hotelRepository;
        this.hotelRoomRepository = hotelRoomRepository;
        this.airportLocationRepository = airportLocationRepository;
        this.ageRangePriceItemRepository = ageRangePriceItemRepository;
        priceListGenerator = new PriceListGenerator();
    }


    public Map<PlaneConnectionHolder, HashSet<String>> getPlaneConnections() {
        return planeConnections;
    }

    public void setPlaneConnections(Map<PlaneConnectionHolder, HashSet<String>> planeConnections) {
        this.planeConnections = planeConnections;
    }

}
