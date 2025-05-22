package co.edu.uptc.server.model.pojos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Schedule {
    // private ArrayList<Screening> screenings;
    private Date dateInit;
    private Date dateEnd;
    private HashMap<String, ArrayList<Screening>> screenings;

    public Schedule(Date dateInit, Date dateEnd) {
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
