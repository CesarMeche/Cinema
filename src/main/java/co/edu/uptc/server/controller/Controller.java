package co.edu.uptc.server.controller;

import co.edu.uptc.server.model.CinemaManager;
import co.edu.uptc.server.network.ConectionManager;
import co.edu.uptc.server.network.JsonResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;

public class Controller {
   private CinemaManager cm;
   private ServerSocket serverSocket;
   private Socket socket;
   private int port;

   public Controller() {
      this.port = 1235;
      try {
         this.serverSocket = new ServerSocket(port);
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @SuppressWarnings("rawtypes")
   public void initCinemaSystem() {
      cm = new CinemaManager();
       //cm.createBook("mikus","papaya",LocalDateTime.of(2025, 5, 28, 13, 50, 1).toString(),"A","6","user");
        
       cm.saveData();
      System.out.println("cine started");

      System.out.println("Server started");
      while (true) {
         try {
            this.socket = serverSocket.accept();
            System.out.println("Client connected");
            ConectionManager conectionManager = new ConectionManager(socket);
            JsonResponse<String> status = conectionManager.receiveMessage(String.class);
            System.out.println("");
            switch (status.getMessage()) {
               case "user":
                  CinemaUserConections cinemaUserConections = new CinemaUserConections(conectionManager, cm,
                        status.getData());
                  cinemaUserConections.start();
                  System.out.println("User conectardo");
                  break;
               case "admin":
                  CinemaAdminConections cinemaAdminConections = new CinemaAdminConections(conectionManager, cm);
                  cinemaAdminConections.start();
                  System.out.println("admin connected");
                  break;
               default:
                  System.err.println("usuario no valido");
                  break;
            }

            System.out.println("Server started");
         } catch (SocketException e) {
            System.err.println("Desconeccion subita" + socket.getLocalAddress());
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }

}
