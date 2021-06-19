package com.example.koohestantest1.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.koohestantest1.AddProductActivity;
import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.MyStoreOrderDetail;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.ViewProductActivity;
import com.example.koohestantest1.activity.ActivityVideoPlay;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.MessageRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.example.koohestantest1.model.FieldList;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_recycler_FragmentTabsProfile extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int PICTURE = 0;
    private final int VIDEO = 1;
    private final int FILE = 2;
    private final int OTHER = 3;
    private final int INFO = 4;
    private final int ORDER = 5;
    private final int SUPPORTERS = 6;
    private final int PRODUCT = 7;
    private final int MUSIC = 8;

    private Context context;
    private List<FieldList> fieldLists;
    private BaseCodeClass baseCodeClass;
    public final static String STATE_MESSAGE_SENDER = "state_message_sender";
    public final static int REGULAR_USER = 0;
    private MediaPlayer mediaPlayer;
    private YoYo.YoYoString yoYoString;
    private Map<Integer, CircularImageView> cr_musics;
    private int playPosition = -1;
    private boolean play = false;
    private boolean play2 = false;
    private boolean myStore = true;
    private String childDirectory;


    public Adapter_recycler_FragmentTabsProfile(Context context, List<FieldList> fieldLists) {
        this.context = context;
        this.fieldLists = fieldLists;
        baseCodeClass = new BaseCodeClass();
        cr_musics = new HashMap<>();
        childDirectory = context.getResources().getString(R.string.app_name);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIDEO) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_profile_recycler_item_video, parent, false);
            return new VideoViewHolder(view);
        } else if (viewType == PICTURE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_recycler_item_picture, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == FILE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_recycler_item_file, parent, false);
            return new FileViewHolder(view);
        } else if (viewType == MUSIC) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_recycler_item_file, parent, false);
            return new MusicViewHolder(view);
        } else if (viewType == INFO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_recycler_item_info, parent, false);
            return new InfoViewHolder(view);
        } else if (viewType == ORDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_recycler_item_order, parent, false);
            return new OrderViewHolder(view);
        } else if (viewType == SUPPORTERS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_customers_list, parent, false);
            return new SupportersViewHolder(view);
        } else if (viewType == PRODUCT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mystore_product, parent, false);
            return new ProductViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_item_recycler_profile_tab1, parent, false);
            return new TabViewHolder(view);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            FieldList fieldList = fieldLists.get(position);
            int valueType = fieldList.getValuType();
            if (valueType == BaseCodeClass.variableType.Image_.getValue()) {
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.holder(fieldList);
            } else if (valueType == BaseCodeClass.variableType.Video_.getValue()) {
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                videoViewHolder.holder(fieldList);
            } else if (valueType == BaseCodeClass.variableType.File_.getValue()) {
                FileViewHolder fileViewHolder = (FileViewHolder) holder;
                fileViewHolder.holder(fieldList);
            } else if (valueType == BaseCodeClass.variableType.Music_.getValue()) {
                MusicViewHolder musicViewHolder = (MusicViewHolder) holder;
                musicViewHolder.holder(fieldList, position);
            } else if (valueType == BaseCodeClass.variableType.string_.getValue() || valueType == BaseCodeClass.variableType.int_.getValue() || valueType == BaseCodeClass.variableType.datetime.getValue()
                    || valueType == BaseCodeClass.variableType.email.getValue() || valueType == BaseCodeClass.variableType.mobile.getValue() || valueType == BaseCodeClass.variableType.webPage.getValue()
                    || valueType == BaseCodeClass.variableType.StartEndTime.getValue() || valueType == BaseCodeClass.variableType.bool_.getValue()) {
                InfoViewHolder infoViewHolder = (InfoViewHolder) holder;
                infoViewHolder.holder(fieldList);
            } else if (valueType == BaseCodeClass.variableType.Order_.getValue()) {
                OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
                orderViewHolder.holder(fieldList);
            } else if (valueType == BaseCodeClass.variableType.Employee_.getValue()) {
                SupportersViewHolder supportersViewHolder = (SupportersViewHolder) holder;
                supportersViewHolder.holder(fieldList);
            } else if (valueType == BaseCodeClass.variableType.Product_.getValue()) {
                ProductViewHolder productViewHolder = (ProductViewHolder) holder;
                productViewHolder.holder(fieldList);
            } else {
                TabViewHolder tabViewHolder = (TabViewHolder) holder;
                tabViewHolder.holder(fieldList);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemViewType(int position) {
        FieldList fieldList = fieldLists.get(position);
        int valueType = fieldList.getValuType();
        if (valueType == BaseCodeClass.variableType.Image_.getValue()) {
            return PICTURE;
        } else if (valueType == BaseCodeClass.variableType.Video_.getValue()) {
            return VIDEO;
        } else if (valueType == BaseCodeClass.variableType.File_.getValue()) {
            return FILE;
        } else if (valueType == BaseCodeClass.variableType.Music_.getValue()) {
            return MUSIC;
        } else if (valueType == BaseCodeClass.variableType.string_.getValue() || valueType == BaseCodeClass.variableType.int_.getValue() || valueType == BaseCodeClass.variableType.datetime.getValue()
                || valueType == BaseCodeClass.variableType.email.getValue() || valueType == BaseCodeClass.variableType.mobile.getValue() || valueType == BaseCodeClass.variableType.webPage.getValue()
                || valueType == BaseCodeClass.variableType.StartEndTime.getValue() || valueType == BaseCodeClass.variableType.bool_.getValue()) {
            return INFO;
        } else if (valueType == BaseCodeClass.variableType.Order_.getValue()) {
            return ORDER;
        } else if (valueType == BaseCodeClass.variableType.Employee_.getValue()) {
            return SUPPORTERS;
        } else if (valueType == BaseCodeClass.variableType.Product_.getValue()) {
            return PRODUCT;
        } else {
            return OTHER;
        }
    }

    @Override
    public int getItemCount() {
        return fieldLists.size();
    }

    public class TabViewHolder extends RecyclerView.ViewHolder {
        TextView txtValue, txtTitle, txtExplain, txtValueType, txtClickActionType, txtFieldDate, txtExtraData, txtId;

        public TabViewHolder(@NonNull View itemView) {
            super(itemView);
            txtValue = itemView.findViewById(R.id.Value);
            txtTitle = itemView.findViewById(R.id.Title);
            txtExplain = itemView.findViewById(R.id.Explain);
            txtValueType = itemView.findViewById(R.id.valuType);
            txtClickActionType = itemView.findViewById(R.id.ClickActionType);
            txtFieldDate = itemView.findViewById(R.id.FieldDate);
            txtExtraData = itemView.findViewById(R.id.EXtraData);
            txtId = itemView.findViewById(R.id.id);
        }

        void holder(FieldList fieldList) {
            txtValue.setText(fieldList.getValue() + "");
            txtTitle.setText(fieldList.getTitle());
            txtExplain.setText(fieldList.getExplain() + "");
            txtValueType.setText(fieldList.getValuType() + "");
            txtClickActionType.setText(fieldList.getClickActionType() + "");
            txtFieldDate.setText(fieldList.getFieldDate() + "");
            txtExtraData.setText(fieldList.getEXtraData() + "");
            txtId.setText(fieldList.getId() + "");
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_layout_profile_recycler_item_picture_imgPicture);
        }

        void holder(FieldList fieldList) {
            if (!fieldList.getEXtraData().equals("") || !fieldList.getEXtraData().equals("null")) {
                Glide.with(context).load(fieldList.getEXtraData()).into(imageView);

            }
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imgPlay;
        CircularProgressBar progressBar;
        TextView txtPersent;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_layout_profile_recycler_item_video_imgVideo);
            imgPlay = itemView.findViewById(R.id.img_layout_profile_recycler_item_video_imgVideoPlay);
            progressBar = itemView.findViewById(R.id.circularProgressBar);
            txtPersent = itemView.findViewById(R.id.txtPersent);
        }

        void holder(FieldList fieldList) {
            if (!StringUtils.textIsEmpty(fieldList.getEXtraData())) {
                Glide.with(context).load(fieldList.getEXtraData()).into(imageView);
            }

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), childDirectory+"/" + fieldList.getValue());

            if (file.exists()) {
                imgPlay.setImageResource(R.drawable.ic_play_icon);
                imgPlay.setContentDescription("downloaded");
            } else {
                imgPlay.setImageResource(R.drawable.donwloadvideo);
                imgPlay.setContentDescription("not_downloaded");
            }

            imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imgPlay.getContentDescription().equals("not_downloaded")) {
                        downloadVideo(progressBar, imgPlay, txtPersent, fieldList.getValue(), generateUrl(Integer.parseInt(fieldList.getId())));

                    } else {
                        playVideo(file.getAbsolutePath());
                      /*  Intent intent = new Intent(context, ActivityVideoPlay.class);
                        intent.putExtra("videoName",fieldList.getValue());
                        context.startActivity(intent);*/
                    }

                }
            });
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        CircularImageView imageView;
        TextView txtFileName, txtDescription, txtPersent, txtDate;
        ProgressBar prg;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_layout_profile_recycler_item_file_imgFile);
            txtFileName = itemView.findViewById(R.id.txt_layout_profile_recycler_item_file_txtNameFile);
            txtDescription = itemView.findViewById(R.id.txt_layout_profile_recycler_item_file_txtsizeFile);
            prg = itemView.findViewById(R.id.prg_layout_profile_recycler_item_file_prgFile);
            txtPersent = itemView.findViewById(R.id.txt_layout_profile_recycler_item_file_txtPersent);
            txtDate = itemView.findViewById(R.id.txt_layout_profile_recycler_item_file_txtDateFile);
        }

        void holder(FieldList fieldList) {
            txtFileName.setText(fieldList.getValue());
            String time = TimeUtils.getPersianCleanDate(fieldList.getFieldDate());
            String size = fieldList.getExplain() + "  ,  ";
            txtDescription.setText(size);
            txtDate.setText(time);

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), childDirectory+"/" + fieldList.getValue());

            if (file.exists()) {
                imageView.setImageResource(R.drawable.ic_file_blue);
                imageView.setContentDescription("downloaded");
            } else {
                imageView.setImageResource(R.drawable.ic_download_blue);
                imageView.setContentDescription("not_downloaded");
            }


            imageView.setOnClickListener(v -> {
                if (imageView.getContentDescription().equals("not_downloaded")) {
                    downloadFile(prg, imageView, txtPersent, fieldList.getValue(), generateUrl(Integer.parseInt(fieldList.getId())));
                } else {
                    openFile(context, file);
                }

            });

        }
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        CircularImageView imageView;
        TextView txtFileName, txtDescription, txtPersent, txtDate;
        ProgressBar prg;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_layout_profile_recycler_item_file_imgFile);
            txtFileName = itemView.findViewById(R.id.txt_layout_profile_recycler_item_file_txtNameFile);
            txtDescription = itemView.findViewById(R.id.txt_layout_profile_recycler_item_file_txtsizeFile);
            prg = itemView.findViewById(R.id.prg_layout_profile_recycler_item_file_prgFile);
            txtPersent = itemView.findViewById(R.id.txt_layout_profile_recycler_item_file_txtPersent);
            txtDate = itemView.findViewById(R.id.txt_layout_profile_recycler_item_file_txtDateFile);
        }

        void holder(FieldList fieldList, int position) {
            txtFileName.setText(fieldList.getValue());
            String time = TimeUtils.getPersianCleanDate(fieldList.getFieldDate());
            String size = fieldList.getExplain() + "  ,  ";
            txtDescription.setText(size);
            txtDate.setText(time);
            cr_musics.put(position, imageView);

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), childDirectory+"/" + fieldList.getValue());

            if (file.exists()) {
                imageView.setImageResource(R.drawable.ic_play_music_blue);
                imageView.setContentDescription("downloaded");
            } else {
                imageView.setImageResource(R.drawable.ic_download_blue);
                imageView.setContentDescription("not_downloaded");
            }


            imageView.setOnClickListener(v -> {
                if (imageView.getContentDescription().equals("not_downloaded")) {
                    downloadFile(prg, imageView, txtPersent, fieldList.getValue(), fieldList.getEXtraData());
                } else {
                    if (play) {
                        initPlayedMusic();
                        imageView.setImageResource(R.drawable.ic_play_music_blue);
                        yoYoString.stop(true);
                        stop(mediaPlayer);
                        playPosition = -1;
                        play = false;


                    } else {
                        playMusic(file.getAbsolutePath(), imageView);
                        playPosition = position;
                    }
                }

            });

        }
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtValue;
        ImageView imgCopy;

        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_layout_profile_recycler_item_info_txtTitle);
            txtValue = itemView.findViewById(R.id.txt_layout_profile_recycler_item_info_txtValue);
            imgCopy = itemView.findViewById(R.id.img_layout_profile_recycler_item_info_imgCopy);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        void holder(FieldList fieldList) {

            if (fieldList.getValuType() == BaseCodeClass.variableType.mobile.getValue() || fieldList.getValuType() == BaseCodeClass.variableType.email.getValue() ||
                    fieldList.getValuType() == BaseCodeClass.variableType.webPage.getValue()) {
                if (!StringUtils.textIsEmpty(fieldList.getValue())) {
                    imgCopy.setVisibility(View.VISIBLE);
                }
            }

            if (fieldList.getValuType() == BaseCodeClass.variableType.string_.getValue()) {
                txtTitle.setText(fieldList.getTitle());
                txtValue.setText(fieldList.getValue());
            } else if (fieldList.getValuType() == BaseCodeClass.variableType.datetime.getValue()) {
                txtTitle.setText(fieldList.getTitle());
                if (!StringUtils.textIsEmpty(fieldList.getFieldDate())) {
                    txtValue.setText(TimeUtils.getPersianCleanDate(fieldList.getFieldDate()));
                }
            } else if (fieldList.getValuType() == BaseCodeClass.variableType.mobile.getValue() || fieldList.getValuType() == BaseCodeClass.variableType.email.getValue() ||
                    fieldList.getValuType() == BaseCodeClass.variableType.webPage.getValue() || fieldList.getValuType() == BaseCodeClass.variableType.StartEndTime.getValue()) {
                txtTitle.setText(fieldList.getTitle());
                txtValue.setText(fieldList.getValue());
                txtValue.setTextColor(Color.parseColor("#1565C0"));
            } else if (fieldList.getValuType() == BaseCodeClass.variableType.bool_.getValue()) {
                txtTitle.setText(fieldList.getTitle());
                if (fieldList.getValue().equals("True")) {
                    txtValue.setText("فعال");
                    txtValue.setTextColor(context.getColor(R.color.green600));
                } else {
                    txtValue.setText("غیر فعال");
                    txtValue.setTextColor(Color.RED);
                }
            }

            txtValue.setOnLongClickListener(v -> {
                if (fieldList.getValuType() == BaseCodeClass.variableType.mobile.getValue() || fieldList.getValuType() == BaseCodeClass.variableType.email.getValue() ||
                        fieldList.getValuType() == BaseCodeClass.variableType.webPage.getValue()) {
                    if (!StringUtils.textIsEmpty(fieldList.getValue())) {
                        setAnimation(Techniques.Shake, 400L, txtValue);
                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("copy", fieldList.getValue());
                        clipboardManager.setPrimaryClip(clip);
                        Toast.makeText(context, "کپی شد", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            });

            imgCopy.setOnClickListener(v -> {
                setAnimation(Techniques.Tada, 200L, imgCopy);
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copy", fieldList.getValue());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(context, "کپی شد", Toast.LENGTH_SHORT).show();

            });

        }
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderName, txtOrderDate, txtOrderSumPrice;
        ImageView imgOrder;
        RelativeLayout relSeeMore;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txt_layout_profile_recycler_item_order_txtOrderId);
            txtOrderName = itemView.findViewById(R.id.txt_layout_profile_recycler_item_order_txtOrderName);
            txtOrderDate = itemView.findViewById(R.id.txt_layout_profile_recycler_item_order_txtOrderDate);
            txtOrderSumPrice = itemView.findViewById(R.id.txt_layout_profile_recycler_item_order_txtSumPriceOrder);
            imgOrder = itemView.findViewById(R.id.img_layout_profile_recycler_item_order_imgOrder);
            relSeeMore = itemView.findViewById(R.id.rel_layout_profile_recycler_item_order_rel_seeMore);


        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        void holder(FieldList fieldList) {
            try {

                txtOrderId.setText(fieldList.getId() + "");
                txtOrderName.setText(fieldList.getExplain());
                txtOrderSumPrice.setText(" جمع فاکتور =" + fieldList.getEXtraData() + " تومان ");
                if (!StringUtils.textIsEmpty(fieldList.getFieldDate())) {
                    txtOrderDate.setText(TimeUtils.getPersianCleanDate(fieldList.getFieldDate()));
                }
                try {
                    Glide.with(context).load(generateUrlOrderPicture(fieldList.getValue())).placeholder(R.drawable.image_placeholder).into(imgOrder);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                relSeeMore.setOnClickListener(v -> {
                    try {
                        MessageApi messageApi = RetrofitInstance.getRetrofit().create(MessageApi.class);
                        Call<SendOrderClass> call = messageApi.getOrderData2(fieldList.getValue());
                        call.enqueue(new Callback<SendOrderClass>() {
                            @Override
                            public void onResponse(Call<SendOrderClass> call, Response<SendOrderClass> response) {
                                SendOrderClass sendOrderClasses = response.body();
                                BaseCodeClass.myStoreSelectedOrder = sendOrderClasses;
                                Intent intent = new Intent(context, MyStoreOrderDetail.class);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<SendOrderClass> call, Throwable t) {
                                Toast.makeText(context, "getDetailsOrderProfile>>>" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public class SupportersViewHolder extends RecyclerView.ViewHolder {
        TextView txtPosition, txtName;
        CircleImageView circleImageView;
        ImageView imgSendMessage;
        CardView root;


        public SupportersViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition = itemView.findViewById(R.id.tv_customer_title);
            txtName = itemView.findViewById(R.id.tv_customer_location);
            imgSendMessage = itemView.findViewById(R.id.iv_customer_chat);
            circleImageView = itemView.findViewById(R.id.img_rowItemCustomer_list);
            root = itemView.findViewById(R.id.card_rowItemCustomersList_root);
        }

        void holder(FieldList fieldList) {
            txtPosition.setText(fieldList.getTitle());
            txtName.setText(fieldList.getExplain());
            Glide.with(context).load(generateUrlEmployeePicture(fieldList.getEXtraData())).placeholder(R.drawable.image_placeholder).into(circleImageView);
            imgSendMessage.setOnClickListener(v -> {
                setAnimation(Techniques.Tada, 200L, imgSendMessage);
                Intent intent = new Intent(context, MessageActivity.class);
                //sender = ourselfes
                intent.putExtra("sender", BaseCodeClass.userID);
                //getter = company(others)
                intent.putExtra("getter", fieldList.getEXtraData());
                intent.putExtra(STATE_MESSAGE_SENDER, REGULAR_USER);
                context.startActivity(intent);
            });

            root.setOnClickListener(v -> {
                setAnimation(Techniques.Tada, 200L, root);
                Intent intent = new Intent(context, MessageActivity.class);
                //sender = ourselfes
                intent.putExtra("sender", BaseCodeClass.userID);
                //getter = company(others)
                intent.putExtra("getter", fieldList.getEXtraData());
                intent.putExtra(STATE_MESSAGE_SENDER, REGULAR_USER);
                context.startActivity(intent);
            });


        }

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtProductName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageProduct);
            txtProductName = itemView.findViewById(R.id.txtName);
        }

        void holder(FieldList fieldList) {
            try {
                String color = fieldList.getExplain();
                if (!StringUtils.textIsEmpty(color)) {
                    imageView.setBackgroundColor(Color.parseColor(color));
                } else {
                    imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                txtProductName.setText(fieldList.getTitle());
                Glide.with(context).load(generateUrlProductPicture(fieldList.getValue())).placeholder(R.drawable.image_placeholder).into(imageView);

                imageView.setOnClickListener(v -> {
                    if (myStore) {
                        if (baseCodeClass.getPermissions() != null) {
                            if (baseCodeClass.getPermissions().get(6).isState()) {
                                startEditProduct(fieldList.getValue());
                            } else {
                                //employee has not permission to edit product
                                if (baseCodeClass.loadSelectedProduct(fieldList.getValue(), context)) {
                                    startViewProduct(fieldList.getValue());
                                }
                            }
                        }

                    } else {
                        if (baseCodeClass.loadSelectedProduct(fieldList.getValue(), context)) {
                            startViewProduct(fieldList.getValue());
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void downloadFile(ProgressBar progressBar, CircularImageView imageView, TextView txtPersent, String fileName, String link) {
        progressBar.setVisibility(View.VISIBLE);
        txtPersent.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(link);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("در حال دانلود");
        request.setDescription("لطفا منتظر بمانید...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/"+childDirectory+"/" + fileName);


        final long id = downloadManager.enqueue(request);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    long totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    final int percent = (int) ((downloadedBytes * 100) / totalBytes);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(percent);
                            txtPersent.setText(" % " + percent);
                            if (percent == 100) {
                                Toast.makeText(context, "دانلود با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                timer.purge();
                                timer.cancel();
                                imageView.setImageResource(R.drawable.ic_file_blue);
                                imageView.setContentDescription("downloaded");
                                progressBar.setVisibility(View.GONE);
                                txtPersent.setVisibility(View.GONE);


                            }
                        }
                    });
                }


            }
        }, 0, 100);
    }

    private String generateUrl(int fileId) {
        String url = baseCodeClass.BASE_URL + "User/DownloadCahtFile?Chatid=" + fileId;
        return url;
    }

    private String generateUrlOrderPicture(String id) {
        String url = baseCodeClass.BASE_URL + "Order/DownloadImage?OrderId=" + id;
        return url;
    }

    private String generateUrlProductPicture(String id) {
        String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + id + "&fileNumber=1";
        return url;
    }

    private String generateUrlEmployeePicture(String id) {
        String url = baseCodeClass.BASE_URL + "User/DownloadFile?UserID=" + id + "&fileNumber=" + 1;
        return url;
    }

    private void setAnimation(Techniques animation, Long duration, View view) {
        YoYo.with(animation)
                .duration(duration)
                .playOn(view);
    }

    public void openFile(Context context, File file) {
        String typeFile = getTypeFile(file);

        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), typeFile);
            PackageManager pm = context.getPackageManager();
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType(typeFile);
            Intent openInChooser = Intent.createChooser(intent, "Choose");
            List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
            if (resInfo.size() > 0) {
                try {
                    context.startActivity(openInChooser);
                } catch (Throwable throwable) {
                    Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
                    // PDF apps are not installed
                }
            } else {
                Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getTypeFile(File file) {

        if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
            // Word document
            return "application/msword";
        } else if (file.toString().contains(".pdf")) {
            // PDF file
            return "application/pdf";
        } else if (file.toString().contains(".ppt") || file.toString().contains(".pptx")) {
            // Powerpoint file
            return "application/vnd.ms-powerpoint";
        } else if (file.toString().contains(".xls") || file.toString().contains(".xlsx")) {
            // Excel file
            return "application/vnd.ms-excel";
        } else if (file.toString().contains(".zip")) {
            // ZIP file
            return "application/zip";
        } else if (file.toString().contains(".rar")) {
            // RAR file
            return "application/x-rar-compressed";
        } else if (file.toString().contains(".rtf")) {
            // RTF file
            return "application/rtf";
        } else if (file.toString().contains(".wav") || file.toString().contains(".mp3")) {
            // WAV audio file
            return "audio/x-wav";
        } else if (file.toString().contains(".gif")) {
            // GIF file
            return "image/gif";
        } else if (file.toString().contains(".jpg") || file.toString().contains(".jpeg") || file.toString().contains(".png")) {
            // JPG file
            return "image/jpeg";
        } else if (file.toString().contains(".txt")) {
            // Text file
            return "text/plain";
        } else if (file.toString().contains(".3gp") || file.toString().contains(".mpg") ||
                file.toString().contains(".mpeg") || file.toString().contains(".mpe") || file.toString().contains(".mp4") || file.toString().contains(".avi")) {
            // Video files
            return "video/*";
        } else {
            return "*/*";
        }

    }

    private void playMusic(String root, CircularImageView imageView) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(root);
            mediaPlayer.prepare();
            mediaPlayer.start();
            setAnimation(Techniques.Tada, 1000L, YoYo.INFINITE, imageView);
            imageView.setImageResource(R.drawable.ic_pause_blue);
            play = true;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop(mp);
                    imageView.setImageResource(R.drawable.ic_play_music_blue);
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void stop(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void initPlayedMusic() {
        for (Map.Entry<Integer, CircularImageView> entry : cr_musics.entrySet()) {
            int key = entry.getKey();
            CircularImageView value = entry.getValue();
            if (key == playPosition) {
                value.setImageResource(R.drawable.ic_play_music_blue);
                stop(mediaPlayer);
                yoYoString.stop(true);
                play2 = true;
            }


        }
    }


    private void setAnimation(Techniques animation, Long duration, int repeat, View view) {
        yoYoString = YoYo.with(animation)
                .repeat(repeat)
                .duration(duration)
                .playOn(view);
    }


    private void downloadVideo(CircularProgressBar progressBar, ImageView imageView, TextView txtPersent, String name, String link) {

        progressBar.setVisibility(View.VISIBLE);
        txtPersent.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(link);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("در حال دانلود");
        request.setDescription("لطفا منتظر بمانید...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Dehkadeh/" + name);

        final long id = downloadManager.enqueue(request);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    long downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    long totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    final int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    final int percent = (int) ((downloadedBytes * 100) / totalBytes);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(percent);
                            txtPersent.setText(" % " + percent);
                            if (percent == 100) {
                                Toast.makeText(context, "دانلود با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                timer.purge();
                                timer.cancel();
                                imageView.setContentDescription("downloaded");
                                progressBar.setVisibility(View.GONE);
                                txtPersent.setVisibility(View.GONE);
                                imageView.setImageResource(R.drawable.ic_play_icon);
                            }
                        }
                    });
                }


            }
        }, 0, 100);
    }


    public void startEditProduct(String id) {
        Intent intent = new Intent(context, AddProductActivity.class);
        intent.putExtra("PID", id);
        if (baseCodeClass.loadSelectedProduct(id, context))
            context.startActivity(intent);
    }

    public void startViewProduct(String id) {
        Intent intent = new Intent(context, ViewProductActivity.class);
        intent.putExtra("PID", id);
        context.startActivity(intent);
    }

    private void playVideo(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        intent.setDataAndType(Uri.parse(path), "video/*");
        context.startActivity(intent);
    }


}
