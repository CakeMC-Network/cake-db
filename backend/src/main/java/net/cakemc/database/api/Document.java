package net.cakemc.database.api;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

/**
 * The interface Document.
 */
public interface Document {

    /**
     * Id long.
     *
     * @return the long
     */
    public long id();

    /**
     * Append document.
     *
     * @param key   the key
     * @param value the value
     * @return the document
     */
    public Document append(String key, Object value);

    /**
     * Get t.
     *
     * @param <T>  the type parameter
     * @param key  the key
     * @param type the type
     * @return the t
     */
    public <T> T get(String key, Class<T> type);

    /**
     * Get t.
     *
     * @param <T>  the type parameter
     * @param key  the key
     * @param type the type
     * @return the t
     */
    public <T> T get(String key, Type type);

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public Object get(String key);

    /**
     * Gets string.
     *
     * @param key the key
     * @return the string
     */
    public String getString(String key);

    /**
     * Gets int.
     *
     * @param key the key
     * @return the int
     */
    public int getInt(String key);

    /**
     * Gets short.
     *
     * @param key the key
     * @return the short
     */
    public short getShort(String key);

    /**
     * Gets double.
     *
     * @param key the key
     * @return the double
     */
    public double getDouble(String key);

    /**
     * Gets float.
     *
     * @param key the key
     * @return the float
     */
    public float getFloat(String key);

    /**
     * Gets long.
     *
     * @param key the key
     * @return the long
     */
    public long getLong(String key);

    /**
     * Gets boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean getBoolean(String key);

    /**
     * Gets uuid.
     *
     * @param key the key
     * @return the uuid
     */
    public UUID getUUID(String key);

    /**
     * To debug string string.
     *
     * @return the string
     */
    public String toDebugString();

    /**
     * Contains key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean containsKey(String key);

    /**
     * Remove.
     *
     * @param key the key
     */
    public void remove(String key);

    /**
     * Entry set iterable.
     *
     * @return the iterable
     */
    Iterable<? extends Map.Entry<String, Object>> entrySet();

    /**
     * Size int.
     *
     * @return the int
     */
    int size();

    /**
     * Sets id.
     *
     * @param id the id
     */
    void setId(long id);
}
