package org.levental.yelp.serial;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class JsonReaders {
    private static final Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new JodaTimeAdapter());
        gson = builder.create();
    }

    public static <E> List<E> read(File file, Class<E> classOfT) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<E> res = new ArrayList<E>();
        try {
            while ((line = reader.readLine()) != null) {
                res.add(gson.fromJson(line, classOfT));
            }
        } catch (IOException e) {
            IOUtils.closeQuietly(reader);
        }
        return res;
    }

}
