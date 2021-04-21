package com.example.koohestantest1.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koohestantest1.activity.MapsActivity;
import com.example.koohestantest1.adapter.CompanySettingDetailsRecyclerAdapter;
import com.example.koohestantest1.adapter.recyclerinterface.ISettingEditAdapter;
import com.example.koohestantest1.constants.CompanySettingTypesConstant;
import com.example.koohestantest1.constants.SettingValueTypes;
import com.example.koohestantest1.databinding.FragmentCompanySettingDetailsBinding;
import com.example.koohestantest1.fragments.bottomsheet.EditSettingsBottomSheet;
import com.example.koohestantest1.model.CompanySetting;
import com.example.koohestantest1.model.SettingField;
import com.example.koohestantest1.viewModel.CompanySettingSharedViewModel;
import com.example.koohestantest1.viewModel.CompanyViewModel;

import java.util.List;

import com.example.koohestantest1.classDirectory.BaseCodeClass;


public class CompanySettingDetailsFragment extends Fragment implements ISettingEditAdapter {

    private CompanyViewModel companyViewModel;
    private CompanySettingSharedViewModel sharedViewModel;
    private CompanySettingDetailsRecyclerAdapter adapter;
    private FragmentCompanySettingDetailsBinding binding;
    private int latestPosition;
    public static String latestValue = "";
    private List<CompanySetting> settingList;
    public static String KEY_INTENT_STATE_COMPANY_SETTING = "companysetting";
    public static int REQ_CODE_MAP = 12;
    private double resLong = -1;
    private double resLat = -1;
    private int mapPositionInList = -1;
    private int enumId;
    private final ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        int resCode = result.getResultCode();
        switch (resCode) {
            case Activity.RESULT_OK:
                if (result.getData() != null) {
                    resLat = result.getData().getDoubleExtra(MapsActivity.INTENT_KEY_LAT, -1);
                    resLong = result.getData().getDoubleExtra(MapsActivity.INTENT_KEY_LONG, -1);

                    //update ui
                    String destination = resLat + ":" + resLong;
                    settingList.get(mapPositionInList).setValue(destination);
                    adapter.setUpData(settingList);
                    addToServer(destination);
                }
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(requireContext(), "آدرسی انتخاب نشد", Toast.LENGTH_SHORT).show();
                break;
        }
    });

    private void addToServer(String res) {
        companyViewModel.setSettingField(new SettingField(BaseCodeClass.userID, BaseCodeClass.token, BaseCodeClass.CompanyID, res, enumId));
    }

    public CompanySettingDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(CompanySettingSharedViewModel.class);
        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        adapter = new CompanySettingDetailsRecyclerAdapter(this , requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompanySettingDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvSettingDetail.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSettingDetail.setAdapter(adapter);

        sharedViewModel.getFilteredList().observe(getViewLifecycleOwner(), companySettings -> {
            //add dynamic header to toolbar
            String title = CompanySettingTypesConstant.getGroupTypeTitle(companySettings.get(0).getGroupType());
            binding.tvPageTitle.setText(title);

            //remove protected items
            for (int i = 0; i < companySettings.size(); i++) {
                if (companySettings.get(i).isProtectedOption()) {
                    if (!CompanySettingHeaderFragment.showProtectedItems) {
                        companySettings.remove(i);
                    }
                }
            }

            settingList = companySettings;
            adapter.setUpData(settingList);
        });

        sharedViewModel.getUpdateData().observe(getViewLifecycleOwner(), integer -> {
            if (integer > -1) {
                settingList.get(integer).setValue(latestValue);
                adapter.setUpData(settingList);
                sharedViewModel.setUpdateData(-1);
            }
        });
        binding.ivSettingDetailBack.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    @Override
    public void onEditClicked(CompanySetting companySetting, int position) {
        if (companySetting.getValueType() == SettingValueTypes.location) {
            String[] values = companySetting.getValue().split(":");
            Intent intent = new Intent(requireContext(), MapsActivity.class);
            intent.putExtra(KEY_INTENT_STATE_COMPANY_SETTING, true);
            intent.putExtra("resLat", Double.parseDouble(values[0]));
            intent.putExtra("resLong", Double.parseDouble(values[1]));
            mapPositionInList = position;
            enumId = companySetting.getEnumId();
            intentActivityResultLauncher.launch(intent);
        } else {
            latestPosition = position;
            sharedViewModel.setSelectedSetting(companySetting);
            EditSettingsBottomSheet editSettingsBottomSheet = new EditSettingsBottomSheet(position);
            editSettingsBottomSheet.show(getChildFragmentManager(), editSettingsBottomSheet.getTag());
        }
    }

}