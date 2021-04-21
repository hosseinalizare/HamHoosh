package com.example.koohestantest1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.AddressApi;
import com.example.koohestantest1.InfoDirectory.MangeAddressClass;
import com.example.koohestantest1.classDirectory.AddressViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CartAddressRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetResualt;
import retrofit2.Call;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedAddress;

public class AddressDialogFragment extends DialogFragment implements AddressApi {

    private Context mContext;
    private List<AddressViewModel> addressViewModels;

    @Override
    public Call<List<String>> listState() {
        return null;
    }

    @Override
    public void onResponseListState(List<String> stateList) {

    }

    @Override
    public Call<List<String>> listCity(String state) {
        return null;
    }

    @Override
    public void onResponseListCity(List<String> cityList) {

    }

    @Override
    public Call<GetResualt> setAddress(AddressViewModel addressViewModel) {
        return null;
    }

    @Override
    public void onResponseSetAddress(GetResualt getResualt) {

    }

    @Override
    public Call<List<AddressViewModel>> getAddress(AddressViewModel addressViewModel) {
        return null;
    }

    @Override
    public void onResponseGetMyAddress(List<AddressViewModel> addressViewModel) {
        try {
//            baseCodeClass.logMessage(addressViewModel.size() + "", mContext);
            this.addressViewModels = addressViewModel;
            initAddressRecyclerView(addressViewModel);
        } catch (Exception e) {
            baseCodeClass.logMessage(e.getMessage() + "888777", mContext);
        }
    }

    @Override
    public Call<GetResualt> delAddress(AddressViewModel addressViewModel) {
        return null;
    }

    @Override
    public void onResponseDelAddress(GetResualt getResualt) {

    }

    @Override
    public Call<AddressViewModel> getAddress(String addressID) {
        return null;
    }

    @Override
    public void onResponseGetAddress(AddressViewModel addressViewModel) {

    }

    @Override
    public void AddressOnCliclckListener(AddressViewModel address) {
        selectedAddress = address;


        clickListener.CartRecyclerViewClickListener(null, true);
        dismiss();
    }

    @Override
    public void CartRecyclerViewClickListener(View v, boolean b) {

    }

    @Override
    public void CartPaymentClickListener(int id) {

    }

    private Button btnAddNewAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_dialog, container);

        mContext = getActivity();
        btnAddNewAddress = view.findViewById(R.id.btn_new_address);
        btnAddNewAddress.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddAddressActivity.class);
            startActivity(intent);
        });
        try {
            baseCodeClass = new BaseCodeClass();
            addressList = view.findViewById(R.id.addressDialogRecycler);
            new MangeAddressClass(this).getMyAddress(baseCodeClass.getUserID());
        } catch (Exception e) {
            logMessage("AddressDialogFragment 100 >> " + e.getMessage(), mContext);
        }

        return view;
    }

    public AddressDialogFragment(AddressApi clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onResume() {
        super.onResume();

        new MangeAddressClass(this).getMyAddress(baseCodeClass.getUserID());
    }

    RecyclerView addressList;
    CartAddressRecyclerViewAdapter adapter;
    BaseCodeClass baseCodeClass;
    AddressApi clickListener;

    public void initAddressRecyclerView(List<AddressViewModel> addressViewModels) {

        try {
            addressList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

            adapter = new CartAddressRecyclerViewAdapter(mContext, addressViewModels, this);

            addressList.setAdapter(adapter);

            this.getDialog().setTitle("dialogAddress");
        } catch (Exception e) {
            logMessage("AddressDialogFragment 200 >> " + e.getMessage(), mContext);
        }
    }
}
