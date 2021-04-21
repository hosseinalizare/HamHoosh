package com.example.koohestantest1.classDirectory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.koohestantest1.ListMessageActivity;
import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.MyStoreOrderListActivity;
import com.example.koohestantest1.MyStoreReportActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.SharedPreferenceUtils;
import com.example.koohestantest1.activity.LoadingActivity;
import com.example.koohestantest1.model.Event;
import com.example.koohestantest1.model.UpdateModeModel;
import com.example.koohestantest1.model.network.RetrofitInstance;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.koohestantest1.ApiDirectory.UpdateModeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.koohestantest1.classDirectory.BaseCodeClass.AppEvent;

public class AppService extends Service {
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    private Timer timer;
    private Call<List<UpdateModeModel>> call;
    private Call<String> stringCall;
    private Call<String> stringCallGetUID;
    private String companyId;
    private String userId;
    private String applicationIsRun;
    private Handler handler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        handler = new Handler(Looper.getMainLooper());
        userId = SharedPreferenceUtils.getUserId(AppService.this);
        applicationIsRun = BaseCodeClass.userID;
        companyId = new String(BaseCodeClass.CompanyID);
        stringCall = RetrofitInstance.getRetrofit().create(UpdateModeApi.class).sendUserId(BaseCodeClass.userID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        repeateJobe();
        return START_STICKY;
    }

/*
    private void repeateJobe() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {


                    try {
                        call = RetrofitInstance.getRetrofit().create(UpdateModeApi.class).getUpdateMode();

                        call.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {

                                Toast.makeText(AppService.this, response.body() + "", Toast.LENGTH_SHORT).show();
                                Event event = parcCode(response.body());
                                if (event.isUserMSG()) {

                                    newMessageUserNotification("پیام جدید", "پیام جدید دارید");
                                }

                                if (event.isCompanyMSG()) {
                                    newMessageUserNotification("پیام جدید", "در فروشگاه پیام جدید دارید");

                                }

                                if (event.isCompanyOrder()) {

                                    newMessageUserNotification("سفارش جدید", "سفارش جدید دارید");

                                }

                                if (event.isUserOrder()) {

                                    newMessageUserNotification("تغییر وضعیت سفارش", "سفارش شما به مرحله بعد رفت");

                                }

                                if (event.isNotify()) {

                                }

                                if (event.isCalls()) {

                                }

                                if (event.isNewCompany()) {

                                }

                                if (event.isEmployeeUpdate()) {

                                }

                                if (event.isErrorReport()) {

                                }

                                if (event.isErrorReportAnswer()) {

                                }

                                if (event.isNewProduct()) {

                                }

                                if (event.isUserProfileChange()) {

                                }

                                if (event.isCompanyProfileChange()) {

                                }

                                if (event.isComment()) {

                                }

                                if (event.isAddress()) {

                                }

                                if (event.isNewVersion()) {

                                }

                                if (event.isOtherPersoLoginWhitthisUser()) {

                                }

                                if (event.isMustLogOut()) {

                                }

                                if (event.isAppMustUpdate()) {

                                }

                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.i("LOG", "Error UPDATE_MODEVM : " + t.getMessage());

                            }
                        });


                    } catch (Exception exception) {

                        Log.i("LOG", exception.getMessage().toString());

                    }


                });

            }
        }, 0, 30000);
    }
*/

