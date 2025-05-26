package co.edu.uptc.server.controller;

import co.edu.uptc.server.model.CinemaManager;
import co.edu.uptc.server.network.ConectionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Controller {
   CinemaManager cm;
   private ServerSocket serverSocket;
   private Socket socket;
   private ConectionManager conectionManager;
   private int port;

   public Controller() {
      this.port = 1234;
      try {
         this.serverSocket = new ServerSocket(port);
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public void initCinemaSystem() {
      cm = new CinemaManager();
      System.out.println("Server started");
      try {
         while (true) {
            this.socket = serverSocket.accept();
            System.out.println("Client connected");
            this.conectionManager = new ConectionManager(socket);
            CinemaAdminConections threadClient= new CinemaAdminConections(conectionManager, cm);
            threadClient.start();
            System.out.println("Server started");
         }
      } catch (IOException e) {

         e.printStackTrace();
      }

   }

}
