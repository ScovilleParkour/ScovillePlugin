package com.uhmily.scovilleplugin.Types.Json.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.UUID;

public class LocationDeserializer extends StdDeserializer<Location> {

    protected LocationDeserializer() { this(null); }

    protected LocationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Location deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        String worldUUID = node.get("world").asText();
        World world = Bukkit.getWorld(UUID.fromString(worldUUID));
        double x = node.get("x").asDouble();
        double y = node.get("y").asDouble();
        double z = node.get("z").asDouble();
        float yaw = node.get("yaw").floatValue();
        float pitch = node.get("pitch").floatValue();

        return new Location(world, x, y, z, yaw, pitch);
    }
}
