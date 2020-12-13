package com.example.facecompare;

import android.os.AsyncTask;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.VerifyResult;

import java.util.UUID;

public class FacesVerifyAsyncTask extends AsyncTask<UUID, Void, VerifyResult> {

    private final String apiEndpoint =
            "https://facecomparesub.cognitiveservices.azure.com/face/v1.0";
    private final String subscriptionKey =
            "c77f30ebb02540348e9e9b52f5e4b346";

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    public FacesVerifyResponse delegate = null;

    /*FacesVerifyAsyncTask(FacesVerifyResponse delegate){
        this.delegate = delegate;
    }*/

    @Override
    protected VerifyResult doInBackground(UUID... uuid) {
        try {
            VerifyResult verifyResult = faceServiceClient.verify(uuid[0], uuid[1]);
            return verifyResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(VerifyResult result){
        this.delegate.onFacesVerified(result);
    }
}
