package net.cakemc.database.api;

/**
 * The type Filters.
 */
public class Filters {

    /**
     * Custom filter.
     *
     * @param key      the key
     * @param consumer the consumer
     * @return the filter
     */
    public static Filter<String, ConditionalConsumer<Object>> custom(String key,
                                                                          ConditionalConsumer<Object> consumer) {
        return new Filter<>(key, consumer) {
            @Override
            public boolean matches(Document document) {
                if (!document.containsKey(key))
                    return false;

                Object object = document.get(key);
                return consumer.expect(object);
            }
        };
    }

    /**
     * Custom 0 filter.
     *
     * @param <Type>   the type parameter
     * @param key      the key
     * @param consumer the consumer
     * @return the filter
     */
    @Deprecated
    public static <Type> Filter<String, ConditionalConsumer<Type>> custom0(String key,
                                                                          ConditionalConsumer<Type> consumer) {
        return new Filter<>(key, consumer) {
            @Override
            public boolean matches(Document document) {
                if (!document.containsKey(key))
                    return false;

                Object object = document.get(key);
                return consumer.expect((Type) object);
            }
        };
    }

    /**
     * Id filter.
     *
     * @param id the id
     * @return the filter
     */
    public static Filter<Void, Long> id(long id) {
        return new Filter<>(null, id) {
            @Override
            public boolean matches(Document document) {
                return document.id() == id;
            }
        };
    }

    /**
     * Equals filter.
     *
     * @param key   the key
     * @param value the value
     * @return the filter
     */
    public static Filter<String, Object> equals(String key, Object value) {
        return new Filter<>(key, value) {
            @Override
            public boolean matches(Document document) {
                return document.containsKey(key) && document.get(key).equals(value);
            }
        };
    }

}
