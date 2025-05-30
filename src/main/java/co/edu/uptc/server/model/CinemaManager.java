package co.edu.uptc.server.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import co.edu.uptc.server.interfaces.IServer.IModel;
import co.edu.uptc.server.model.enums.AudithoriumSize;
import co.edu.uptc.server.model.enums.EditAudithorium;
import co.edu.uptc.server.model.enums.EditMovie;
import co.edu.uptc.server.model.pojos.Auditorium;
import co.edu.uptc.server.model.pojos.Book;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.model.pojos.Schedule;
import co.edu.uptc.server.model.pojos.Screening;
import co.edu.uptc.server.model.pojos.Seat;
import co.edu.uptc.server.persistence.FileManager;
import co.edu.uptc.server.structures.MyQueueu;
import co.edu.uptc.server.structures.avltree.AVLTree;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CinemaManager implements IModel {
    // tentativo hacer que el admin pueda ver las reservas
    private Schedule actualSchedule;
    private ArrayList<Schedule> previusSchedules;
    private ArrayList<Movie> moviesList;
    private ArrayList<Auditorium> auditoriumsList;
    private MyQueueu<Book> booksqQueueu;

    public CinemaManager() {
        moviesList = new ArrayList<>();
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
        loadBooks(data.get(3));

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
        schedule.addAll(previusSchedules);
        data.add(schedule);
        data.add(booksqQueueu.toList());
        fm.saveData(data);
    }

    // user operations
    @Override
    public HashMap<String, AVLTree<Screening>> getMovieSchedule() {
        return actualSchedule.getScreenings();
    }

    @Override
    public boolean selectSeat(String movieName, String auditoriumName, String dateString, String row, String seat) {
        try {
            return selectSeats(movieName, auditoriumName, dateString, row, seat);
        } catch (NumberFormatException e) {
            System.err.println("Invalid seat number: " + e.getMessage());
            return false;
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error selecting seat: " + e.getMessage());
            return false;
        }

    }

    private boolean selectSeats(String movieName, String auditoriumName, String dateString, String row, String seat) {
        LocalDateTime date = parseDate(dateString);

        AVLTree<Screening> screenings = getScreeningsForMovie(movieName);
        Screening screening = findMatchingScreening(screenings, date, auditoriumName);

        int rowNumber = convertRowToIndex(row);
        int seatNumber = parseSeatNumber(seat);

        validateSeatIndices(screening, rowNumber, seatNumber);

        return reserveSeat(screening, rowNumber, seatNumber);
    }

    private LocalDateTime parseDate(String dateString) {
        return LocalDateTime.parse(dateString);
    }

    private AVLTree<Screening> getScreeningsForMovie(String movieName) {
        AVLTree<Screening> screenings = actualSchedule.getScreenings().get(movieName);
        if (screenings == null) {
            throw new IllegalArgumentException("No screenings found for movie: " + movieName);
        }
        return screenings;
    }

    private Screening findMatchingScreening(AVLTree<Screening> screenings, LocalDateTime date, String auditoriumName) {
        Screening screening = findScreening(screenings, date, auditoriumName);
        if (screening == null) {
            throw new IllegalArgumentException("No screening found for the provided date and auditorium.");
        }
        return screening;
    }

    private int parseSeatNumber(String seat) {
        return Integer.parseInt(seat);
    }

    private void validateSeatIndices(Screening screening, int rowNumber, int seatNumber) {
        Seat[][] seats = screening.getScreeningAuditorium().getSeat();
        if (rowNumber < 0 || rowNumber >= seats.length || seatNumber < 0 || seatNumber >= seats[rowNumber].length) {
            throw new IndexOutOfBoundsException("Seat or row index out of bounds.");
        }
    }

    private boolean reserveSeat(Screening screening, int rowNumber, int seatNumber) {
        if (!screening.isocuped(rowNumber, seatNumber)) {
            screening.getScreeningAuditorium().getSeat()[rowNumber][seatNumber].setOcuped(true);
            return true;
        } else {
            return false;
        }
    }

    private int convertRowToIndex(String row) {
        return row.toUpperCase().charAt(0) - 'A';
    }

    private Screening findScreening(AVLTree<Screening> screenings, LocalDateTime date, String auditoriumName) {
        Iterator<Screening> it = screenings.getInOrder().iterator();
        while (it.hasNext()) {
            Screening screening = it.next();
            if (screening.getScreeningAuditorium().getName().equals(auditoriumName)
                    && findDate(screening.getDate(), date)) {
                return screening;
            }
        }
        return null;
    }

    private boolean findDate(LocalDateTime date, LocalDateTime screeningDate) {
        if (date == null || screeningDate == null) {
            return false;
        }
        // Ignorar segundos y nanos comparando año, mes, día, hora y minuto
        return date.getYear() == screeningDate.getYear() &&
                date.getMonth() == screeningDate.getMonth() &&
                date.getDayOfMonth() == screeningDate.getDayOfMonth() &&
                date.getHour() == screeningDate.getHour() &&
                date.getMinute() == screeningDate.getMinute();
    }

    @Override
    public void createBook(String movie, String auditorium, String dateStr, String row, String seat, String userName) {
        LocalDateTime date = LocalDateTime.parse(dateStr);
        if (selectSeat(movie, auditorium, dateStr, row, seat)) {
            Book book = createBookEntry(movie, auditorium, date, row, seat, userName);
            booksqQueueu.push(book);
        } else {
            throw new RuntimeException("El asiento ya está ocupado");
        }
    }

    private Book createBookEntry(String movie, String auditorium, LocalDateTime date, String row, String seat,
            String userName) {
        Book book = new Book();
        book.setId(UUID.randomUUID().toString());
        book.setMovieTitle(movie);
        book.setAuditoriumName(auditorium);
        book.setDate(date);
        book.setSeatRow(row);
        book.setSeatNumber(Integer.parseInt(seat));
        book.setValidated(false);
        book.setUser(userName);
        return book;
    }

    @Override
    public List<Book> checkBook(String user) {
        List<Book> books = new ArrayList<>();
        for (Book book : booksqQueueu) {
            if (book.getUser().equals(user)) {
                books.add(book);
            }
        }
        return books;
    }

    @Override
    public boolean validateBook(String bookId) {
        for (Book book : booksqQueueu) {
            if (book.getId().equals(bookId)) {
                book.setValidated(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cancelBook(String bookId) {
        Iterator<Book> it = booksqQueueu.iterator();
        while (it.hasNext()) {
            Book book = it.next();
            if (book.getId().equals(bookId)) {
                it.remove();

                releaseSeat(book);
                return true;
            }
        }
        return false;
    }

    private void releaseSeat(Book book) {
        Screening screening = findScreening(
                actualSchedule.getScreenings().get(book.getMovieTitle()),
                book.getDate(),
                book.getAuditoriumName());
        if (screening != null) {
            int rowNumber = convertRowToIndex(book.getSeatRow());
            int seatNumber = book.getSeatNumber() - 1;
            if (rowNumber >= 0 && rowNumber < screening.getScreeningAuditorium().getSeat().length &&
                    seatNumber >= 0 && seatNumber < screening.getScreeningAuditorium().getSeat()[rowNumber].length) {
                screening.getScreeningAuditorium().getSeat()[rowNumber][seatNumber].setOcuped(false);
            }
        }
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
        EditMovie editOption = EditMovie.valueOf(data);

        switch (editOption) {
            case TITLE:
                movie.setTitle(atribute);
                break;
            case CALIFICATION:
                movie.setCalification(atribute);
                break;
            case MOVIE_SYNOPSIS:
                movie.setMovieSynopsis(atribute);
                break;
            case RATE:
                movie.setRate(atribute);
                break;
            case DURATION_IN_MINUTES:
                movie.setDurationInMinutes(atribute);
                break;
            default:
                System.out.println("Atributo no reconocido: " + editOption);
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
        // TODO has la clase de excepcion
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
        // TODO validar si si es actual
        if (actualSchedule == null) {
            actualSchedule = new Schedule(findWeek(date), findWeek(date).plusDays(6));
        }
        if (isBetween(actualSchedule.getDateInit(), date, actualSchedule.getDateEnd())) {
            actualSchedule(movie.getTitle());
            actualSchedule.addScreening(movie.getTitle(), screening);
        } else {
            Schedule schedule = new Schedule(findWeek(date), findWeek(date).plusDays(6));
            if (!schedule.getScreenings().containsKey(screening.getMovie().getTitle())) {
                schedule.addMovie(screening.getMovie().getTitle());
            }
            schedule.addScreening(screening.getMovie().getTitle(), screening);
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

    private boolean isBetween(LocalDateTime first, LocalDateTime middle, LocalDateTime second) {
        return middle.isAfter(first) & middle.isBefore(second) || middle.getDayOfWeek() == DayOfWeek.MONDAY
                || middle.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

    @Override
    public boolean deleteScreening(String AuditoriumName, LocalDateTime date, String movieName) {
        try {
            if (date.isBefore(actualSchedule.getDateEnd())) {
                AVLTree<Screening> screenings = actualSchedule.getScreenings().get(movieName);

                if (screenings == null) {
                    System.out.println("No hay screenings para la película: " + movieName);
                    return false;
                }

                Screening screening = findScreening(screenings, date, AuditoriumName);

                if (screening == null) {
                    System.out.println("No se encontró la función con esos parámetros.");
                    return false;
                }

                screenings.remove(screening);
                return true;
            }
        } catch (NullPointerException e) {
            System.err.println("Error: Se intentó acceder a un objeto nulo al eliminar screening.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al eliminar screening:");
            e.printStackTrace();
        }
        return false;
    }

    // @Override
    public boolean configurateAuditorium(String data, String auditoriumName, String option) {

        Auditorium auditoriumn = searchAuditoriumByName(auditoriumName);
        EditAudithorium editOption = EditAudithorium.valueOf(option);
        if (auditoriumn != null) {
            switch (editOption) {
                case NAME:
                    actualizeData(auditoriumName, data);
                    auditoriumn.setName(data);
                    break;
                case SIZE:
                    editAuditorium(data, auditoriumn);
                    break;
                default:
                    // TODO HAS ESTO configurateAuditorium
                    break;
            }
            return true;
        }
        {
            return false;
        }
    }

    private void actualizeData(String auditoriumName, String data) {
        actualizeDataFromQueue(auditoriumName, data);
        actualizeDataFromSchedule(auditoriumName, data, actualSchedule);
        actualizeDataFromList(auditoriumName, data, previusSchedules);
    }

    private void actualizeDataFromQueue(String auditoriumName, String data) {
        for (Book book : booksqQueueu.toList()) {
            if (book.getAuditoriumName().equals(auditoriumName)) {
                book.setAuditoriumName(data);
            }
        }
    }

    private void actualizeDataFromList(String auditoriumName, String data, ArrayList<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            actualizeDataFromSchedule(auditoriumName, data, schedule);
        }
    }

    private void actualizeDataFromSchedule(String auditoriumName, String data, Schedule schedule) {
        for (AVLTree<Screening> screeningTree : schedule.getScreenings().values()) {
            for (Screening screening : screeningTree.getInOrder()) {
                if (screening.getScreeningAuditorium().getName().equals(auditoriumName)) {
                    screening.getScreeningAuditorium().setName(data);
                }
            }
        }
    }

    private boolean editAuditorium(String data, Auditorium auditorium) {
        // Validar que el valor de "data" sea uno de los tamaños predefinidos
        AudithoriumSize sizeEnum;
        try {
            sizeEnum = AudithoriumSize.valueOf(data.toUpperCase());
        } catch (Exception e) {
            System.err.println(

                    "Tamaño de auditorio no válido. Usa: SMALL, MEDIUM o BIG.");
            return false;
        }

        int size;
        switch (sizeEnum) {
            case SMALL:
                size = 4;
                break;
            case MEDIUM:
                size = 7;
                break;
            case BIG:
                size = 10;
                break;
            default:
                throw new RuntimeException("Error inesperado al asignar tamaño.");
        }

        // Creamos el nuevo auditorio con el tamaño correcto y el mismo nombre
        Auditorium updatedAuditorium = new Auditorium(auditorium.getName(), size, size);

        // Actualizamos la lista principal
        int index = auditoriumsList.indexOf(auditorium);
        auditoriumsList.set(index, updatedAuditorium);

        return true;
    }

    @Override
    public int generateReport(LocalDateTime first, LocalDateTime second) {
        int sells = 0;

        // Contar asientos ocupados de la programación actual, si está dentro del rango
        if (actualSchedule != null && isBetween(first, actualSchedule.getDateInit(), second)) {
            sells += countOccupiedSeats(actualSchedule, first, second);
        }

        // Contar asientos ocupados de las programaciones anteriores, si están dentro
        // del rango
        for (Schedule schedule : previusSchedules) {
            if (isBetween(first, schedule.getDateInit(), second)) {
                sells += countOccupiedSeats(schedule, first, second);
            }
        }

        // Multiplicar por el precio (9000 pesos por asiento)
        return sells * 9000;
    }

    // Método para contar asientos ocupados de un Schedule entre dos fechas
    private int countOccupiedSeats(Schedule schedule, LocalDateTime first, LocalDateTime second) {
        int occupiedSeats = 0;

        for (AVLTree<Screening> screenings : schedule.getScreenings().values()) {
            for (Screening screening : screenings.getInOrder()) {
                LocalDateTime screeningDate = screening.getDate();

                // Validar si la función está dentro del rango
                if (isBetween(first, screeningDate, second)) {
                    // Contar asientos ocupados del auditorio de esta función
                    occupiedSeats += countOccupiedSeatsInAuditorium(screening.getScreeningAuditorium());
                }
            }
        }

        return occupiedSeats;
    }

    // Método para contar asientos ocupados en un auditorio
    private int countOccupiedSeatsInAuditorium(Auditorium auditorium) {
        int count = 0;
        Seat[][] seats = auditorium.getSeat();

        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (seat.isOcuped()) {
                    count++;
                }
            }
        }

        return count;
    }

}
