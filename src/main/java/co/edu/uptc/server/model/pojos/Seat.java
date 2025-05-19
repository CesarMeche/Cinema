package co.edu.uptc.server.model.pojos;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {
    private String row;
    private String seatNumber;
    private boolean ocuped;
}
