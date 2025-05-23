package co.edu.uptc.server.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JsonResponse<T> {
    private String status;
    private String message;
    private T data;

}
