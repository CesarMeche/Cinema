package co.edu.uptc.server.model.pojos;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Screening {
    private Movie movie;
    private Date date;
    private Auditorium screeningAuditorium;
}
