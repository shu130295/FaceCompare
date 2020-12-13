/*package com.example.facecompare;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

public class FaceApiRestClient {
    private static final String apiEndpoint = "https://facecomparesub.cognitiveservices.azure.com/face/v1.0";
    private static final String subscriptionKey = "c77f30ebb02540348e9e9b52f5e4b346";

    private static UUID faceIdImage1;
    private static UUID faceIdImage2;

    private static int taskCounter = 2;

    private static final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    //private static AsyncHttpClient client = new AsyncHttpClient();

    public static void verify(Bitmap image1, Bitmap image2){
        taskCounter = 2;

        detect(image1);
        detect(image2);
    }

    private static void detect(final Bitmap imageBitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true, // return FaceId
                                    false, // return FaceLandmarks
                                    null // return FaceAttributes
                            );
                            if (result == null) {
                                publishProgress("Detection Finished. Nothing detected.");
                                return null;
                            }
                            publishProgress(String.format("Detection Finished. %d face(s) detected,\n", result.length));
                            return result;
                        } catch (Exception e) {
                            exceptionMessage = String.format("Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }
                    @Override
                    protected void onPostExecute(Face[] result){
                        if (result == null) return;
                        if(taskCounter == 2){
                            faceIdImage1 = result[0].faceId;
                            taskCounter--;
                        } else if(taskCounter == 1){
                            faceIdImage2 = result[0].faceId;
                            startVerification();
                        }
                    }
                };
                detectTask.execute(inputStream);
    }

    private static void startVerification() {
        AsyncTask<Void, Void, Void> verifyTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    VerifyResult verifyResult = faceServiceClient.verify(faceIdImage1, faceIdImage2);

                    if (verifyResult == null)
                        return null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void... voids){
                return null;
            }
        };
        verifyTask.execute();
    }
}
*/