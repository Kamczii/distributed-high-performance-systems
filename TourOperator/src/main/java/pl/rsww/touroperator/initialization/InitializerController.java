package pl.rsww.touroperator.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InitializerController {
    @Autowired
    private InitService initService;

    @GetMapping(path="/init")
    public @ResponseBody String initialize() {
        initService.initialize();
        return "Asynchronous initialization started";
    }

}
