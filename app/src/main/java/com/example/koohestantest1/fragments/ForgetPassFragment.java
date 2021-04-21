package com.example.koohestantest1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koohestantest1.R;
import com.example.koohestantest1.databinding.FragmentForgetPassBinding;
import com.example.koohestantest1.model.SmsRecoveryBody;
import com.example.koohestantest1.viewModel.AuthorizationViewModel;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.HardwareIdsMobile;


public class ForgetPassFragment extends Fragment {

    private AuthorizationViewModel authorizationViewModel;
    private FragmentForgetPassBinding binding;
    private String TAG = ForgetPassFragment.class.getSimpleName();

    public ForgetPassFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authorizationViewModel = new ViewModelProvider(requireActivity()).get(AuthorizationViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgetPassBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSendVerification.setOnClickListener(v -> {
            String phoneNumber = binding.edtUserPhone.getText().toString().trim();
            BaseCodeClass.mobileNumber = phoneNumber;
            HardwareIdsMobile hardwareIdsMobile = new HardwareIdsMobile(requireContext());
            boolean permission = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                permission = hardwareIdsMobile.getMobileConfig();
            } else
                permission = true;

            if (permission) {
                authorizationViewModel.getRecoveryCode(new SmsRecoveryBody(BaseCodeClass.mobileNumber, BaseCodeClass.IMEI, BaseCodeClass.deviceModel));
            }

            Log.d(TAG, "onCreate: phoneNumber " + BaseCodeClass.mobileNumber + " BaseCodeClass.IMEI" + BaseCodeClass.IMEI + " " + BaseCodeClass.deviceModel);
        });

        authorizationViewModel.getLiveResSendCode().observe(getViewLifecycleOwner(), resualt -> {
            if (resualt != null) {
                if (resualt.getResualt().equals("100")) {
                    Navigation.findNavController(view).navigate(R.id.action_forgetPassFragment_to_authorizeCodeFragment);
                    authorizationViewModel.clearLivePhoneSendCode();
                    Toast.makeText(requireContext(), "کد تایید ارسال شد", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}