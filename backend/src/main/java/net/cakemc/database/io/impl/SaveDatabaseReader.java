package net.cakemc.database.io.impl;

import net.cakemc.database.ZypherDatabase;
import net.cakemc.database.api.Collection;
import net.cakemc.database.api.Document;
import net.cakemc.database.api.impl.ZypherCollection;
import net.cakemc.database.api.impl.ZypherDocument;
import net.cakemc.database.io.DatabaseRead;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.zip.DataFormatException;

/**
 * The type Save database reader.
 */
public class SaveDatabaseReader implements DatabaseRead {

    private final ZypherDatabase database;

    /**
     * Instantiates a new Save database reader.
     *
     * @param database the database
     */
    public SaveDatabaseReader(ZypherDatabase database) {
        this.database = database;
    }

    @Override
    public Collection readFromFile(File file) {
        if (!file.exists())
            throw new IllegalStateException("file '%s' does not exists!".formatted(file));
        try {
            FileInputStream inputStream = new FileInputStream(file);

            long fileLength = inputStream.available();

            byte[] data = new byte[(int) fileLength];
            int bytesRead = inputStream.read(data);
            if (bytesRead != fileLength) {
                throw new IllegalStateException("Failed to read the entire file");
            }

            byte[] finalCopy = database.getEncryption()
                    .decrypt(database.getCompression().decompress(data));

            return this.deserializeCollection(finalCopy);
        } catch (IOException | DataFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection deserializeCollection(byte[] data) throws IOException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(byteStream);

        // Read number of documents
        long id  = dataStream.readLong();
        int numDocuments = dataStream.readInt();
        List<Document> documents = new ArrayList<>(numDocuments);

        // Deserialize each document
        for (int i = 0; i < numDocuments; i++) {
            int docSize = dataStream.readInt();
            byte[] docBytes = new byte[docSize];
            dataStream.readFully(docBytes);
            Document doc = deserializeDocument(docBytes);
            documents.add(doc);
        }

        return new ZypherCollection(documents, id);
    }

    @Override
    public Document deserializeDocument(byte[] data) throws IOException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(byteStream);

        long documentId = dataStream.readLong();

        int size = dataStream.readInt();
        Hashtable<String, Object> values = new Hashtable<>(size);

        for (int i = 0; i < size; i++) {
            String key = dataStream.readUTF();

            byte typeId = dataStream.readByte();
            Object value;
            switch (typeId) {
                case 1:
                    value = dataStream.readUTF();
                    break;
                case 2:
                    value = dataStream.readInt();
                    break;
                case 3:
                    value = dataStream.readLong();
                    break;
                case 4:
                    value = dataStream.readDouble();
                    break;
                case 5:
                    value = dataStream.readBoolean();
                    break;
                case 6:
                    value = UUID.fromString(dataStream.readUTF());
                    break;
                case 7:
                    int objectSize = dataStream.readInt();
                    byte[] objectBytes = new byte[objectSize];
                    dataStream.readFully(objectBytes);
                    ByteArrayInputStream objectStream = new ByteArrayInputStream(objectBytes);
                    ObjectInputStream objInStream = new ObjectInputStream(objectStream);
                    try {
                        value = objInStream.readObject();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    throw new IOException("Unknown type identifier: " + typeId);
            }
            values.put(key, value);
        }

        return new ZypherDocument(values);
    }

}
