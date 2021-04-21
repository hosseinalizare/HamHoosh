package com.example.koohestantest1.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.koohestantest1.databinding.ActivityCompanySettingBinding;
import com.example.koohestantest1.fragments.CompanySettingDetailsFragment;
import com.example.koohestantest1.fragments.CompanySettingHeaderFragment;
import com.example.koohestantest1.viewModel.CompanySettingSharedViewModel;

import java.io.IOException;

public class CompanySettingActivity extends AppCompatActivity {

    private String TAG = CompanySettingActivity.class.getSimpleName();
    private ActivityCompanySettingBinding binding;
    private CompanySettingSharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCompanySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedViewModel = new ViewModelProvider(this).get(CompanySettingSharedViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // callback for picking image in CompanySettingHeaderFragment.java

        //send user to crop page in order to crop image
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            cropRequest(imageUri);
        }


        //image cropped successfully, now pass it to CompanySettingHeaderFragment.java
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                sharedViewModel.setCroppedImage(data);
            }
        }

        Log.d(TAG, "onActivityResult: " + requestCode + " " + resultCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //resetting protected items handler
        CompanySettingHeaderFragment.showProtectedItems = false;
    }

    public void cropRequest(Uri uri) {
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
}