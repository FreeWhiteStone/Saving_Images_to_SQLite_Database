package com.example.free.grad;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.free.grad.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends Activity {


    ListView lv;
    ImageView iwCam,iwDb;

    String CameraPicture = "";
    String DatabasePicture = "";

    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> plant_list;
    String plant_names[];
    int plant_ids[];
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        Button btnCamera = (Button)findViewById(R.id.compare);
        iwCam = (ImageView)findViewById(R.id.imageView12);
        iwDb = (ImageView)findViewById(R.id.imageView13);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(getApplicationContext());
                HashMap<String, String> map = db.getProperImage();
                String image=(map.get("picture"));



                DatabasePicture=image;
                Log.d("database_pic:",DatabasePicture);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(image, options);
                iwDb.setImageBitmap(bitmap);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);


            }
        });

        NotificationAlarmService notificationAlarmService = new NotificationAlarmService(this);
        notificationAlarmService.startAlarm();

    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                iwCam.setImageBitmap(bmp);

                iwCam.setImageBitmap(bmp);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                String fileName = "TEMP_IMG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File pictureFile = null;
                try {
                    pictureFile = File.createTempFile(fileName, ".jpg", storageDir);
                    CameraPicture=pictureFile.getAbsolutePath();
                    int result=surf(CameraPicture,DatabasePicture);
                    Log.d("sssss:",CameraPicture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("tttt",pictureFile.toString());
                String tempImageFilePath=pictureFile.getAbsolutePath();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(pictureFile);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    fos.flush();
                    fos.close();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                Database db = new Database(getApplicationContext());
                db.addTempPicture(tempImageFilePath);
                db.close();

            }
        }





    public void onResume()
    {
        super.onResume();
        Database db = new Database(getApplicationContext());
        plant_list = db.plants();
        if(plant_list.size()==0){
            Toast.makeText(getApplicationContext(), "There is no plant\nPlease use + button to add plant...", Toast.LENGTH_LONG).show();
        }else{
            plant_names = new String[plant_list.size()];
            plant_ids = new int[plant_list.size()];
            for(int i=0;i<plant_list.size();i++){
                plant_names[i] = plant_list.get(i).get("plant_name");

                plant_ids[i] = Integer.parseInt(plant_list.get(i).get("id"));
            }
            lv = (ListView) findViewById(R.id.list_view);

            adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.plant_name, plant_names);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    Intent intent = new Intent(getApplicationContext(), PlantDetail.class);
                    intent.putExtra("id", (int)plant_ids[arg2]);
                    startActivity(intent);

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                AddPlant();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AddPlant() {
        Intent i = new Intent(MainActivity.this, AddPlant.class);
        startActivity(i);
    }

    int surf(String cam, String db){

        String newPath=cam+db;
        Log.d("newPath",newPath);

        return 1;
    }
}
