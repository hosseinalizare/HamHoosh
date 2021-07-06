package com.example.koohestantest1.classDirectory;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.activity.LoadingActivity;
import com.example.koohestantest1.model.Event;
import com.example.koohestantest1.model.UpdateModeModel;
import com.example.koohestantest1.model.network.RetrofitInstance;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.example.koohestantest1.ApiDirectory.UpdateModeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.koohestantest1.classDirectory.BaseCodeClass.AppEvent;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static com.example.koohestantest1.Utils.Cache.TAG;

public class AppService extends Service {
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    private Timer timer;
    private Call<List<UpdateModeModel>> call;
    private Call<String> stringCall;
    private Call<String> stringCallGetUID;
    private String companyId;
    private String userId = null;
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
        applicationIsRun = BaseCodeClass.userID;
        companyId = new String(BaseCodeClass.CompanyID);
        stringCall = RetrofitInstance.getRetrofit().create(UpdateModeApi.class).sendUserId(BaseCodeClass.userID);
        userId = SharedPreferenceUtils.getUserId(AppService.this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        repeateJobe();
        return START_REDELIVER_INTENT;
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
                                    Random rand = new Random();
                                    String notId = String.valueOf(rand.nextInt(100));
                                    for (UpdateModeModel updateModeModel : response.body()) {

                                        switch (initEvent(updateModeModel.getEventId())) {
                                            case UserMSG:
                                                showNotif(updateModeModel.getTitle(), updateModeModel.getMsge(), MessageActivity.class, notId,updateModeModel.getAction());
                                                break;
                                            case CompanyMSG:
                                                showNotif(updateModeModel.getTitle(), updateModeModel.getMsge(), ListMessageActivity.class, notId,updateModeModel.getAction());
                                                break;
                                            case CompanyOrder:
                                                showNotif(updateModeModel.getTitle(), updateModeModel.getMsge(), MyStoreOrderListActivity.class, notId,updateModeModel.getAction());
                                                break;
                                            case UserOrder:
                                                showNotif(updateModeModel.getTitle(), updateModeModel.getMsge(), MyStoreReportActivity.class, notId,updateModeModel.getAction());
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
                            }

                            @Override
                            public void onFailure(Call<List<UpdateModeModel>> call, Throwable t) {
                                Log.i("AppServiceClasss >>>", "AppServiceClass >>> " + t.getMessage());
                            }
                        });
                    } catch (Exception exception) {
                        Log.i("LOG", exception.getMessage().toString());
                    }

                });
            }
        }, 0, 10000);
    }


    private void showNotifSabegh(String title, String description, Class targetActivity, String channelId) {
        try {
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
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.logo))
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#2f2f2f"))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);

            notificationManager.notify(Integer.parseInt(channelId), builder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void showNotif(String title, String description, Class targetActivity, String channelId,String action) {
        try {
            Intent intent;


            if (applicationIsRun != null) {
                if (description.contains("سفارش جدید")){
                    intent = new Intent(this, MyStoreOrderListActivity.class);

                }else if (title.contains("لغو شده توسط فروشگاه") || title.contains("در حال آماده") || title.contains("در حال ارسال") || title.contains("آماده تحویل") || title.contains("تحویل شده")){
                    intent = new Intent(this, MyStoreReportActivity.class);
                }else {
                    intent = new Intent(this, targetActivity);

                }
            } else {
                intent = new Intent(this, LoadingActivity.class);
            }

            intent.putExtra("sender", userId);
            //getter = company(others)
            intent.putExtra("getter", companyId);
            intent.putExtra("state_message_sender", 0);
            intent.putExtra("status", action);

            intent.putExtra("mode", "user");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


            NotificationCompat.Builder notificationBuilder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder = new NotificationCompat.Builder(this, channelId);
                NotificationChannel notificationChannel = new NotificationChannel(channelId, TAG, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableVibration(true);
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
            } else {
                notificationBuilder = new NotificationCompat.Builder(this);
            }
            notificationBuilder
                    .setContentTitle(title)
                    .setContentText(description)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                     .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setColor(Color.RED);

            notificationBuilder.setDefaults(DEFAULT_VIBRATE);
            notificationBuilder.setLights(Color.YELLOW, 1000, 300);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, TAG,
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
                startService(intent);

               /* Notification notification = new Notification.Builder(getApplicationContext(), channelId).build();
                startForeground(Integer.parseInt(channelId), notification);*/
            } else {
               /* NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
                 startForeground(Integer.parseInt(channelId), builder.build());*/
                 startService(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.logo))
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


}
