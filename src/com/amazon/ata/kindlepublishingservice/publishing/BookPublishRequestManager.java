package com.amazon.ata.kindlepublishingservice.publishing;

import java.util.LinkedList;

import java.util.Queue;

public class BookPublishRequestManager {

    Queue<BookPublishRequest> bookPublishRequests = new LinkedList<>();

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest){
        bookPublishRequests.add(bookPublishRequest);
    }
    public BookPublishRequest getBookPublishRequestToProcess() {
        if (bookPublishRequests.isEmpty())
            return null;

        return bookPublishRequests.remove();
    }
}
