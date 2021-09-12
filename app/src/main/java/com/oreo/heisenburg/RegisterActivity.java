package com.oreo.heisenburg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    EditText Username, Password, Name;
    Button Register;
    String NameHolder, UsernameHolder, PasswordHolder, encryptedPassword;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder;
    DatabaseHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Register = findViewById(R.id.register_buttonR);

        Username = findViewById(R.id.text_usernameR);
        Password = findViewById(R.id.text_passwordR);
        Name = findViewById(R.id.text_nameR);

        sqLiteHelper = new DatabaseHelper(this);

        // Adding click listener to register button.

        Register.setOnClickListener(view -> {

            // Creating SQLite database if doesn't exists
            SQLiteDataBaseBuild();

            // Creating SQLite table if doesn't exists.
            SQLiteTableBuild();

            // Checking EditText is empty or Not.
            CheckEditTextStatus();

            // Method to check Username is already exists or not.
            CheckingUsernameAlreadyExistsOrNot();

            // Empty EditText After done inserting process.
            EmptyEditTextAfterDataInsert();


        });
    }

    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite table build method.
    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseHelper.TABLE_NAME + "(" + DatabaseHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + DatabaseHelper.Table_Column_1_Name + " VARCHAR, " + DatabaseHelper.Table_Column_2_Username + " VARCHAR, " + DatabaseHelper.Table_Column_3_Password + " VARCHAR);");

    }

    // Insert data into SQLite database method.
    public void InsertDataIntoSQLiteDatabase(){

        // If editText is not empty then this block will executed.
        if(EditTextEmptyHolder)
        {

            // SQLite query to insert data into table.
            SQLiteDataBaseQueryHolder = "INSERT INTO "+DatabaseHelper.TABLE_NAME+" (name, username, password) VALUES('"+NameHolder+"', '"+UsernameHolder+"', '"+encryptedPassword+"');";

            // Executing query.
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            // Closing SQLite database object.
            sqLiteDatabaseObj.close();

            // Printing toast message after done inserting.
            Toast.makeText(RegisterActivity.this,"User Registered Successfully", Toast.LENGTH_LONG).show();

        }
        // This block will execute if any of the registration EditText is empty.
        else {

            // Printing toast message if any of EditText is empty.
            Toast.makeText(RegisterActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }
    }
    // Empty edittext after done inserting process method.
    public void EmptyEditTextAfterDataInsert(){

        Name.getText().clear();

        Username.getText().clear();

        Password.getText().clear();

    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        NameHolder = Name.getText().toString() ;
        UsernameHolder = Username.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(!PasswordHolder.isEmpty()) {
            encryptedPassword = md5(PasswordHolder);
        }

        EditTextEmptyHolder = !TextUtils.isEmpty(NameHolder) && !TextUtils.isEmpty(UsernameHolder) && !TextUtils.isEmpty(PasswordHolder);
    }
    // Checking Username is already exists or not.
    public void CheckingUsernameAlreadyExistsOrNot(){

        // Opening SQLite database write permission.
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(DatabaseHelper.TABLE_NAME, null, " " + DatabaseHelper.Table_Column_2_Username + "=?", new String[]{UsernameHolder}, null, null, null);

        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Username is already exists then Result variable value set as Username Found.
                F_Result = "Username Found";

                // Closing cursor.
                cursor.close();
            }
        }
        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();
    }
    // Checking result
    public void CheckFinalResult(){

        // Checking whether Username is already exists or not.
        if(F_Result.equalsIgnoreCase("Email Found"))
        {

            // If username is exists then toast msg will display.
            Toast.makeText(RegisterActivity.this,"Email Already Exists",Toast.LENGTH_LONG).show();

        }
        else {

            // If username already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();

        }

        F_Result = "Not_Found" ;

    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
