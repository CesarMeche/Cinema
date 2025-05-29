package co.edu.uptc.server.structures.avltree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class AVLTreeSerializer extends JsonSerializer<AVLTree> {
    @Override
    public void serialize(AVLTree tree, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        List<Object> list = new ArrayList<>();
        for (Object item : tree.getInOrder()) {
            list.add(item);
        }
        gen.writeObject(list);
    }
}

