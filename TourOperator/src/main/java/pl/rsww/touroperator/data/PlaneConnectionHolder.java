package pl.rsww.touroperator.data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PlaneConnectionHolder {
    private String destinationCity;
    private String destinationCountry;
//    private Set<String> departures;

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

//    public Set<String> getDepartures() {
//        return departures;
//    }
//
//    public void setDepartures(Set<String> departures) {
//        this.departures = departures;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaneConnectionHolder that = (PlaneConnectionHolder) o;
        return Objects.equals(destinationCity, that.destinationCity) && Objects.equals(destinationCountry, that.destinationCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationCity, destinationCountry);
    }

    public PlaneConnectionHolder(String destinationCity, String destinationCountry) {
        this.destinationCity = destinationCity;
        this.destinationCountry = destinationCountry;
//        this.departures = new HashSet<>();
    }

//    public void addDeparture(String city){
//        departures.add(city);
//    }
}
