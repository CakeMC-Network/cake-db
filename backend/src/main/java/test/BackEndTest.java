package test;

import net.cakemc.database.CakeDatabase;
import net.cakemc.database.collection.Collection;
import net.cakemc.database.collection.Document;
import net.cakemc.database.collection.Filters;
import net.cakemc.database.collection.impl.CakeDocument;

import java.io.File;
import java.util.UUID;

public class BackEndTest {
    private final static UUID UUID = java.util.UUID.fromString("e6e17702-d148-432e-bfba-e7bcb875028c");

    public static void main(String[] args) {
        CakeDatabase database = new CakeDatabase(new File("./database/"));
        database.load();

        Collection collection = database.getCollection("BedWars");

        Document document = collection.findSingle(Filters.equals("uuid", UUID));
        if(document == null) {
            document = new CakeDocument().append("uuid", UUID).append("kills", 0);
            collection.insetOne(document);
        }

        document.append("kills",12);

        collection.updateOne(document);
        System.out.println(collection.findSingle(Filters.equals("uuid",  UUID)));

        database.save();



        database.save(); // todo automate
    }

}
