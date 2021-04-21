package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.AddAddressActivity;
import com.example.koohestantest1.AddNewAddressActivity;
import com.example.koohestantest1.LoadAddressRecyclerViewClickListener;
import com.example.koohestantest1.R;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.AddressApi;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedAddress;

public class LoadAddressRecyclerViewAdapter extends RecyclerView.Adapter<LoadAddressRecyclerViewAdapter.ViewHolder> implements LoadAddressRecyclerViewClickListener {

    private final Context mContext;
    private final List<AddressViewModel> addressList;
    private static AddressApi clickListener;

    public LoadAddressRecyclerViewAdapter(Context mContext, List<AddressViewModel> addressList, AddressApi callBack) {
        this.mContext = mContext;
        this.addressList = addressList;
        clickListener = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address_recycler, parent, false);
        return new LoadAddressRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.address.setText(addressList.get(position).getAddress1());
        holder.state.setText(addressList.get(position).getState() + " , " + addressList.get(position).getCity() + " , " + addressList.get(position).getArea());
        holder.number.setText(addressList.get(position).getPhoneNumber());

        holder.btnDelete.setOnClickListener(v -> {
            clickListener.AddressOnCliclckListener(addressList.get(position));
            addressList.remove(position);
            notifyItemRemoved(position);
        });

        holder.btnEdit.setOnClickListener(v -> {
            editAddress(position);
        });

        holder.edit.setOnClickListener(v -> {
            editAddress(position);
        });

    }

    private void editAddress(int position) {
        Intent intent = new Intent(mContext, AddNewAddressActivity.class);
        intent.putExtra(AddAddressActivity.KEY_INTENT_EDIT, addressList.get(position).getId());
        selectedAddress = addressList.get(position);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    @Override
    public void loadAddressRecyclerViewListClicked(View v, AddressViewModel value) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView address, state, number, edit, delete;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.txtAddress);
            state = itemView.findViewById(R.id.txtCity);
            number = itemView.findViewById(R.id.txtphone);
            edit = itemView.findViewById(R.id.editSendAddress);
            delete = itemView.findViewById(R.id.delAddress);

            btnEdit = itemView.findViewById(R.id.editSendAddressBtn);
            btnDelete = itemView.findViewById(R.id.delAddressbtn);
        }
    }
}

