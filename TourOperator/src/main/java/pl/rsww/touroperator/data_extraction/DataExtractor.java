package pl.rsww.touroperator.data_extraction;

import pl.rsww.touroperator.data.HotelInfo;

import java.util.List;

public interface DataExtractor {
    List<HotelInfo> extract();

}
