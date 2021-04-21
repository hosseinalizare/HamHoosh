package com.example.koohestantest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.KeyEvent;

import com.example.koohestantest1.databinding.ActivityForgotPasswordBinding;
import com.example.koohestantest1.viewModel.ForgetPassSharedViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private final String TAG = ForgotPasswordActivity.class.getSimpleName();
    private ForgetPassSharedViewModel forgetPassSharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        forgetPassSharedViewModel = new ViewModelProvider(this).get(ForgetPassSharedViewModel.class);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            forgetPassSharedViewModel.setOnRemovedClicked(true);
            return true;
        } else
            return super.onKeyUp(keyCode, event);
    }

}
