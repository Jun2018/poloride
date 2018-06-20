package org.androidtown.poloride;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FilterActivity extends AppCompatActivity implements ThumbnailCallback {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private Activity activity;
    private RecyclerView thumbListView;
    private ImageView placeHolderImageView;
    int drawable;
    String uri;
    Bitmap image;
    Bitmap nocropImage;
    Bitmap filteredImage;
    String FileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setContentView(R.layout.activity_filter);
        activity = this;
//        drawable = R.drawable.dog;

        Intent intent = getIntent();

        byte[] arr = getIntent().getByteArrayExtra("imageViewByte");
        uri = intent.getStringExtra("URI");
        nocropImage= BitmapFactory.decodeByteArray(arr, 0, arr.length);;
        image = cropBitmap(nocropImage);
        Log.v("initUIWidgets", uri);

        initUIWidgets();
    }

    private void initUIWidgets() {
        thumbListView = (RecyclerView) findViewById(R.id.thumbnails);
        placeHolderImageView = (ImageView) findViewById(R.id.place_holder_imageview);
//        placeHolderImageView.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), drawable), 640, 640, false));

        image = cropBitmap(nocropImage);
//
        Bitmap result = Bitmap.createBitmap(640, 1024, nocropImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(nocropImage, 0f, 0f, null);
        canvas.drawBitmap(image, 65, 120, null);

        placeHolderImageView.setImageBitmap(result);

        initHorizontalList();
    }

    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = image;
//                Bitmap thumbImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), drawable), 640, 640, false);
                ThumbnailsManager.clearThumbs();
                List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());

                for (Filter filter : filters) {
                    ThumbnailItem thumbnailItem = new ThumbnailItem();
                    thumbnailItem.image = thumbImage;
                    thumbnailItem.filter = filter;
                    ThumbnailsManager.addThumb(thumbnailItem);
                }

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, (ThumbnailCallback) activity);
                thumbListView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public void onThumbnailClick(Filter filter) {
//        Bitmap image = BitmapFactory.decodeFile(uri);

//        placeHolderImageView.setImageBitmap(filter.processFilter(image));

//        placeHolderImageView.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), drawable), 640, 640, false)));
        filteredImage = image.copy(Bitmap.Config.ARGB_8888, true);
        // preview filtered image
        Bitmap filterImage = filter.processFilter(filteredImage);

        Bitmap result = Bitmap.createBitmap(640, 1024, nocropImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(nocropImage, 0f, 0f, null);
        canvas.drawBitmap(filterImage, 65, 120, null);

        placeHolderImageView.setImageBitmap(result);

    }

    public void onBackButtonClicked(View v) {
        this.finish();
    }

    public void onSaveButtonClicked(View v) {

        Bitmap filterBitmap = combineFrame(filteredImage);


        Intent intent = new Intent(getApplicationContext(), DecoActivity.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        filterBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent.putExtra("image",byteArray);
        intent.putExtra("URI", uri);
        startActivity(intent);

       // saveBitmaptoJpeg(filterBitmap);
    }

    public Bitmap combineFrame(Bitmap saveBitmap) {
        BitmapDrawable d = (BitmapDrawable) ((ImageView) findViewById(R.id.place_holder_imageview)).getDrawable();
        Bitmap b = d.getBitmap();

        Bitmap result = Bitmap.createBitmap(640, 1024, nocropImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(nocropImage, 0f, 0f, null);
        canvas.drawBitmap(saveBitmap, 65, 120, null);

        return result;
    }
/*
    public void saveBitmaptoJpeg(Bitmap saveBitmap) {
        //    String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String split1[] = uri.split("_");
        String split[] = split1[1].split("[.]");
        FileName = split[0];

        String folder = Environment.getExternalStorageDirectory() + File.separator + "pola" + "/";
        String file = "pola_" + FileName + "_" + getDateString() + ".jpg";
        Toast.makeText(FilterActivity.this, file, Toast.LENGTH_LONG).show();
        File file_path;
        try {
            file_path = new File(folder);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(folder + file);
            saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + folder + file)));

            out.close();

            Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
            Toast.makeText(FilterActivity.this, "저장하였습니다.", Toast.LENGTH_LONG).show();
            startActivity(intent);

        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }

    }

    public String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }
*/
    static public Bitmap cropBitmap(Bitmap original) {
        Bitmap result = Bitmap.createBitmap(original
                , 65 //X 시작위치 (원본의 4/1지점)
                , 120 //Y 시작위치 (원본의 4/1지점)
                , 510 // 넓이 (원본의 절반 크기)
                , 680); // 높이 (원본의 절반 크기)
       /*
        if (result != original) {
            original.recycle();
        }*/
        return result;
    }

}