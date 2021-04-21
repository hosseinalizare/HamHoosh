package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;

public interface CompanyCustomersCallBack {
    void onCustomersSuccess(List<CompanyFollowerViewModel> companyCustomers);

    void onCustomersError(String error);
}
