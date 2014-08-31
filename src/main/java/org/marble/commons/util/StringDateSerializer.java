package org.marble.commons.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StringDateSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public void serialize(Date aDate, JsonGenerator aJsonGenerator, SerializerProvider aSerializerProvider)
            throws IOException, JsonProcessingException {
        String dateString = dateFormat.format(aDate);
        aJsonGenerator.writeString(dateString);
    }

}