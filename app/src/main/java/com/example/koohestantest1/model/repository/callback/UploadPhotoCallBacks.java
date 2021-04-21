package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.classDirectory.GetResualt;

public interface UploadPhotoCallBacks {
    void onSuccess(GetResualt resualt);
    void onError(String s);
}
