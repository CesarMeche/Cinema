package co.edu.uptc.server.model;

import java.util.Date;
import java.util.Set;
import java.util.ArrayList;

import co.edu.uptc.server.interfaces.IServer.IModel;
import co.edu.uptc.server.model.enums.EditMovie;
import co.edu.uptc.server.model.pojos.Auditorium;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.model.pojos.Schedule;
import co.edu.uptc.server.model.pojos.Screening;

import lombok.Getter;

@Getter
public class CinemaManager implements IModel {
    private Schedule actualSchedule;
    private ArrayList<Schedule> futureSchedule;
    private ArrayList<Schedule> previusSchedules;
    private ArrayList<Movie> movies;
    // private ArrayList<Screening> screenings;
    private ArrayList<Auditorium> auditoriums;

    public CinemaManager() {
        movies = new ArrayList<>();
        // screenings = new ArrayList<>();
        auditoriums = new ArrayList<>();
    }

    // user operations
    @Override
    public Screening[] getMovieSchedule() {

        throw new UnsupportedOperationException("Unimplemented method 'getMovieSchedule'");
    }

    @Override
    public void selectSeat() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectSeat'");
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
    public void createScreening(String auditoriumName, Date date, String movieName) {
        Movie movie = searchMovieByName(movieName);
        Auditorium auditoriumn = searchAuditoriumByName(auditoriumName);
        findRigthShedule(movie, date, auditoriumn);
        // TODO validacionmes createScreening
    }

    private void findRigthShedule(Movie movie, Date date, Auditorium auditoriumn) {
        Screening screening = new Screening(movie, date, auditoriumn);
        if (isbetween(actualSchedule.getDateInit(), date, actualSchedule.getDateEnd())) {
            actualSchedule(movie.getTitle());
            actualSchedule.addScreening(movie.getTitle(), screening);
        }else{
            futureSchedule.add(new Schedule(date, date));
        }
    }
    private void findWeek(Date date){
        date.toString();
    }
    private void actualSchedule(String title) {
        Set<String> titles = actualSchedule.getScreenings().keySet();
        if (!titles.contains(title)) {
            actualSchedule.addMovie(title);
        }
    }

    private boolean isbetween(Date first, Date middle, Date second) {
        return middle.after(first) & middle.before(second);
    }

    @Override
    public void deleteScreening(String AuditoriumName, Date date) {

        // for (Screening screening : screenings) {
        // if (screening.getScreeningAuditorium().getName().equals(AuditoriumName)
        // & screening.getDate().equals(date)) {
        // screenings.remove(screening);
        // }
        // }
        // // TODO validacionmes deleteScreening

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
    public void generateReport() {
        System.out.println("monie");
        // TODO Auto-generated method stub

    }

}
