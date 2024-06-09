package pl.rsww.touroperator.updater;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.touroperator.price.PriceRepository;
import pl.rsww.touroperator.price.Price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;

@Slf4j
@Service
public class UpdaterService {
    private static final int SECONDS = 2;
    private Timer timer;
    private UpdaterTask updaterTask;
    private PriceRepository priceRepository;

    @Async
    public void startUpdating() {

        updaterTask = new UpdaterTask(priceRepository);
        timer = new Timer(true);
        timer.scheduleAtFixedRate(updaterTask, SECONDS*1000, SECONDS*1000);
    }

    @Async
    public void stopUpdating() {
        timer.cancel();

    }
}
