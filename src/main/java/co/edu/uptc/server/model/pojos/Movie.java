package co.edu.uptc.server.model.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Movie {
    private String Title;
    private String calification;
    private String movieSynopsis;
    private String rate;
    private String durationInMinutes;
}
