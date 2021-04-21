package com.example.koohestantest1.viewModel;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.CompanySetting;

import java.util.List;

public class CompanySettingSharedViewModel extends ViewModel {
    private final MutableLiveData<List<CompanySetting>> filteredList = new MutableLiveData<>();
    private final MutableLiveData<CompanySetting> selectedSetting = new MutableLiveData<>();

    private final MutableLiveData<Integer> updateData = new MutableLiveData<>();

    private final MutableLiveData<Intent> croppedImage = new MutableLiveData<>();

    public void setFilteredList(List<CompanySetting> data) {
        filteredList.setValue(data);
    }

    public void setSelectedSetting(CompanySetting companySetting) {
        selectedSetting.setValue(companySetting);
    }

    public void setCroppedImage(Intent intent){
        croppedImage.setValue(intent);
    }

    public void setUpdateData(int pos) {
        updateData.setValue(pos);
    }

    public LiveData<List<CompanySetting>> getFilteredList() {
        return filteredList;
    }

    public LiveData<CompanySetting> getSelectedSetting() {
        return selectedSetting;
    }

    public LiveData<Integer> getUpdateData() {
        return updateData;
    }

    public LiveData<Intent> getCroppedImage() {
        return croppedImage;
    }
}
