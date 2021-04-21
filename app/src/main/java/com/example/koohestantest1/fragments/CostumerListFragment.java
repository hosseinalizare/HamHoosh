package com.example.koohestantest1.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.adapter.CustomersRecyclerAdapter;
import com.example.koohestantest1.databinding.FragmentCostumerListBinding;
import com.example.koohestantest1.fragments.transinterface.CustomerChatInterface;
import com.example.koohestantest1.viewModel.CompanyViewModel;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;


public class CostumerListFragment extends Fragment implements CustomerChatInterface {

    private FragmentCostumerListBinding binding;
    private CompanyViewModel companyViewModel;
    private CustomersRecyclerAdapter customersRecyclerAdapter;
    private List<CompanyFollowerViewModel> companyFollowerViewModels;
    private BaseCodeClass baseCodeClass;
    private boolean searchMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseCodeClass = new BaseCodeClass();
        companyFollowerViewModels = new ArrayList<>();
        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        customersRecyclerAdapter = new CustomersRecyclerAdapter(this::onChatClicked,getContext());
        companyViewModel.callForCompanyCustomers(baseCodeClass.getCompanyID());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCostumerListBinding.inflate(inflater, container, false);
        binding.rvCostumersList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCostumersList.setAdapter(customersRecyclerAdapter);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.ivBackCostumersList.setOnClickListener(v -> {
            requireActivity().finish();
        });
        companyViewModel.getCustomers().observe(getViewLifecycleOwner(), list -> {
            if (list.size() > 0) {
                binding.pbCustomersList.setVisibility(View.GONE);
                binding.rvCostumersList.setVisibility(View.VISIBLE);
                customersRecyclerAdapter.setData(list);
                companyFollowerViewModels = list;
            }
        });
        companyViewModel.getErrorCustomers().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });

        binding.svCustomerFragmentName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customersRecyclerAdapter.searchInCustomer(newText);
                searchMode = true;
                return false;
            }
        });

        binding.svCustomerFragmentName.setOnSearchClickListener(v -> {
            searchMode = true;
            //Toast.makeText(getContext(), "cl2", Toast.LENGTH_SHORT).show();
            binding.tvCostumerListTitle.setVisibility(View.GONE);
        });

        binding.svCustomerFragmentName.setOnCloseListener(() -> {
            //Toast.makeText(getContext(), "cl", Toast.LENGTH_SHORT).show();
            binding.svCustomerFragmentName.setQuery("", false);

            if (!binding.svCustomerFragmentName.isIconfiedByDefault()) {
                binding.svCustomerFragmentName.setIconifiedByDefault(true);
            }

            updateRecyclerView(companyFollowerViewModels);
            searchMode = false;
            binding.tvCostumerListTitle.setVisibility(View.VISIBLE);
            return false;
        });

    }

    @Override
    public void onChatClicked(String userId) {
        Intent intent = new Intent(requireContext(), MessageActivity.class);
        // getter = Another user
        intent.putExtra("getter", userId);
        // sender = Ourselves
        intent.putExtra("sender", baseCodeClass.getCompanyID());
        requireActivity().startActivity(intent);
    }

    public void updateRecyclerView(List<CompanyFollowerViewModel> companyFollowers){
        companyFollowerViewModels = companyFollowers;
        customersRecyclerAdapter.updateData(companyFollowerViewModels);
    }
}