package sql2o.extensions.quirks;

import sql2o.converters.Converter;
import sql2o.quirks.NoQuirks;

import java.util.Map;

/**
 * @author aldenquimby@gmail.com
 * @since 4/6/14
 */
public class PostgresQuirks extends NoQuirks {
    public PostgresQuirks() {
        super();
    }

    public PostgresQuirks(Map<Class, Converter> converters) {
        super(converters);
    }

    @Override
    public boolean returnGeneratedKeysByDefault() {
        return false;
    }
}
