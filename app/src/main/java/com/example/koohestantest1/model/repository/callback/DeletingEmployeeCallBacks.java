package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.classDirectory.GetResualt;

public interface DeletingEmployeeCallBacks {
    void onSuccessDeleting(GetResualt resualt);

    void onErrorDeleting(String error);
}
