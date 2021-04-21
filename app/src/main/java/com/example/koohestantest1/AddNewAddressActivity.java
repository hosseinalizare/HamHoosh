package com.example.koohestantest1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.Utils.JSONUtils;
import com.example.koohestantest1.Utils.ValidationCheck;
import com.example.koohestantest1.activity.MapsActivity;
import com.example.koohestantest1.model.IranCity;
import com.example.koohestantest1.model.IranProvince;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.classDirectory.AddressViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.PageShow;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.addressSaved;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myAppPage.saveAddress;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedAddress;

public class AddNewAddressActivity extends AppCompatActivity implements OnMapReadyCallback {


    private BaseCodeClass baseCodeClass;
    private String province;
    private AutoCompleteTextView edState, edCity;
    private EditText postalCode, edAddress, edPhoneNumber;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    private String addressId = "";
    private ImageView ivGoToMap;
    private TextView tvGoToMap;
    private int REQ_CODE_INTENT_ADD_MAP = 123;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastLocation;
    private LatLng lng;
    private ConstraintLayout constraintLayout;
    private FragmentContainerView fragmentContainer;
    private String finalLong;
    private String finalLat;

    private String TAG = AddNewAddressActivity.class.getSimpleName() + "Debug";
    private List<IranProvince> provinces = new ArrayList<>();
    private List<IranCity> cities;
    private List<String> filteredCities = new ArrayList<>();
    int provinceId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);

        baseCodeClass = new BaseCodeClass();


        edState = findViewById(R.id.StateEd);
        edCity = findViewById(R.id.cityEd);
        postalCode = findViewById(R.id.postalCodeTxt);
        edAddress = findViewById(R.id.AddressTxt);
        edPhoneNumber = findViewById(R.id.phoneNumber);
        progressBar = findViewById(R.id.pb_address);
        relativeLayout = findViewById(R.id.rl_address_fields);
        PageShow = BaseCodeClass.myAppPage.addressPage;
        tvGoToMap = findViewById(R.id.tv_add_map);
        ivGoToMap = findViewById(R.id.iv_add_map);
        constraintLayout = findViewById(R.id.cl_add_map);
        fragmentContainer = findViewById(R.id.map_add_address);

        loadJsonFiles();

        edState.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ArrayAdapter<IranProvince> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, provinces);
                edState.setAdapter(adapter);
                edState.showDropDown();
            }
//            } else {
//                if (!edState.getText().toString().equals("") && !edState.getText().toString().isEmpty()) {
//                    BaseCodeClass.selectedState = edState.getText().toString();
//                    PageShow = BaseCodeClass.myAppPage.addressPage;
//                }
//            }
        });

