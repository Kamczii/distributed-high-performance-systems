package pl.rsww.touroperator.data;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
public class PlaneConnectionHolder {
    private String destinationCity;
    private String destinationCountry;


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
    }

}
