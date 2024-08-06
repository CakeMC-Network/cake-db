package test;

import net.cakemc.database.ZypherDatabase;
import net.cakemc.database.api.Collection;
import net.cakemc.database.api.Document;
import net.cakemc.database.api.Filters;
import net.cakemc.database.api.impl.ZypherDocument;

import java.io.File;

public class BackEndTest {

    public static void main(String[] args) {
        ZypherDatabase database = new ZypherDatabase(new File("./database/"));
        database.load(); // todo automate

        Collection collection = database.getCollection("test");

        if (collection.findSingle(Filters.equals("name", "test")) == null) {
            Document document = new ZypherDocument();
            document.append("name", "test");

            collection.insetOne(document);
        }

        Document found = collection.findSingle(Filters.equals("name", "test"));

        System.out.println(found);

        database.save(); // todo automate
    }

}
