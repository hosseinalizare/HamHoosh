package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.AddressApi;

public class CartAddressRecyclerViewAdapter extends RecyclerView.Adapter<CartAddressRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<AddressViewModel> addressViewModels;
    private AddressApi addressApi;

    public CartAddressRecyclerViewAdapter(Context mContext, List<AddressViewModel> addressViewModels, AddressApi addressApi) {
        this.mContext = mContext;
        this.addressViewModels = addressViewModels;
        this.addressApi = addressApi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address_dialog, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.address.setText(addressViewModels.get(position).getAddress1());// + "\n" + addressViewModels.get(position).);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressApi.AddressOnCliclckListener(addressViewModels.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressViewModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView address;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.addressTextView);
            cardView = itemView.findViewById(R.id.cardViewAddressDialog);
        }
    }
}
