package co.edu.uptc.server.model.pojos;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {
    private String row;
    private int seatNumber;
    private boolean ocuped;
    public Seat(String row, int seatNumber) {
        this.row = row;
        this.seatNumber = seatNumber;
        ocuped=false;
    }
}
