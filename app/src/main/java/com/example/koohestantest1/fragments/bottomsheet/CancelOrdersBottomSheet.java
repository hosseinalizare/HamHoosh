package com.example.koohestantest1.fragments.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.koohestantest1.R;
import com.example.koohestantest1.databinding.BottomSheetFragmentCancelOrdersBinding;
import com.example.koohestantest1.viewModel.OrderDetailSharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CancelOrdersBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private OrderDetailSharedViewModel detailSharedViewModel;
    private BottomSheetFragmentCancelOrdersBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailSharedViewModel = new ViewModelProvider(requireActivity()).get(OrderDetailSharedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetFragmentCancelOrdersBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCancelCantSend.setOnClickListener(this::onClick);
        binding.btnCancelEmpty.setOnClickListener(this::onClick);
        binding.btnCancelFinancial.setOnClickListener(this::onClick);
        binding.btnCancelDidntCome.setOnClickListener(this::onClick);
        binding.btnCancelCustomerWontThere.setOnClickListener(this::onClick);
        binding.btnCancelOther.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        detailSharedViewModel.setCanceledStatus(9);
        dismiss();
    }
}
