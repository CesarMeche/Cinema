package co.edu.uptc.server.model;

import java.util.ArrayList;

import co.edu.uptc.server.interfaces.IServer.IModel;
import co.edu.uptc.server.model.enums.EditMovie;
import co.edu.uptc.server.model.pojos.Auditorium;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.network.JsonResponse;
import lombok.Getter;

@Getter
public class CinemaManager implements IModel {
    private ArrayList<Movie> movies;

    public CinemaManager() {
        movies = new ArrayList<>();
    }

    // user operations
    @Override
    public void getMovieSchedule() {
        // TODO Auto-generated method stub
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

    public void editMovie(String data, String atribute, Movie movie) {
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
                //TODO has esto we
                break;
        }
    }

    public Movie searchMovieByName(String movieName) {

        for (Movie movie : movies) {
            if (movie.getTitle().equals(movieName)) {
                return movie;
            }
        }
        System.out.println("has esto we searchMovieByName");
//TODO has esto we
        return null;
    }

    @Override
    public void createScreening() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createScreening'");
    }

    @Override
    public void deleteScreening() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteScreening'");
    }

    @Override
    public void configurateAuditorium() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'configurateAuditorium'");
    }

    @Override
    public void generateReport() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateReport'");
    }

}
