package org.androidtown.poloride;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
    int drawBtnState = 0;

    String FileName = null;
    String inputString = null;

    private BitmapDrawable bitmapDrawable = null;
    private Bitmap imageviewBitmap =null;
    private int width = 0;
    private int height = 0;
    private Bitmap result = null;

    private Canvas  canvas = null;

    private Paint   DrawPaint = null;
    private float   backX = 0.0f;
    private float   backY = 0.0f;

    private ImageView imageView = null;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            float[] location = getImageViewLocation((ImageView)view, event);
            float x = location[0];
            float y = location[1];

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    canvas.drawPoint(x, y, DrawPaint);
                    backX = x;
                    backY = y;
                    imageView.invalidate();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    canvas.drawLine(backX, backY, x, y, DrawPaint);
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

        //uri를 이미지로 변경하여
        Bitmap image = BitmapFactory.decodeFile(uri);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);


        width = image.getWidth();
        height = image.getHeight();
        result = Bitmap.createBitmap(width, height, image.getConfig());

   /*
        bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        imageviewBitmap = bitmapDrawable.getBitmap();
        width = imageviewBitmap.getWidth();
        height = imageviewBitmap.getHeight();
        result = Bitmap.createBitmap(width, height, imageviewBitmap.getConfig());
*/
        canvas = new Canvas(result);
        canvas.drawBitmap(image, 0f, 0f, null);

        editText = (EditText) findViewById(R.id.text);

        Button btn_InputDate = (Button) findViewById(R.id.btnInputdate);
        btn_InputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat df = new SimpleDateFormat("yy  MM  dd", Locale.KOREA);
                String strTime = df.format(new Date());

                int timeColor = getResources().getColor(R.color.colorTime);

                Paint tPaint = new Paint();
                tPaint.setTextSize(30);
                tPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                tPaint.setColor(timeColor);
                tPaint.setStyle(Paint.Style.FILL);

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
                if (btnState == 1) {
                    btn_inputfont.setText("입력완료");
                    editText.setHint("문자를 입력하세요");
                    editText.setHintTextColor(Color.RED);
                    editText.setEnabled(true);
                }
                if (btnState == 2) {
                    btn_inputfont.setText("적용됨");
                    if (editText.getText().toString().length() != 0) {

                        inputString = editText.getText().toString();

                        int timeColor = getResources().getColor(R.color.colorTime);

                        Paint paint = new Paint();
                        paint.setTextSize(90);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        paint.setColor(timeColor);
                        paint.setStyle(Paint.Style.FILL);


                        canvas.drawText(inputString, 80f, 935f, paint);

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
                BitmapDrawable d = (BitmapDrawable) ((ImageView) findViewById(R.id.imageView)).getDrawable();
                Bitmap b = d.getBitmap();
                saveBitmaptoJpeg(b);
            }
        });

        final Button btn_draw = (Button) findViewById(R.id.btnDraw);
        btn_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawBtnState++;
                if(drawBtnState==1){
                    btn_draw.setText("그리기 완료");

                    imageView.setImageBitmap(result);
                    imageView.setOnTouchListener(touchListener);

                    DrawPaint = new Paint();
                    DrawPaint.setColor(Color.BLACK);
                    DrawPaint.setAlpha(255);
                    DrawPaint.setStrokeWidth(5);
                    DrawPaint.setStrokeJoin(Paint.Join.ROUND);
                    DrawPaint.setStyle(Paint.Style.STROKE);
                    DrawPaint.setStrokeCap(Paint.Cap.ROUND);
                    DrawPaint.setAntiAlias(true);

                }
                if(drawBtnState!=1){
                    btn_draw.setText("적용됨");
                    imageView.setImageBitmap(result);
                    DrawPaint.setAlpha(0);
                }

            }
        });
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
    float[] getImageViewLocation(ImageView view, MotionEvent event)
    {
        int index = event.getActionIndex();
        float[] result = new float[] { event.getX(index), event.getY(index) };

        Matrix matrix = new Matrix();
        view.getImageMatrix().invert(matrix);
        matrix.postTranslate(view.getScrollX(), view.getScrollY());
        matrix.mapPoints(result);

        return result;
    }
}