//        edState.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                ArrayAdapter<IranProvince> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, provinces);
//                adapter.setNotifyOnChange(true);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                BaseCodeClass.selectedState = edState.getText().toString();
//                PageShow = BaseCodeClass.myAppPage.addressPage;
//            }
//        });

        edCity.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                //reset selected province's cities
                if (filteredCities.size()>0)
                    filteredCities.clear();

                String selectedProvince = edState.getText().toString();

                Observable<IranCity> observable = Observable.fromIterable(cities)
                        .filter(iranCity -> {
                                    Log.d(TAG, "filter: " + provinceId);
                                    return iranCity.getProvince_id() == provinceId;

                                }
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

                Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
                    for (IranProvince iranProvince :
                            provinces) {
                        if (iranProvince.getTitle().equals(selectedProvince)) {
                            provinceId = iranProvince.getId();
                            emitter.onNext(iranProvince.getId());
                            break;
                        }

                    }
                    emitter.onComplete();
                })
                        .switchMap(integer -> observable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IranCity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull IranCity iranCity) {
                        Log.d(TAG, "onNext: " + iranCity);
                        filteredCities.add(iranCity.getTitle());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        addCityDropDowns();
                    }
                });

            }
        });

        ivGoToMap.setOnClickListener(v -> {
//            goToAddLocation();
        });

        tvGoToMap.setOnClickListener(v -> {
//            goToAddLocation();
        });

        getPassedData();

    }

    private void addCityDropDowns() {
        Log.d(TAG, "addCityDropDowns: " + filteredCities.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, filteredCities);
        edCity.setAdapter(adapter);
        edCity.showDropDown();
    }

    private void loadJsonFiles() {
        String jsonFileString = JSONUtils.getJsonFromAssets(getApplicationContext(), "province.json");
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<IranProvince>>() {
        }.getType();
        provinces = gson.fromJson(jsonFileString, listUserType);

        if (provinces == null)
            provinces = new ArrayList<>();

        String jsonFileStringCity = JSONUtils.getJsonFromAssets(getApplicationContext(), "cities.json");
        Gson gsonCity = new Gson();
        Type cityType = new TypeToken<List<IranCity>>() {
        }.getType();
        cities = gsonCity.fromJson(jsonFileStringCity, cityType);
        Log.d(TAG, "loadJsonFiles: " + cities.size());
    }

    private void goToAddLocation() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, REQ_CODE_INTENT_ADD_MAP);
    }

    private void getPassedData() {
        Intent passedData = getIntent();
        String addId = passedData.getStringExtra(AddAddressActivity.KEY_INTENT_EDIT);
        addressId = addId;
        setUpPassedData(addId);
    }

    private void setUpPassedData(String id) {

        if (addressId != null) {
            if (selectedAddress.getState() != null)
                edState.setText(selectedAddress.getState());
            if (selectedAddress.getCity() != null)
                edCity.setText(selectedAddress.getCity());

            if (selectedAddress.getPostalCode() != null) {
                Log.d(TAG, "setUpPassedData: " + selectedAddress.getPostalCode());
                postalCode.setText(selectedAddress.getPostalCode());
            }
            if (selectedAddress.getAddress1() != null)
                edAddress.setText(selectedAddress.getAddress1());

            if (selectedAddress.getPhoneNumber() != null)
                edPhoneNumber.setText(selectedAddress.getPhoneNumber());

            if (selectedAddress.getLocation() != null) {
                String[] mapData = selectedAddress.getLocation().split(":");
                Log.d(TAG, "setUpPassedData: " + mapData[0] + " " + mapData[1]);
                if (!mapData[0].equals("null") && !mapData[1].equals("null")) {
                    Log.d(TAG, "setUpPassedData: 8");
                    finalLat = mapData[0];
                    finalLong = mapData[1];
                    lng = new LatLng(Double.parseDouble(finalLat), Double.parseDouble(finalLong));
                    setUpMap();
                }
            }
        }
    }

    public void btnNewAddressCancel(View view) {
        try {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        } catch (Exception e) {
            logMessage("AddNewAddress 899 >> " + e.getMessage(), this);
        }
    }

    public void btnNewAddressAddClick(View view) {
        try {
            String state = edState.getText().toString();
            if (isEmpty(state)) {
                Toast.makeText(this, "نام استان نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
                return;
            }

            String city = edCity.getText().toString();
            if (isEmpty(city)) {
                Toast.makeText(this, "نام شهر نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
                return;
            }
            String address = edAddress.getText().toString();
            if (isEmpty(address)) {
                Toast.makeText(this, "آدرس نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
                return;
            }

            String postalCodeFinal = postalCode.getText().toString();
            if (!ValidationCheck.postalCodeValidation(postalCodeFinal)){
                Toast.makeText(this, "کد پستی نا معتبر است", Toast.LENGTH_LONG).show();
                return;
            }

            String phone = edPhoneNumber.getText().toString();
            if (!ValidationCheck.mobileValidation(phone)){
                if (!ValidationCheck.phoneValidation(phone)){
                    Toast.makeText(this, "شماره تلفن نا معتبر است", Toast.LENGTH_LONG).show();
                    return;

                }
            }







            relativeLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            String mapDestination = finalLat + ":" + finalLong;


            Log.d(TAG, "btnNewAddressAddClick: " + postalCodeFinal);
            BaseCodeClass.addressViewModel = new AddressViewModel(addressId, baseCodeClass.getUserID(), baseCodeClass.getToken(),
                    baseCodeClass.getUserID(), "Iran", city, state, postalCodeFinal,
                    "14", mapDestination, "NiroHavaie", "5", "2",
                    address, phone);

            PageShow = saveAddress;

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);

            Handler handler = new Handler();

            handler.postDelayed(() -> {
                if (addressSaved) {
                    finish();
                } else {

                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    logMessage("اطلاعات شما ثبت نشد دوباره تلاش کنید", AddNewAddressActivity.this);
                }
            }, 2000);

        } catch (Exception e) {
            logMessage("404 >> " + e.getMessage(), AddNewAddressActivity.this);
        }
    }

    private boolean isEmpty(String content) {
        if (content == null || content.equals(""))
            return true;
        else return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_INTENT_ADD_MAP) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    double mapLat = data.getDoubleExtra(MapsActivity.INTENT_KEY_LAT, -1);
                    double mapLong = data.getDoubleExtra(MapsActivity.INTENT_KEY_LONG, -1);
                    finalLat = String.valueOf(mapLat);
                    finalLong = String.valueOf(mapLong);
                    lng = new LatLng(mapLat, mapLong);
                    setUpMap();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "آدرسی انتخاب نشد!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void setUpMap() {
        constraintLayout.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_add_address);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(lng).title("آدرس انتخاب شده"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 15.0f));
        mMap.getUiSettings().setZoomControlsEnabled(false);
    }
}
