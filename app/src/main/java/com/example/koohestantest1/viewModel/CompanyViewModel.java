package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.BodySettingRequest;
import com.example.koohestantest1.model.CompanySetting;
import com.example.koohestantest1.model.EmployeeAdding;
import com.example.koohestantest1.model.EmployeeDeleting;
import com.example.koohestantest1.model.SettingField;
import com.example.koohestantest1.model.repository.CompanyRepository;
import com.example.koohestantest1.model.repository.callback.CompanyCallBacks;
import com.example.koohestantest1.model.repository.callback.CompanyCustomersCallBack;
import com.example.koohestantest1.model.repository.callback.CompanySettingCallBacks;
import com.example.koohestantest1.model.repository.callback.DeletingEmployeeCallBacks;
import com.example.koohestantest1.model.repository.callback.EditSettingCallBack;

import java.util.List;

import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import okhttp3.MultipartBody;

public class CompanyViewModel extends ViewModel implements CompanyCallBacks, CompanyCustomersCallBack, DeletingEmployeeCallBacks, CompanySettingCallBacks, EditSettingCallBack {
    private MutableLiveData<GetResualt> addEmployeeResLive = new MutableLiveData<>();
    private MutableLiveData<String> errorLive = new MutableLiveData<>();
    private MutableLiveData<List<CompanyFollowerViewModel>> liveCompanyCustomers = new MutableLiveData<>();
    private MutableLiveData<String> liveErrorCompanyCustomers = new MutableLiveData<>();
    private MutableLiveData<GetResualt> liveResDeleting = new MutableLiveData<>();
    private MutableLiveData<String> liveErrorDeleting = new MutableLiveData<>();

    private final MutableLiveData<GetResualt> liveResEditSetting = new MutableLiveData<>();

    private CompanyRepository companyRepository;

    private MutableLiveData<List<CompanySetting>> liveResCompanySettings = new MutableLiveData<>();

    public CompanyViewModel() {
        companyRepository = new CompanyRepository(this, this, this, this);
    }

    public void callForAddingEmployeeToCo(EmployeeAdding employeeAdding) {
        companyRepository.callForAddingEmployeeToCo(employeeAdding);
    }

    public void callForDeletingEmployee(EmployeeDeleting employeeDeleting) {
        companyRepository.deleteEmployee(employeeDeleting);
    }

    public void callForCompanyCustomers(String companyName) {
        companyRepository.callCompanyCustomers(companyName);
    }

    public void callForCompanySettings(BodySettingRequest bodySettingRequest) {
        companyRepository.callForGettingCompanySettings(bodySettingRequest);
    }

    public void setSettingField(SettingField settingField) {
        companyRepository.setSettingField(settingField, this);
    }

    public LiveData<GetResualt> getUploadPhotoRes(String companyId, String userId, String token, MultipartBody.Part part) {
        return LiveDataReactiveStreams.fromPublisher(companyRepository.uploadCompanyImage(companyId, userId, token, part));
    }

    public LiveData<GetResualt> getDeletingRes() {
        return liveResDeleting;
    }


    public LiveData<String> getErrorCustomers() {
        return liveErrorCompanyCustomers;
    }

    public LiveData<List<CompanyFollowerViewModel>> getCustomers() {
        return liveCompanyCustomers;
    }

    public LiveData<String> getErrorLive() {
        return errorLive;
    }

    public LiveData<GetResualt> getAddEmployeeResLive() {
        return addEmployeeResLive;
    }

    public LiveData<List<CompanySetting>> getLiveResCompanySettings() {
        return liveResCompanySettings;
    }


    public LiveData<GetResualt> getLiveResEditSetting() {
        return liveResEditSetting;
    }

    @Override
    public void onSuccess(GetResualt result) {
        addEmployeeResLive.setValue(result);
    }

    @Override
    public void onError(String error) {
        errorLive.setValue(error);
    }

    @Override
    public void onCustomersSuccess(List<CompanyFollowerViewModel> companyCustomers) {
        liveCompanyCustomers.setValue(companyCustomers);
    }

    @Override
    public void onCustomersError(String error) {
        liveErrorCompanyCustomers.setValue(error);
    }

    @Override
    public void onSuccessDeleting(GetResualt resualt) {
        liveResDeleting.setValue(resualt);
    }

    @Override
    public void onErrorDeleting(String error) {
        liveErrorDeleting.setValue(error);
    }

    @Override
    public void onSuccessSettings(List<CompanySetting> companySettings) {
        liveResCompanySettings.setValue(companySettings);
    }


    @Override
    public void onEditSuccessful(GetResualt res) {
        liveResEditSetting.setValue(res);
    }
}
