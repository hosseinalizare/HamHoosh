package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.R;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.AddressApi;

public class PaymentTypeRecyclerViewAdapter extends RecyclerView.Adapter<PaymentTypeRecyclerViewAdapter.ViewHolder> {

   // private ArrayList<String> mText = new ArrayList<>();
 //   private ArrayList<String> mImageName = new ArrayList<>();
    private AddressApi addressApi;
    private Context mContext;
    private int mode;
    private List<Integer> actions;
    private static ViewHolder lastHolder;
    private static int pos;
    private List<PayType> payTypeList;
    public PaymentTypeRecyclerViewAdapter(Context mContext, ArrayList<String> mText, ArrayList<String> mImageName, AddressApi api, int mod) {
        this.mContext = mContext;
        this.addressApi = api;
        this.mode = mod;
    }

    public PaymentTypeRecyclerViewAdapter(Context mContext, List<PayType> payTypeList,AddressApi api) {
        this.mContext = mContext;
        this.payTypeList = payTypeList;
        addressApi = api;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new PaymentTypeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(payTypeList.get(position).getImageAddress())
                .into(holder.image);
        holder.text.setText(payTypeList.get(position).getTitle());

       /* if (mode == 1) {
            if (holder.text.getText().toString().equals(mContext.getString(R.string.personal))) {
                holder.cardView.setEnabled(false);
                holder.backgroundLayout.setBackgroundResource(R.color.backgroundGray);
            }
        } else {
            if (holder.text.getText().toString().equals(mContext.getString(R.string.peik))) {
                holder.cardView.setEnabled(false);
                holder.backgroundLayout.setBackgroundResource(R.color.backgroundGray);
            }
        }*/

        holder.cardView.setOnClickListener(v -> {
            if (lastHolder != null) {
                lastHolder.backgroundLayout.setBackgroundResource(R.color.white);
            }
            pos = payTypeList.get(position).getId();
            addressApi.CartPaymentClickListener(pos);
            holder.backgroundLayout.setBackgroundResource(R.color.LighterBlue);

            lastHolder = holder;
        });
    }

    @Override
    public int getItemCount() {
        return payTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        CardView cardView;
        RelativeLayout backgroundLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ProductImage);
            text = itemView.findViewById(R.id.cardName);
            cardView = itemView.findViewById(R.id.cardView);
            backgroundLayout = itemView.findViewById(R.id.backgroundLayout);
        }
    }
}
