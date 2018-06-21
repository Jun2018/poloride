package org.androidtown.poloride;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.Console;
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
    boolean drawBtnState =false;

    String FileName = null;
    String inputString = null;

    private BitmapDrawable bitmapDrawable = null;
    private Bitmap imageviewBitmap =null;
    private int width = 0;
    private int height = 0;
    private Bitmap result = null;
    private byte[] arr = null;
    private Bitmap image = null;
    private Canvas  canvas = null;
    private Path path = null;

    private Paint   DrawPaint = null;
    private Paint   fontpaint = new Paint();
    private float   backX = 0.0f;
    private float   backY = 0.0f;

    Typeface input_font;
    Typeface time_font;

    private ImageView imageView = null;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            ImageView imageView = (ImageView)view;

            float x = event.getX() / (imageView.getWidth() / 640f);
            float y = event.getY() / (imageView.getHeight() / 1024f);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    path.reset();
                    path.moveTo(x, y);
                    canvas.drawPoint(x, y, DrawPaint);
                    backX = x;
                    backY = y;
                    imageView.invalidate();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    path.quadTo(backX, backY, x, y);
                    canvas.drawPath(path, DrawPaint);
                    backX = x;
                    backY = y;
                    imageView.invalidate();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_deco);

        //인텐트로 uri받음

        Intent intent = getIntent();
        uri = intent.getStringExtra("URI");
        arr = getIntent().getByteArrayExtra("image");

        if(arr==null){
            image = BitmapFactory.decodeFile(uri);
        }else if(arr != null){
            image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        }
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

        width = image.getWidth();
        height = image.getHeight();
        result = Bitmap.createBitmap(width, height, image.getConfig());

        canvas = new Canvas(result);
        canvas.drawBitmap(image, 0f, 0f, null);

        path = new Path();

        editText = (EditText) findViewById(R.id.text);

        final Button btn_InputDate = (Button) findViewById(R.id.btnInputdate);
        btn_InputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_InputDate.setBackgroundResource(R.drawable.date_icon_finish);

                String split1[] = uri.split("_");
                String split[] = split1[1].split("-");
                String yearSplit[] = split[0].split("0");

                String year = yearSplit[1];
                String month = split[1];
                String day = split[2];

                String strTime = "'"+ year + "  " + month+ "  "+day+ "  ";

                int timeColor = getResources().getColor(R.color.colorTime);

                time_font = Typeface.createFromAsset(getAssets(),"font_digital.ttf");
                Paint tPaint = new Paint();
                tPaint.setTextSize(25);
                tPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                tPaint.setColor(timeColor);
                tPaint.setStyle(Paint.Style.FILL);
                tPaint.setTypeface(time_font);
                canvas.drawText(strTime, 455f, 775f, tPaint);

                //Drawable drawable = new BitmapDrawable(result);

                imageView.setImageBitmap(result);

            }
        });

        final Button btn_inputfont = (Button) findViewById(R.id.btnInputFont);
        btn_inputfont.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                btnState++;
                input_font = Typeface.createFromAsset(getAssets(),"font_papyrus.ttf");
                if (btnState==1) {
                    btn_inputfont.setBackgroundResource(R.drawable.font_icon_activ);
                    editText.setHint("문자를 입력하세요");
                    editText.setTypeface(input_font);
                    editText.setHintTextColor(Color.RED);
                    editText.setEnabled(true);

                }else if(btnState==2) {
                    btn_inputfont.setBackgroundResource(R.drawable.font_icon_finish);
                    if (editText.getText().toString().length() != 0) {

                        inputString = editText.getText().toString();


                        fontpaint.setTextSize(70);
                        fontpaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        fontpaint.setColor(Color.BLACK);
                        fontpaint.setStyle(Paint.Style.FILL);
                        fontpaint.setTypeface(input_font);


                        canvas.drawText(inputString, 75f, 925f, fontpaint);

                        //Drawable drawable = new BitmapDrawable(result);

                        imageView.setImageBitmap(result);
                    }

                    editText.setHint(null);
                    editText.setText(null);
                    editText.setEnabled(false);
                }

            }
        });

        final Button btn_filter = (Button)findViewById(R.id.btnFilter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable d = (BitmapDrawable) ((ImageView) findViewById(R.id.imageView)).getDrawable();
                Bitmap b = d.getBitmap();

                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("imageViewByte",byteArray);
                intent.putExtra("URI", uri);
                startActivity(intent);
            }
        });

        final Button btn_save = (Button) findViewById(R.id.btnSave);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable d = (BitmapDrawable) ((ImageView) findViewById(R.id.imageView)).getDrawable();
                Bitmap b = d.getBitmap();
                saveBitmaptoJpeg(b);
            }
        });

        final Button btn_draw = (Button) findViewById(R.id.btnDraw);
        btn_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!drawBtnState){
                    btn_draw.setBackgroundResource(R.drawable.draw_icon_activ);
                    imageView.setImageBitmap(result);
                    imageView.setOnTouchListener(touchListener);

                    DrawPaint = new Paint();
                    DrawPaint.setColor(Color.BLACK);
                    DrawPaint.setAlpha(255);
                    DrawPaint.setStrokeWidth(3);
                    DrawPaint.setStrokeJoin(Paint.Join.ROUND);
                    DrawPaint.setStyle(Paint.Style.STROKE);
                    DrawPaint.setStrokeCap(Paint.Cap.ROUND);
                    DrawPaint.setAntiAlias(true);

                    drawBtnState=true;
                }else if(drawBtnState){
                    btn_draw.setBackgroundResource(R.drawable.draw_icon);
                    imageView.setImageBitmap(result);
                    DrawPaint.setAlpha(0);
                    drawBtnState=false;
                }

            }
        });
/*
        final Button btn_filter = (Button) findViewById(R.id.btnFilter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bitmap filterBitmap = changeToOld(result);
//                canvas.drawBitmap(filterBitmap, 0f, 0f, null);
//
//                imageView.setImageBitmap(result);


                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                intent.putExtra("URI", uri);
                startActivity(intent);

            }
        });
*/
    }


    public void saveBitmaptoJpeg(Bitmap saveBitmap) {
        //    String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String split1[] = uri.split("_");
        String split[] = split1[1].split("[.]");
        FileName = split[0];

        String folder = Environment.getExternalStorageDirectory() + File.separator + "pola" + "/";
        String file = "pola_" + FileName + "_" + getDateString() + ".jpg";
        Toast.makeText(DecoActivity.this, file, Toast.LENGTH_LONG).show();
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
            Toast.makeText(DecoActivity.this, "저장하였습니다.", Toast.LENGTH_LONG).show();
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


    public static Bitmap changeToOld(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 120; i < height-224; i++)
        {
            for (int k = 65; k < width-65; k++)
            {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }

        Bitmap returnBitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return returnBitmap;
	}

}