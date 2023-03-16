package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.activity.SubmitBookForPublishingActivity;
import com.amazon.ata.kindlepublishingservice.converters.BookPublishRequestConverter;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.models.requests.SubmitBookForPublishingRequest;
import com.amazon.ata.kindlepublishingservice.models.response.SubmitBookForPublishingResponse;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BookPublishTaskTest {
    PublishingStatusDao publishingStatusDao;
    CatalogDao catalogDao;
    BookPublishRequestManager bookPublishRequestManager;

    private SubmitBookForPublishingActivity activity;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }
    @Test
    void testBookPublishTastTest() throws Exception {


    SubmitBookForPublishingRequest request = SubmitBookForPublishingRequest.builder()
            .withAuthor("Author")
            .withTitle("Title")
            .withGenre(BookGenre.FANTASY.name())
            .build();

    PublishingStatusItem item = new PublishingStatusItem();
        item.setPublishingRecordId("publishing.123");
    when(publishingStatusDao.setPublishingStatus(anyString(),
    eq(PublishingRecordStatus.QUEUED),
    isNull())).thenReturn(item);

    // WHEN
    SubmitBookForPublishingResponse response = activity.execute(request);
       // BookPublishRequest bookPublishRequest = BookPublishRequestConverter.toBookPublishRequest(request);
        //BookPublishRequestManager bookPublishRequestManager = new BookPublishRequestManager();
        //bookPublishRequestManager.addBookPublishRequest(bookPublishRequest);

    // THEN
    assertEquals("publishing.123", response.getPublishingRecordId(), "Expected response to return a publishing" +
            "record id.");
    }
}

