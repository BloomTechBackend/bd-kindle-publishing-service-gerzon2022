package com.amazon.ata.kindlepublishingservice.lambda;

import com.amazon.ata.kindlepublishingservice.dagger.ApplicationComponent;

import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveBookFromCatalogProvider implements RequestHandler<RemoveBookFromCatalogRequest, RemoveBookFromCatalogResponse> {
    /**
     * Handles a Lambda Function request
     *
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    @Override
    public RemoveBookFromCatalogResponse handleRequest(RemoveBookFromCatalogRequest input, Context context) {
        return getApp().provideRemoveBookFromCatalogActivity().execute(input);
    }
    private ApplicationComponent getApp() {
        ApplicationComponent applicationComponent = null;//DaggerApplicationComponent.create();
        return applicationComponent;
    }
}
