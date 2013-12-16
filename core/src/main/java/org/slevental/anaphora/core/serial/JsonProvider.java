package org.slevental.anaphora.core.serial;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.slevental.anaphora.core.txt.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class JsonProvider implements TextProvider {
    public static final String JSON = "json";

    static {
        TextProviders.register(JSON, JsonProvider.class);
    }

    private final JsonFactory factory = new JsonFactory();

    public JsonProvider(){
    }

    @Override
    public Text deserialize(InputStream stream) throws IOException {
        JsonParser parser = factory.createParser(stream);

        Text.Builder builder = Text.builder();
        JsonToken token = null;
        String currentName = null;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            currentName = parser.getCurrentName();
            switch (token) {
                case VALUE_STRING:
                    if      ("txt".equals(currentName))  { builder.setText(parser.getText()); }
                    else if ("name".equals(currentName)) { builder.setName(parser.getText()); }
                    else    { throw new IllegalArgumentException("Unknown text's string field: " + currentName); }
                    break;
                case START_OBJECT:
                    if   ( "annotations".equals(currentName)) { deserializeAnnotations(parser, builder); }
                    break;
                default:
            }
        }
        return builder.build();
    }

    private void deserializeAnnotations(JsonParser parser, Text.Builder builder) throws IOException {
        JsonToken token;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            switch (token) {
                case FIELD_NAME:
                    builder.setAnnotationSetName(parser.getText());
                    break;
                case START_OBJECT:
                    Annotation.Builder annBuilder = Annotation.builder();
                    deserializeAnnotation(parser, annBuilder);
                    builder.addAnnotation(annBuilder.build());
                    break;
                default:
            }
        }
    }

    private void deserializeAnnotation(JsonParser parser, Annotation.Builder builder) throws IOException {
        JsonToken token;
        String currentName = null;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            currentName = parser.getCurrentName();
            switch (token) {
                case VALUE_NUMBER_INT:
                    if      ("lo".equals(currentName)){ builder.from(parser.getIntValue()); }
                    else if ("hi".equals(currentName)){ builder.to(parser.getIntValue());   }
                    else if ("id".equals(currentName)){ builder.id(parser.getIntValue());   }
                    else    { throw new IllegalArgumentException("Unknown annotation's field: " + currentName);}
                    break;
                case VALUE_STRING:
                    if      ("type".equals(currentName)){ builder.type(AnnotationType.valueOf(parser.getValueAsString())); }
                    else    { throw new IllegalArgumentException("Unknown annotation's field: " + currentName);}
                    break;
                case START_OBJECT:
                    deserializeFeatures(parser, builder);
                    break;
                default:
            }
        }
    }

    private void deserializeFeatures(JsonParser parser, Annotation.Builder builder) throws IOException {
        JsonToken token;
        String currentName;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            currentName = parser.getCurrentName();
            switch (token) {
                case VALUE_STRING:
                    StaticFeature feature = StaticFeature.of(currentName);
                    builder.feature(feature == null ? new RawFeature(currentName) : feature, parser.getValueAsString());
                default:
            }
        }
    }

    @Override
    public void serialize(Text txt, OutputStream stream) throws IOException {
        JsonGenerator generator = factory.createGenerator(stream);
        generator.writeStartObject();
        generator.setPrettyPrinter(new DefaultPrettyPrinter());

        /**
         * Write text name
         */
        generator.writeStringField("name", txt.getName());

        /**
         * Write text itself
         */
        generator.writeStringField("txt", txt.getText());

        /**
         * Write annotations block
         */
        generator.writeObjectFieldStart("annotations");

        /**
         * For each annotation set
         */
        for (Map.Entry<String, IntervalCollection<Annotation>> each : txt.getAll().entrySet()) {
            generator.writeArrayFieldStart(each.getKey());
            for (Annotation ann : each.getValue()) {
                generator.writeStartObject();
                generator.writeStringField("type", ann.getType().name());
                generator.writeNumberField("id", ann.getId());
                generator.writeNumberField("lo", ann.getLo());
                generator.writeNumberField("hi", ann.getHi());

                /**
                 * Write features
                 */
                generator.writeObjectFieldStart("features");
                for (Map.Entry<Feature, Object> feature : ann.getFeatures().entrySet()) {
                    generator.writeStringField(feature.getKey().getName(), String.valueOf(feature.getValue()));
                }
                generator.writeEndObject(); // end of features object
                generator.writeEndObject(); // end of annotation object
            }
            generator.writeEndArray(); // end of annotation set
        }

        generator.writeEndObject(); // end of annotations

        generator.writeEndObject(); // end
        generator.close();
    }
}
