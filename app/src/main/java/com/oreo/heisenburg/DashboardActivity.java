package com.oreo.heisenburg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabaseObj;
    DatabaseHelper sqLiteHelper;
    Cursor cursor;
    String UsernameHolder;
    Button LogOUT ;
    TextView User;
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        User = findViewById(R.id.textView1);


        txt1 = findViewById(R.id.textView);
        txt2 = findViewById(R.id.textView2);
        txt3 = findViewById(R.id.textView3);
        txt4 = findViewById(R.id.textView4);


        LogOUT = findViewById(R.id.logout_button);

        sqLiteHelper = new DatabaseHelper(this);

        Intent intent = getIntent();

        // Receiving Username Send By MainActivity.
        UsernameHolder = intent.getStringExtra(MainActivity.UserName);

        // Setting up received email to TextView.
        User.setText(User.getText().toString()+ UsernameHolder);

        sqLiteDatabaseObj = sqLiteHelper.getReadableDatabase();
        cursor = sqLiteDatabaseObj.rawQuery("SELECT * FROM "+ DatabaseHelper.TABLE_NAME +" WHERE "+ DatabaseHelper.Table_Column_2_Username + " = '" + UsernameHolder +"'", null);
        cursor.moveToFirst();
        if (cursor != null) {
            txt1.setText("ID: " + cursor.getString(0));
            txt2.setText("Name: "+ cursor.getString(1));
            txt3.setText("UserName: "+ cursor.getString(2));
            txt3.setText("Password: "+ cursor.getString(3));
        }

        // Adding click listener to Log Out button.
        LogOUT.setOnClickListener(view -> {

            //Finishing current DashBoard activity on button click.
            finish();
            Toast.makeText(DashboardActivity.this,"Log Out Successful", Toast.LENGTH_LONG).show();
        });

    }
}
