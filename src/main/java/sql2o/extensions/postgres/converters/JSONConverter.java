package sql2o.extensions.postgres.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.cotide.dapper.core.gson.EnumAdapterFactory;
import sql2o.converters.Converter;
import sql2o.converters.ConverterException;
import sql2o.converters.ConvertersProvider;
import sql2o.converters.StringConverter;
import org.postgresql.util.PGobject;

import java.util.Map;

/**
 * User: dimzon
 * Date: 4/25/14
 * Time: 12:08 AM
 */
public class JSONConverter implements Converter<JsonElement>, ConvertersProvider {

    static class writerHolder {
        static final Gson gson =  new GsonBuilder()
                .registerTypeAdapterFactory(new EnumAdapterFactory())
                .setDateFormat("YYYY-MM-dd HH:mm:ss")
                .create();
    }

    public static Gson createGson(){
        return writerHolder.gson;
    }


    public void fill(Map<Class<?>, Converter<?>> mapToFill) {
        mapToFill.put(JsonElement.class, this);
    }

    public JsonElement convert(Object val) throws ConverterException {
        if (val == null) return null;
        if (val instanceof JsonElement) return (JsonElement) val;
        String jsonString;
        if (val instanceof String) {
            jsonString = (String) val;
        } else if (val instanceof PGobject) {
            // at one side this is just a demo
            // the better way is to unwrap PGObject to String
            // with type==json in Quirks.getRSVal
            jsonString = ((PGobject) val).getValue();
        } else {
            jsonString = stringConverterHolder.converter.convert(val);
        }
        return parserHolder.parser.parse(jsonString);
    }

    public Object toDatabaseParam(JsonElement val) {
        if (val == null) return null;
        final StringBuilder stringBuilder = new StringBuilder();
        writerHolder.gson.toJson(val, stringBuilder);
        return stringBuilder.toString();
    }

    static class parserHolder {
        static final JsonParser parser = new JsonParser();
    }


    static class stringConverterHolder {
        static final StringConverter converter = new StringConverter();
    }
}
