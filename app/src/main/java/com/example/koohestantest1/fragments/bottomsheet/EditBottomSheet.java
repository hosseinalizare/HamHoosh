package com.example.koohestantest1.fragments.bottomsheet;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.koohestantest1.AddProductActivity;
import com.example.koohestantest1.EditFieldActivity;
import com.example.koohestantest1.Utils.NumberTextChanger;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.classDirectory.SendProductClass;
import com.example.koohestantest1.databinding.BottomSheetEditBinding;
import com.example.koohestantest1.model.DeleteProduct;
import com.example.koohestantest1.model.EditBodyRequest;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.ProductViewModel;
import com.example.koohestantest1.viewModel.ProfileSharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.koohestantest1.ApiDirectory.CompanyApi;
import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.ApiDirectory.UserProfileApi;
import com.example.koohestantest1.ViewModels.CompanyProfileFieldViewModel;
import com.example.koohestantest1.classDirectory.AllProductData;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.PageShow;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myAppPage.myProfile;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProfileField;

public class EditBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetEditBinding binding;
    private BaseCodeClass baseCodeClass;
    private CompanyApi companyApi;
    private UserProfileApi userProfileApi;
    private Retrofit retrofit;
    private String TAG = EditBottomSheet.class.getSimpleName() + "Debug";

    public static String PARAM_KEY_STATE = "PARAM_EDIT_STATE";
    public static String PARAM_KEY_PRICE = "PARAM_KEY_PRICE";
    public static String PARAM_KEY_PRODUCT_ID = "PARAM_PRODUCT_ID";
    private String MARRIED = "متاهل";
    private String SINGLE = "مجرد";
    private String bornDate;
    private ProfileSharedViewModel profileSharedViewModel;
    private ProductViewModel productViewModel;
    String pid;

    private boolean shouldRefresh = false;
    private static BaseCodeClass.productFieldEnum mode = null;
    private static String value = null;
    private static AllProductData productData;
    private static SendProductClass productData2;
   /* public static EditBottomSheet onNewInstance(int state, String price, String productId) {
        EditBottomSheet editBottomSheet = new EditBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_KEY_STATE, state);
        bundle.putString(PARAM_KEY_PRICE, price);
        bundle.putString(PARAM_KEY_PRODUCT_ID, productId);

        editBottomSheet.setArguments(bundle);
        return editBottomSheet;
    }*/

    public static EditBottomSheet onNewInstance(int state, String myValue, String productId, BaseCodeClass.productFieldEnum myMode, AllProductData allProductData) {
        mode = myMode;
        value = myValue;
        productData = allProductData;
        EditBottomSheet editBottomSheet = new EditBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_KEY_STATE, state);
        bundle.putString(PARAM_KEY_PRICE, myValue);
        bundle.putString(PARAM_KEY_PRODUCT_ID, productId);

        editBottomSheet.setArguments(bundle);
        return editBottomSheet;
    }



    public static EditBottomSheet onNewInstance2(int state, String myValue, String productId, BaseCodeClass.productFieldEnum myMode, SendProductClass sendProductClass) {
        mode = myMode;
        value = myValue;
        productData2 = sendProductClass;
        EditBottomSheet editBottomSheet = new EditBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_KEY_STATE, state);
        bundle.putString(PARAM_KEY_PRICE, myValue);
        bundle.putString(PARAM_KEY_PRODUCT_ID, productId);

        editBottomSheet.setArguments(bundle);
        return editBottomSheet;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = RetrofitInstance.getRetrofit();
        baseCodeClass = new BaseCodeClass();
        companyApi = retrofit.create(CompanyApi.class);
        userProfileApi = retrofit.create(UserProfileApi.class);
        profileSharedViewModel = new ViewModelProvider(requireActivity()).get(ProfileSharedViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetEditBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView: ");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        setUpView();

    }

    private void setUpView() {
        if (getArguments() == null) {

            //(1)user comes from profile

            binding.tvEditTitle.setText(selectedProfileField.Explain);
            binding.etEdit.setText(selectedProfileField.Value);

            Log.d(TAG, "onViewCreated:  " + selectedProfileField.type);

            //for editing numbers
            if (selectedProfileField.type.equals(BaseCodeClass.variableType.date)) {
                binding.etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            //for editing birthday
            if (selectedProfileField.type.equals(BaseCodeClass.variableType.date)) {
                binding.etEdit.setVisibility(View.GONE);
                binding.tvBornDate.setVisibility(View.VISIBLE);
                if (selectedProfileField.Value == null)
                    binding.tvBornDate.setText("تاریخی انتخاب نشده");

                binding.btnChooseDate.setVisibility(View.VISIBLE);
            }

            //for editing marriage status
            if (selectedProfileField.type.equals(BaseCodeClass.variableType.marital)) {
                if (selectedProfileField.getValue().trim().equals(MARRIED)) {
                    Log.d(TAG, "onViewCreated: married");
                    binding.rbMarried.setChecked(true);
                } else if (selectedProfileField.getValue().trim().equals(SINGLE)) {
                    Log.d(TAG, "onViewCreated: ");
                    binding.rbSingle.setChecked(true);
                }
                binding.etEdit.setVisibility(View.GONE);
                binding.rgGroup.setVisibility(View.VISIBLE);
            }


            binding.btnChooseDate.setOnClickListener(v -> {
                PersianDatePickerDialog picker = new PersianDatePickerDialog(requireContext())
                        .setPositiveButtonString("باشه")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1300)
                        .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                        .setActionTextColor(Color.GRAY)
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(new Listener() {
                            @Override
                            public void onDateSelected(PersianCalendar persianCalendar) {
                                Log.d(TAG, "onDateSelected: " + persianCalendar.getGregorianChange());//Fri Oct 15 03:25:44 GMT+04:30 1582
                                Log.d(TAG, "onDateSelected: " + persianCalendar.getTimeInMillis());//1583253636577
                                Log.d(TAG, "onDateSelected: " + persianCalendar.getTime());//Tue Mar 03 20:10:36 GMT+03:30 2020
                                Log.d(TAG, "onDateSelected: " + persianCalendar.getDelimiter());//  /
                                Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDate());// سه‌شنبه  13  اسفند  1398
                                Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDateAndTime()); //سه‌شنبه  13  اسفند  1398 ساعت 20:10:36
                                Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianMonthName()); //اسفند
                                Log.d(TAG, "onDateSelected: " + persianCalendar.isPersianLeapYear());//false
//                                bornDate = persianCalendar.getPersianYear(+"/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay();
                                Date date = new Date(persianCalendar.getTimeInMillis());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                bornDate = sdf.format(date);

                                binding.tvBornDate.setText(persianCalendar.getPersianLongDate());

                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            });

        } else {
            pid = getArguments().getString(PARAM_KEY_PRODUCT_ID);


            //(2) user comes from price editing
            if (mode != null) {
                switch (mode) {
                    case ProductName:
                        binding.tvEditTitle.setText("به روز زسانی نام محصول");
                        binding.etEdit.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case CompanyID:
                        Log.d("Temporary", "CompanyID");
                        break;
                    case SupplierID:
                        Log.d("Temporary", "SupplierID");
                        break;
                    case ProductID:
                        Log.d("Temporary", "ProductID");
                        break;
                    case Description:
                        Log.d("Temporary", "Description");
                        break;
                    case StandardCost:
                        binding.tvEditTitle.setText("به روز زسانی قیمت محصول");
                        binding.etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case ListPrice:
                        Log.d("Temporary", "ListPrice");
                        break;
                    case ReorderLevel:
                        Log.d("Temporary", "ReorderLevel");
                        break;
                    case TargetLevel:
                        Log.d("Temporary", "TargetLevel");
                        break;
                    case Unit:
                        Log.d("Temporary", "Unit");
                        break;
                    case QuantityPerUnit:
                        /*binding.tvEditTitle.setText("به روز رسانی تعداد محصول");
                        binding.etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);*/
                        break;
                    case Discontinued:
                        binding.tvEditTitle.setText("به روز رسانی تعداد محصول");
                        binding.etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case MinimumReorderQuantity:
                        Log.d("Temporary", "MinimumReorderQuantity");
                        break;
                    case Category:
                        Log.d("Temporary", "Category");
                        break;
                    case Show:
                        binding.tvEditTitle.setText("مدیریت نمایش محصول");
                        binding.rbMarried.setText("نمایش محصول");
                        binding.rbSingle.setText("عدم نمایش محصول");
                        if (value.equals("true")) {
                            binding.rbMarried.setChecked(true);
                            binding.rbSingle.setChecked(false);
                        } else if (value.equals("false")) {
                            binding.rbMarried.setChecked(false);
                            binding.rbSingle.setChecked(true);
                        }
                        binding.etEdit.setVisibility(View.GONE);
                        binding.rgGroup.setVisibility(View.VISIBLE);
                        break;
                    case Deleted:
                        binding.tvEditTitle.setText("آیا از حذف این محصول مطمئن هستید؟");
                        binding.etEdit.setVisibility(View.INVISIBLE);
                        binding.rgGroup.setVisibility(View.GONE);
                        break;
                }
            } else {
                binding.tvEditTitle.setText("به روز رسانی قیمت");
                binding.etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            String mobileLangFormat = Locale.getDefault().getLanguage();
            //we should add comma otherwise persian font(koodak) does it for us
            if (!mobileLangFormat.equals("fa"))
                binding.etEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (mode != BaseCodeClass.productFieldEnum.ProductName)
                            new NumberTextChanger(binding.etEdit);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            binding.etEdit.setText(getArguments().getString(PARAM_KEY_PRICE));


            productViewModel.getLiveErrorEditProduct().observe(getViewLifecycleOwner(), s -> {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            });

            productViewModel.getLiveResultEditProduct().observe(getViewLifecycleOwner(), resualt -> {
                if (shouldRefresh) {
                    shouldRefresh = false;
                    if (resualt.getResualt().equals("100")) {

                        editSuccess(mode);
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "در حال حاضر امکان تغییر فیلد وجود ندارد", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        binding.ivClose.setOnClickListener(v -> {
            dismiss();
        });

        binding.ivDone.setOnClickListener(v -> {
            try {
                if (selectedProfileField.mode == BaseCodeClass.editMode.companyProfile) {
                    editCompany();
                } else if (selectedProfileField.mode == BaseCodeClass.editMode.userProfile) {
                    editUSer();
                } else if (mode != null) {
                    String value = binding.etEdit.getText().toString();
                    shouldRefresh = true;
                    // user comes from updating product name
                    Log.d(TAG, "onViewCreated: " + value);
                    if (mode == BaseCodeClass.productFieldEnum.StandardCost) {
                        int price = (int) StringUtils.getNumberFromStringV2(binding.etEdit.getText().toString());
                        value = String.valueOf(price);
                    } else if (mode == BaseCodeClass.productFieldEnum.ProductName) {
                        value = StringUtils.getNumberFromStringV3(binding.etEdit.getText().toString());
                    } else if (mode == BaseCodeClass.productFieldEnum.Discontinued) {
                        value = StringUtils.getNumberFromStringV3(binding.etEdit.getText().toString());
                    } else if (mode == BaseCodeClass.productFieldEnum.Deleted) {
                        if (productData == null)
                            Toast.makeText(getContext(), "این محصول قبلا حذف شده است", Toast.LENGTH_SHORT).show();
                        else
                            deleteProduct(productData);

                        return;
                    } else {
                        if (binding.rbMarried.isChecked())
                            value = String.valueOf(true);
                        else
                            value = String.valueOf(false);
                    }
                    productViewModel.callForEditingProduct(new EditBodyRequest(baseCodeClass.getToken(), baseCodeClass.getUserID(), baseCodeClass.getCompanyID(), pid
                            , String.valueOf(mode), value));
                } else {
                    shouldRefresh = true;
                    // user comes from updating prices
                    int price = (int) StringUtils.getNumberFromStringV2(binding.etEdit.getText().toString());
                    String currentPrice = String.valueOf(price);
                    Log.d(TAG, "onViewCreated: " + currentPrice);
                    productViewModel.callForEditingProduct(new EditBodyRequest(baseCodeClass.getToken(), baseCodeClass.getUserID(), baseCodeClass.getCompanyID(), pid
                            , String.valueOf(BaseCodeClass.productFieldEnum.StandardCost), currentPrice));
                }

            } catch (Exception e) {
                logMessage("EditField 100 >> " + e.getMessage(), requireContext());
            }
        });
    }

    private void deleteProduct(AllProductData productData){
        LoadProductApi loadProductApi;
        loadProductApi = retrofit.create(LoadProductApi.class);
        DeleteProduct deleteProduct = new DeleteProduct();
        deleteProduct.setCompanyID(BaseCodeClass.CompanyID);
        deleteProduct.setProductID(productData.getProductClass().getProductID());
        deleteProduct.setToken(BaseCodeClass.token);
        deleteProduct.setUserID(BaseCodeClass.userID);
        Call<GetResualt> call = loadProductApi.deleteProduct(deleteProduct);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                String result = response.body().getResualt();
                String msg = response.body().getMsg();
                if (result.equals("100")) {
                    Toast.makeText(getContext(), "محصول با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
                    dismiss();

                } else {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d("Error", t.toString());
            }
        });
    }

    private void editSuccess(BaseCodeClass.productFieldEnum mode) {
        String newValue = binding.etEdit.getText().toString();
        baseCodeClass.loadSelectedProduct(pid, requireContext());
        //productViewModel.setEditedValue(newValue);
        Toast.makeText(requireContext(), "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
        switch (mode) {
            case ProductName:

                selectedProduct.getProductClass().setProductName(newValue);
                break;
            case CompanyID:
                Log.d("Temporary", "CompanyID");
                break;
            case SupplierID:
                Log.d("Temporary", "SupplierID");
                break;
            case ProductID:
                Log.d("Temporary", "ProductID");
                break;
            case Description:
                Log.d("Temporary", "Description");
                break;
            case StandardCost:
                selectedProduct.getProductClass().setStandardCost(newValue);
                productViewModel.setEditedValue(newValue);
                break;
            case ListPrice:
                Log.d("Temporary", "ListPrice");
                break;
            case ReorderLevel:
                Log.d("Temporary", "ReorderLevel");
                break;
            case TargetLevel:
                Log.d("Temporary", "TargetLevel");
                break;
            case Unit:
                Log.d("Temporary", "Unit");
                break;
            case QuantityPerUnit:
                Log.d("Temporary", "QuantityPerUnit");
                break;
            case Discontinued:
                selectedProduct.getProductClass().setDiscontinued(Integer.valueOf(newValue));
                break;
            case MinimumReorderQuantity:
                Log.d("Temporary", "MinimumReorderQuantity");
                break;
            case Category:
                Log.d("Temporary", "Category");
                break;
            case Show:
                Log.d("Temporary", "Show");
                break;
            case Deleted:
                Log.d("Temporary", "Deleted");
                break;
        }
    }


    public void editCompany() {
        try {
            Log.d(TAG, "editCompany: ");
            Call<GetResualt> call = companyApi.updateCompanyProfileField(new CompanyProfileFieldViewModel(baseCodeClass.getUserID(),
                    baseCodeClass.getToken(),
                    baseCodeClass.getCompanyID(),
                    binding.etEdit.getText().toString(),
                    String.valueOf(selectedProfileField.getCenum())));
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                    if (response.body().getResualt().equals("100")) {
                        Toast.makeText(requireContext(), "با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    Log.d(TAG, "onResponse: " + response.body().getMsg());
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    baseCodeClass.logMessage("onFailure >> " + t.getMessage(), requireContext());
                }
            });
        } catch (Exception e) {
            logMessage("EditField 100 >> " + e.getMessage(), requireContext());
        }
    }

    public void editUSer() {
        try {

            //get date for editing born date
            String value = bornDate != null ? bornDate : binding.etEdit.getText().toString();

            //get marital for editing its status
            if (selectedProfileField.type.equals(BaseCodeClass.variableType.marital)) {
                value = binding.rbMarried.isChecked() ? MARRIED : SINGLE;
            }

            Log.d(TAG, "editUSer: " + value);

            Call<GetResualt> call = userProfileApi.updateUserProfileField(new CompanyProfileFieldViewModel(baseCodeClass.getUserID(),
                    baseCodeClass.getToken(),
                    baseCodeClass.getUserID(),
                    value,
                    String.valueOf(selectedProfileField.getCenum())));
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                    if (response.body().getResualt().equals("100")) {
                        PageShow = myProfile;
                        profileSharedViewModel.shouldRefresh(true);
                        dismiss();
                    }
                    Log.d(TAG, "onResponse: " + response.body().getMsg());
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    baseCodeClass.logMessage("onFailure >> " + t.getMessage(), requireContext());
                }
            });
        } catch (Exception e) {
            logMessage("EditField 100 >> " + e.getMessage(), requireContext());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
        binding = null;
    }
}