    private void sendUserId() {
        try {
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (SharedPreferenceUtils.getUserId(AppService.this) == null) {
                        SharedPreferenceUtils.saveUserId(AppService.this, response.body());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("Error SendUserId: ", t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void getUserId() {
        try {
            stringCallGetUID.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(AppService.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    private Event parcCode(Integer integer) {
        Event event = new Event();
        event.setUserMSG(AnalizInt(AppEvent.UserMSG, integer));
        event.setCompanyMSG(AnalizInt(AppEvent.CompanyMSG, integer));
        event.setCompanyOrder(AnalizInt(AppEvent.CompanyOrder, integer));
        event.setUserOrder(AnalizInt(AppEvent.UserOrder, integer));

        return event;

    }

    private boolean AnalizInt(BaseCodeClass.AppEvent AE, Integer valu) {
        if (((valu >> AE.getValue()) & 0x01) == 1)
            return true;
        return false;
    }

    private void repeateJobe() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        call = RetrofitInstance.getRetrofit().create(UpdateModeApi.class).getUpdateMode2(userId, companyId);
                        call.enqueue(new Callback<List<UpdateModeModel>>() {
                            @Override
                            public void onResponse(Call<List<UpdateModeModel>> call, Response<List<UpdateModeModel>> response) {

                                if (response.body() != null) {
                                    for (UpdateModeModel updateModeModel : response.body()) {

                                        switch (initEvent(updateModeModel.getEventId())) {


                                            case UserMSG:
                                                showNotif(updateModeModel.getTitle(), updateModeModel.getMsge(), MessageActivity.class, "1");
                                                break;
                                            case CompanyMSG:
                                                showNotif(updateModeModel.getTitle(), updateModeModel.getMsge(), ListMessageActivity.class, "2");
                                                break;
                                            case CompanyOrder:
                                                showNotif(updateModeModel.getTitle(), updateModeModel.getMsge(), MyStoreOrderListActivity.class, "3");
                                                break;
                                            case UserOrder:
                                                showNotifUserOrder(updateModeModel.getTitle(), updateModeModel.getMsge(), MyStoreReportActivity.class, "4", updateModeModel.getAction());
                                                break;
                                            case Notify:
                                                break;
                                            case calls:
                                                break;
                                            case NewCompany:
                                                break;
                                            case EmployeeUpdate:
                                                break;
                                            case ErrorReport:
                                                break;
                                            case ErrorReportAnswer:
                                                break;
                                            case NewProduct:
                                                break;
                                            case UserProfileChange:
                                                break;
                                            case CompanyProfileChange:
                                                break;
                                            case Comment:
                                                break;
                                            case Address:
                                                break;
                                            case NewVersion:
                                                break;
                                            case OtherPersoLoginWhitthisUser:
                                                break;
                                            case MustLogOut:
                                                break;
                                            case AppMustUpdate:
                                                break;
                                        }
                                    }

                                }

               /*                 Event event = parcCode(5);
                                if (event.isUserMSG()) {

                                    newMessageUserNotification("پیام جدید", "پیام جدید دارید");
                                }

                                if (event.isCompanyMSG()) {
*//*
                                    newMessageCompanyNotification("پیام جدید", "در فروشگاه پیام جدید دارید");
*//*

                                }

                                if (event.isCompanyOrder()) {

                                    newMessageUserNotification("سفارش جدید", "سفارش جدید دارید");

                                }

                                if (event.isUserOrder()) {

                                    newMessageUserNotification("تغییر وضعیت سفارش", "سفارش شما به مرحله بعد رفت");

                                }

                                if (event.isNotify()) {

                                }

                                if (event.isCalls()) {

                                }

                                if (event.isNewCompany()) {

                                }

                                if (event.isEmployeeUpdate()) {

                                }

                                if (event.isErrorReport()) {

                                }

                                if (event.isErrorReportAnswer()) {

                                }

                                if (event.isNewProduct()) {

                                }

                                if (event.isUserProfileChange()) {

                                }

                                if (event.isCompanyProfileChange()) {

                                }

                                if (event.isComment()) {

                                }

                                if (event.isAddress()) {

                                }

                                if (event.isNewVersion()) {

                                }

                                if (event.isOtherPersoLoginWhitthisUser()) {

                                }

                                if (event.isMustLogOut()) {

                                }

                                if (event.isAppMustUpdate()) {

                                }*/
                            }

                            @Override
                            public void onFailure(Call<List<UpdateModeModel>> call, Throwable t) {

                            }
                        });


                    } catch (Exception exception) {

                        Log.i("LOG", exception.getMessage().toString());
                    }
                });

            }
        }, 0, 30000);
    }

    private void showNotif(String title, String description, Class targetActivity, String channelId) {
        Intent intent;
        if (applicationIsRun != null) {
            intent = new Intent(this, targetActivity);
        } else {
            intent = new Intent(this, LoadingActivity.class);
        }
        intent.putExtra("sender", userId);
        //getter = company(others)
        intent.putExtra("getter", companyId);
        intent.putExtra("state_message_sender", 0);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new
                    NotificationChannel(channelId, "NEW_MESSAGE",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher_khanat_round))
                .setAutoCancel(true)
                .setColor(Color.parseColor("#2f2f2f"))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        notificationManager.notify(Integer.parseInt(channelId), builder.build());
    }

    private void showNotifUserOrder(String title, String description, Class targetActivity, String channelId, String action) {
        Intent intent;
        if (applicationIsRun != null) {
            intent = new Intent(this, targetActivity);
            intent.putExtra("sender", userId);
            //getter = company(others)
            intent.putExtra("getter", companyId);
            intent.putExtra("state_message_sender", 0);
            intent.putExtra("status", action);
            intent.putExtra("mode", "user");

        } else {
            intent = new Intent(this, LoadingActivity.class);
        }
        /*intent.putExtra("sender", userId);
        //getter = company(others)
        intent.putExtra("getter", companyId);
        intent.putExtra("state_message_sender", 0);
        intent.putExtra("action", action);*/


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new
                    NotificationChannel(channelId, "NEW_MESSAGE",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher_khanat_round))
                .setAutoCancel(true)
                .setColor(Color.parseColor("#2f2f2f"))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        notificationManager.notify(Integer.parseInt(channelId), builder.build());
    }


    private AppEvent initEvent(int eventId) {
        for (AppEvent appEvent : AppEvent.values()) {
            if (appEvent.getValue() == eventId) {
                return appEvent;
            }

        }

        return AppEvent.calls;

    }


}
