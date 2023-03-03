package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class RemoveBookFromCatalogActivity {
    private CatalogDao catalogDao;
    @Inject
    RemoveBookFromCatalogActivity( CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }
    public RemoveBookFromCatalogResponse execute(RemoveBookFromCatalogRequest removeBookFromCatalogRequest) {
        //try {

           catalogDao.delete(removeBookFromCatalogRequest.getBookId());
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        return new RemoveBookFromCatalogResponse.Builder()
                .withBookId(removeBookFromCatalogRequest.getBookId())
                .build();
    }
}
