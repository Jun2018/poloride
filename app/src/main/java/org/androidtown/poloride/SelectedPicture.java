package org.androidtown.poloride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class SelectedPicture extends AppCompatActivity {
    String uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_selected_picture);

        Intent intent = getIntent();
        uri = intent.getStringExtra("Data");

        Bitmap image = BitmapFactory.decodeFile(uri);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);


        /*
        Intent intent = getIntent();
        uri = intent.getStringExtra("Data");

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(uri));
*/


        Toast.makeText(SelectedPicture.this, uri, Toast.LENGTH_LONG).show(); //uri 확인용...


        final Button btn_home = (Button)findViewById(R.id.btnHome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        final Button btn_font = (Button)findViewById(R.id.btnDeco);
        btn_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DecoActivity.class);
                intent.putExtra("URI", uri);
                startActivity(intent);
            }
        });


        final Button btn_del = (Button)findViewById(R.id.btn_del);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
          //      boolean deleted = Intent.delete();
//                startActivity(intent);

                File file = new File(Uri.parse(uri).getPath());
                System.out.println("file Deleted :" + uri);
                boolean isFileDeleted = file.delete();
                System.out.println("file isFileDeleted :" + isFileDeleted);
                if(isFileDeleted) {
                    Toast.makeText(SelectedPicture.this, "삭제했습니다.", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
//                if (file.exists()) {
//                    if (file.delete()) {
//                        System.out.println("file Deleted :" + uri);
//                    } else {
//                        System.out.println("file not Deleted :" + uri);
//                    }
//                }
//
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
//                        Uri.parse("file://" +  Environment.getExternalStorageDirectory())));

            }
        });


        //공유하기 버튼 구현
        final Button btnSharing = (Button)findViewById(R.id.btnShare); //버튼 찾아옴
        btnSharing.setOnClickListener(new View.OnClickListener() { //버튼 클릭이벤트함수
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND); //전송 메소드 호출. Intent.ACTION_SEND
                intent.setType("image/*"); //jpg 이미지를 공유 하기 위해 Type을 지정. 여기서는 모든 파일하려고 *
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "공유"));

            }

        });

    }

}
