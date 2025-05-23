package co.edu.uptc.server.controller;

import java.io.IOException;
import co.edu.uptc.server.model.CinemaManager;
import co.edu.uptc.server.model.enums.UserOptions;
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

    @Override
    public void run() {
        while (true) {

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
                        break;
                    case CHECK_BOOK:

                        break;
                    case VALIDATE_BOOK:

                        break;
                    case CANCEL_BOOK:

                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (

            IOException e) {
                e.printStackTrace();
                System.out.println("Cliente desconectado");

            }
        }
    }

    private void selectSeat(JsonResponse<String[]> message) {
        String[] seat= message.getData();
        try {
          conectionManager.sendMessage(new JsonResponse<Boolean>("","Asiento ocupado",  cinemaManager.selectSeat(seat[0], seat[1], seat[2], seat[3], seat[4])));
     } catch (IOException e) {
         // TODO Auto-generated catch block
       e.printStackTrace();
  }
    }

    private void getMovieSchedule() {
        try {
            conectionManager.sendMessage(new JsonResponse<>("","",cinemaManager.getActualSchedule()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
