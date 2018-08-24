package org.dapper.core.provider.mapper;

import org.dapper.core.provider.Internal.ArrayKey;
import org.dapper.core.provider.Internal.Tuple;
import org.dapper.core.unit.Cache;

import java.lang.reflect.Type;

public class MultiPocoFactory {

    /**
     * Various cached stuff
     */
    private static final Cache<Tuple<Type, ArrayKey<Type>, String, String>, Object> MultiPocoFactories = new Cache<Tuple<Type, ArrayKey<Type>, String, String>, Object>();
    private static final Cache<ArrayKey<Type>, Object> AutoMappers = new Cache<ArrayKey<Type>, Object>();


}
