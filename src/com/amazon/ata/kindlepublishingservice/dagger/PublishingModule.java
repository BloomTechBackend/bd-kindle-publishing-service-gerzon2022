package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.App;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequestManager;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishTask;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublisher;


import dagger.Module;
import dagger.Provides;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;

@Module
public class PublishingModule {
    @Provides
    @Singleton
    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService, PublishingStatusDao publishingStatusDao,
                                              CatalogDao catalogDao) {
        return new BookPublisher(scheduledExecutorService, App.component.provideBookPublishTask());
    }

    @Provides
    @Singleton
    public ScheduledExecutorService provideBookPublisherScheduler() {
        return Executors.newScheduledThreadPool(1);
    }

    @Provides
    @Singleton
    public static ConcurrentLinkedQueue<BookPublishRequest> provideBookPublisherSchedulerBookPublishRequests() {
        ConcurrentLinkedQueue<BookPublishRequest> queue = new ConcurrentLinkedQueue<>();
        System.out.println("queue create is " + System.identityHashCode(queue));
        return queue;
    }

    @Provides
    @Singleton
    public  static final BookPublishRequestManager provideBookPublishRequestManager() {
        return new BookPublishRequestManager(provideBookPublisherSchedulerBookPublishRequests());
    }




}
