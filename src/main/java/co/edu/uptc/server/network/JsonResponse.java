package co.edu.uptc.server.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JsonResponse {
    private String status;
    private String message;
    private String data;
}
