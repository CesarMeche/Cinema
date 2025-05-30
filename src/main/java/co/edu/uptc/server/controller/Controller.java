package co.edu.uptc.server.controller;

import co.edu.uptc.server.model.CinemaManager;
import co.edu.uptc.server.model.enums.Msg;
import co.edu.uptc.server.network.ConectionManager;
import co.edu.uptc.server.network.JsonResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
         e.printStackTrace();
      }
   }

   public void initCinemaSystem() {
      cm = new CinemaManager();
      System.out.println("Server started");
      initAceptThread();
   }

   private void initAceptThread() {
      while (true) {
         try {
            this.socket = serverSocket.accept();
            System.out.println("Client connected");
            ConectionManager conectionManager = new ConectionManager(socket);
            JsonResponse<String> status = conectionManager.receiveMessage(String.class);
            System.out.println("");
            userOption(status, conectionManager);
         } catch (SocketException e) {
            System.err.println("Desconeccion subita" + socket.getLocalAddress());
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   private void userOption(JsonResponse<String> status, ConectionManager conectionManager) {
      switch (status.getMessage()) {
         case "user":
            handleUserConection(conectionManager, status.getData());
            break;
         case "admin":
            handleAdminConection(conectionManager);
            break;
         default:
            handleInvalidUser(conectionManager);
            break;
      }
   }

   private void handleInvalidUser(ConectionManager conectionManager) {
      conectionManager
            .sendMessage(new JsonResponse<>(Msg.Error.name(), Msg.Error.name(), "usuario no valido"));
      System.err.println("usuario no valido");
   }

   private void handleAdminConection(ConectionManager conectionManager) {

      CinemaAdminConections cinemaAdminConections = new CinemaAdminConections(conectionManager, cm);
      cinemaAdminConections.start();
      conectionManager.sendMessage(new JsonResponse<>(Msg.Error.name(), Msg.DONE.name(), "usuario valido"));
      System.out.println("admin connected");
   }

   private void handleUserConection(ConectionManager conectionManager, String userName) {
      CinemaUserConections cinemaUserConections = new CinemaUserConections(conectionManager, cm,
            userName);
      cinemaUserConections.start();
      conectionManager.sendMessage(new JsonResponse<>(Msg.Error.name(), Msg.DONE.name(), "usuario valido"));
      System.out.println("User conectado");
   }

}
