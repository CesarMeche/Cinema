package co.edu.uptc.server.structures.avltree;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import co.edu.uptc.server.model.pojos.Screening;
import co.edu.uptc.server.model.pojos.comparator.ScreeningDateComparator;

import java.io.IOException;
import java.util.List;

public class AVLTreeDeserializer extends JsonDeserializer<AVLTree<Screening>> {

    @Override
public AVLTree<Screening> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    CollectionType listType = ctxt.getTypeFactory().constructCollectionType(List.class, Screening.class);
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    List<Screening> list = mapper.readValue(p, listType);
    AVLTree<Screening> tree = new AVLTree<>(new ScreeningDateComparator());
    for (Screening screening : list) {
        tree.insert(screening);
    }
    return tree;
}

}
