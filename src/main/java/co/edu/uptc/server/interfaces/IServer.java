package co.edu.uptc.server.interfaces;

public class IServer {
    public interface IModel {
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
