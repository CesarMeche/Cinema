package co.edu.uptc.server.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import co.edu.uptc.server.model.CinemaManager;
import co.edu.uptc.server.model.enums.AdminOptions;
import co.edu.uptc.server.model.enums.Msg;
import co.edu.uptc.server.model.enums.UserOptions;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.network.ConectionManager;
import co.edu.uptc.server.network.JsonResponse;

public class CinemaAdminConections extends Thread {
    private ConectionManager conectionManager;
    private CinemaManager cinemaManager;
    private AdminOptions adminOption;

    public CinemaAdminConections(ConectionManager conectionManager, CinemaManager cinemaManager) {
        this.conectionManager = conectionManager;
        this.cinemaManager = cinemaManager;
    }

    @Override
    public void run() {
        try {
            while (true) {
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
                    default:
                        System.out.println("Opción inválida");
                }
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Opción desconocida recibida: " + e.getMessage());
        } finally {
            conectionManager.close();
            System.out.println("Conexión finalizada con el cliente.");
        }
    }

    private void generateReport(JsonResponse<LocalDateTime[]> message) {
        int report = cinemaManager.generateReport(message.getData()[0], message.getData()[1]);
        // TODO mejorar msg
        // String msg = answer? Msg.DONE.name() : Msg.Error.name();
        try {
            conectionManager.sendMessage(new JsonResponse<Integer>("", Msg.DONE.name(), report));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void configurateAuditorium(JsonResponse<String[]> message) {
        boolean answer = cinemaManager.configurateAuditorium(message.getData()[0], message.getData()[1],
                message.getData()[2]);
        // TODO mejorar msg
        String msg = answer ? Msg.DONE.name() : Msg.Error.name();
        try {
            conectionManager.sendMessage(new JsonResponse<Boolean>("", msg, answer));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void deleteScreening(JsonResponse<String[]> message) {
        boolean answer = cinemaManager.deleteScreening(message.getData()[0], LocalDateTime.parse(message.getData()[1]),
                message.getData()[2]);
        // TODO mejorar msg
        String msg = answer ? Msg.DONE.name() : Msg.Error.name();
        try {
            conectionManager.sendMessage(new JsonResponse<Boolean>("", msg, answer));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createScreening(JsonResponse<String[]> message) {
        boolean answer = cinemaManager.createScreening(message.getData()[0], LocalDateTime.parse(message.getData()[1]),
                message.getData()[2]);
        // TODO mejorar msg
        String msg = answer ? Msg.DONE.name() : Msg.Error.name();
        try {
            conectionManager.sendMessage(new JsonResponse<Boolean>("", msg, answer));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void editMovieData(JsonResponse<String[]> message) {
        // TODO mensaje de error con que quedo mal
        boolean answer = cinemaManager.editMovieData(message.getData()[0], message.getData()[1], message.getData()[2]);
        // TODO mejorar msg
        String msg = answer ? Msg.DONE.name() : Msg.Error.name();
        try {
            conectionManager.sendMessage(new JsonResponse<Boolean>("", msg, answer));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addMovie(JsonResponse<Movie> message) {
        message = conectionManager.convertData(message, Movie.class);
        try {
            boolean answer = cinemaManager.addMovie(message.getData());
            // TODO mejorar msg
            String msg = answer ? Msg.DONE.name() : Msg.Error.name();
            conectionManager.sendMessage(new JsonResponse<Boolean>("", msg, answer));
            System.out.println("Pelicula creada");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
