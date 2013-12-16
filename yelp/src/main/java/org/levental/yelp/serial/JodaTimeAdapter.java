package org.levental.yelp.serial;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.IOException;

/**
* Created by IntelliJ IDEA.
* User: esLion
* Date: 5/12/13
* Time: 9:38 PM
* To change this template use File | Settings | File Templates.
*/
class JodaTimeAdapter extends TypeAdapter {
    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        return LocalDate.parse(in.nextString());
    }
}
