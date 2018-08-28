package io.github.cotide.dapper.core.provider.mapper;

import io.github.cotide.dapper.core.provider.Internal.ArrayKey;
import io.github.cotide.dapper.core.unit.Cache;
import io.github.cotide.dapper.core.provider.Internal.Tuple;

import java.lang.reflect.Type;

/**
 * @author cotide
 */
public class MultiPocoFactory {

    /**
     * Various cached stuff
     */
    private static final Cache<Tuple<Type, ArrayKey<Type>, String, String>, Object> MultiPocoFactories = new Cache<Tuple<Type, ArrayKey<Type>, String, String>, Object>();
    private static final Cache<ArrayKey<Type>, Object> AutoMappers = new Cache<ArrayKey<Type>, Object>();


}
