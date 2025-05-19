package co.edu.uptc.server.model.pojos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auditorium {
    private Seat[][] seat;

    public Auditorium(int row,int seatNumber) {
        seat= new Seat[row][seatNumber];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < seatNumber; j++) {
                seat[i][j]=new Seat(String.valueOf((char)(i+65)), j+1);
            }
        }
    }

}
