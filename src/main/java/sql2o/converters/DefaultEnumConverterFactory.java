package sql2o.converters;

import io.github.cotide.dapper.basic.enums.EnumMapping;
import io.github.cotide.dapper.basic.enums.IEnum;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Default implementation of {@link EnumConverterFactory},
 * used by sql2o to convert a value from the database into an {@link Enum}.
 */
public class DefaultEnumConverterFactory implements EnumConverterFactory {
    public <E extends Enum> Converter<E> newConverter(final Class<E> enumType) {
        return new Converter<E>() {
            @SuppressWarnings("unchecked")
            public E convert(Object val) throws ConverterException {
                if (val == null) {
                    return null;
                }
                try {
                    if (val instanceof String){
                        return (E)Enum.valueOf(enumType, val.toString());
                    } else if (val instanceof Number){

                        for (Enum item: enumType.getEnumConstants())
                        {
                            if(item instanceof IEnum)
                            {
                                if(((IEnum)item).getCode() == val)
                                {
                                    return (E)item;
                                }
                            }
                        }
                        return null;
                    }
                } catch (Throwable t) {
                    throw new ConverterException("Error converting value '" + val.toString() + "' to " + enumType.getName(), t);
                }
                throw new ConverterException("Cannot convert type '" + val.getClass().getName() + "' to an Enum");
            }

            public Object toDatabaseParam(Enum val) {
                return val.name();
            }
        };
    }
}
