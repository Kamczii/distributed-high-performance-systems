package pl.rsww.touroperator.data;

public class StringNumberPair {
    private String string;
    private long number;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public StringNumberPair(String room, long number) {
        this.string = room;
        this.number = number;
    }
}
