package io.github.cotide.dapper.core.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.github.cotide.dapper.basic.enums.IEnum;
import io.github.cotide.dapper.basic.enums.IntegerEnum;
import io.github.cotide.dapper.basic.enums.StringEnum;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 枚举处理
 */
public class EnumAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        Class<T> rawType = (Class<T>) type.getRawType();
        if (!rawType.isEnum()) return null;
        final Map<String, T> constants = new HashMap<>();
        for (T constant : rawType.getEnumConstants()) {
            if(constant instanceof IEnum)
            {
                constants.put(((IEnum)constant).getCode().toString(),constant);
            }else{
                constants.put(constant.toString(), constant);
            }
        }
        return new TypeAdapter<T>() {
            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) {
                    out.nullValue();
                }
                else {
                    if(value instanceof IntegerEnum)
                    {
                        out.value(((IntegerEnum)value).getCode());
                    }else  if(value instanceof StringEnum)
                    {
                        out.value(((StringEnum)value).getCode());
                    }else{
                        out.value(value.toString());
                    }
                }
            }

            public T read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                }
                else {
                    return constants.get(reader.nextString());
                }
            }
        };
    }
}