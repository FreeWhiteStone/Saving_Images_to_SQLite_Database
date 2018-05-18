package com.example.free.grad;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.free.grad.R;

public class PlantDetail extends Activity {
    Button b1,b2;
    TextView t1,t2,t3,t4;
    ImageView detailPlantImage;
    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantdetail);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Plant List");

        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);

        t1 = (TextView)findViewById(R.id.name);
        t2 = (TextView)findViewById(R.id.color);
        t3 = (TextView)findViewById(R.id.period);
        t4 = (TextView)findViewById(R.id.amount);
        detailPlantImage=(ImageView)findViewById(R.id.imageView11);


        Intent intent=getIntent();
        id = intent.getIntExtra("id", 0);
        Database db = new Database(getApplicationContext());
        HashMap<String, String> map = db.plantDetail(id);

        t1.setText(map.get("plant_name"));
        t2.setText(map.get("color").toString());
        t3.setText(map.get("irrigation_period").toString());
        t4.setText(map.get("irrigation_amount").toString());
        String image=(map.get("picture"));
        Log.d("fromDb",image);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(image, options);
        detailPlantImage.setImageBitmap(bitmap);

        b1.setOnClickListener(new View.OnClickListener() {//Kitap d�zenle butonuna t�kland�g�nda tekrardan kitab�n id sini g�nderdik

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditPlant.class);
                intent.putExtra("id", (int)id);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlantDetail.this);
                alertDialog.setTitle("Warning..!");
                alertDialog.setMessage("Are you sure want to delete plant..?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Database db = new Database(getApplicationContext());
                        db.deletePlant(id);
                        Toast.makeText(getApplicationContext(), "Plant deleted successfully...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                    }
                });
                alertDialog.show();

            }
        });


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
