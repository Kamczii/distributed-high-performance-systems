package pl.rsww.touroperator.hotels;


import pl.rsww.tour_operator.api.HotelRequests;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.touroperator.initialization.EventSender;

import java.util.*;

@Controller
@RequestMapping(path="/hotels")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping(path="/send")
    public @ResponseBody String sendRequests() {
        hotelService.sendRequests();
        return "Started publishing hotels";
    }

}