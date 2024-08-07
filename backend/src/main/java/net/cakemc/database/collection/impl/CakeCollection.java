package net.cakemc.database.collection.impl;

import net.cakemc.database.CakeDatabase;
import net.cakemc.database.collection.Collection;
import net.cakemc.database.collection.Document;
import net.cakemc.database.collection.Filter;
import net.cakemc.database.collection.Filters;

import javax.print.Doc;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * The type Cake collection.
 */
public class CakeCollection implements Collection {

    private final List<Document> documents;
    private final long id;


    public CakeCollection(List<Document> documents, long id) {
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
        this.documents.stream().filter(var -> var.id() == document.id()).findFirst().ifPresent(current -> {
            for (Entry<String, Object> stringObjectEntry : document.entrySet()) {
                current.append(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        });
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
        long id = CakeDatabase.RANDOM.nextLong();
        return this.find(Filters.id(id)) == null ? id : CakeDatabase.RANDOM.nextLong();
    }

    public void checkDocumentId(Document document) {
        if (document.id() == -999)
            document.setId(this.availableId());
    }

    @Override
    public List<Document> collect() {
        return documents;
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
