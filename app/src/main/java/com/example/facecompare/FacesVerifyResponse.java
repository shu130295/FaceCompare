package com.example.facecompare;

import com.microsoft.projectoxford.face.contract.VerifyResult;

public interface FacesVerifyResponse {
    void onFacesVerified(VerifyResult verifyResult);
}
