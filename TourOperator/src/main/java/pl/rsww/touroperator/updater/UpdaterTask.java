package pl.rsww.touroperator.updater;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.rsww.tour_operator.api.UpdatePriceListRequests;
import pl.rsww.touroperator.busses.lines.BusLine;
import pl.rsww.touroperator.events.EventSender;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.hotels.Hotel;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import pl.rsww.touroperator.price.*;

import java.math.BigDecimal;
import java.util.*;

@Setter
public class UpdaterTask extends TimerTask {
    private PriceRepository priceRepository;
    private ArrayList<Price> priceList;
    private Random random;
    private int size;
    private EventSender eventSender;

    private ArrayList<Price> makeList(){
        Iterable<Price> priceListIt = priceRepository.findAll();
        int n = 0;
        for (Price price: priceListIt) {
            n++;
        }
        ArrayList<Price> priceList = new ArrayList<>(n);
        for (Price price: priceListIt) {
            priceList.add(price);
        }
        return priceList;
    }

    private BigDecimal randomChange(BigDecimal priceNumber){
        boolean isInc = random.nextBoolean();
        double x;
        if(isInc){
            x = 1.1;
        }else{
            x = 0.9;
        }
        return priceNumber.multiply(BigDecimal.valueOf(x));
    }

    private List<UpdatePriceListRequests.AgeRangePrice> createAgeRangePriceList(Price price){
        List<AgeRangePriceItem> priceItems = PriceListGenerator.getPriceList(price.getPrice());
        List<UpdatePriceListRequests.AgeRangePrice> prices = new LinkedList<>();
        for(AgeRangePriceItem item: priceItems){
            prices.add(new UpdatePriceListRequests.AgeRangePrice(item.getStartingRange(), item.getEndingRange(), item.getPrice()));
        }
        return prices;
    }

    private void sendHotelRoomRequest(HotelRoom room, Price price){
        Hotel hotel = room.getHotel();
        List<UpdatePriceListRequests.AgeRangePrice> prices = createAgeRangePriceList(price);

        UpdatePriceListRequests.UpdateHotelRoomPrices request = new UpdatePriceListRequests.UpdateHotelRoomPrices
                (hotel.getId(), room.getDescription(), prices);
        String key = hotel.getId() + ":" + room.getDescription();
        eventSender.sendPriceListUpdateHotelRoom(request, key);
    }

    private void sendFlightRequest(FlightLine line, Price price){
        List<UpdatePriceListRequests.AgeRangePrice> prices = createAgeRangePriceList(price);

        UpdatePriceListRequests.UpdateFlightPrices request = new UpdatePriceListRequests.UpdateFlightPrices
                (line.flightNumber(), prices);
        String key = line.flightNumber();
        eventSender.sendPriceListUpdateFlight(request, key);
    }

    private void sendBusRequest(BusLine line, Price price){
        List<UpdatePriceListRequests.AgeRangePrice> prices = createAgeRangePriceList(price);

        UpdatePriceListRequests.UpdateBusPrices request = new UpdatePriceListRequests.UpdateBusPrices
                (line.busNumber(), prices);
        String key = line.busNumber();
        eventSender.sendPriceListUpdateBus(request, key);
    }

    private void updatePrice(){
        Price price = priceList.get(random.nextInt(size));
        BigDecimal priceNumber = price.getPrice();
        BigDecimal newPriceNumber = randomChange(priceNumber);
        price.setPrice(newPriceNumber);
        priceRepository.save(price);

        HotelRoom hotelRoom = price.getHotelRoom();
        FlightLine flightLine = price.getFlightLine();
        BusLine busLine = price.getBusLine();

        if(hotelRoom != null){
            sendHotelRoomRequest(hotelRoom, price);
        }else if(flightLine != null){
            sendFlightRequest(flightLine, price);
        }else if(busLine != null){
            sendBusRequest(busLine, price);
        }else{
            System.out.println("Warning: price do not belong to any entity");
        }
    }

    @Override
    public void run() {
        updatePrice();
    }

    public UpdaterTask(PriceRepository priceRepository){
        random = new Random();
        this.priceList = makeList();
        this.priceRepository = priceRepository;
        size = priceList.size();
        eventSender = EventSender.getEventSender();
    }
}
