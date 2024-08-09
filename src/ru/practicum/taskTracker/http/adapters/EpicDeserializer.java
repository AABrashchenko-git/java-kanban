package ru.practicum.taskTracker.http.adapters;

import com.google.gson.*;
import ru.practicum.taskTracker.model.Epic;

import java.lang.reflect.Type;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    // Нагуглил и создал EpicDeserializer
    // т.к. при обновлении эпика мы копируем id сабтасок из старого эпика в новый, а при десериализации subTasksIdList
    // не инициализировался и выбрасывалось исключение
    // Соответственно, в конструкторе для обновления эпика добавил subTasksIdList = new ArrayList<>();
    // Единственное, пришлось делать subTasksIdList не final
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