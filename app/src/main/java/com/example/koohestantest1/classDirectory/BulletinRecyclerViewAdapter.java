package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.R;

import java.util.List;

public class BulletinRecyclerViewAdapter extends RecyclerView.Adapter<BulletinRecyclerViewAdapter.ViewHolder> {

    private Context mContext;

    List<AllProductData> productData;

    public BulletinRecyclerViewAdapter(Context context, List<AllProductData> allProductData){
        this.productData = allProductData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bulletin_item, parent, false);

        return new BulletinRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(productData.get(position).getProductClass().getProductName());
        holder.txtPrice.setText(productData.get(position).getProductClass().getStandardCost());
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.bulletin_PName);
            txtPrice = (TextView) itemView.findViewById(R.id.bulletin_PPrice);
        }
    }
}
