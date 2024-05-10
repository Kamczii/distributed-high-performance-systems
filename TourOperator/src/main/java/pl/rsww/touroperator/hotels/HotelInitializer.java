package pl.rsww.touroperator.hotels;


import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.data_extraction.HotelInfo;
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

    private Map<PlaneConnectionHolder, HashSet<String>> planeConnections;

    private void initLocation(){
        location = airportLocationRepository.findByCityAndCountry(info.getCity(), country);
        if(location == null){
            location = new AirportLocation();
            location.setCountry(country);
            location.setCity(info.getCity());
//            location.setHotels(new LinkedList<>());
            airportLocationRepository.save(location);
        }
//        location.addHotel(hotel);
        airportLocationRepository.save(location);
        hotel.setLocation(location);
    }

    private void initRooms(){
        List<HotelRoom> rooms = new LinkedList<>();
//        roomRequests = new LinkedList<>();
        int numRooms = 2;
        for(String roomDesc: info.getRooms()){
            int numBeds = 2;
            int capacity = numBeds;
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
        hotel.setLocalRegionName(info.getRegion());
        country = info.getCountry();
        hotelRepository.save(hotel);
        initLocation();
        initRooms();
        collectConnections();

        initPriceList();
        hotelRepository.save(hotel);

    }

    public HotelInitializer(HotelRepository hotelRepository, HotelRoomRepository hotelRoomRepository,
                            AirportLocationRepository airportLocationRepository, AgeRangePriceItemRepository ageRangePriceItemRepository)
    {
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
