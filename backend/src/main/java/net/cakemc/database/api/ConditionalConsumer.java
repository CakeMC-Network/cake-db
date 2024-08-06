package net.cakemc.database.api;

/**
 * The interface Conditional consumer.
 *
 * @param <T> the type parameter
 */
public interface ConditionalConsumer<T> {

    /**
     * Expect boolean.
     *
     * @param value the value
     * @return the boolean
     */
    public boolean expect(T value);

}
