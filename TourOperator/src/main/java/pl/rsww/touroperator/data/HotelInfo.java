package pl.rsww.touroperator.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelInfo {
    public final static String DRIVE_INDIVIDUAL = "Dojazd własny";
    public final static String DRIVE_AIRPLANE = "Samolot";
    private String name;
    private String country;
    private String region;
    private String drive;
    private String city;
    private List<String> rooms;
    private List<String> departures;

    public ModesOfTransportSetting getModeOfTransport(){
        if(drive.equals(HotelInfo.DRIVE_INDIVIDUAL)){
            return ModesOfTransportSetting.INDIVIDUAL;
        } else {
            return ModesOfTransportSetting.AIRPLANE_OR_BUS;
        }
    }
}
