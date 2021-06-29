package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.AddProductActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewProductActivity;
import com.example.koohestantest1.local_db.entity.Product;

import java.util.List;

class MainViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView name;

    public MainViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.imageProduct);
        name = itemView.findViewById(R.id.txtName);
    }
}


public class MyStoreProductRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Product> productData;
    List<Product> allProduct;
    LayoutInflater inflater;
    boolean myStore;

    int ITEM_SHOW = 9;

    public boolean isLoading;

    public final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    public int loadPOS = 0;

    private String TAG = MyStoreProductRecyclerViewAdapter.class.getSimpleName();

    BaseCodeClass baseCodeClass = new BaseCodeClass();


    public MyStoreProductRecyclerViewAdapter(Context context, List<Product> allProductData, boolean isMyStore) {
        this.context = context;
        this.allProduct = allProductData;
        this.myStore = isMyStore;
        this.inflater = LayoutInflater.from(context);
        productData = allProduct;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mystore_product, parent, false);

        return new MainViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {

            MainViewHolder mainViewHolder = (MainViewHolder) holder;
            loadProduct(mainViewHolder, position);

        } catch (Exception e) {
            baseCodeClass.logMessage("onBindViewHolder >> " + e.getMessage(), context);
        }


    }

    public void newDownloadImage(String pid, MainViewHolder holder) {
        String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
        Glide.with(context).load(url).into(holder.imageView);
    }

    public void loadProduct(MainViewHolder holder, final int position) {
        try {
            if (productData.get(position) != null) {
                newDownloadImage(productData.get(position).ProductID, holder);
                String color = productData.get(position).Spare2;
                if (!color.equals("null")) {
                    holder.imageView.setBackgroundColor(Color.parseColor(color));
                }
                holder.name.setText(productData.get(position).ProductName);

                holder.imageView.setOnClickListener(v -> {

                    if (myStore) {
                        if (baseCodeClass.getPermissions().get(6).isState()) {
                            startEditProduct(position);
                        } else {
                            //employee has not permission to edit product

                            startViewProduct(position);

                        }
                    } else {

                        startViewProduct(position);


                    }
                });
            }
//            }
        } catch (Exception e) {
            baseCodeClass.logMessage("loadProduct >> " + e.getMessage(), context);
        }
    }

    public void startViewProduct(int position) {
        Intent intent = new Intent(context, ViewProductActivity.class);
        intent.putExtra("PID", productData.get(position).ProductID);
        context.startActivity(intent);
    }

    public void startEditProduct(int position) {
        Intent intent = new Intent(context, AddProductActivity.class);
        intent.putExtra("PID", productData.get(position).ProductID);

            context.startActivity(intent);
    }

    @Override
    public int getItemViewType(int position) {
        return productData.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "getItemCount: " + productData.size());
        return productData.size();
    }


}


