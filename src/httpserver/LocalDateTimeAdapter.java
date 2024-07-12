package httpserver;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH.mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime dateTime) throws IOException {
        if (dateTime != null) {
            jsonWriter.value(dateTime.format(dtf));
        } else {
            jsonWriter.value((String) null);
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if(jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return LocalDateTime.parse(jsonReader.nextString(), dtf);
    }
}
