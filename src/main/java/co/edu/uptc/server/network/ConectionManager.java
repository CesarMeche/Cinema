package co.edu.uptc.server.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConectionManager {

    private Socket socket;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private Gson gson;

    public ConectionManager(Socket socket) {
        // chanchito feliz
        this.socket = socket;

        // 👉 Crea un Gson que maneja LocalDateTime
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                })
                .create();

        try {
            this.dataInput = new DataInputStream(this.socket.getInputStream());
            this.dataOutput = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(JsonResponse message)  {
        String jsonMessage = gson.toJson(message);
        try {
            dataOutput.writeUTF(jsonMessage);
            dataOutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public <T> JsonResponse<T> receiveMessage(Class<T> clazz) throws IOException {
        String jsonMessage = dataInput.readUTF();
        Type type = TypeToken.getParameterized(JsonResponse.class, clazz).getType();
        return gson.fromJson(jsonMessage, type);
    }
    public JsonResponse receiveMessage() throws IOException {
        String jsonMessage = dataInput.readUTF();
        return gson.fromJson(jsonMessage, JsonResponse.class);
    }

    public <T> JsonResponse<T> convertData(JsonResponse<?> response, Class<T> classType) {
        String jsonData = gson.toJson(response.getData());
        T convertedData = gson.fromJson(jsonData, classType);
        return new JsonResponse<>(response.getStatus(), response.getMessage(), convertedData);
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
