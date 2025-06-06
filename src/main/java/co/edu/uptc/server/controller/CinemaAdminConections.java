package co.edu.uptc.server.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import co.edu.uptc.server.model.CinemaManager;
import co.edu.uptc.server.model.enums.AdminOptions;
import co.edu.uptc.server.model.enums.Msg;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.network.ConectionManager;
import co.edu.uptc.server.network.JsonResponse;

public class CinemaAdminConections extends Thread {
    private ConectionManager conectionManager;
    private CinemaManager cinemaManager;
    private AdminOptions adminOption;
    private Controller controller;
    private boolean conected;

    public CinemaAdminConections(ConectionManager conectionManager, CinemaManager cinemaManager,
            Controller controller) {
        this.controller = controller;
        this.conectionManager = conectionManager;
        this.cinemaManager = cinemaManager;
         conected = true;

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void run() {

        while (conected) {
            try {
                JsonResponse message = conectionManager.receiveMessage();
                adminOption = AdminOptions.valueOf(message.getStatus());
                switch (adminOption) {
                    case ADD_MOVIE:
                        addMovie(message);
                        break;
                    case EDIT_MOVIE_DATA:
                        editMovieData(message);
                        break;
                    case CREATE_SCREENING:
                        createScreening(message);
                        break;
                    case DELETE_SCREENING:
                        deleteScreening(message);
                        break;
                    case CONFIGURATE_AUDITORIUM:
                        configurateAuditorium(message);
                        break;
                    case GENERATE_REPORT:
                        generateReport(message);
                        break;
                    case CLOSE:
                        closeServer();
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado: " + e.getMessage());
                conected = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Opción desconocida recibida: " + e.getMessage());
                conected = false;
            }
            if (!conected)

            {
                conectionManager.close();
                System.out.println("Conexión finalizada con el cliente." + this.getName());
            }
        }
    }

    private void generateReport(JsonResponse<String[]> message) {
        message = conectionManager.convertData(message, String[].class);
        System.out.println(message.getData());
        LocalDateTime a = LocalDateTime.parse(message.getData()[0]);
        LocalDateTime b = LocalDateTime.parse(message.getData()[1]);
        int report = cinemaManager.generateReport(a, b);
        conectionManager.sendMessage(new JsonResponse<Integer>("", Msg.DONE.name(), report));
    }

    private void configurateAuditorium(JsonResponse<String[]> message) {
        message = conectionManager.convertData(message, String[].class);
        try {
            boolean answer = cinemaManager.configurateAuditorium(message.getData()[0], message.getData()[1],
                    message.getData()[2]);
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.DONE.name(), answer));
        } catch (DateTimeParseException e) {
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.Error.name(), false));
        }

    }

    private void deleteScreening(JsonResponse<String[]> message) {
        message = conectionManager.convertData(message, String[].class);
        try {

            boolean answer = cinemaManager.deleteScreening(message.getData()[0],
                    LocalDateTime.parse(message.getData()[1]),
                    message.getData()[2]);
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.DONE.name(), answer));
        } catch (DateTimeParseException e) {
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.Error.name(), false));
        }

    }

    private void createScreening(JsonResponse<String[]> message) {
        message = conectionManager.convertData(message, String[].class);
        try {
            boolean answer = cinemaManager.createScreening(message.getData()[0],
                    LocalDateTime.parse(message.getData()[1]),
                    message.getData()[2]);
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.DONE.name(), answer));
        } catch (DateTimeParseException e) {
            System.err.println("formato de fecha incorrecto");
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.Error.name(), false));
        }

    }

    private void editMovieData(JsonResponse<String[]> message) {
        message = conectionManager.convertData(message, String[].class);
        try {
            boolean answer = cinemaManager.editMovieData(message.getData()[0], message.getData()[1],
                    message.getData()[2]);
            String msg = answer ? Msg.DONE.name() : Msg.Error.name();
            conectionManager.sendMessage(new JsonResponse<Boolean>("", msg, answer));
        } catch (DateTimeParseException e) {
            System.err.println("formato de fecha incorrecto");
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.Error.name(), false));
        }
    }

    private void addMovie(JsonResponse<Movie> message) {
        message = conectionManager.convertData(message, Movie.class);
        boolean answer = cinemaManager.addMovie(message.getData());
        String msg = answer ? Msg.DONE.name() : Msg.Error.name();
        conectionManager.sendMessage(new JsonResponse<Boolean>("", msg, answer));
        System.out.println("Pelicula creada");

    }

    private void closeServer() {
        controller.stopServer();
        conected=false;
    }

}
