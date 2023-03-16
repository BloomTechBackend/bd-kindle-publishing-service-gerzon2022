package com.amazon.ata.kindlepublishingservice.publishing;



import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public  class BookPublishRequestManager {
    private static Queue<BookPublishRequest> bookPublishRequests ;
    @Inject
    public BookPublishRequestManager(ConcurrentLinkedQueue<BookPublishRequest> bookPublishRequests) {
        System.out.println("created original manager " + System.identityHashCode(bookPublishRequests));
        this.bookPublishRequests = bookPublishRequests;

    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest){
        System.out.println("adding item to queue " + bookPublishRequests);
        System.out.println("adding to manager " + System.identityHashCode(bookPublishRequests));
        System.out.println("adding "+ bookPublishRequest.getPublishingRecordId() );
        bookPublishRequests.add(bookPublishRequest);
        System.out.println("added "+ bookPublishRequests.size() );
    }
    public BookPublishRequest getBookPublishRequestToProcess() {
        System.out.println("removing item " + bookPublishRequests );
        System.out.println("removing to manager " + System.identityHashCode(bookPublishRequests));
        if (bookPublishRequests.isEmpty()) {
            System.out.println("no item to remove " );
            return null;
        }
        BookPublishRequest bookPublishRequest = bookPublishRequests.remove();
        System.out.println("removing .. " + bookPublishRequest.getPublishingRecordId());
        return bookPublishRequest;
    }

    public Queue<BookPublishRequest> getBookPublishRequests() {
        System.out.println("getting from manager " + System.identityHashCode(bookPublishRequests));
        return bookPublishRequests;
    }
}
