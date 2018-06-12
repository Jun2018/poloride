package org.androidtown.poloride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private TextView textView;
    private Timer timer;

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


        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(250);
        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    private void update(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(count>30){
                    Toast.makeText(EditGalleryActivity.this, "완료되었습니다.", Toast.LENGTH_LONG).show();
                    timer.cancel();
                }else{
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
                    textview = (TextView)findViewById(R.id.mainTextView);
                    textview.setText(Integer.toString(++count));
                }

                Paint paint = new Paint();
                switch (count){
                    case 5:
                        paint.setColor(Color.WHITE);
                        paint.setAlpha(215);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 10:
                        paint.setColor(Color.WHITE);
                        paint.setAlpha(175);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 15:
                        paint.setColor(Color.WHITE);
                        paint.setAlpha(135);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 20:
                        paint.setColor(Color.WHITE);
                        paint.setAlpha(95);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 25:
                        paint.setColor(Color.WHITE);
                        paint.setAlpha(55);
                        ((LinearLayout)findViewById(R.id.AlphaLayout)).setBackgroundColor(paint.getColor());
                        break;
                    case 30:
                        paint.setColor(Color.WHITE);
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

