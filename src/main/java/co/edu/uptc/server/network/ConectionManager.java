package co.edu.uptc.server.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConectionManager {
    private ObjectMapper mapper;
    private Socket socket;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private Gson gson;

    public ConectionManager(Socket socket) {
        //chanchito feliz
        this.socket = socket;
        this.gson = new Gson();
        try {
            this.dataInput = new DataInputStream(this.socket.getInputStream());
            this.dataOutput = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(JsonResponse message) throws IOException {
        String jsonMessage = gson.toJson(message);
        dataOutput.writeUTF(jsonMessage);
        dataOutput.flush();
    }

    public JsonResponse receiveMessage() throws IOException {
        String jsonMessage = dataInput.readUTF();
        return gson.fromJson(jsonMessage, JsonResponse.class);
    }

    public void close() {
        try {
            if (dataInput != null)
                dataInput.close();
            if (dataOutput != null)
                dataOutput.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
