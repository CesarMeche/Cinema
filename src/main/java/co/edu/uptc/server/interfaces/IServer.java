package co.edu.uptc.server.interfaces;

import co.edu.uptc.server.model.pojos.Movie;


public class IServer {
    public interface IModel {
        //client operations
        public void getMovieSchedule( );
        public void selectSeat();
        public void createBook();
        public void checkBook();
        public void validateBook();
        //admin operations
        public void addMovie(Movie newmovie);
        public void editMovieData(String data,String atribute,String movieName);
        public void createScreening();
        public void deleteScreening();
        public void configurateAuditorium();
        public void generateReport();

    }
    public interface IController {
    //client operations
    public void getMovieSchedule( );
    public void selectSeat();
    public void createBook();
    public void checkBook();
    public void validateBook();
    //admin operations
    public void addMovie();
    public void editMovieData();
    public void createScreening();
    public void deleteScreening();
    public void configurateAuditorium();
    public void generateReport();
    
    
    }

    
    
}
