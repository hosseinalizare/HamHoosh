package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.BodyVerifiedCode;
import com.example.koohestantest1.model.NewPasswordBody;
import com.example.koohestantest1.model.SmsRecoveryBody;
import com.example.koohestantest1.model.repository.AuthorizationRepository;
import com.example.koohestantest1.model.repository.callback.AuthorizationCallBack;

import com.example.koohestantest1.classDirectory.CheckVerification;
import com.example.koohestantest1.classDirectory.GetResualt;

public class AuthorizationViewModel extends ViewModel implements AuthorizationCallBack {
    private final AuthorizationRepository authorizationRepository = new AuthorizationRepository(this);

    private final MutableLiveData<GetResualt> liveResSendCode = new MutableLiveData<>();

    private final MutableLiveData<CheckVerification> liveResVerificationCode = new MutableLiveData<>();

    private final MutableLiveData<String> liveSharedCode = new MutableLiveData<>();

    private final MutableLiveData<GetResualt> liveChangingPassRes = new MutableLiveData<>();

    public void setLiveSharedCode(String code){
        liveSharedCode.setValue(code);
    }

    public void getRecoveryCode(SmsRecoveryBody smsRecoveryBody) {
        authorizationRepository.getSmsCode(smsRecoveryBody);
    }

    public void setNewPassword(NewPasswordBody newPassword) {
        authorizationRepository.setNewPassword(newPassword);
    }

    public void checkVerificationCode(BodyVerifiedCode bodyVerifiedCode) {
        authorizationRepository.checkVerifiedCode(bodyVerifiedCode);
    }

    public LiveData<GetResualt> getLiveResSendCode() {
        return liveResSendCode;
    }


    @Override
    public void onSuccessSms(GetResualt res) {
        liveResSendCode.setValue(res);
    }

    @Override
    public void onSuccessPassword(GetResualt res) {
        liveChangingPassRes.setValue(res);
    }

    @Override
    public void onSuccessVerifiedCode(CheckVerification res) {
        liveResVerificationCode.setValue(res);
    }

    public LiveData<CheckVerification> getLiveResVerificationCode() {
        return liveResVerificationCode;
    }

    public void clearLivePhoneSendCode() {
        liveResSendCode.setValue(null);
    }

    public LiveData<String> getLiveSharedCode() {
        return liveSharedCode;
    }

    public LiveData<GetResualt> getLiveChangingPassRes() {
        return liveChangingPassRes;
    }
}
