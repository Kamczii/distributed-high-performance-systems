package pl.rsww.touroperator.data_extraction;

import java.util.List;

public class HotelInfo {
    private String name;
    private String country;
    private String region;
    private String drive;
    private String city;
    private List<String> rooms;
    private List<String> departures;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }

    public List<String> getDepartures() {
        return departures;
    }

    public void setDepartures(List<String> departures) {
        this.departures = departures;
    }
}