package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazon.ata.kindlepublishingservice.models.response.SubmitBookForPublishingResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetPublishingStatusActivity {
    PublishingStatusDao publishingStatusDao;
    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;

    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        List<PublishingStatusItem> publishingStatusItems = new ArrayList<>();
        String tgetPrecordID = publishingStatusRequest.getPublishingRecordId();
        publishingStatusItems = publishingStatusDao.getPublishingStatus(publishingStatusRequest.getPublishingRecordId());
        if (publishingStatusItems == null)
            throw new PublishingStatusNotFoundException("No status found");

        List<PublishingStatusRecord> publishingStatusRecords = new ArrayList<>();

       for(PublishingStatusItem status : publishingStatusItems )
           publishingStatusRecords.add(new PublishingStatusRecord(PublishingStatusRecord.builder()
                   .withStatus(status.getStatus().toString())
                   .withStatusMessage(status.getStatusMessage())
                   .withBookId(status.getBookId())
           ));

        return GetPublishingStatusResponse.builder().withPublishingStatusHistory(publishingStatusRecords).build();

    }
}
