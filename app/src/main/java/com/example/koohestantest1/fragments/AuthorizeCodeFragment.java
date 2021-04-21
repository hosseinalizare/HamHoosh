package com.example.koohestantest1.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.koohestantest1.R;
import com.example.koohestantest1.databinding.FragmentAuthorizeCodeBinding;
import com.example.koohestantest1.model.BodyVerifiedCode;
import com.example.koohestantest1.model.SmsRecoveryBody;
import com.example.koohestantest1.viewModel.AuthorizationViewModel;
import com.example.koohestantest1.viewModel.ForgetPassSharedViewModel;

import java.util.concurrent.TimeUnit;

import com.example.koohestantest1.classDirectory.BaseCodeClass;


public class AuthorizeCodeFragment extends Fragment {

    private FragmentAuthorizeCodeBinding binding;
    private AuthorizationViewModel authorizationViewModel;
    private String code;
    private String TAG = AuthorizeCodeFragment.class.getSimpleName();
    private ForgetPassSharedViewModel forgetPassSharedViewModel;

    public AuthorizeCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authorizationViewModel = new ViewModelProvider(requireActivity()).get(AuthorizationViewModel.class);
        forgetPassSharedViewModel = new ViewModelProvider(requireActivity()).get(ForgetPassSharedViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAuthorizeCodeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        automaticFocusing();

        CountDownTimer countDownTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvTimerAuth.setText("" + String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                binding.tvResendCode.setVisibility(View.INVISIBLE);
                binding.tvResendCodeAction.setVisibility(View.VISIBLE);
            }
        }.start();

        binding.tvResendCodeAction.setOnClickListener(v -> {
            authorizationViewModel.getRecoveryCode(new SmsRecoveryBody(BaseCodeClass.mobileNumber, BaseCodeClass.IMEI, BaseCodeClass.deviceModel));

        });
        binding.tvEditPhone.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        binding.btnDoneAuth.setOnClickListener(v -> {
            if (binding.edtCode1.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtCode2.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtCode3.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtCode4.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtCode5.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtCode6.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtCode7.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtCode8.getText().toString().equals("")) {
                Toast.makeText(requireContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder codeBuilder = new StringBuilder()
                    .append(binding.edtCode1.getText())
                    .append(binding.edtCode2.getText())
                    .append(binding.edtCode3.getText())
                    .append(binding.edtCode4.getText())
                    .append(binding.edtCode5.getText())
                    .append(binding.edtCode6.getText())
                    .append(binding.edtCode7.getText())
                    .append(binding.edtCode8.getText());
            code = codeBuilder.toString();
            authorizationViewModel.checkVerificationCode(new BodyVerifiedCode(code, BaseCodeClass.mobileNumber, BaseCodeClass.IMEI, BaseCodeClass.deviceModel));
        });

        authorizationViewModel.getLiveResVerificationCode().observe(getViewLifecycleOwner(), resualt -> {
            if (resualt != null) {
                if (resualt.getMsg().equals("ok")) {
                    authorizationViewModel.setLiveSharedCode(code);
                    Navigation.findNavController(view).navigate(R.id.action_authorizeCodeFragment_to_changePasswordFragment);
                } else {
                    Toast.makeText(requireContext(), "کد وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT).show();
                }
            }
        });

        authorizationViewModel.getLiveResSendCode().observe(getViewLifecycleOwner(), resualt -> {
            if (resualt != null) {
                if (resualt.getResualt().equals("100")) {
                    Toast.makeText(requireContext(), "کد تایید مجددا ارسال شد", Toast.LENGTH_SHORT).show();
                    authorizationViewModel.clearLivePhoneSendCode();

                    countDownTimer.start();
                    binding.tvResendCode.setVisibility(View.VISIBLE);
                    binding.tvResendCodeAction.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(requireContext(), "خطایی رخ داد", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgetPassSharedViewModel.getOnRemoveClickedRes().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                handleKeyBoardFocus();
                forgetPassSharedViewModel.setOnRemovedClicked(false);
            }
        });
    }

    private void handleKeyBoardFocus() {
        if (binding.edtCode2.isFocused()) {
            binding.edtCode2.clearFocus();
            binding.edtCode1.requestFocus();
            return;
        }

        if (binding.edtCode3.isFocused()) {
            binding.edtCode3.clearFocus();
            binding.edtCode2.requestFocus();
            return;
        }

        if (binding.edtCode4.isFocused()) {
            binding.edtCode4.clearFocus();
            binding.edtCode3.requestFocus();
            return;
        }

        if (binding.edtCode5.isFocused()) {
            binding.edtCode5.clearFocus();
            binding.edtCode4.requestFocus();
            return;
        }

        if (binding.edtCode6.isFocused()) {
            binding.edtCode6.clearFocus();
            binding.edtCode5.requestFocus();
            return;
        }
        if (binding.edtCode7.isFocused()) {
            binding.edtCode7.clearFocus();
            binding.edtCode6.requestFocus();
            return;
        }
        if (binding.edtCode8.isFocused()) {
            binding.edtCode8.clearFocus();
            binding.edtCode7.requestFocus();
            return;
        }
    }

    private void automaticFocusing() {
        binding.edtCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + binding.edtCode1.getText().toString());
                Log.d(TAG, "onTextChanged: length " + s.length());
                if (s.length() == 1) {
                    binding.edtCode1.clearFocus();
                    binding.edtCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edtCode2.clearFocus();
                    binding.edtCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edtCode3.clearFocus();
                    binding.edtCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edtCode4.clearFocus();
                    binding.edtCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edtCode5.clearFocus();
                    binding.edtCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edtCode6.clearFocus();
                    binding.edtCode7.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtCode7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.edtCode7.clearFocus();
                    binding.edtCode8.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtCode8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    View view = requireActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    binding.edtCode8.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}