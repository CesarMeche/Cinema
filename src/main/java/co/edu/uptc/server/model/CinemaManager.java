package co.edu.uptc.server.model;

import java.util.ArrayList;

import co.edu.uptc.server.interfaces.IServer.IModel;
import co.edu.uptc.server.model.pojos.Auditorium;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.network.JsonResponse;
import lombok.Getter;
@Getter
public class CinemaManager implements IModel{
    private ArrayList<Movie> movies;
    public CinemaManager() {
        movies= new ArrayList<>();
    }

    //user operations
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
//admin operations
    @Override
    public void addMovie(Movie newMovie) {
        movies.add(newMovie);
    }

    @Override
    public void editMovieData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editMovieData'");
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
