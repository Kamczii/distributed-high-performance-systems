package pl.rsww.touroperator.initialization;

import pl.rsww.touroperator.api.requests.FlightRequests;
import pl.rsww.touroperator.api.requests.HotelRequests;
import pl.rsww.touroperator.data_extraction.HotelInfo;
import pl.rsww.touroperator.flights.Flight;
import pl.rsww.touroperator.flights.FlightRepository;
import pl.rsww.touroperator.flights.bookings.FlightBookingRepository;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.Hotel;
import pl.rsww.touroperator.hotels.HotelRepository;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import pl.rsww.touroperator.hotels.rooms.HotelRoomRepository;
import pl.rsww.touroperator.hotels.reservations.RoomReservationRepository;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class HotelInitializer {
    private final static String HOME_COUNTRY = "Polska";
    private HotelRepository hotelRepository;
    private HotelRoomRepository hotelRoomRepository;
    private RoomReservationRepository roomReservationRepository;
    private FlightLineRepository flightLineRepository;
    private FlightRepository flightRepository;
    private AirportLocationRepository airportLocationRepository;
    private FlightBookingRepository flightBookingRepository;

    private HotelInfo info;
    private Hotel hotel;
    private String country;
    private AirportLocation location;
    private List<FlightLine> flightLines;
    private HotelRequests.LocationRequest requestLoc;
    private List<HotelRequests.RoomRequest> roomRequests;
    private EventSender eventSender;

//    private void initRegion(){
//        region = regionRepository.findByRegion(info.getRegion());
//        if(region == null){
//            region = new String();
//            region.setCountry(info.getCountry());
//            region.setRegion(info.getRegion());
//            regionRepository.save(region);
//        }
//        homeRegion = regionRepository.findByRegion("HOME");
//        if(homeRegion == null){
//            homeRegion = new String();
//            homeRegion.setRegion("HOME");
//            homeRegion.setCountry("Polska");
//            regionRepository.save(homeRegion);
//        }
//    }

    private void initLocation(){
        location = airportLocationRepository.findByCityAndCountry(info.getCity(), country);
        if(location == null){
            location = new AirportLocation();
            location.setCountry(country);
            location.setCity(info.getCity());
            location.setHotels(new LinkedList<>());
            airportLocationRepository.save(location);
        }
        location.addHotel(hotel);
        airportLocationRepository.save(location);
        hotel.setLocation(location);
        requestLoc = new HotelRequests.LocationRequest(location.getCountry(), location.getCity());
    }

    private void initRooms(){
        List<HotelRoom> rooms = new LinkedList<>();
        roomRequests = new LinkedList<>();
        for(String roomDesc: info.getRooms()){
            int numBeds = 2;
            int numRooms = 5;
            HotelRoom r = new HotelRoom();
            r.setHotel(hotel);
            r.setDescription(roomDesc);
            r.setNumberInHotel(numRooms);
            r.setMaxPeople(numBeds);
            hotelRoomRepository.save(r);
            rooms.add(r);
            roomRequests.add(new HotelRequests.RoomRequest(roomDesc, numRooms, numBeds));
        }
        hotel.setRooms(rooms);
        hotelRepository.save(hotel);
    }

    private void initFlightLines(){
        hotel.setFlightLines(new HashSet<>());
        List<java.lang.String> departures = info.getDepartures();
        flightLines = new LinkedList<>();
        for(String departure: departures){
            AirportLocation homeLocation = airportLocationRepository.findByCityAndCountry(departure, HOME_COUNTRY);
            if(homeLocation == null){
                homeLocation = new AirportLocation();
                homeLocation.setCity(departure);
                homeLocation.setCountry(HOME_COUNTRY);
                homeLocation.setHotels(new LinkedList<>());
                airportLocationRepository.save(homeLocation);
            }
            FlightLine fl = flightLineRepository.findByHomeLocationAndDestinationLocation(homeLocation, location);
            if(fl == null){
                fl = new FlightLine();
                fl.setDestinationLocation(location);
                fl.setHomeLocation(homeLocation);
                fl.setHotels(new HashSet<>());
                fl.setMaxPassengers(50);
                flightLineRepository.save(fl);
                flightLines.add(fl);
            }
            fl.addHotel(hotel);
        }
    }

    private void initFlights(){
        LocalDate date = LocalDate.now(); //.plusDays(7);
        for(FlightLine line: flightLines){
            Flight flight = new Flight();
            flight.setLine(line);
            flight.setDepartureDate(date);
            flightRepository.save(flight);

            if(eventSender != null){
                FlightRequests.LocationRequest lrHome = new FlightRequests.LocationRequest(
                        line.getHomeLocation().getCountry(), line.getHomeLocation().getCity());
                FlightRequests.LocationRequest lrDest = new FlightRequests.LocationRequest(
                        line.getDestinationLocation().getCountry(), line.getDestinationLocation().getCity());
                java.lang.String flightNumber = flight.getLine().flightNumber();
                java.lang.String key = flightNumber + flight.getDepartureDate();
                FlightRequests.CreateFlight flightRequest = new FlightRequests.CreateFlight(
                        flightNumber, line.getMaxPassengers(), lrHome, lrDest, date);
                eventSender.sendFlight(flightRequest, key);
            }
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

    public Boolean checkIfOk(){
        return hotelRepository != null && hotelRoomRepository != null
                && roomReservationRepository != null && flightLineRepository != null && flightRepository != null &&
                airportLocationRepository != null && flightBookingRepository != null;
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
        initFlightLines();
        initFlights();
        hotelRepository.save(hotel);

        if(eventSender != null){
            UUID uuid = hotel.getId();
            HotelRequests.CreateHotel hotelRequest = new HotelRequests.CreateHotel(uuid, hotel.getName(), requestLoc, roomRequests);
            java.lang.String key = uuid.toString();
            eventSender.sendHotel(hotelRequest, key);
        }
    }

    public HotelInitializer(){
    }

    public void setEventSender(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    public void setHotelRepository(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public void setHotelRoomRepository(HotelRoomRepository hotelRoomRepository) {
        this.hotelRoomRepository = hotelRoomRepository;
    }

    public void setRoomReservationRepository(RoomReservationRepository roomReservationRepository) {
        this.roomReservationRepository = roomReservationRepository;
    }

    public void setFlightLineRepository(FlightLineRepository flightLineRepository) {
        this.flightLineRepository = flightLineRepository;
    }

    public void setFlightRepository(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void setTouristicLocationRepository(AirportLocationRepository airportLocationRepository) {
        this.airportLocationRepository = airportLocationRepository;
    }

    public void setFlightBookingRepository(FlightBookingRepository flightBookingRepository) {
        this.flightBookingRepository = flightBookingRepository;
    }
}
