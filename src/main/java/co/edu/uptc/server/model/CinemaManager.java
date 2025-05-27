package co.edu.uptc.server.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.edu.uptc.server.interfaces.IServer.IModel;

import co.edu.uptc.server.model.pojos.Auditorium;
import co.edu.uptc.server.model.pojos.Book;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.model.pojos.Schedule;
import co.edu.uptc.server.model.pojos.Screening;
import co.edu.uptc.server.model.pojos.Seat;
import co.edu.uptc.server.persistence.FileManager;
import co.edu.uptc.server.structures.MyQueueu;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CinemaManager implements IModel {
    // tentativo hacer que el admin pueda ver las reservas
    private Schedule actualSchedule;
    private ArrayList<Schedule> futureSchedule;
    private ArrayList<Schedule> previusSchedules;
    private ArrayList<Movie> moviesList;
    private ArrayList<Auditorium> auditoriumsList;
    private MyQueueu<Book> booksqQueueu;

    public CinemaManager() {
        moviesList = new ArrayList<>();
        futureSchedule = new ArrayList<>();
        previusSchedules = new ArrayList<>();
        auditoriumsList = new ArrayList<>();
        booksqQueueu = new MyQueueu<>();
        loadData();
    }

    private void loadData() {
        FileManager fm = new FileManager();
        List<List> data = fm.getData();
        loadMovies((List<Movie>) data.get(0));
        loadAuditoriums((List<Auditorium>) data.get(1));
        loadSchedules(data.get(2));
        /*
         * TODO ver que pedo los books
         * //loadBooks(data.get(3));
         */
    }

    private void loadAuditoriums(List<Auditorium> auditoriums) {
        for (Auditorium auditorium : auditoriums) {
            auditoriumsList.add(auditorium);
        }
    }

    private void loadBooks(List<Book> books) {
        for (Book book : books) {
            booksqQueueu.push(book);
        }
    }

    private void loadSchedules(List<Schedule> schedules) {
        if (!schedules.isEmpty()) {
            actualSchedule = schedules.get(0);
            for (int i = 1; i < schedules.size(); i++) {
                previusSchedules.add(schedules.get(i));
            }
        }
    }

    private void loadMovies(List<Movie> moviesList) {
        if (!moviesList.isEmpty()) {
            for (Movie movie : moviesList) {
                addMovie(movie);
            }
        }
    }

    public void saveData() {
        FileManager fm = new FileManager();
        ArrayList data = new ArrayList<>();
        data.add(moviesList);
        data.add(auditoriumsList);
        ArrayList schedule = new ArrayList<>();
        schedule.add(actualSchedule);
        schedule.addAll(futureSchedule);
        schedule.addAll(previusSchedules);
        data.add(schedule);
        /*
         * TODO ver que pedo los books
         * data.add(booksqQueueu);
         */
        fm.saveData(data);
    }

    // user operations
    @Override
    public HashMap<String, ArrayList<Screening>> getMovieSchedule() {
        return actualSchedule.getScreenings();
    }

    @Override
    public boolean selectSeat(String movieName, String auditoriumName, String dateString, String row, String seat) {
        LocalDateTime date = LocalDateTime.parse(dateString);
        ArrayList<Screening> screenings = actualSchedule.getScreenings().get(movieName);
        Screening screening = findScreening(screenings, date, auditoriumName);
        int rowNumber = Integer.parseInt(row);
        int seatNumber = Integer.parseInt(seat);
        if (!screening.isocuped(rowNumber, seatNumber)) {
            screening.getScreeningAuditorium().getSeat()[rowNumber][seatNumber].setOcuped(true);
            return true;
        } else {
            // TODO hacer esta verificacion xdd throw new NullPointerException("ocupao");
            return false;
        }

    }

    private Screening findScreening(ArrayList<Screening> screenings, LocalDateTime date, String auditoriumName) {
        int i = 0;
        Screening screening = null;
        while (i < screenings.size()) {
            screening = screenings.get(i);
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

    public void cancelBook() {
    }

    // admin operations
    @Override
    public boolean addMovie(Movie newMovie) {
        moviesList.add(newMovie);
        return true;
    }

    @Override
    public boolean editMovieData(String data, String atribute, String movieName) {
        Movie movie = searchMovieByName(movieName);
        if (movie != null) {
            editMovie(data, atribute, movie);
            return true;
        } else {
            return false;
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

        for (Movie movie : moviesList) {
            if (movie.getTitle().equals(movieName)) {
                return movie;
            }
        }
        System.out.println("has esto we searchMovieByName");
        // TODO has esto we
        return null;
    }

    private Auditorium searchAuditoriumByName(String auditoriumName) {

        for (Auditorium auditorium : auditoriumsList) {
            if (auditorium.getName().equals(auditoriumName)) {
                return auditorium;
            }
        }
        System.out.println("has esto we searchAuditoriumByName");
        // TODO has esto we
        return null;
    }

    @Override
    public boolean createScreening(String auditoriumName, LocalDateTime date, String movieName) {
        Movie movie = searchMovieByName(movieName);
        if (movie != null) {

            Auditorium auditoriumn = searchAuditoriumByName(auditoriumName);
            findRigthShedule(movie, date, auditoriumn);
            // TODO validacionmes createScreening
            return true;
        }
        return false;
    }

    private void findRigthShedule(Movie movie, LocalDateTime date, Auditorium auditoriumn) {

        Screening screening = new Screening(movie, date, auditoriumn);
        //TODO validar si si es actual
        if (actualSchedule == null) {
            actualSchedule = new Schedule(findWeek(date), findWeek(date).plusDays(6));
        }
        if (isbetween(actualSchedule.getDateInit(), date, actualSchedule.getDateEnd())) {
            actualSchedule(movie.getTitle());
            actualSchedule.addScreening(movie.getTitle(), screening);
        } else {
            Schedule schedule=new Schedule(findWeek(date), findWeek(date).plusDays(6));
            if (!schedule.getScreenings().containsKey(screening.getMovie().getTitle())) {
                schedule.addMovie(screening.getMovie().getTitle());
            }
            schedule.addScreening(screening.getMovie().getTitle(),screening);
            futureSchedule.add(schedule);

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
        return middle.isAfter(first) & middle.isBefore(second) || middle.getDayOfWeek() == DayOfWeek.MONDAY
                || middle.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

    @Override
    public boolean deleteScreening(String AuditoriumName, LocalDateTime date, String moveiName) {
        // TODO preguntar al ticher si queda tiempo
        // TODO combair el orden de los parametros pq sdjka

        if (date.isBefore(actualSchedule.getDateEnd())) {
            ArrayList<Screening> screenings = actualSchedule.getScreenings().get(moveiName);
            Screening screening = findScreening(screenings, date, AuditoriumName);
            screenings.remove(screening);

        }
        // TODO validacionmes deleteScreening
        return false;
    }

    // @Override
    public boolean configurateAuditorium(String data, String auditoriumName, String option) {
        Auditorium auditoriumn = searchAuditoriumByName(auditoriumName);
        if (auditoriumn != null) {

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
            return true;
        }{
            return false;
        }
    }

    private void editAuditorium(String data, Auditorium auditoriumn) {
        String[] parts = data.split(";");
        int[] size = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            size[i] = Integer.parseInt(parts[i]);
        }
        int index = auditoriumsList.indexOf(auditoriumn);
        auditoriumsList.set(index, new Auditorium(auditoriumn.getName(), size[0], size[1]));
    }

    @Override
    public int generateReport(LocalDateTime first, LocalDateTime second) {
        int sells = 0;
        // TODO reduncancia
        if (isbetween(first, actualSchedule.getDateInit(), second)) {
            sells += ocupedSeatsFromASchedule(actualSchedule, first, second);
        } else {
            for (Schedule schedule : previusSchedules) {
                sells += ocupedSeatsFromASchedule(schedule, first, second);
            }
        }
        // TODO variable y validacion y reservas y asi

        sells *= 9000;
        return sells;
    }

    private int ocupedSeatsFromASchedule(Schedule schedule, LocalDateTime first, LocalDateTime second) {
        int sells = 0;
        if (isbetween(first, schedule.getDateInit(), second)) {
            for (ArrayList<Screening> screenings : schedule.getScreenings().values()) {
                for (Screening screening : screenings) {
                    sells += ocupedSeats(screening.getScreeningAuditorium());
                }
            }
        }
        return sells;
    }

    private int ocupedSeats(Auditorium auditorium) {
        int sells = 0;
        Seat[][] seats = auditorium.getSeat();
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j].isOcuped()) {
                    sells++;
                }
            }
        }
        return sells;
    }

}
