package org.androidtown.poloride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DecoActivity extends AppCompatActivity {
    EditText editText;

    String uri = null;
    int btnState = 0;
    float x = 0;
    float y = 0;
    String FileName = null;
    String inputString=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_deco);

        Intent intent = getIntent();
        uri = intent.getStringExtra("URI");

        Bitmap image = BitmapFactory.decodeFile(uri);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

        editText = (EditText)findViewById(R.id.text);
        //editText.setOnTouchListener(this);



        Button btn_InputDate = (Button) findViewById(R.id.btnInputdate);
        btn_InputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmap = (BitmapDrawable) ((ImageView) findViewById(R.id.imageView)).getDrawable();
                Bitmap imageviewBitmap = bitmap.getBitmap(); // 이미지뷰에 있는 이미지를 비트맵 b로 만듬

                int width, height = 0;
                width = imageviewBitmap.getWidth();
                height = imageviewBitmap.getHeight();

                SimpleDateFormat df = new SimpleDateFormat("yy  MM  dd", Locale.KOREA);
                String strTime = df.format(new Date());

                int timeColor = getResources().getColor(R.color.colorTime);

                Paint tPaint = new Paint();
                tPaint.setTextSize(30);
                tPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                tPaint.setColor(timeColor);
                tPaint.setStyle(Paint.Style.FILL);

                Bitmap result = Bitmap.createBitmap(width, height, imageviewBitmap.getConfig());
                Canvas canvas = new Canvas(result);
                canvas.drawBitmap(imageviewBitmap, 0f, 0f, null);
                canvas.drawText(strTime, 420f, 770f, tPaint);

                //Drawable drawable = new BitmapDrawable(result);

                imageView.setImageBitmap(result);

            }
        });

        final Button btn_inputfont = (Button) findViewById(R.id.btnInputFont);
        btn_inputfont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                btnState++;
                if (btnState == 1){
                    btn_inputfont.setText("입력완료");
                    editText.setHint("문자를 입력하세요");
                    editText.setHintTextColor(Color.RED);
                    editText.setEnabled(true);
                }
                if (btnState ==2){
                    btn_inputfont.setText("적용됨");
                    if ( editText.getText().toString().length() != 0 ) {
                        BitmapDrawable bitmap = (BitmapDrawable) ((ImageView) findViewById(R.id.imageView)).getDrawable();
                        Bitmap imageviewBitmap = bitmap.getBitmap(); // 이미지뷰에 있는 이미지를 비트맵 b로 만듬

                        int width, height = 0;
                        width = imageviewBitmap.getWidth();
                        height = imageviewBitmap.getHeight();

                        inputString=editText.getText().toString();

                        int timeColor = getResources().getColor(R.color.colorTime);

                        Paint tPaint = new Paint();
                        tPaint.setTextSize(90);
                        tPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        tPaint.setColor(timeColor);
                        tPaint.setStyle(Paint.Style.FILL);

                        Bitmap result = Bitmap.createBitmap(width, height, imageviewBitmap.getConfig());
                        Canvas canvas = new Canvas(result);
                        canvas.drawBitmap(imageviewBitmap, 0f, 0f, null);
                        canvas.drawText(inputString, 80f, 935f, tPaint);

                        //Drawable drawable = new BitmapDrawable(result);

                        imageView.setImageBitmap(result);
                    }

                    editText.setHint(null);
                    editText.setText(null);
                    editText.setEnabled(false);
                }

            }
        });

        final Button btn_save = (Button) findViewById(R.id.btnSave);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable d = (BitmapDrawable)((ImageView) findViewById(R.id.imageView)).getDrawable();
                Bitmap b = d.getBitmap();
                saveBitmaptoJpeg(b);
            }
        });
    }

    public  void saveBitmaptoJpeg(Bitmap bitmap){
        //    String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String split1[] = uri.split("_");
        String split[] = split1[1].split("[.]");
        FileName = split[0];

        String folder = Environment.getExternalStorageDirectory() + File.separator+ "pola" + "/";
        String file = "pola_" + FileName + "_" + getDateString() + ".jpg";
        Toast.makeText(DecoActivity.this, file, Toast.LENGTH_LONG).show();
        File file_path;
        try{
            file_path = new File(folder);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(folder+file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ folder + file)));

            out.close();

            Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
            Toast.makeText(DecoActivity.this, "저장하였습니다.", Toast.LENGTH_LONG).show();
            startActivity(intent);

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }

    }
    public String getDateString()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }
}

/*
    float oldXvalue;
    float oldYvalue;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int width = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
        int height = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldXvalue = event.getX();
            oldYvalue = event.getY();
            //  Log.i("Tag1", "Action Down X" + event.getX() + "," + event.getY());
          //  Toast.makeText(FontActivity.this, String.valueOf(event.getRawX()) + " / " + String.valueOf(event.getRawY()), Toast.LENGTH_LONG).show();
            Log.i("Tag1", "Action Down rX " + event.getRawX() + "," + event.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            v.setX(event.getRawX() - oldXvalue);
            v.setY(event.getRawY() - (oldYvalue + v.getHeight()));
            //  Log.i("Tag2", "Action Down " + me.getRawX() + "," + me.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (v.getX() > width && v.getY() > height) {
                v.setX(width);
                v.setY(height);
            } else if (v.getX() < 0 && v.getY() > height) {
                v.setX(0);
                v.setY(height);
            } else if (v.getX() > width && v.getY() < 0) {
                v.setX(width);
                v.setY(0);
            } else if (v.getX() < 0 && v.getY() < 0) {
                v.setX(0);
                v.setY(0);
            } else if (v.getX() < 0 || v.getX() > width) {
                if (v.getX() < 0) {
                    v.setX(0);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                } else {
                    v.setX(width);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                }
            } else if (v.getY() < 0 || v.getY() > height) {
                if (v.getY() < 0) {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(0);
                } else {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(height);
                }
            }
            Toast.makeText(FontActivity.this, String.valueOf(v.getX()) + " / " + String.valueOf(v.getY()), Toast.LENGTH_LONG).show();

            x = v.getX() - widthGap;
            y = v.getY() - heightGap;
        }
        return true;
    }
*/
            /* 문자열 길이가 5이면 문자열 입력 막기. 터치 발생시 문자열 입력 막고 문자 받아오기.
         if(s.length()==5)
        {
            et.setEnabled(false);
        }
         */
/*
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                break;
        }

        Log.v("MotionEvent",
                "action = " + action + ", " +
                        "x = " + String.valueOf(event.getX()) + ", " +
                        "y = " + String.valueOf(event.getY()));

        Toast. makeText(FontActivity. this, "좌표: " + String.valueOf(event.getX()), Toast.LENGTH_SHORT ).show();


        return super.onTouchEvent(event);
    }
*/

/*
        이미지뷰에 있는 거 저장하는 코드
        BitmapDrawable d = (BitmapDrawable)((ImageView) findViewById(R.id.imageView)).getDrawable();
        Bitmap b = d.getBitmap();  // 이미지뷰에 있는 이미지를 비트맵 b로 만듬
        saveBitmaptoJpeg(b);
*/
/*
    public  void saveBitmaptoJpeg(Bitmap bitmap){
        //    String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String folder = Environment.getExternalStorageDirectory() + File.separator+ "pola" + "/";
        String file = "pola_" + getDateString() + "plus"+".jpg";

        File file_path;
        try{
            file_path = new File(folder);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }

            FileOutputStream out = new FileOutputStream(folder+file);
            //사진 저장
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            //갤러리에 보이게 함
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ folder + file)));

            out.close();

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
    }
    public String getDateString()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }
*/