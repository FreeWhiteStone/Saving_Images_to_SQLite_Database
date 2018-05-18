package com.example.free.grad;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.free.grad.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPlant extends Activity {


    String imageFilePath = "";
    ImageView plantImage;
    String date_save ="";
    private Button pickImage;
    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    Button addPlantButton;
    EditText e1,e2,e3,e4;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplant);

        Button btnCamera = (Button)findViewById(R.id.btnCamera);
        plantImage = (ImageView)findViewById(R.id.imageView10);

//        if (ContextCompat.checkSelfPermission(AddPlant.class, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
//                PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(AddPlant.class, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_PERMISSION);
//        }


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Plant List");
        addPlantButton = (Button)findViewById(R.id.button1);
        e1 = (EditText)findViewById(R.id.editText1);
        e2 = (EditText)findViewById(R.id.editText2);
        e3 = (EditText)findViewById(R.id.editText3);
        e4 = (EditText)findViewById(R.id.editText4);

        addPlantButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String name,color,period,amount;
                name = e1.getText().toString();
                color = e2.getText().toString();
                period = e3.getText().toString();
                amount = e4.getText().toString();

                if(name.matches("") || color.matches("") || period.matches("") || amount.matches("") || imageFilePath.matches("")  ){
                    Toast.makeText(getApplicationContext(), "Please fill all areas...", Toast.LENGTH_LONG).show();
                }else{
                    Database db = new Database(getApplicationContext());

                    db.addPlant(name,color,period,amount,imageFilePath);
                    Log.d("pathhh",imageFilePath);
                    db.close();
                    Toast.makeText(getApplicationContext(), "Your plant added succesfully...", Toast.LENGTH_LONG).show();
                    e1.setText("");
                    e2.setText("");
                    e3.setText("");
                    e4.setText("");
                    plantImage.setVisibility(View.INVISIBLE);
                }


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            plantImage.setImageBitmap(bmp);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
            date_save=timeStamp;
            Log.d("saveee: ",date_save);
            String fileName = "IMG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File pictureFile = null;
            try {
                pictureFile = File.createTempFile(fileName, ".jpg", storageDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("tttt",pictureFile.toString());
            imageFilePath=pictureFile.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(pictureFile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();

            }


//            String fileName = "IMG_" + timeStamp + "_";
//            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            try {
//                File image = File.createTempFile(fileName, ".jpg", storageDir);
//                Log.d("tttt",image.toString());
//                imageFilePath=image.getAbsolutePath();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//        plantImage.setImageBitmap(bitmap);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }




}



