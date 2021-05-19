package com.example.koohestantest1.classDirectory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.koohestantest1.MyStoreOrderDetail;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.downloadmanager.FileDownloadManager;
import com.example.koohestantest1.downloadmanager.IDownloadManager;
import com.example.koohestantest1.model.MyTime;
import com.example.koohestantest1.viewModel.TimeViewModel;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class CompanyOrderRecyclerViewAdapter extends RecyclerView.Adapter<CompanyOrderRecyclerViewAdapter.ViewModel> {

    private String TAG = CompanyOrderRecyclerViewAdapter.class.getSimpleName();
    private Context mContext;
    public List<SendOrderClass> sendOrderClasses;
    boolean Usermod = false;
    private TimeViewModel timeViewModel;
    BaseCodeClass baseCodeClass = new BaseCodeClass();

    private ActivityResultLauncher<String> resultLauncher;
    private IDownloadManager iDownloadManager;


    public CompanyOrderRecyclerViewAdapter(Context mContext, List<SendOrderClass> sendOrderClasses, TimeViewModel timeViewModel, boolean user) {
        this.mContext = mContext;
        this.sendOrderClasses = sendOrderClasses;
        this.timeViewModel = timeViewModel;
        Usermod = user;

    }

    public CompanyOrderRecyclerViewAdapter(Context mContext, List<SendOrderClass> sendOrderClasses, TimeViewModel timeViewModel, boolean user, IDownloadManager iDownloadManager, ActivityResultLauncher<String> resultLauncher) {
        this(mContext, sendOrderClasses, timeViewModel, user);
        this.iDownloadManager = iDownloadManager;
        this.resultLauncher = resultLauncher;
    }

    public void updateData(List<SendOrderClass> sendOrderClasses) {
        this.sendOrderClasses = sendOrderClasses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mystore_orderlist, parent, false);

        return new CompanyOrderRecyclerViewAdapter.ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModel holder, final int position) {
//        try {
        String orderedDateString = sendOrderClasses.get(position).getOrderDate();

        String[] splitedOrderedDate = orderedDateString.split("T");
        MyTime currentTime = timeViewModel.getCurrentTimeLiveData().getValue();

        Date orderedDate = null;
        try {
            orderedDate = TimeUtils.getDateFromString(splitedOrderedDate[0] + " " + splitedOrderedDate[1], 1);
            Date nowDate = TimeUtils.getDateFromString(currentTime.getCurrentDate() + " " + currentTime.getCurrentTime(), 0);
            holder.date.setText(TimeUtils.getDurationExpression(orderedDate, nowDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.orderID.setText(sendOrderClasses.get(position).getId());
        holder.customerName.setText(sendOrderClasses.get(position).getSpare2());
        holder.costumerAddress.setText(sendOrderClasses.get(position).getSpare1());
        String price = sendOrderClasses.get(position).getSumPrice();

        holder.sumPrice.setText(price);
        int quantity = sendOrderClasses.get(position).getOrder_Details().get(0).getQuantity();
        String orderCount = "اقلام: "+ quantity;
        /*String orderCount = "اقلام: "+ sendOrderClasses.get(position).Order_Details.size();*/


        holder.productCount.setText(orderCount);

        String urlProductImage = null;
        if (sendOrderClasses.get(position).Order_Details.size() > 0) {
            urlProductImage = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + sendOrderClasses.get(position).Order_Details.get(0).getProductID() + "&fileNumber=1";
        }
        String urlCustomerImage = baseCodeClass.BASE_URL + "User/DownloadCustomerFile?CustomerID=" + sendOrderClasses.get(position).getCustomerID() + "&fileNumber=" + 1;

        holder.productImage.setOnLongClickListener(view -> {
            String urlReceipt = baseCodeClass.BASE_URL + "Order/DownloadFile?OrderId=" + sendOrderClasses.get(position).Order_Details.get(0).getOrderID() + "&fileNumber=1";
            String name = "dehkade_receipt";

            if (isPermissionGranted(view.getContext())) {
                Toast.makeText(view.getContext(), "در حال دانلود فاکتور سفارش", Toast.LENGTH_SHORT).show();
                FileDownloadManager fileDownloadManager = new FileDownloadManager(view.getContext());
                long id = fileDownloadManager.downloadFromUrl(urlReceipt, name, FileDownloadManager.JPG_FORMAT);
                iDownloadManager.onDownloadCalled(id);
            } else {
                resultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            return false;
        });
        if (urlProductImage != null) {
            Glide.with(mContext).load(urlProductImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.logodehkade).into(holder.productImage);
        }
        Glide.with(mContext).load(urlCustomerImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_profile).into(holder.CUSTOMERIM);


//        } catch (Exception e) {
//            baseCodeClass.logMessage(e.getMessage() + " >> 888", mContext);
//        }

        holder.detail.setOnClickListener(v -> {
            BaseCodeClass.myStoreSelectedOrder = sendOrderClasses.get(position);

            Intent intent = new Intent(mContext, MyStoreOrderDetail.class);
            if (Usermod) {
                intent.putExtra("User", true);
            }
            mContext.startActivity(intent);
        });
    }


    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);

        //  sendBroadcast(mediaScanIntent);
    }

    @Override
    public int getItemCount() {
        return sendOrderClasses != null ? sendOrderClasses.size() : 0;
    }

    private boolean isPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public class ViewModel extends RecyclerView.ViewHolder {

        TextView orderID, customerName, costumerAddress, sumPrice, detail, date,productCount;
        CardView cardView;
        ImageView productImage, CUSTOMERIM;

        public ViewModel(@NonNull View itemView) {
            super(itemView);
            try {
                orderID = itemView.findViewById(R.id.OrderID);
                customerName = itemView.findViewById(R.id.costumertName);
                costumerAddress = itemView.findViewById(R.id.OrderAddress);
                sumPrice = itemView.findViewById(R.id.sumPrice);
                cardView = itemView.findViewById(R.id.cardViewOrder);
                productImage = itemView.findViewById(R.id.sumProductImage);
                detail = itemView.findViewById(R.id.detail);
                date = itemView.findViewById(R.id.orderDate);
                CUSTOMERIM = itemView.findViewById(R.id.customerImage);
                productCount = itemView.findViewById(R.id.txtMaystoreOrderListProductCount);
            } catch (Exception e) {
                baseCodeClass.logMessage(e.getMessage() + " >> 999", mContext);
            }
        }
    }
}
