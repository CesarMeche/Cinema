package co.edu.uptc.server.model.pojos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class Book {
    private String id; // ID único
    private String movieTitle;
    private String auditoriumName;
    private LocalDateTime date;
    private String seatRow;
    private int seatNumber;
    private boolean isValidated;
    // getters y setters
}

