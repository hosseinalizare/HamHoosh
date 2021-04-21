package com.example.koohestantest1.fragments.bottomsheet;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.koohestantest1.databinding.BottomSheetFragmentCompanySettingBinding;
import com.example.koohestantest1.fragments.CompanySettingDetailsFragment;
import com.example.koohestantest1.model.SettingField;
import com.example.koohestantest1.viewModel.CompanySettingSharedViewModel;
import com.example.koohestantest1.viewModel.CompanyViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static com.example.koohestantest1.constants.SettingValueTypes.*;


import java.util.Calendar;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;


public class EditSettingsBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetFragmentCompanySettingBinding binding;
    private CompanyViewModel companyViewModel;
    private CompanySettingSharedViewModel sharedViewModel;
    private final String TAG = EditSettingsBottomSheet.class.getSimpleName();
    private int currentValueType = -1;
    private int itemId = -1;
    private String value;
    private int pos;

    public EditSettingsBottomSheet() {

    }

    public EditSettingsBottomSheet(int position) {
        pos = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(CompanySettingSharedViewModel.class);
        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetFragmentCompanySettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel.getSelectedSetting().observe(getViewLifecycleOwner(), companySetting -> {
            Log.d(TAG, "onViewCreated: " + companySetting.getValue());

            binding.tvSettingTitle.setText(companySetting.getTitle());
            binding.tvExplainSetting.setText(companySetting.getExplain());

            currentValueType = companySetting.getValueType();
            itemId = companySetting.getEnumId();
            SetUpValue(companySetting.getValue(), currentValueType);
        });

        companyViewModel.getLiveResEditSetting().observe(getViewLifecycleOwner(), resualt -> {
            if (resualt.getResualt().equals("100")) {
                dismiss();
                Toast.makeText(requireContext(), "عملیات موفقیت آمیز بود", Toast.LENGTH_SHORT).show();

                //share data for update parent view
                CompanySettingDetailsFragment.latestValue = value;
                sharedViewModel.setUpdateData(pos);
            }
        });

        binding.btnChooseDate.setOnClickListener(v -> {
            PersianDatePickerDialog picker = new PersianDatePickerDialog(requireContext())
                    .setPositiveButtonString("باشه")
                    .setNegativeButton("بیخیال")
                    .setTodayButton("امروز")
                    .setTodayButtonVisible(true)
                    .setMinYear(1300)
                    .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                    .setActionTextColor(Color.GRAY)
                    .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                    .setShowInBottomSheet(true)
                    .setListener(new Listener() {
                        @Override
                        public void onDateSelected(PersianCalendar persianCalendar) {
                            Log.d(TAG, "onDateSelected: " + persianCalendar.getGregorianChange());//Fri Oct 15 03:25:44 GMT+04:30 1582
                            Log.d(TAG, "onDateSelected: " + persianCalendar.getTimeInMillis());//1583253636577
                            Log.d(TAG, "onDateSelected: " + persianCalendar.getTime());//Tue Mar 03 20:10:36 GMT+03:30 2020
                            Log.d(TAG, "onDateSelected: " + persianCalendar.getDelimiter());//  /
                            Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDate());// سه‌شنبه  13  اسفند  1398
                            Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDateAndTime()); //سه‌شنبه  13  اسفند  1398 ساعت 20:10:36
                            Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianMonthName()); //اسفند
                            Log.d(TAG, "onDateSelected: " + persianCalendar.isPersianLeapYear());//false

                            binding.tvSettingDate.setText(persianCalendar.getPersianLongDate());
                        }

                        @Override
                        public void onDismissed() {

                        }
                    });

            picker.show();
        });

        binding.ivRangeStart.setOnClickListener(v -> {
            TimePickerFragment timePickerFragment = new TimePickerFragment(true, binding.tvRangeStart, binding.tvRangeEnd);
            timePickerFragment.show(getChildFragmentManager(), timePickerFragment.getTag());
        });

        binding.ivRangeEnd.setOnClickListener(v -> {
            TimePickerFragment timePickerFragment = new TimePickerFragment(false, binding.tvRangeStart, binding.tvRangeEnd);
            timePickerFragment.show(getChildFragmentManager(), timePickerFragment.getTag());
        });

        binding.ivCloseEditSetting.setOnClickListener(v -> dismiss());

        binding.ivDoneEditSetting.setOnClickListener(v -> {
            value = null;
            switch (currentValueType) {
                case bool:
                    if (binding.rbActive.isChecked())
                        value = "True";
                    else if (binding.rbDiactive.isChecked())
                        value = "False";
                    break;
                case rangeDate:
                    value = binding.tvRangeStart.getText().toString() + "_" + binding.tvRangeEnd.getText().toString();
                    break;
                default:
                    value = binding.edtValueSetting.getText().toString();
            }
            if (value != null || !value.equals(""))
                companyViewModel.setSettingField(new SettingField(BaseCodeClass.userID, BaseCodeClass.token, BaseCodeClass.CompanyID, value, itemId));
            else
                Toast.makeText(requireContext(), "فیلدی نمیتواند خالی باشد", Toast.LENGTH_SHORT).show();
        });
    }

    private void SetUpValue(String value, int valueType) {

        switch (valueType) {
            case string:
            case city:
            case province:
                binding.edtValueSetting.setVisibility(View.VISIBLE);
                binding.edtValueSetting.setText(value);
                break;
            case numberInt:
                binding.edtValueSetting.setVisibility(View.VISIBLE);
                binding.edtValueSetting.setText(value);
                binding.edtValueSetting.setInputType(InputType.TYPE_CLASS_NUMBER);

                break;
            case date:
                binding.llChooseSettingDate.setVisibility(View.VISIBLE);
                binding.tvSettingDate.setText(value);
                break;
            case bool:
                binding.rgState.setVisibility(View.VISIBLE);
                if (value.equals("True"))
                    binding.rbActive.setChecked(true);
                else if (value.equals("False"))
                    binding.rbDiactive.setChecked(true);
                break;
            case website:
                binding.edtValueSetting.setVisibility(View.VISIBLE);
                binding.edtValueSetting.setText(value);
                binding.edtValueSetting.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                break;
            case email:
                binding.edtValueSetting.setVisibility(View.VISIBLE);
                binding.edtValueSetting.setText(value);
                binding.edtValueSetting.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case mobile:
                binding.edtValueSetting.setVisibility(View.VISIBLE);
                binding.edtValueSetting.setText(value);
                binding.edtValueSetting.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case rangeDate:
                binding.llChooseSettingRange.setVisibility(View.VISIBLE);
                String[] range = value.split("_");
                binding.tvRangeStart.setText(range[0]);
                binding.tvRangeEnd.setText(range[1]);
                break;
            default:
                Toast.makeText(requireContext(), "" + valueType, Toast.LENGTH_SHORT).show();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private boolean started;
        private TextView tvStart, tvEnd;

        public TimePickerFragment(boolean started, TextView start, TextView end) {
            this.started = started;
            tvStart = start;
            tvEnd = end;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            if (started)
                tvStart.setText(hourOfDay + ":" + minute);
            else tvEnd.setText(hourOfDay + ":" + minute);

        }
    }
}
