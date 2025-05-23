package co.edu.uptc.server.model.pojos;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.HashMap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    private LocalDateTime dateInit;
    private LocalDateTime dateEnd;
    private HashMap<String, ArrayList<Screening>> screenings;

    public Schedule(LocalDateTime dateInit, LocalDateTime dateEnd) {
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
        screenings = new HashMap<>();
    }

    

    public void addScreening(String title, Screening screening) {
        screenings.get(title).add(screening);
    }

    public void addMovie(String title) {
        screenings.put(title, new ArrayList<>());
    }
}
