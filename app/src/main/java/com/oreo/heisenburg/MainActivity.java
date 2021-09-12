package com.oreo.heisenburg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button LogInButton;
    Button RegisterButton;
    EditText Username, Password;
    String UsernameHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    DatabaseHelper sqLiteHelper;
    Cursor cursor;
    String TempPassword="NOT_FOUND";
    public static final String UserName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogInButton = findViewById(R.id.login_button);

        RegisterButton = findViewById(R.id.register_button);

        Username = findViewById(R.id.text_username);
        Password = findViewById(R.id.text_password);

        sqLiteHelper = new DatabaseHelper(this);

        //Adding click listener to log in button.
        LogInButton.setOnClickListener(view -> {
            // Calling EditText is empty or no method.
            CheckEditTextStatus();

            // Calling login method.
            LoginFunction();
        });

        // Adding click listener to register button.
        RegisterButton.setOnClickListener(view -> {
            // Opening new user registration activity using intent on button click.
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Login function starts from here.
    public void LoginFunction(){
        if(EditTextEmptyHolder) {
            // Opening SQLite database write permission.
            sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

            // Adding search email query to cursor.
            cursor = sqLiteDatabaseObj.query(DatabaseHelper.TABLE_NAME, null, " " + DatabaseHelper.Table_Column_2_Username + "=?", new String[]{UsernameHolder}, null, null, null);

            while (cursor.moveToNext()) {

                if (cursor.isFirst()) {

                    cursor.moveToFirst();

                    // Storing Password associated with entered username.
                    TempPassword = cursor.getString(cursor.getColumnIndex(DatabaseHelper.Table_Column_3_Password));

                    // Closing cursor.
                    cursor.close();
                }
            }
        // Calling method to check final result ..
        CheckFinalResult();
        }
        else {
            //If any of login EditText empty then this block will be executed.
            Toast.makeText(MainActivity.this,"Please Enter UserName or Password.",Toast.LENGTH_LONG).show();
        }
    }

    // Checking EditText is empty or not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        UsernameHolder = Username.getText().toString();
        PasswordHolder = Password.getText().toString();

        // Checking EditText is empty or no using TextUtils.
        EditTextEmptyHolder = !TextUtils.isEmpty(UsernameHolder) && !TextUtils.isEmpty(PasswordHolder);
    }

    // Checking entered password from SQLite database username associated password.
    public void CheckFinalResult(){

        String encryptedPassword = RegisterActivity.md5(PasswordHolder);

        if(TempPassword.equals(encryptedPassword))
        {

            Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();

            // Going to Dashboard activity after login success message.
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);

            // Sending Username to Dashboard Activity using intent.
            intent.putExtra(UserName, UsernameHolder);

            startActivity(intent);


        }
        else {

            Toast.makeText(MainActivity.this,"UserName or Password is Wrong, Please Try Again.",Toast.LENGTH_LONG).show();

        }
        TempPassword = "NOT_FOUND" ;

    }
}