package co.edu.uptc.server.controller;

import java.io.IOException;
import co.edu.uptc.server.model.CinemaManager;
import co.edu.uptc.server.model.enums.Msg;
import co.edu.uptc.server.model.enums.UserOptions;
import co.edu.uptc.server.model.pojos.Book;
import co.edu.uptc.server.network.ConectionManager;
import co.edu.uptc.server.network.JsonResponse;

public class CinemaUserConections extends Thread {
    private ConectionManager conectionManager;
    private CinemaManager cinemaManager;
    private UserOptions uOptions;

    public CinemaUserConections(ConectionManager conectionManager, CinemaManager cinemaManager) {
        this.conectionManager = conectionManager;
        this.cinemaManager = cinemaManager;

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void run() {
        boolean conected = true;
        while (conected) {

            JsonResponse message;
            try {
                message = conectionManager.receiveMessage();
                uOptions = UserOptions.valueOf(message.getStatus());
                switch (uOptions) {
                    case GET_MOVIE_SCHEDULE:
                        getMovieSchedule();
                        break;
                    case SELECT_SEAT:
                        selectSeat(message);
                        break;
                    case CREATE_BOOK:
                        createBook(message);
                        break;
                    case CHECK_BOOK:
                        checkBook(message);
                        break;
                    case VALIDATE_BOOK:
                        validateBook(message);
                        break;
                    case CANCEL_BOOK:
                        cancelBook(message);
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
                System.out.println("Conexión finalizada con el cliente.");
                conected = false;

            }
        }
    }

    private void selectSeat(JsonResponse<String[]> message) {
        JsonResponse<String[]> msg = conectionManager.convertData(message, String[].class);
        String[] seat = msg.getData();
        try {

            boolean answer = cinemaManager.selectSeat(seat[0], seat[1], seat[2], seat[3], seat[4]);
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.DONE.name(), answer));

        } catch (Exception e) {
            conectionManager.sendMessage(new JsonResponse<Boolean>("", Msg.Error.name(), false));
        }
    }

    private void getMovieSchedule() {

        conectionManager.sendMessage(new JsonResponse<>("", "", cinemaManager.getActualSchedule()));

    }

    private void createBook(JsonResponse<String[]> msg) {
        String[] data = msg.getData(); // [movie, auditorium, dateStr, row, seat]
        try {
            cinemaManager.createBook(data[0], data[1], data[2], data[3], data[4]);
            conectionManager.sendMessage(new JsonResponse<>("Reserva creada", Msg.DONE.name(), true));
        } catch (Exception e) {
            conectionManager.sendMessage(new JsonResponse<>("Error al crear reserva", Msg.Error.name(), false));
        }
    }

    private void checkBook(JsonResponse<String> msg) {
        Book book = cinemaManager.checkBook(msg.getData());
        if (book != null) {
            conectionManager.sendMessage(new JsonResponse<>("Reserva encontrada", Msg.DONE.name(), book));
        } else {
            conectionManager.sendMessage(new JsonResponse<>("No existe la reserva", Msg.Error.name(), null));
        }
    }

    private void validateBook(JsonResponse<String> msg) {
        boolean validated = cinemaManager.validateBook(msg.getData());
        conectionManager.sendMessage(new JsonResponse<>("Validación", Msg.DONE.name(), validated));
    }

    private void cancelBook(JsonResponse<String> msg) {
        boolean cancelled = cinemaManager.cancelBook(msg.getData());
        conectionManager.sendMessage(new JsonResponse<>("Cancelación", Msg.DONE.name(), cancelled));
    }

}
