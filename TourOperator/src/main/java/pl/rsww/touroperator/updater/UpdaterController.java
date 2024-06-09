package pl.rsww.touroperator.updater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/to-update")
public class UpdaterController {
    @Autowired
    private UpdaterService updaterService;

    @GetMapping(path="/start")
    public @ResponseBody String startUpdating() {
        updaterService.startUpdating();
        return "Started updating prices";
    }

    @GetMapping(path="/stop")
    public @ResponseBody String stopUpdating() {
        updaterService.stopUpdating();
        return "Stopped updating prices";
    }
}
