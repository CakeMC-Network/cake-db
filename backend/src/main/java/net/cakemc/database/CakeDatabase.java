package net.cakemc.database;

import net.cakemc.database.collection.Collection;
import net.cakemc.database.collection.impl.CakeCollection;
import net.cakemc.database.file.KeyFile;
import net.cakemc.database.io.DatabaseCompression;
import net.cakemc.database.io.DatabaseEncryption;
import net.cakemc.database.io.DatabaseRead;
import net.cakemc.database.io.DatabaseWrite;
import net.cakemc.database.io.impl.SaveDatabaseReader;
import net.cakemc.database.io.impl.SaveDatabaseWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class CakeDatabase {
    public static final Random RANDOM = ThreadLocalRandom.current();

    private static final String COLLECTION_SUFFIX = ".db.bin";
    private static final String COLLECTION_FORMAT = "%s%s";

    private final Map<String, Collection> collections = new ConcurrentHashMap<>();

    private final DatabaseCompression compression;
    private final DatabaseEncryption encryption;

    private final DatabaseRead databaseRead;
    private final DatabaseWrite databaseWrite;

    private final KeyFile keyFile;
    private final File baseDirectory;

    public CakeDatabase(File baseDirectory) {
        this.baseDirectory = baseDirectory;

        if (!baseDirectory.exists())
            baseDirectory.mkdir();

        this.keyFile = new KeyFile(baseDirectory);
        this.keyFile.loadKey();

        this.compression = new DatabaseCompression();
        this.encryption = new DatabaseEncryption(keyFile);

        this.databaseRead = new SaveDatabaseReader(this);
        this.databaseWrite = new SaveDatabaseWriter(this);
    }

    public void load() {
        File[] files = baseDirectory.listFiles();
        if (files == null)
            return;

        this.collections.clear();

        for (File current : files) {
            if (current.isDirectory())
                continue;

            if (!current.getName().endsWith(COLLECTION_SUFFIX))
                continue;

            String collectionName = current.getName().replace(COLLECTION_SUFFIX, "");
            Collection collection = this.databaseRead.readFromFile(current);

            this.collections.put(collectionName, collection);
        }
    }

    public void save() {
        long start = System.currentTimeMillis();
        collections.forEach((string, collection) -> {
            try {
                this.databaseWrite.writeToFile(new File(baseDirectory,
                        COLLECTION_FORMAT.formatted(string, COLLECTION_SUFFIX)), collection);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Collection getCollection(String name) {
        if (this.collections.containsKey(name))
            return this.collections.get(name);


        Collection collection = new CakeCollection(new ArrayList<>(), RANDOM.nextLong());
        this.collections.put(name, collection);
        this.save();
        return collection;
    }

    public DatabaseCompression getCompression() {
        return compression;
    }

    public DatabaseEncryption getEncryption() {
        return encryption;
    }


    public KeyFile getKeyFile() {
        return keyFile;
    }
}
