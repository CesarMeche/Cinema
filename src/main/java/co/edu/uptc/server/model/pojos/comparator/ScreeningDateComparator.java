package co.edu.uptc.server.model.pojos.comparator;

import java.io.Serializable;
import java.util.Comparator;

import co.edu.uptc.server.model.pojos.Screening;

public class ScreeningDateComparator implements Comparator<Screening>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Screening s1, Screening s2) {
        return s1.getDate().compareTo(s2.getDate());
    }
}
