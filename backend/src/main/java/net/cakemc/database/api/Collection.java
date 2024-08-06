package net.cakemc.database.api;

import java.util.List;
import java.util.stream.Stream;

/**
 * The interface Collection.
 */
public interface Collection {

    /**
     * Id long.
     *
     * @return the long
     */
    public long id();

    /**
     * Find list.
     *
     * @param filter the filter
     * @return the list
     */
    public List<Document> find(Filter<?, ?> filter);

    /**
     * Find single document.
     *
     * @param filter the filter
     * @return the document
     */
    public Document findSingle(Filter<?, ?> filter);

    /**
     * Update one.
     *
     * @param document the document
     */
    public void updateOne(Document document);

    /**
     * Replace one.
     *
     * @param document the document
     */
    public void replaceOne(Document document);

    /**
     * Update many.
     *
     * @param document the document
     */
    public void updateMany(Document... document);

    /**
     * Replace many.
     *
     * @param document the document
     */
    public void replaceMany(Document... document);

    /**
     * Delete one.
     *
     * @param filter the filter
     */
    public void deleteOne(Filter<?, ?> filter);

    /**
     * Inset one.
     *
     * @param document the document
     */
    public void insetOne(Document document);

    /**
     * Available id long.
     *
     * @return the long
     */
    public long availableId();

    /**
     * Collect list.
     *
     * @return the list
     */
    List<Document> collect();
}
