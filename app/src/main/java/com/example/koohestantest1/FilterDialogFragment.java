package com.example.koohestantest1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.classDirectory.SendProduct;
import com.example.koohestantest1.model.DeleteProduct;
import com.example.koohestantest1.model.UpdatedProductBody;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.ViewModels.BookMarkViewModel;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewModels.PostViewViewModel;
import com.example.koohestantest1.classDirectory.FilterRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetPropertisOfCompanyProducts;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendDeleteProduct;
import com.example.koohestantest1.classDirectory.ReceiveProductClass;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.filterValue;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class FilterDialogFragment extends DialogFragment implements LoadProductApi {


    private LoadProductApi listener;

    public FilterDialogFragment(LoadProductApi listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_filter, container);
        try {


            Button clearFilter = view.findViewById(R.id.btnClearFilter);
            clearFilter.setOnClickListener(v -> {
                filterValue = "همه";
                listener.recyclerViewListClicked(v, filterValue, true);
                dismiss();
            });


            RecyclerView list = view.findViewById(R.id.recView);
            list.setLayoutManager(new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false));

            FilterRecyclerViewAdapter adapter = new FilterRecyclerViewAdapter(this.getActivity(), Main2Fragment.filterName, Main2Fragment.filterImage, filterValue, this);

            list.setAdapter(adapter);

            this.getDialog().setTitle("dialogAlert");
        } catch (Exception e) {
            logMessage("filterDialog >> " + e.getMessage(), this.getActivity());
        }

        return view;
    }



    @Override
    public Call<GetResualt> sendProductDetail(SendProduct sendProductClass) {
        return null;
    }

    @Override
    public void onResponseSendProduct(GetResualt getResualt) {

    }

    @Override
    public Call<GetResualt> uploadProductImage(String prId, String coId, String uID, String token, MultipartBody.Part file) {
        return null;
    }

    @Override
    public Call<List<ReceiveProductClass>> loadProduct(String companyId) {
        return null;
    }

    @Override
    public void onResponseLoadProduct(List<ReceiveProductClass> receiveProductClasses) {

    }

    @Override
    public Call<List<ReceiveProductClass>> loadProduct(String companyId, String userID) {
        return null;
    }

    @Override
    public Call<GetResualt> deleteProduct(SendDeleteProduct sendDeleteProduct) {
        return null;
    }

    @Override
    public void onResponseDeleteProduct(GetResualt getResualt) {

    }

    @Override
    public Call<ResponseBody> downloadImage(String PId, String fileNumber) {
        return null;
    }

    @Override
    public void onResponseDownloadImage(ResponseBody responseBody, String pid) {

    }

    @Override
    public Call<List<String>> getCategory(int companyType) {
        return null;
    }

    @Override
    public void onResponseGetCategory(List<String> cat) {

    }

    @Override
    public Call<List<String>> getSubCatOne(int companytype, String mainCat) {
        return null;
    }

    @Override
    public void onResponseGetSubCatOne(List<String> subCat1) {

    }

    @Override
    public Call<List<GetPropertisOfCompanyProducts>> getProperty(int companytype) {
        return null;
    }

    @Override
    public void onResponseGetProperty(List<GetPropertisOfCompanyProducts> propertisOfCompanyProducts) {

    }

    @Override
    public Call<GetResualt> viewProduct(PostViewViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> likeProduct(PostLikeViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> saveProduct(BookMarkViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> editProductDetail(SendProduct receiveProductClass) {
        return null;
    }



    @Override
    public Call<List<ReceiveProductClass>> getUpdatedData(UpdatedProductBody updatedProductBody) {
        return null;
    }

    @Override
    public void recyclerViewListClicked(View v, String value, boolean notify) {
        listener.recyclerViewListClicked(v, value, true);
        dismiss();
    }

    @Override
    public void brandRecyclerViewListClicked(View v, String value, boolean notify) {

    }

    @Override
    public void recyclerViewCanUpdating() {

    }

    @Override
    public void imageAdapterCanUpdating(String imagePID) {

    }

    @Override
    public Call<GetResualt> deleteProduct(DeleteProduct deleteProduct) {
        return null;
    }

}
