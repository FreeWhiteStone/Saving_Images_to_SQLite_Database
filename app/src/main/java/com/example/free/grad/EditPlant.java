package com.example.free.grad;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.free.grad.R;

import java.text.ParseException;
import java.util.HashMap;

public class EditPlant extends Activity {

    Button b1;
    EditText e1,e2,e3,e4;
    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplant);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Plant Detail");

        b1 = (Button)findViewById(R.id.button1);
        e1 = (EditText)findViewById(R.id.editText1);
        e2 = (EditText)findViewById(R.id.editText2);
        e3 = (EditText)findViewById(R.id.editText3);
        e4 = (EditText)findViewById(R.id.editText4);

        Intent intent=getIntent();
        id = intent.getIntExtra("id", 0);

        Database db = new Database(getApplicationContext());
        HashMap<String, String> map = db.plantDetail(id);

        e1.setText(map.get("plant_name"));
        e2.setText(map.get("color").toString());
        e3.setText(map.get("irrigation_period").toString());
        e4.setText(map.get("irrigation_amount").toString());

        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String name,color,period,amount;
                name = e1.getText().toString();
                color = e2.getText().toString();
                period = e3.getText().toString();
                amount = e4.getText().toString();
                if(name.matches("") || color.matches("") || period.matches("") || amount.matches("")  ){
                    Toast.makeText(getApplicationContext(), "Please fill all areas...", Toast.LENGTH_LONG).show();
                }else{
                    Database db = new Database(getApplicationContext());
                    try {
                        db.editPlant(name,color,period,amount,id);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.d("errrrr","hata");
                    }
                    db.close();
                    Toast.makeText(getApplicationContext(), "Your plant editted succesfully...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }


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
