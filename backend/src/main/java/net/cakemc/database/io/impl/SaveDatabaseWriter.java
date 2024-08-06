package net.cakemc.database.io.impl;

import net.cakemc.database.ZypherDatabase;
import net.cakemc.database.api.Collection;
import net.cakemc.database.api.Document;
import net.cakemc.database.api.impl.ZypherDocument;
import net.cakemc.database.io.DatabaseWrite;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The type Save database writer.
 */
public class SaveDatabaseWriter implements DatabaseWrite {

    // TODO ADD SYSTEM WITH DYNAMIC DE/SERIALIZER

    private final ZypherDatabase database;

    /**
     * Instantiates a new Save database writer.
     *
     * @param database the database
     */
    public SaveDatabaseWriter(ZypherDatabase database) {
        this.database = database;
    }

    @Override
    public void writeToFile(File file, Collection collection) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] data = this.serializeCollection(collection);
            byte[] finalData = this.database.getCompression()
                    .compress(this.database.getEncryption().encrypt(data));

            fileOutputStream.write(finalData);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serializeCollection(Collection collection) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        List<Document> documents = collection.collect();

        // Write number of documents
        dataStream.writeLong(collection.id());
        dataStream.writeInt(documents.size());

        // Serialize each document
        for (Document doc : documents) {
            byte[] docBytes = this.serializeDocument(doc);
            dataStream.writeInt(docBytes.length);
            dataStream.write(docBytes);
        }

        dataStream.flush();
        return byteStream.toByteArray();
    }

    @Override
    public byte[] serializeDocument(Document document) throws IOException {
        if (!(document instanceof ZypherDocument zypherDocument))
            return new byte[0];

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        dataStream.writeLong(zypherDocument.id());

        dataStream.writeInt(zypherDocument.size());

        for (Map.Entry<String, Object> entry : zypherDocument.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null)
                continue;

            dataStream.writeUTF(key);

            switch (value) {
                case String s -> {
                    dataStream.writeByte(1);

                    dataStream.writeUTF(s);
                }
                case Integer i -> {
                    dataStream.writeByte(2);

                    dataStream.writeInt(i);
                }
                case Long l -> {
                    dataStream.writeByte(3);

                    dataStream.writeLong(l);
                }
                case Double v -> {
                    dataStream.writeByte(4);

                    dataStream.writeDouble(v);
                }
                case Boolean b -> {
                    dataStream.writeByte(5);
                    dataStream.writeBoolean(b);
                }
                case UUID uuid -> {
                    dataStream.writeByte(6);
                    dataStream.writeUTF(uuid.toString());
                }
                case Serializable serializable -> {
                    dataStream.writeByte(7);
                    ByteArrayOutputStream objectStream = new ByteArrayOutputStream();
                    ObjectOutputStream objOutStream = new ObjectOutputStream(objectStream);
                    objOutStream.writeObject(value);
                    objOutStream.flush();
                    byte[] objectBytes = objectStream.toByteArray();
                    dataStream.writeInt(objectBytes.length);
                    dataStream.write(objectBytes);
                }
                case null, default -> throw new IOException("Unsupported data type: " + value.getClass().getSimpleName());
            }
        }

        dataStream.flush();
        return byteStream.toByteArray();
    }

}
