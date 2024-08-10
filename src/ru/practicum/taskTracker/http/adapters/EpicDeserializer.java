package ru.practicum.taskTracker.http.adapters;

import com.google.gson.*;
import ru.practicum.taskTracker.model.Epic;

import java.lang.reflect.Type;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int id = 0;
        if (jsonObject.get("id") != null)
            id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        return new Epic(id, name, description);
    }

}