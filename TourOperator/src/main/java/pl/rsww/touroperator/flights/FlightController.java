package pl.rsww.touroperator.flights;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;


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
