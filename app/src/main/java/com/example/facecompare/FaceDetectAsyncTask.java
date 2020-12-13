package com.example.facecompare;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;


public class FaceDetectAsyncTask extends AsyncTask<Bitmap, String, Face[]> {
    private final String apiEndpoint =
            "https://facecomparesub.cognitiveservices.azure.com/face/v1.0";
    private final String subscriptionKey =
            "c77f30ebb02540348e9e9b52f5e4b346";

    public FaceDetectResponse delegate = null;

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    /*FaceDetectAsyncTask(FaceDetectResponse delegate){
        this.delegate = delegate;
    }*/

    @Override
    protected Face[] doInBackground(Bitmap ...bitmaps) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        try {
            Face[] result = faceServiceClient.detect(
                    inputStream,
                    true, // return FaceId
                    false, // return FaceLandmarks
                    null // return FaceAttributes
            );
            /*if (result == null) {
                publishProgress("Detection Finished. Nothing detected.");
                return null;
            }
            publishProgress(String.format("Detection Finished. %d face(s) detected,\n", result.length));
            */return result;
        } catch (Exception e) {
            //exceptionMessage = String.format("Detection failed: %s", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Face[] result){
        this.delegate.onFaceDetected(result);
    }
}
