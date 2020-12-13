package com.example.facecompare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements FaceDetectResponse, FacesVerifyResponse {

    ImageView imageView1;
    ImageView imageView2;
    Button image1Btn;
    Button image2Btn;
    Button compareBtn;

    private static int RESULT_LOAD_IMAGE_1 = 1;
    private static  int RESULT_LOAD_IMAGE_2 = 2;
    private static int imagesToBeDetected = 2;

    UUID faceId1, faceId2;

    FaceDetectAsyncTask faceDetectAsyncTask1 = new FaceDetectAsyncTask();
    FaceDetectAsyncTask faceDetectAsyncTask2 = new FaceDetectAsyncTask();
    FacesVerifyAsyncTask facesVerifyAsyncTask = new FacesVerifyAsyncTask();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image1Btn = findViewById(R.id.image1Btn);
        image2Btn = findViewById(R.id.image2Btn);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        compareBtn = findViewById(R.id.compareBtn);

        faceDetectAsyncTask1.delegate = this;
        faceDetectAsyncTask2.delegate = this;
        facesVerifyAsyncTask.delegate = this;

        progressDialog = new ProgressDialog(this);

        image1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToLoadImage(RESULT_LOAD_IMAGE_1);
            }
        });

        image2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToLoadImage(RESULT_LOAD_IMAGE_2);
            }
        });

        compareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Comparing");
                progressDialog.show();
                Bitmap bm1 = ((BitmapDrawable) imageView1.getDrawable()).getBitmap();
                Bitmap bm2 = ((BitmapDrawable) imageView2.getDrawable()).getBitmap();
                faceDetectAsyncTask1.execute(bm1);
                faceDetectAsyncTask2.execute(bm2);
                //new FaceDetectAsyncTask(this).execute(bm1);
                //new FaceDetectAsyncTask(this).execute(bm2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE_1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageView1.setImageBitmap(getBitmap(data.getData()));
        }
        else if(requestCode == RESULT_LOAD_IMAGE_2 && resultCode == RESULT_OK && data != null) {
            imageView2.setImageBitmap(getBitmap(data.getData()));
        }
    }

    private Bitmap getBitmap(Uri selectedImage){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            return bitmap;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void intentToLoadImage(int imageNum){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), imageNum);
    }

    @Override
    public void onFaceDetected(Face[] result) {
        if(result == null || result.length == 0){
            stopProgressDialog();
            AlertUtil.showOkDialog(this, "Error","The image does not contain faces.");
        }
        else if(result.length > 1){
            stopProgressDialog();
            AlertUtil.showOkDialog(this,
                    "Error","The image contains more than 1 face.");
        }
        else {
            if(imagesToBeDetected == 2){
                faceId2 = result[0].faceId;
            } else if(imagesToBeDetected == 1){
                faceId1 = result[0].faceId;
            }
            imagesToBeDetected--;
            if(imagesToBeDetected == 0)
            {
                facesVerifyAsyncTask.execute(faceId1, faceId2);
            }
        }
    }

    @Override
    public void onFacesVerified(VerifyResult verifyResult) {
        stopProgressDialog();
        AlertUtil.showOkDialog(this,
                "Success", String.format("Both faces are %.0f%% same.",verifyResult.confidence*100));
    }

    private void stopProgressDialog() {
        if(this.progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
