package com.example.koohestantest1.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koohestantest1.databinding.FragmentChangePasswordBinding;
import com.example.koohestantest1.model.NewPasswordBody;
import com.example.koohestantest1.viewModel.AuthorizationViewModel;

import com.example.koohestantest1.classDirectory.BaseCodeClass;


public class ChangePasswordFragment extends Fragment {

    private AuthorizationViewModel authorizationViewModel;
    private FragmentChangePasswordBinding binding;
    private String code;
    private String TAG = ChangePasswordFragment.class.getSimpleName();

    public ChangePasswordFragment() {
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
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnSaveNewPass.setOnClickListener(v -> {
            String pass = binding.edtChangePass.getText().toString();
            if (pass.length() < 5) {
                Toast.makeText(requireContext(), "طول رمز عبور باید حداقل برابر 5 باشه", Toast.LENGTH_LONG).show();
            } else {
                if (pass.equals(binding.edtReenterPass.getText().toString()))
                    authorizationViewModel.setNewPassword(new NewPasswordBody(code, BaseCodeClass.mobileNumber, pass, BaseCodeClass.IMEI, BaseCodeClass.deviceModel));
                else
                    Toast.makeText(requireContext(), "هر دو فیلد رمز عبور باید برابر باشه!", Toast.LENGTH_SHORT).show();
            }
        });
        authorizationViewModel.getLiveChangingPassRes().observe(getViewLifecycleOwner(), resualt -> {
            if (resualt.getResualt().equals("111")) {
                requireActivity().finish();
                Toast.makeText(requireContext(), "عملیات موفقیت آمیز بود", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "onViewCreated: " + resualt.toString());

        });
        authorizationViewModel.getLiveSharedCode().observe(getViewLifecycleOwner(), s -> code = s);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                        .setTitle("خروج")
                        .setMessage("با تایید خروج، از فرآیند تغییر رمزعبور خارج میشید")
                        .setPositiveButton("تایید", (dialog, which) -> {
                            requireActivity().finish();
                        })
                        .setNeutralButton("بیخیال", (dialog, which) -> {
                            dialog.dismiss();
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}