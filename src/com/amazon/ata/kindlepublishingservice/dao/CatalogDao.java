package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);
//        book.setBookId(bookId);
//
//        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
//                .withHashKeyValues(book)
//                .withLimit(1);
//
//        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
//        if (results.isEmpty() || results.get(0).isInactive()) {
//            throw new BookNotFoundException("no book found!");
//        }
//
//        System.out.println("get from catalog book?" + results.get(0).isInactive());
        return book;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty() || results.get(0).isInactive()) {
            return null;
        }
        return results.get(0);
    }

    public CatalogItemVersion delete(String bookId) {

        CatalogItemVersion book = getBookFromCatalog(bookId);
        book.setInactive(true);
//        Map expected = new HashMap<>();
//        expected.put("inactive", new ExpectedAttributeValue());
//        DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression();
//        deleteExpression.setExpected(expected);
        dynamoDbMapper.save(book);
        return book;
    }
    public CatalogItemVersion save(CatalogItemVersion book) {

//        Map expected = new HashMap<>();
//        expected.put("inactive", new ExpectedAttributeValue());
//        DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression();
//        deleteExpression.setExpected(expected);
        dynamoDbMapper.save(book);
        return book;
    }
    public CatalogItemVersion validateBookExists(String bookId) {

        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
        return book;

    }
    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook kindleFormattedBook) {
        CatalogItemVersion result = null;
        System.out.println(kindleFormattedBook.getBookId() + "is null ?");
        if (kindleFormattedBook.getBookId() == null) {
            System.out.println(kindleFormattedBook.getBookId() + "it is null ");
            String generateBookId = KindlePublishingUtils.generateBookId();
            CatalogItemVersion book = new CatalogItemVersion();
            book.setBookId(generateBookId);
            book.setAuthor(kindleFormattedBook.getAuthor());
            book.setText(kindleFormattedBook.getText());
            book.setGenre(kindleFormattedBook.getGenre());
            book.setTitle(kindleFormattedBook.getTitle());
            book.setVersion(1);
            book.setInactive(false);
            try {
                save(book);
                result = book;
            } catch (Exception e ) {
                System.out.println(e + "null error?");
            }
        } else {
            CatalogItemVersion oldBook = getLatestVersionOfBook(kindleFormattedBook.getBookId());
            System.out.println("old book?" +oldBook.getBookId());
            if(oldBook != null) {

                int oldVersion = oldBook.getVersion();
                //oldBook.setBookId(kindleFormattedBook.getBookId());
                try {
                    oldBook.setInactive(true);
                    save(oldBook);
                } catch (Exception e ) {
                    System.out.println(e);
                }

                int newVersion = oldVersion + 1;
                String newBookId = KindlePublishingUtils.generateBookId();
                CatalogItemVersion newBook = new CatalogItemVersion();
                newBook.setBookId(newBookId);
                newBook.setVersion(newVersion);
                newBook.setInactive(false);
                newBook.setTitle(kindleFormattedBook.getTitle());
                newBook.setText(kindleFormattedBook.getText());
                newBook.setGenre(kindleFormattedBook.getGenre());
                newBook.setAuthor(kindleFormattedBook.getAuthor());
                try {
                    save(newBook);
                    result = newBook;
                } catch (Exception e ) {
                    System.out.println(e);
                }


                System.out.println("done "+ result.getVersion() );

            } else {

                    System.out.println("what? new" );
                    String generateBookId = KindlePublishingUtils.generateBookId();
                    CatalogItemVersion book = new CatalogItemVersion();
                    book.setBookId(generateBookId);
                    book.setAuthor(kindleFormattedBook.getAuthor());
                    book.setText(kindleFormattedBook.getText());
                    book.setGenre(kindleFormattedBook.getGenre());
                    book.setTitle(kindleFormattedBook.getTitle());
                    book.setVersion(1);
                    book.setInactive(false);
                    try {
                        save(book);
                        result = book;
                    } catch (Exception e ) {
                        System.out.println(e);
                    }


                }

        }



        System.out.println("oldnewversion catag"+ result.getBookId());
        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(result)
                //.withScanIndexForward(false)
                .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            throw new BookNotFoundException("Book not in Catalog");
        }
        for(CatalogItemVersion c : results)
            System.out.println("CatalogItemVersion "+ c.getVersion() + " " +c.getBookId());
        System.out.println("oldnewversion catag"+ result.getBookId());
        return results.get(0);
    }


}
