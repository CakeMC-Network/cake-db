package net.cakemc.database.collection.impl;

import net.cakemc.database.collection.Document;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class CakeDocument implements Document {

    private final Hashtable<String, Object> values;
    private long documentId;


    public CakeDocument(Hashtable<String, Object> values) {
        this.values = values;
        this.documentId = this.getLong("_id");
    }

    public CakeDocument() {
        this.documentId = -999;
        this.values = new Hashtable<>();
        this.append("_id", documentId);
    }

    @Override
    public long id() {
        return documentId;
    }

    @Override
    public Document append(String key, Object value) {
        this.values.put(key, value);
        return this;
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        if (this.containsKey(key)) {
            return type.cast(this.values.get(key));
        }
        return null;
    }

    @Override
    public <T> T get(String key, Type type) {
        Class<T> classType = null;
        if (type instanceof ParameterizedType parameterizedType) {
            classType = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new RuntimeException("No parameterized type information");
        }
        if (classType != null && this.containsKey(key)) {
            return classType.cast(this.values.get(key));
        }
        return null;
    }

    @Override
    public String toDebugString() {
        StringBuilder content = new StringBuilder();
        content.append("[id=%s - size=%s] ".formatted(this.documentId, this.values.size()))
                .append("(\n");
        this.values.forEach((string, o) -> content.append("  %s: %s \n".formatted(string, o)));
        content.append(");");
        return content.toString();
    }

    @Override
    public String toString() {
        return toDebugString();
    }

    @Override
    public Object get(String key) {
        return values.get(key);
    }

    @Override
    public String getString(String key) {
        return this.get(key, String.class);
    }

    @Override
    public int getInt(String key) {
        return (int) this.get(key);
    }

    @Override
    public short getShort(String key) {
        return (short) this.get(key);
    }

    @Override
    public double getDouble(String key) {
        return (double) this.get(key);
    }

    @Override
    public float getFloat(String key) {
        return (float) this.get(key);
    }

    @Override
    public long getLong(String key) {
        return (long) this.get(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) this.get(key);
    }

    @Override
    public UUID getUUID(String key) {
        return this.get(key, UUID.class);
    }

    @Override
    public boolean containsKey(String key) {
        return this.values.containsKey(key);
    }

    @Override
    public void remove(String key) {
        this.values.remove(key);
    }

    @Override
    public Iterable<? extends Map.Entry<String, Object>> entrySet() {
        return values.entrySet();
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public void setId(long id) {
        this.documentId = id;
        this.append("_id", id);
    }
}
