package co.edu.uptc.server.interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.model.pojos.Screening;


public class IServer {
    public interface IModel {
        //client operations
        public  HashMap<String, ArrayList<Screening>> getMovieSchedule( );
        public boolean selectSeat(String moviename,String auditoriumName,String date,String row,String seat);
        public void createBook();
        public void checkBook();
        public void validateBook();
        public void cancelBook();
        //admin operations
        public void addMovie(Movie newmovie);
        public void editMovieData(String data,String atribute,String movieName);
        public void createScreening(String Auditorium,LocalDateTime date, String movieName);
        public void deleteScreening(String AuditoriumName,String moveiName, LocalDateTime date);
        public void configurateAuditorium(String data,String auditoriumName,String option);
         public int generateReport(LocalDateTime first, LocalDateTime second);

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
