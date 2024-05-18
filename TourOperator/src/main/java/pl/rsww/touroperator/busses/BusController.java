package pl.rsww.touroperator.busses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/busses")
public class BusController {
    @Autowired
    private BusService busService;

    @GetMapping(path="/send")
    public @ResponseBody String sendRequests() {
        busService.sendRequests();
        return "Started publishing busses";
    }
}