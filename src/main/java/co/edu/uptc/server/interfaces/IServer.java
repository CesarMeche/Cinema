package co.edu.uptc.server.interfaces;

import java.time.LocalDateTime;
import java.util.HashMap;

import co.edu.uptc.server.model.pojos.Book;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.model.pojos.Screening;
import co.edu.uptc.server.structures.avltree.AVLTree;


public class IServer {
    public interface IModel {
        //client operations
        public  HashMap<String, AVLTree<Screening>> getMovieSchedule( );
        public boolean selectSeat(String moviename,String auditoriumName,String date,String row,String seat);
        public void createBook(String movie, String auditorium, String dateStr, String row, String seat);
        public Book checkBook(String bookId);
        public boolean validateBook(String bookId);
        public boolean cancelBook(String bookId);
        //admin operations
        public boolean addMovie(Movie newmovie);
        public boolean editMovieData(String data,String atribute,String movieName);
        public boolean createScreening(String Auditorium,LocalDateTime date, String movieName);
        public boolean deleteScreening(String AuditoriumName, LocalDateTime date,String moveiName);
        public boolean configurateAuditorium(String data,String auditoriumName,String option);
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
