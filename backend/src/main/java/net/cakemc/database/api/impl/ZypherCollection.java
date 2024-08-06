package net.cakemc.database.api.impl;

import net.cakemc.database.ZypherDatabase;
import net.cakemc.database.api.Collection;
import net.cakemc.database.api.Document;
import net.cakemc.database.api.Filter;
import net.cakemc.database.api.Filters;

import java.util.List;

/**
 * The type Zypher collection.
 */
public class ZypherCollection implements Collection {

    private final List<Document> documents;
    private final long id;

    /**
     * Instantiates a new Zypher collection.
     *
     * @param documents the documents
     * @param id        the id
     */
    public ZypherCollection(List<Document> documents, long id) {
        this.documents = documents;
        this.id = id;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public List<Document> find(Filter<?, ?> filter) {
        return this.documents.stream().filter(filter::matches).toList();
    }

    @Override
    public Document findSingle(Filter<?, ?> filter) {
        return this.documents.stream().filter(filter::matches).findFirst().orElse(null);
    }

    @Override
    public void updateOne(Document document) {

    }

    @Override
    public void replaceOne(Document document) {
        this.checkDocumentId(document);

        this.documents.removeIf(current -> current.id() == document.id());
        this.documents.add(document);
    }

    @Override
    public void updateMany(Document... document) {

    }

    @Override
    public void replaceMany(Document... document) {
        for (Document current : document) {
            this.replaceOne(current);
        }
    }

    @Override
    public void deleteOne(Filter<?, ?> filter) {
        this.documents.removeIf(filter::matches);
    }

    @Override
    public void insetOne(Document document) {
        this.checkDocumentId(document);

        this.documents.add(document);
    }

    @Override
    public long availableId() {
        long id = ZypherDatabase.RANDOM.nextLong();
        return this.find(Filters.id(id)) == null ? id : ZypherDatabase.RANDOM.nextLong();
    }

    /**
     * Check document id.
     *
     * @param document the document
     */
    public void checkDocumentId(Document document) {
        if (document.id() == -999)
            document.setId(this.availableId());
    }

    @Override
    public List<Document> collect() {
        return documents;
    }

    /**
     * Gets documents.
     *
     * @return the documents
     */
    public List<Document> getDocuments() {
        return documents;
    }
}
