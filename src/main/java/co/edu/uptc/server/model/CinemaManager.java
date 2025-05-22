package co.edu.uptc.server.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

import co.edu.uptc.server.interfaces.IServer.IModel;
import co.edu.uptc.server.model.enums.EditMovie;
import co.edu.uptc.server.model.pojos.Auditorium;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.model.pojos.Schedule;
import co.edu.uptc.server.model.pojos.Screening;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CinemaManager implements IModel {
    private Schedule actualSchedule;
    private ArrayList<Schedule> futureSchedule;
    private ArrayList<Schedule> previusSchedules;
    private ArrayList<Movie> movies;
    private ArrayList<Auditorium> auditoriums;

    public CinemaManager() {
        movies = new ArrayList<>();
        futureSchedule = new ArrayList<>();
        auditoriums = new ArrayList<>();
    }

    // user operations
    @Override
    public HashMap<String, ArrayList<Screening>> getMovieSchedule() {
        return actualSchedule.getScreenings();
    }

    @Override
    public void selectSeat(String movieName, String auditoriumName, String dateString, String row, String seat) {
        LocalDateTime date = LocalDateTime.parse(dateString);
        ArrayList<Screening> screenings = actualSchedule.getScreenings().get(movieName);
        Screening screening=findScreening(screenings, date, auditoriumName);
        int rowNumber=Integer.parseInt(row);
        int seatNumber=Integer.parseInt(seat);
        if (!screening.isocuped(rowNumber,seatNumber)) {
            screening.getScreeningAuditorium().getSeat()[rowNumber][seatNumber].setOcuped(true);
        }else{
            //TODO hacer esta verificacion xdd throw new NullPointerException("ocupao");
        }

    }

    private Screening findScreening(ArrayList<Screening> screenings,LocalDateTime date,String auditoriumName) {
        int i = 0;
        Screening screening=null;
        while (i < screenings.size()) {
             screening= screenings.get(i);
            if (screening.getScreeningAuditorium().equals(searchAuditoriumByName(auditoriumName))
                    && screening.getDate().equals(date)) {

                break;
            }

            i++;
        }
        return screening;
    }

    @Override
    public void createBook() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBook'");
    }

    @Override
    public void checkBook() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkBook'");
    }

    @Override
    public void validateBook() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateBook'");
    }

    // admin operations
    @Override
    public void addMovie(Movie newMovie) {
        movies.add(newMovie);
    }

    @Override
    public void editMovieData(String data, String atribute, String movieName) {
        Movie movie = searchMovieByName(movieName);
        if (movie != null) {
            editMovie(data, atribute, movie);
        }

    }

    private void editMovie(String data, String atribute, Movie movie) {
        switch (atribute) {
            case "title":
                movie.setTitle(data);
                break;
            case "calification":
                movie.setCalification(data);
                break;
            case "movieSynopsis":
                movie.setMovieSynopsis(data);
                break;
            case "rate":
                movie.setRate(data);
                break;
            case "durationInMinutes":
                movie.setDurationInMinutes(data);
                break;
            default:
                System.out.println("has esto we editMovie");
                // TODO has esto we
                break;
        }
    }

    private Movie searchMovieByName(String movieName) {

        for (Movie movie : movies) {
            if (movie.getTitle().equals(movieName)) {
                return movie;
            }
        }
        System.out.println("has esto we searchMovieByName");
        // TODO has esto we
        return null;
    }

    private Auditorium searchAuditoriumByName(String auditoriumName) {

        for (Auditorium auditorium : auditoriums) {
            if (auditorium.getName().equals(auditoriumName)) {
                return auditorium;
            }
        }
        System.out.println("has esto we searchAuditoriumByName");
        // TODO has esto we
        return null;
    }

    @Override
    public void createScreening(String auditoriumName, LocalDateTime date, String movieName) {
        Movie movie = searchMovieByName(movieName);
        Auditorium auditoriumn = searchAuditoriumByName(auditoriumName);
        findRigthShedule(movie, date, auditoriumn);
        // TODO validacionmes createScreening
    }

    private void findRigthShedule(Movie movie, LocalDateTime date, Auditorium auditoriumn) {
        Screening screening = new Screening(movie, date, auditoriumn);
        if (isbetween(actualSchedule.getDateInit(), date, actualSchedule.getDateEnd())) {
            actualSchedule(movie.getTitle());
            actualSchedule.addScreening(movie.getTitle(), screening);
        } else {
            futureSchedule.add(new Schedule(findWeek(date), findWeek(date).plusDays(6)));
        }
    }

    private LocalDateTime findWeek(LocalDateTime date) {
        int day = date.getDayOfWeek().compareTo(DayOfWeek.MONDAY);
        date = date.minusDays(day);
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
    }

    private void actualSchedule(String title) {
        Set<String> titles = actualSchedule.getScreenings().keySet();
        if (!titles.contains(title)) {
            actualSchedule.addMovie(title);
        }
    }

    private boolean isbetween(LocalDateTime first, LocalDateTime middle, LocalDateTime second) {
        return middle.isAfter(first) & middle.isBefore(second);
    }

    @Override
    public void deleteScreening(String AuditoriumName, String moveiName, LocalDateTime date) {
        // TODO preguntar al ticher si queda tiempo
        // TODO codigo repetido
        if (date.isBefore(actualSchedule.getDateEnd())) {
            ArrayList<Screening> screenings = actualSchedule.getScreenings().get(moveiName);
            int i = 0;
            while (i < screenings.size()) {
                Screening screening = screenings.get(i);

                if (screening.getScreeningAuditorium().equals(searchAuditoriumByName(AuditoriumName)) &&
                        screening.getDate().equals(date)) {
                    screenings.remove(screening);
                    break;
                }

                i++;
            }
        }
        // TODO validacionmes deleteScreening

    }

    @Override
    public void configurateAuditorium(String data, String auditoriumName, String option) {
        Auditorium auditoriumn = searchAuditoriumByName(auditoriumName);
        switch (option) {
            case "name":
                auditoriumn.setName(data);
                break;
            case "size":
                editAuditorium(data, auditoriumn);
                break;
            default:
                // TODO HAS ESTO configurateAuditorium
                break;
        }
    }

    private void editAuditorium(String data, Auditorium auditoriumn) {
        String[] parts = data.split(";");
        int[] size = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            size[i] = Integer.parseInt(parts[i]);
        }
        int index = auditoriums.indexOf(auditoriumn);
        auditoriums.set(index, new Auditorium(auditoriumn.getName(), size[0], size[1]));
    }

    @Override
    public String generateReport(LocalDateTime first, LocalDateTime second) {
        System.out.println("monie");
        // TODO Auto-generated method stub
        return "moniexdd";
    }

}
