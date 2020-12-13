package com.example.facecompare;

import com.microsoft.projectoxford.face.contract.Face;

public interface FaceDetectResponse {
    void onFaceDetected(Face[] faces);
}
