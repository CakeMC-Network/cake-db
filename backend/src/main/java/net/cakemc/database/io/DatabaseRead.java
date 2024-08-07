package net.cakemc.database.io;

import net.cakemc.database.collection.Collection;
import net.cakemc.database.collection.Document;

import java.io.File;
import java.io.IOException;

/**
 * The interface Database read.
 */
public interface DatabaseRead {

    /**
     * Read from file collection.
     *
     * @param file the file
     * @return the collection
     */
    Collection readFromFile(File file);

    /**
     * Deserialize collection collection.
     *
     * @param data the data
     * @return the collection
     * @throws IOException the io exception
     */
    Collection deserializeCollection(byte[] data) throws IOException;

    /**
     * Deserialize document document.
     *
     * @param data the data
     * @return the document
     * @throws IOException the io exception
     */
    Document deserializeDocument(byte[] data) throws IOException;

}
