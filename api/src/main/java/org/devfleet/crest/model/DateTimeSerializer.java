package org.devfleet.crest.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by maurerit on 8/13/16.
 */
public class DateTimeSerializer extends StdSerializer<Long> {

    public DateTimeSerializer() {
        super(Long.class);
    }

    @Override
    public void serialize ( Long value, JsonGenerator gen, SerializerProvider provider ) throws IOException {
        final SimpleDateFormat f = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date(value);
        String dateFormatted = f.format(date);
        gen.writeString(dateFormatted);
    }
}
