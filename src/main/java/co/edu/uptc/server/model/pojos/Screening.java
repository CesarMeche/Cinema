package co.edu.uptc.server.model.pojos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Screening {
    private Movie movie;
    private LocalDateTime date;
    private Auditorium screeningAuditorium;
    private boolean done;
    public Screening(Movie movie, LocalDateTime date, Auditorium screeningAuditorium) {
        this.movie = movie;
        this.date = date;
        this.screeningAuditorium = screeningAuditorium;
        done=false;
    }
    public boolean isocuped(int row,int seatNumber){
        Seat[][] seats=screeningAuditorium.getSeat();        
        return seats[row][seatNumber].isOcuped();
    }
}
