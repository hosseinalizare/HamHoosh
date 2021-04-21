package com.example.koohestantest1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.koohestantest1.model.DeleteProduct;
import com.example.koohestantest1.model.UpdatedProductBody;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.ExplorerApi;
import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.DB.DataBase;
import com.example.koohestantest1.ViewModels.BookMarkViewModel;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewModels.PostViewViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CategoryRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.ExplorerRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetPropertisOfCompanyProducts;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendDeleteProduct;
import com.example.koohestantest1.classDirectory.SendHashtagClass;
import com.example.koohestantest1.classDirectory.SendProductClass;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.categoryRecyclerViewAdapter;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class ExplorerFragment extends Fragment implements LoadProductApi {

    private Context mContext;
    private static final int ACTIVITY_NU = 1;
    EditText edSearch;
    //    Button btnSearch;
//    ListView lvSearch;
    TextView record;
    CardView company;

    ExplorerApi explorerApi;
    LoadProductApi loadProductApi;

    DataBase dataBase;
    BaseCodeClass baseCodeClass;

    private String TAG = ExplorerFragment.class.getSimpleName();
//    List<SendProductClass>

    ExplorerRecyclerViewAdapter adapter;

    private ArrayList<String> mCategory = new ArrayList<String>();

    private ArrayList<String> mCompanyNames = new ArrayList<>();
    private ArrayList<Integer> mCompanyLogo = new ArrayList<>();
    private ImageView ivLogo;
    View view;
    private String hashtagWord ;

    public ExplorerFragment() {
    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explorer, container, false);

        mContext = getActivity();


        ivLogo = view.findViewById(R.id.iv_explore_logo);
        edSearch = (EditText) view.findViewById(R.id.EdSearch);
        record = (TextView) view.findViewById(R.id.txtRecord);
        company = view.findViewById(R.id.company);

        //ivLogo.setImageResource(CompanyId.COMPANY_IMAGE);
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShowStoreActivity.class));
            }
        });

        dataBase = new DataBase(mContext);
        baseCodeClass = new BaseCodeClass();
        baseCodeClass.LoadBaseData(mContext);
        String url = baseCodeClass.BASE_URL + "Company/DownloadFile?CompanyID=" + baseCodeClass.getCompanyID() + "&ImageAddress=" + 1;
        Glide.with(this).load(url)
                .placeholder(R.drawable.emptycart)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivLogo);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseCodeClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        explorerApi = retrofit.create(ExplorerApi.class);
        loadProductApi = retrofit.create(LoadProductApi.class);

//        edSearch.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                textChange(edSearch.getText().toString());
//
//                return true;
//            }
//        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                textChange(edSearch.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                textChange(edSearch.getText().toString());
                Log.d(TAG, "onTextChanged: " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                textChange(s.toString());
                Log.d(TAG, "afterTextChanged: " + s.toString());
            }
        });

