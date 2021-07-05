package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.ApiDirectory.AddressApi;
import com.example.koohestantest1.CartRecyclerViewClickListener;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewProductActivity;
import com.example.koohestantest1.adapter.recyclerinterface.ICartEvents;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;

public class CartProductRecyclerViewAdapterVer2 extends RecyclerView.Adapter<CartProductRecyclerViewAdapterVer2.ViewHolder> implements CartRecyclerViewClickListener {

    private ICartEvents iCartEvents;
    private Context mContext;
    private SendOrderClass mSendOrderClasses;
    private static AddressApi cartRecyclerViewClickListener;
    private boolean storeOrder;
    private final String TAG = CartProductRecyclerViewAdapter.class.getSimpleName() + "Debug";
    private BadgeSharedViewModel badgeSharedViewModel;
    private LocalCartViewModel localCartViewModel;

    BaseCodeClass baseCodeClass = new BaseCodeClass();

    public CartProductRecyclerViewAdapterVer2(Context mContext, SendOrderClass mSendOrderClasses, AddressApi clickListener, boolean storeOrder, BadgeSharedViewModel badgeSharedViewModel, LocalCartViewModel localCartViewModel, ICartEvents iCartEvents) {
        this.mContext = mContext;
        this.badgeSharedViewModel = badgeSharedViewModel;
        this.mSendOrderClasses = mSendOrderClasses;
        this.cartRecyclerViewClickListener = clickListener;
        this.storeOrder = storeOrder;
        this.localCartViewModel = localCartViewModel;
        this.iCartEvents = iCartEvents;
    }

    public CartProductRecyclerViewAdapterVer2(Context mContext, SendOrderClass mSendOrderClasses, AddressApi clickListener, boolean storeOrder) {
        this.mContext = mContext;
        this.mSendOrderClasses = mSendOrderClasses;
        this.cartRecyclerViewClickListener = clickListener;
        this.storeOrder = storeOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_list, parent, false);
        return new ViewHolder(view);
    }

    public void newDownloadImage(String pid, ImageView _imageView) {
        try {
            String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
            Glide.with(mContext).load(url).into(_imageView);
        } catch (Exception e) {
            baseCodeClass.logMessage("ViewProduct glide :" + e.getMessage(), mContext);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        newDownloadImage(mSendOrderClasses.Order_Details.get(position).getProductID(), holder.imageProduct);
        holder.productName.setText(mSendOrderClasses.getOrder_Details().get(position).getProductName());
        String companyId = mSendOrderClasses.getCompanyID();
        if (companyId != null) {
            if (companyId.equals(BaseCodeClass.CompanyID))
                holder.companyName.setText(BaseCodeClass.CompanyName);
            else holder.companyName.setText(companyId);
        }
        holder.warranty.setText("تضمین کیفیت محصول");
        holder.shipperName.setText(mSendOrderClasses.getShipperID());
        int quantity = mSendOrderClasses.getOrder_Details().get(position).getQuantity();


        holder.discount.setText(mSendOrderClasses.getOrder_Details().get(position).getSumOff()+"");

        String price = mSendOrderClasses.getOrder_Details().get(position).getSumPrice()+"";
        /*int decimalPrice = price.indexOf(".");

        int cost = Integer.parseInt(decimalPrice == -1 ? price : price.substring(0, decimalPrice));
        cost*= quantity;*/
        holder.price.setText(mSendOrderClasses.getOrder_Details().get(position).getSumPrice() + "");


        // int decimalQuantity = quantity.indexOf(".");
        holder.quantity.setText(quantity + "");


        holder.cartAdd.setVisibility(View.GONE);
        holder.cartRemove.setVisibility(View.GONE);

        holder.trash.setVisibility(View.GONE);
        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ViewProductActivity.class);
            intent.putExtra("PID", mSendOrderClasses.getOrder_Details().get(position).getProductID());
            mContext.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {

        if (mSendOrderClasses != null) {
            Log.d(TAG, "getItemCount: ");
            if (mSendOrderClasses.getOrder_Details() != null) {
                Log.d(TAG, "getItemCount: details " + mSendOrderClasses.getOrder_Details().size());
                return mSendOrderClasses.getOrder_Details().size();
            }
        }
        return 0;
    }

    @Override
    public void CartRecyclerViewClickListener(View v, boolean b) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct, cartAdd, cartRemove, trash;
        TextView productName, companyName, warranty, shipperName, discount, price, quantity;
        CardView cardView, item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProduct = (ImageView) itemView.findViewById(R.id.cartProductImage);
            productName = (TextView) itemView.findViewById(R.id.cartProductName);
            companyName = (TextView) itemView.findViewById(R.id.cartCompanyName);
            warranty = (TextView) itemView.findViewById(R.id.txtWarranty);
            shipperName = (TextView) itemView.findViewById(R.id.deliveryName);
            discount = (TextView) itemView.findViewById(R.id.carDiscountValue);
            price = (TextView) itemView.findViewById(R.id.cartPrice);
            quantity = (TextView) itemView.findViewById(R.id.cartQuantity);
            cartAdd = (ImageView) itemView.findViewById(R.id.cartAdd);
            cartRemove = (ImageView) itemView.findViewById(R.id.cartRemove);
            trash = (ImageView) itemView.findViewById(R.id.trashBtn);
            cardView = itemView.findViewById(R.id.cardView);
            item = itemView.findViewById(R.id.cardViewProduct);
        }
    }
}

