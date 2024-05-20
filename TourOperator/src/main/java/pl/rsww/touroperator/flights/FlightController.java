package pl.rsww.touroperator.flights;

import org.springframework.web.bind.annotation.RequestParam;
import pl.rsww.tour_operator.api.FlightRequests;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.initialization.EventSender;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Controller
@RequestMapping(path="/flights")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @GetMapping(path="/send")
    public @ResponseBody String sendRequests(@RequestParam Optional<Integer> limit) {
        flightService.sendRequests(limit.orElse(Integer.MAX_VALUE));
        return "Started publishing flights";
    }
}
