package net.cakemc.database.io;

import net.cakemc.database.api.Collection;
import net.cakemc.database.api.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The interface Database write.
 */
public interface DatabaseWrite {

    /**
     * Write to file.
     *
     * @param file       the file
     * @param collection the collection
     * @throws FileNotFoundException the file not found exception
     */
    public void writeToFile(File file, Collection collection) throws FileNotFoundException;

    /**
     * Serialize collection byte [ ].
     *
     * @param collection the collection
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] serializeCollection(Collection collection) throws IOException;

    /**
     * Serialize document byte [ ].
     *
     * @param document the document
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] serializeDocument(Document document) throws IOException;

}
