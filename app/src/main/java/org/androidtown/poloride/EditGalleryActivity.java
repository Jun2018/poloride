package org.androidtown.poloride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;



public class EditGalleryActivity extends Activity implements SensorEventListener{
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;

    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

    private TextView textview=null;
    private int count = 0;
    private int saveCount = 0;
    private TextView textView;
    private Timer timer;

    ProgressBar progress;

    Paint paint = new Paint();
    private final android.os.Handler handler = new android.os.Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_edit_gallery);


        Intent intent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        progress = (ProgressBar) findViewById(R.id.progress) ;


        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 2000);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count>30){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void update(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(count>30){
                    Toast.makeText(EditGalleryActivity.this, "완료되었습니다.", Toast.LENGTH_LONG).show();
                    timer.cancel();
                }else{
                    progress.setProgress(count);
                    count++;
                }
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime); //최근 측정한 시간과 현재 시간을 비교하여 0.1초 이상되었을 때, 흔듬을 감지
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) { // SHAKE_THRESHOLD : 속도가 얼마 이상일 때, 흔듬을 감지하겠다는 것을 설정
                    progress.setProgress(count);
                    count++;

                }


                switch (count){
                    case 1:
                        paint.setAlpha(242);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 2:
                        paint.setAlpha(234);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 3:
                        paint.setAlpha(226);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 4:
                        paint.setAlpha(218);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 5:
                        paint.setAlpha(210);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 6:
                        paint.setAlpha(202);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 7:
                        paint.setAlpha(194);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 8:
                        paint.setAlpha(186);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 9:
                        paint.setAlpha(178);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 10:
                        paint.setAlpha(170);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 11:
                        paint.setAlpha(161);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 12:
                        paint.setAlpha(152);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 13:
                        paint.setAlpha(143);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 14:
                        paint.setAlpha(134);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 15:
                        paint.setAlpha(125);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 16:
                        paint.setAlpha(116);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 17:
                        paint.setAlpha(107);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 18:
                        paint.setAlpha(98);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 19:
                        paint.setAlpha(89);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 20:
                        paint.setAlpha(80);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 21:
                        paint.setAlpha(72);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 22:
                        paint.setAlpha(64);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 23:
                        paint.setAlpha(56);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 24:
                        paint.setAlpha(48);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 25:
                        paint.setAlpha(40);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 26:
                        paint.setAlpha(32);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 27:
                        paint.setAlpha(24);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 28:
                        paint.setAlpha(16);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 29:
                        paint.setAlpha(8);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 30:
                        paint.setAlpha(0);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }

    }

}

