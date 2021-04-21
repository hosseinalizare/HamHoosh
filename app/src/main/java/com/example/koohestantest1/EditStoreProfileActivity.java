package com.example.koohestantest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.CallBackComoanyProfile;
import com.example.koohestantest1.InfoDirectory.GetOnlineInformationClass;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CompanyProfile;
import com.example.koohestantest1.classDirectory.EditProfileField;
import com.example.koohestantest1.classDirectory.EditProfileRecyclerViewAdapter;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.companyProfile;

public class EditStoreProfileActivity extends AppCompatActivity {

    private List<EditProfileField> fields = new ArrayList<>();

    private String TAG = EditStoreProfileActivity.class.getSimpleName();
    BaseCodeClass baseCodeClass;
    BaseCodeClass.CompanyEnum companyField=BaseCodeClass.CompanyEnum.NONE;
    CompanyProfile companyView;
Context context;
    GetOnlineInformationClass getOnlineInformationClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store_profile);


        try {
            context = this;
            getOnlineInformationClass = new GetOnlineInformationClass(this);

            baseCodeClass = new BaseCodeClass();
            companyView = companyProfile;
            companyField = BaseCodeClass.CompanyEnum.SettingMenu;
            if(getIntent().hasExtra("mode")) {
                String mod = getIntent().getStringExtra("mode");

                if(mod.equals(BaseCodeClass.CompanyEnum.SettingMenu.toString())) companyField = BaseCodeClass.CompanyEnum.SettingMenu;
                else if(mod.equals(BaseCodeClass.CompanyEnum.CallInfo.toString())) companyField = BaseCodeClass.CompanyEnum.CallInfo;
                else if(mod.equals(BaseCodeClass.CompanyEnum.money.toString())) companyField = BaseCodeClass.CompanyEnum.money;
                else if(mod.equals(BaseCodeClass.CompanyEnum.AddresMenu.toString())) companyField = BaseCodeClass.CompanyEnum.AddresMenu;
            }
                    Log.d(TAG,companyField.toString());
            fillProfile();
        } catch (Exception ex) {
            baseCodeClass.logMessage("onCreate >> " + ex.getMessage(), this);
        }
    }

    public void fillProfile() {
        try {

            fields.clear();
            EditProfileField profileField;
            for (BaseCodeClass.CompanyEnum c : BaseCodeClass.CompanyEnum.values()
            ) {
                profileField = GetProfileField(c);

                if(profileField != null)
                if (!fields.contains(profileField)) {

                    profileField.setExplain(profileField.getExplain()+" ");
                    profileField.setValue(profileField.getValue()+" ");

                    fields.add(profileField);
                }
            }
        } catch (Exception ex) {
            baseCodeClass.logMessage("fillProfile >> " + ex.getMessage(), this);
        }
        initRecyclerView();
    }
    EditProfileRecyclerViewAdapter adapter=null;
    private void initRecyclerView() {
        try {
            if(adapter==null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.editStoreProfile_RecyclerView);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new EditProfileRecyclerViewAdapter(fields, this, getSupportFragmentManager());
                recyclerView.setAdapter(adapter);
            }
            else
            {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            baseCodeClass.logMessage("initRecyclerView >> " + ex.getMessage(), this);
        }
    }

    public void closeBtn(View view) {
        finish();
    }

    public void saveProfile(View view) {
    }
    CallBackComoanyProfile callBackComoanyProfile;
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        try {
            baseCodeClass.LoadBaseData(this);
            getOnlineInformationClass.readCompany(baseCodeClass.getCompanyID(), new CallBackComoanyProfile() {
                @Override
                public void OnReciveData(CompanyProfile cp) {
                    companyView = cp;
                    fillProfile();
                }

                @Override
                public void OnFaile() {
                    baseCodeClass.logMessage(" >>OnFaile call back  ", context);
                }
            });


        } catch (Exception ex) {
            baseCodeClass.logMessage("onCreate >> " + ex.getMessage(), this);
        }
    }

    public EditProfileField GetProfileField(BaseCodeClass.CompanyEnum Cenum_) {
        try {
            switch (Cenum_) {
                case CompanyName:
                    if (companyField == BaseCodeClass.CompanyEnum.SettingMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "نام فروشگاه",
                                companyView.getCompanyName(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Bio:
                    if (companyField == BaseCodeClass.CompanyEnum.SettingMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "توضيحات",
                                companyView.getBio(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case EmailAddress:
                    if (companyField == BaseCodeClass.CompanyEnum.CallInfo)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "ايميل",
                                companyView.getEmailAddress(),
                                "",
                                BaseCodeClass.variableType.email,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case MobilePhone:
                    if (companyField == BaseCodeClass.CompanyEnum.CallInfo)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "همراه مالک",
                                companyView.getMobilePhone(),
                                "",
                                BaseCodeClass.variableType.mobile,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case BusinessPhone:
                    if (companyField == BaseCodeClass.CompanyEnum.CallInfo)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "تلفن فروشگاه",
                                companyView.getBusinessPhone(),
                                "",
                                BaseCodeClass.variableType.phoneNumber,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case FaxNumber:
                    if (companyField == BaseCodeClass.CompanyEnum.CallInfo)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "شماره فکس",
                                companyView.getFaxNumber(),
                                "",
                                BaseCodeClass.variableType.phoneNumber,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case NationalCode:
                    if (companyField == BaseCodeClass.CompanyEnum.SettingMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "شناسه ملي",
                                companyView.getNationalCode(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case RegistrationNumber:
                    if (companyField == BaseCodeClass.CompanyEnum.SettingMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "شماره ثبت",
                                companyView.getRegistrationNumber(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case DateOfRegistration:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "تاريخ ثبت",
                                String.valueOf(companyView.getDateOfRegistration()),
                                "",
                                BaseCodeClass.variableType.date,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case EconomicaNumber:
                    if (companyField == BaseCodeClass.CompanyEnum.money)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "شماره کارت",
                                companyView.getEconomicaNumber(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case CreationDate:
                    if (companyField == BaseCodeClass.CompanyEnum.SettingMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "تاريخ ايجاد فروشگاه",
                                String.valueOf(companyView.getCreationDate()),
                                "",
                                BaseCodeClass.variableType.datetime,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case WebPage:
                    if (companyField == BaseCodeClass.CompanyEnum.CallInfo)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "تارنما",
                                companyView.getWebPage(),
                                "",
                                BaseCodeClass.variableType.webPage,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Referredby:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "معرفي کننده",
                                companyView.getReferredby(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Privasy:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "خصوصي",
                                "",
                                "",
                                BaseCodeClass.variableType.bool_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Union:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "واحد",
                                companyView.getUnion(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case ObjectID:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "ايدي آبجکت",
                                companyView.getObjectID(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Country:
                    if (companyField == BaseCodeClass.CompanyEnum.AddresMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "کشور",
                                companyView.getCountry(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case City:
                    if (companyField == BaseCodeClass.CompanyEnum.AddresMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "شهر",
                                companyView.getCity(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case State:
                    if (companyField == BaseCodeClass.CompanyEnum.AddresMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "خيابان",
                                companyView.getCity(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case PostalCode:
                    if (companyField == BaseCodeClass.CompanyEnum.AddresMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "کد پستي",
                                companyView.getPostalCode(),
                                "",
                                BaseCodeClass.variableType.number,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Area:
                    if (companyField == BaseCodeClass.CompanyEnum.AddresMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "منطقه",
                                companyView.getArea(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Location:
                    if (companyField == BaseCodeClass.CompanyEnum.AddresMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "موقعيت جغرافيايي",
                                companyView.getLocation(),
                                "",
                                BaseCodeClass.variableType.location,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Neighborhood:
                    if (companyField == BaseCodeClass.CompanyEnum.AddresMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "محله",
                                companyView.getNeighborhood(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Address:
                    if (companyField == BaseCodeClass.CompanyEnum.AddresMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "آدرس",
                                companyView.getAddress(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case CompanyType:
                    if (companyField == BaseCodeClass.CompanyEnum.SettingMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "نوع فروشگاه",
                                companyView.getCompanyType() + "",
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case UpdateDate:
                    if (companyField == BaseCodeClass.CompanyEnum.SettingMenu)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "تاريخ به روز رساني",
                                String.valueOf(companyView.getUpdateDate()),
                                "",
                                BaseCodeClass.variableType.datetime,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Deleted:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "حذف شده",
                                String.valueOf(companyView.getDeleted()),
                                "",
                                BaseCodeClass.variableType.bool_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case HasCourier:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "داراي پيک",
                                String.valueOf(companyView.getHasCourier()),
                                "",
                                BaseCodeClass.variableType.bool_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case StartTime:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "ساعت شروع به کار روزانه",
                                companyView.getStartTime(),
                                "",
                                BaseCodeClass.variableType.time,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case EndTime:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "ساعت پايان کار روزانه",
                                companyView.getEndTime(),
                                "",
                                BaseCodeClass.variableType.time,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case ActiveDayID:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "روزهاي غعال درهفته",
                                companyView.getActiveDayID(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Spare1:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "1",
                                companyView.getSpare1(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Spare2:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "2",
                                companyView.getSpare2(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case Spare3:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "3",
                                companyView.getSpare3(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case TimeToCourier:
                    if (companyField == BaseCodeClass.CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "زمان ارسال",
                                companyView.getTimeToCourier(),
                                "",
                                BaseCodeClass.variableType.datetime,
                                BaseCodeClass.editMode.companyProfile);
                    break;
                case AddresMenu:
                    break;

                case SettingMenu:
                    break;

                case CallInfo:
                    break;

                case money:
                    break;
            }
        } catch (Exception ex) {

        }
        return null;
    }
}
