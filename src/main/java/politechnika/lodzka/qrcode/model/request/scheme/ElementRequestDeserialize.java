package politechnika.lodzka.qrcode.model.request.scheme;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.util.concurrent.AtomicDouble;
import politechnika.lodzka.qrcode.model.scheme.TypeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ElementRequestDeserialize extends StdDeserializer<ElementRequest> {

    public ElementRequestDeserialize() {
        this(null);
    }

    protected ElementRequestDeserialize(Class<?> vc) {
        super(vc);
    }

    @Override
    public ElementRequest deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        return deserializeGroup(node);
    }

    private ElementRequest deserializeGroup(JsonNode root) {
        ElementRequest result = new ElementRequest();
        JsonNode element = root.get("element");

        List<ElementRequest> elements = new ArrayList<>();

        ArrayNode array = (ArrayNode) element;
        array.elements().forEachRemaining(e -> {
            elements.add(deserializeElement(e));
        });

        result.setCode(root.get("code").asText());
        result.setType(TypeClass.getTypeClassByCode(root.get("type").asText()));
        result.setElement(elements);
        return result;
    }

    private ElementRequest deserializeElement(JsonNode element) {
        ElementRequest result = new ElementRequest();
        result.setCode(element.get("code").asText());
        result.setType(TypeClass.getTypeClassByCode(element.get("type").asText()));

        if (TypeClass.GROUP.equals(result.getType())) {
            return deserializeGroup(element);
        }

        JsonNode object = element.get("element");

        switch (result.getType()) {
            case STRING:
                result.setElement(object.asText());
                break;
            case BOOLEAN:
                result.setElement(new AtomicBoolean(object.asBoolean()));
                break;
            case LONG:
                result.setElement(new AtomicLong(object.asLong()));
                break;
            case DOUBLE:
                result.setElement(new AtomicDouble(object.asDouble()));
                break;
//            case OBJECT:
////                result.setElement(object.asLong());
////                break;
            case GROUP:
                break;
            case DATE:
                result.setElement(object.asText());
                break;
        }

        return result;
    }
}
