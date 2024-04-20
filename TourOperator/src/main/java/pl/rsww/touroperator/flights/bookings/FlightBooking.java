package pl.rsww.touroperator.flights.bookings;

import pl.rsww.touroperator.flights.Flight;
import pl.rsww.touroperator.hotels.reservations.RoomReservation;
import jakarta.persistence.*;

@Entity
public class FlightBooking {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne
    private Flight flight;
    @OneToOne
    private RoomReservation reservation;
    private int numberOfPeople;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public RoomReservation getReservation() {
        return reservation;
    }

    public void setReservation(RoomReservation reservation) {
        this.reservation = reservation;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
