package org.androidtown.poloride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    GridView gv;
    ArrayList<File> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int backgroundColor = getResources().getColor(R.color.colorBackground);

        setContentView(R.layout.activity_gallery);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);

 //       final ImageView imageView2 = (ImageView)findViewById(R.id.imageView2);

        layout.setBackgroundColor(Color.argb(0,0,0,0));

//        list = imageReader(Environment.getExternalStorageDirectory());
        list = imageReader( new File(Environment.getExternalStorageDirectory() + File.separator+ "pola"));
//        Environment.getExternalStorageDirectory() + File.separator+ "pola"


        for(int i=0; i< list.size(); i++) {
            ImageView imageView = new ImageView(this);

            imageView.setAdjustViewBounds(true);
            Log.v( "Gallery" ,"IMAGEPARAM1: "+ layout.getHeight());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = list.get(view.getId()).toString();

                    Intent intent = new Intent(getApplicationContext(), SelectedPicture.class);
                    intent.putExtra("Data", uri);
                    startActivity(intent);
/*
                   imageView2.setBackgroundColor(Color.BLACK);
                   Log.v("imageViewClick",view.getBackground().toString());
                   Log.v( "Gallery" ,"setOnClickListener: "+ list.get(view.getId()));
                   imageView2.setImageURI(Uri.parse(list.get(view.getId()).toString()));
*/


                }
            });


            try {
                ExifInterface exif = new ExifInterface(list.get(i).toString());
                Log.v("FileInfo", exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)) ;
                Log.v("FileInfo", exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)) ;
                Integer width = Integer.parseInt( exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) );
                Integer height = Integer.parseInt( exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) );

                LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(width, height);

                Params.leftMargin = 210;
                Params.rightMargin = 210;

                imageView.setLayoutParams(Params);


                imageView.setImageURI(Uri.parse(list.get(i).toString()));

                imageView.setId(i);

                Log.v( "Gallery" ,"IMAGEPARAM2: "+ imageView.getLayoutParams());

                //          GradientDrawable gd = new GradientDrawable();
                //          gd.setSize(width, height);
                //          gd.setShape(GradientDrawable.RECTANGLE);
                //          gd.setColor(Color.BLACK);
                //         imageView.setImageDrawable(gd);
                int background = getResources().getColor(R.color.colorBackground);
                imageView.setBackgroundColor(background);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.addView(imageView);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }

//        for (int i = 0; i < 10; i++) {
//            ImageView imageView = new ImageView(this);
//            imageView.setId(i);
//            imageView.setPadding(2, 2, 2, 2);
//            imageView.setImageBitmap(BitmapFactory.decodeResource(
//                    getResources(), R.drawable.ic_launcher_background));
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            layout.addView(imageView);
//        }


//        list = imageReader(Environment.getExternalStorageDirectory());
//
//        gv = (GridView) findViewById(R.id.gridView);
//        gv.setAdapter(new GridAdapter());
    }

    //    class GridAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return list.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            view = getLayoutInflater().inflate(R.layout.single_grid, viewGroup,false);
//            ImageView iv = (ImageView) view.findViewById(R.id.imageView);
//
//            iv.setImageURI(Uri.parse(getItem(i).toString()));
//
//            return view;
//        }
//    }
//
    ArrayList<File> imageReader(File root) {
        ArrayList<File> a = new ArrayList<>();

        File[] files = root.listFiles();
        Log.v( "Gallery" ,"imageReader: "+ root);

        Log.v( "Gallery" ,"imageReader: "+ files);
        for(int i = 0; i< files.length; i++) {

            if(files[i].isDirectory()) {
                a.addAll(imageReader(files[i]));
            } else {
                if(files[i].getName().endsWith(".jpg")) {
                    a.add(files[i]);
                }
            }
        }

        return a;
    }
    public void onBackButtonClicked(View v) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}