package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.ViewProductActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class ExplorerRecyclerViewAdapter extends RecyclerView.Adapter<ExplorerRecyclerViewAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    List<ReceiveProductClass> showProductData = new ArrayList<>();
    List<ReceiveProductClass> allProduct = new ArrayList<>();
    public LoadProductApi loadProductApi;

    BaseCodeClass baseCodeClass = new BaseCodeClass();

    public ExplorerRecyclerViewAdapter(Context mContext, List<ReceiveProductClass> showProductData, LoadProductApi loadApi) {
        try {
            this.mContext = mContext;
            this.allProduct = showProductData;
//            for (SendProductClass s : allProduct
//                 ) {
//                this.showProductData.add(s);
//            }
            this.showProductData = showProductData;
            this.loadProductApi = loadApi;
        } catch (Exception e) {
            baseCodeClass.logMessage("ExplorerAdapter cons : " + e.getMessage() + " >> " + e.getLocalizedMessage(), mContext);
        }
    }

    public void clearList() {
        showProductData.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_explorer_recycler_view, parent, false);

            return new ViewHolder(view);
        } catch (Exception e) {
            baseCodeClass.logMessage("ExplorerAdapter onCreateView : " + e.getMessage(), mContext);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.name.setText(showProductData.get(position).getProductName());
            String myPrice = showProductData.get(position).getStandardCost().getShowPrice();
            holder.price.setText(StringUtils.getNumberWithoutDot(myPrice));
            newDownloadImage(showProductData.get(position).getProductID(), holder);

            holder.cardView.setOnClickListener(v -> {
                if (baseCodeClass.loadSelectedProduct(showProductData.get(position).getProductID(), mContext)) {
                    Intent intent = new Intent(mContext, ViewProductActivity.class);
                    intent.putExtra("PID", showProductData.get(position).getProductID());
                    mContext.startActivity(intent);
                } else {
//                    toastMessage("خطای درون برنامه ای");
                }
            });
        } catch (Exception e) {
            baseCodeClass.logMessage("ExplorerAdapter onBinding : " + e.getMessage(), mContext);
        }
    }

    @Override
    public int getItemCount() {
        return showProductData.size();
    }

    @Override
    public Filter getFilter() {
        try {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    showProductData = (List<ReceiveProductClass>) results.values;
                    //load10Data();
                    loadProductApi.recyclerViewCanUpdating();
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    ArrayList<ReceiveProductClass> FilteredArrayNames = new ArrayList<>();
                    // perform your search here using the searchConstraint String.

                    // constraint = constraint.toString().toLowerCase();
                    if (constraint.toString().equals("ترشی")) {
                        constraint = "ترشیجات";
                    }
                    for (int i = 0; i < allProduct.size(); i++) {
                        String[] dataNames = allProduct.get(i).getCategory().split("\\.");
                        if (dataNames[0].equals(constraint.toString()) ||
                                constraint.toString().equals(mContext.getResources().getString(R.string.all))) {
                            FilteredArrayNames.add(allProduct.get(i));
                        }
                    }

                    results.count = FilteredArrayNames.size();
                    results.values = FilteredArrayNames;

                    return results;
                }
            };

            return filter;
        } catch (Exception e) {
            logMessage("ProductAdapter 300 >> " + e.getMessage(), mContext);
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price;
        ImageView image;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.explorerName);
            price = itemView.findViewById(R.id.explorerPrice);

            image = itemView.findViewById(R.id.explorerImage);

            cardView = itemView.findViewById(R.id.explorerCardView);
        }
    }

    public void newDownloadImage(String pid, ViewHolder holder) {
        try {

            String url = baseCodeClass.pBASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
            Glide.with(mContext).load(url).into(holder.image);
        } catch (Exception e) {
            baseCodeClass.logMessage("ExplorerAdapter glide : " + e.getMessage(), mContext);
        }
    }
}
