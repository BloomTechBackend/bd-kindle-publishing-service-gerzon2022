package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import dagger.Module;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BookPublishTask implements Runnable{
    PublishingStatusDao publishingStatusDao;
    CatalogDao catalogDao;

    private static  BookPublishRequestManager bookPublishRequestManager;

    @Inject
    public BookPublishTask( PublishingStatusDao publishingStatusDao,  CatalogDao catalogDao, BookPublishRequestManager bookPublishRequestManager) {

        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
        this.bookPublishRequestManager = bookPublishRequestManager;
        System.out.println("bpt created! with manager + " + bookPublishRequestManager);
        System.out.println("created BookPublishTask " + System.identityHashCode(bookPublishRequestManager.getBookPublishRequests()));
    }

    @Override
    public void run() {
        System.out.println("bpt running!" + bookPublishRequestManager);
        //bookPublishRequest = BookPublishRequestConverter.toBookPublishRequest(request);
        //System.out.println("processing "+ bookPublishRequestManager.getBookPublishRequestToProcess().getPublishingRecordId() +"books...");
        BookPublishRequest  bookPublishRequest = bookPublishRequestManager.getBookPublishRequestToProcess();
            System.out.println("hey "+ bookPublishRequest);


        //System.out.println("bpt processing "+ bookPublishRequest.getPublishingRecordId() +" books...");

        if (bookPublishRequest != null) {
            publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS,
                    bookPublishRequest.getBookId());

            try {
                KindleFormattedBook kindleFormattedBook = KindleFormatConverter.format(bookPublishRequest);
                CatalogItemVersion result =  catalogDao.createOrUpdateBook(kindleFormattedBook);
                publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.SUCCESSFUL,
                        result.getBookId());

            } catch (Exception e) {
                System.out.println(e);
                publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED,
                        bookPublishRequest.getBookId(), "not successful!");
                catalogDao.delete(bookPublishRequest.getBookId());
            }

        } else {
            System.out.println("Nothing to process!");
        }
    }
}