//        loadCategory();
//        loadCompany();

        return view;
    }//end onCreate

    public void textChange(String s) {
        try {
            if (edSearch.getText().toString().equals("")) {

                adapter.clearList();
                company.setVisibility(View.VISIBLE);
                record.setVisibility(View.GONE);

            } else {
                company.setVisibility(View.GONE);
                if (edSearch.getText().toString().charAt(0) == '#') {
                    record.setVisibility(View.VISIBLE);
//                    String value = s.replace("#", "");
                    loadHashTag(s, baseCodeClass.getCompanyID());
                } else {
                    LoadSearchProduct(s, 2, baseCodeClass.getCompanyID());
                    record.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            logMessage("ExplorerFragment 100 >> " + e.getMessage(), mContext);
        }
    }

    public void LoadSearchProduct(String txt, final int c, String companyID) {

        try {
            if (companyID.equals("") || companyID.isEmpty() || companyID == "") {

                Call<List<SendProductClass>> call = explorerApi.loadAllProduct(txt, "1");
                call.enqueue(new Callback<List<SendProductClass>>() {
                    @Override
                    public void onResponse(Call<List<SendProductClass>> call, Response<List<SendProductClass>> response) {
                        search(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<SendProductClass>> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
            } else {
                Call<List<SendProductClass>> call = explorerApi.loadProduct(txt, "1", companyID);
                call.enqueue(new Callback<List<SendProductClass>>() {
                    @Override
                    public void onResponse(Call<List<SendProductClass>> call, Response<List<SendProductClass>> response) {
                        search(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<SendProductClass>> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            logMessage("ExplorerFragment 200 >> " + e.getMessage(), mContext);
        }


    }

    public void loadHashTag(String tag, String companyID) {
        try {
            if (companyID.equals("") || companyID.isEmpty() || companyID == "") {
                /**1399/10/11**/
                Call<List<SendProductClass>> call = explorerApi.searchAllTag(new SendHashtagClass(tag, "1"));
                call.enqueue(new Callback<List<SendProductClass>>() {
                    @Override
                    public void onResponse(Call<List<SendProductClass>> call, Response<List<SendProductClass>> response) {
                        search(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<SendProductClass>> call, Throwable t) {
                        Log.d("Error",t.getMessage());
                    }
                });
            } else {
                Call<List<SendProductClass>> call = explorerApi.searchAllTagNewVersion(new SendHashtagClass(tag, "1", companyID));
                call.enqueue(new Callback<List<SendProductClass>>() {
                    @Override
                    public void onResponse(Call<List<SendProductClass>> call, Response<List<SendProductClass>> response) {
                        search(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<SendProductClass>> call, Throwable t) {

                    }
                });
            }
        } catch (Exception e) {
            logMessage("ExplorerFragment 300 >> " + e.getMessage(), mContext);
        }
    }

    public void search(List<SendProductClass> productClasses) {
        try {
            if (productClasses == null) {
                //baseCodeClass.logMessage("محصولی یافت نشد!", mContext);
            } else {
                mCategory.clear();
                mCategory.add("همه");
                for (SendProductClass spc : productClasses
                ) {
                    if (!spc.getCompanyID().equals(baseCodeClass.getCompanyID())) {
                        productClasses.remove(spc);
                    }
                    try {
                        String[] cat = spc.getCategory().split("\\.");
                        if (!mCategory.contains(cat[0]))
                            mCategory.add(cat[0]);
                    } catch (Exception e) {

                    }
                }
                initCategoryRecyclerView();

                if (productClasses.size() == 0) {
                    record.setText("نتیجه ای یافت نشد");
                } else {
                    record.setText("تعداد : " + String.valueOf(productClasses.size()));
                }
                initExplorerRecyclerView(productClasses);
            }
        } catch (Exception e) {
            logMessage("ExplorerFragment 400 >> " + e.getMessage(), mContext);
        }
    }

    public void initExplorerRecyclerView(List<SendProductClass> sendProductClass) {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.searchProduct_RecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ExplorerRecyclerViewAdapter(mContext, sendProductClass, this);
            recyclerView.setAdapter(adapter);
            if (edSearch.getText().length() == 0) {
                adapter.clearList();
            }
        } catch (Exception e) {
            logMessage("ExplorerFragment 500 >> " + e.getMessage(), mContext);
        }
    }

    public void loadCategory() {
        try {
            Call<List<String>> call = loadProductApi.getCategory(2);
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    List<String> SProductCat = response.body();
                    mCategory.add(getResources().getString(R.string.all));
                    for (String s : SProductCat
                    ) {
                        mCategory.add(s);
                    }

                    initCategoryRecyclerView();
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });
        } catch (Exception e) {
            logMessage("ExplorerFragment 600 >> " + e.getMessage(), mContext);
        }


    }

    private void initCategoryRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.CategoryRecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(mContext, mCategory, this);
            recyclerView.setAdapter(categoryRecyclerViewAdapter);

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public Call<GetResualt> sendProductDetail(SendProductClass sendProductClass) {
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
    public Call<List<SendProductClass>> loadProduct(String companyId) {
        return null;
    }

    @Override
    public void onResponseLoadProduct(List<SendProductClass> sendProductClasses) {

    }

    @Override
    public Call<List<SendProductClass>> loadProduct(String companyId, String userID) {
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
    public Call<GetResualt> editProductDetail(SendProductClass sendProductClass) {
        return null;
    }

    @Override
    public Call<List<SendProductClass>> getUpdatedData(UpdatedProductBody updatedProductBody) {
        return null;
    }

    @Override
    public void recyclerViewListClicked(View v, String value, boolean notify) {
        adapter.getFilter().filter(value);
    }

    @Override
    public void recyclerViewCanUpdating() {
        record.setText("تعداد : " + String.valueOf(adapter.getItemCount()));
    }

    @Override
    public void imageAdapterCanUpdating(String imagePID) {

    }

    @Override
    public Call<GetResualt> deleteProduct(DeleteProduct deleteProduct) {
        return null;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        hashtagWord =BaseCodeClass.hashtagsValue;
        if (hashtagWord!=null){
            edSearch.setText(hashtagWord);
        }
    }
}
