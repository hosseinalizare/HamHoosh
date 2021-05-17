package com.example.koohestantest1.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.adapter.CompanySettingHeaderRecyclerAdapter;
import com.example.koohestantest1.adapter.recyclerinterface.ICompanySettingAdapter;
import com.example.koohestantest1.constants.CompanySettingTypesConstant;
import com.example.koohestantest1.databinding.FragmentCompanySettingHeaderBinding;
import com.example.koohestantest1.model.BodySettingRequest;
import com.example.koohestantest1.model.CompanySetting;
import com.example.koohestantest1.viewModel.CompanySettingSharedViewModel;
import com.example.koohestantest1.viewModel.CompanyViewModel;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class CompanySettingHeaderFragment extends Fragment implements ICompanySettingAdapter {
    private BaseCodeClass baseCodeClass = new BaseCodeClass();
    public static boolean showProtectedItems = false;
    private FragmentCompanySettingHeaderBinding binding;
    private CompanyViewModel companyViewModel;
    private CompanySettingSharedViewModel sharedViewModel;
    private CompanySettingHeaderRecyclerAdapter adapter;
    private String TAG = CompanySettingHeaderFragment.class.getSimpleName() + "Debug";
    private final List<CompanySetting> allData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        companyViewModel = new ViewModelProvider(requireActivity()).get(CompanyViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(CompanySettingSharedViewModel.class);

        adapter = new CompanySettingHeaderRecyclerAdapter(this);

        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompanySettingHeaderBinding.inflate(inflater, container, false);

        binding.rvCompanySettingHeader.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCompanySettingHeader.setAdapter(adapter);

        //call for getting data
        companyViewModel.callForCompanySettings(new BodySettingRequest(BaseCodeClass.userID, BaseCodeClass.CompanyID, BaseCodeClass.token));

        loadCompanyImage();

        companyViewModel.getLiveResCompanySettings().observe(getViewLifecycleOwner(), companySettings -> {

            binding.llCompanyData.setVisibility(View.VISIBLE);
            binding.pbSettings.setVisibility(View.INVISIBLE);

            //do some edit in list
            Set<String> editedData = setUpData(companySettings);
            List<String> finalData = new ArrayList<>(editedData);

            adapter.setUpData(finalData);


            //all data without any change(without removing protectedItems...)
            allData.addAll(companySettings);

        });


        binding.tvChangeCompanyProfile.setOnClickListener(v -> {
            //its callback will call in CompanySettingActivity
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(requireActivity());
        });

        binding.tvPageTitleSettings.setOnLongClickListener(v -> {
            showProtectedItems = true;
            Toast.makeText(requireContext(), "حالت پیشرفته فعال شد!", Toast.LENGTH_SHORT).show();
            Set<String> editedData = setUpData(allData);
            List<String> editedList = new ArrayList<>(editedData);
            adapter.setUpData(editedList);
            return true;
        });

        binding.ivSettingHeaderBack.setOnClickListener(v -> {
            requireActivity().finish();
        });
        Log.d(TAG, "onCreateView: ");
        return binding.getRoot();
    }

    private void loadCompanyImage() {
        String url = baseCodeClass.BASE_URL + "Company/DownloadFile?CompanyID=" + baseCodeClass.getCompanyID() + "&ImageAddress=" + 1;
        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivCompanyProfile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        //on activity result alternative for cropping image
        sharedViewModel.getCroppedImage().observe(getViewLifecycleOwner(), intent -> {
            CropImage.ActivityResult result = CropImage.getActivityResult(intent);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), result.getUri());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //show bitmap in image view
            Glide.with(this).load(bitmap)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.ivCompanyProfile);

            //send image to server
            binding.pbImageUploading.setVisibility(View.VISIBLE);
            try {
                uploadImage(bitmap);
            } catch (IOException e) {
                Log.d(TAG, "onViewCreated: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onHeaderClicked(String title) {
        binding.pbSettings.setVisibility(View.VISIBLE);
        int typeId = CompanySettingTypesConstant.getGroupTypeKey(title);
        List<CompanySetting> companySettings = new ArrayList<>();

        for (int i = 0; i < allData.size(); i++) {
            Log.d(TAG, "onHeaderClicked: " + "class: " + allData.get(i).getGroupType() + " typeId: " + typeId);
            if (allData.get(i).getGroupType() == typeId) {
                Log.d(TAG, "onHeaderClicked: added");
                companySettings.add(allData.get(i));
            }
        }

        sharedViewModel.setFilteredList(companySettings);
        Navigation.findNavController(requireView()).navigate(R.id.action_companySettingHeaderFragment_to_companySettingDetailsFragment);
        //reset latest data in Viewmodel
        companyViewModel.getLiveResCompanySettings().getValue().clear();
        allData.clear();
    }

    public Set<String> setUpData(List<CompanySetting> companySettings) {
        Set<String> listHeader = new LinkedHashSet<>();
        for (CompanySetting companySetting :
                companySettings) {
            int notShowsHeaders = 14;
            int gpType = companySetting.getGroupType();

            if (gpType == notShowsHeaders && !showProtectedItems)
                continue;

            String value = CompanySettingTypesConstant.getGroupTypeTitle(gpType);
            listHeader.add(value);
        }
        return listHeader;
    }

    private void uploadImage(Bitmap bitmap) throws IOException {
        Toast.makeText(requireContext(), "درحال آپلود عکس...", Toast.LENGTH_SHORT).show();
        Cache cache = new Cache(requireContext());
        File file = cache.saveToCacheAndGetFile(bitmap, "photo");
        //create RequestBody instance from file
        RequestBody requestBody = RequestBody.create(MediaType.parse("image"), file);
        Log.d(TAG, "uploadImage: 3");
        MultipartBody.Part body = MultipartBody.Part.createFormData("", "image.jpg", requestBody);

        Log.d(TAG, "uploadImage: 4");
        companyViewModel.getUploadPhotoRes(BaseCodeClass.CompanyID, BaseCodeClass.userID, BaseCodeClass.token, body).observe(getViewLifecycleOwner(), resualt -> {
            Log.d(TAG, "uploadImage: " + resualt.toString());
            if (resualt.getResualt().equals("100")) {
                Toast.makeText(requireContext(), "عکس با موفقیت آپلود شد", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "آپلود عکس موفقیت آمیز نبود!", Toast.LENGTH_SHORT).show();
            }
            binding.pbImageUploading.setVisibility(View.GONE);
        });
    }
}