package com.uhmily.scovilleplugin.Types.Json.Serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.Location;

import java.io.IOException;

public class LocationSerializer extends StdSerializer<Location> {

    protected LocationSerializer() { this(null); }

    protected LocationSerializer(Class<Location> t) {
        super(t);
    }

    @Override
    public void serialize(Location value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeStringField("world", value.getWorld().getUID().toString());
        gen.writeNumberField("x", value.getX());
        gen.writeNumberField("y", value.getY());
        gen.writeNumberField("z", value.getZ());
        gen.writeNumberField("yaw", value.getYaw());
        gen.writeNumberField("pitch", value.getPitch());
        gen.writeEndObject();

    }

}
