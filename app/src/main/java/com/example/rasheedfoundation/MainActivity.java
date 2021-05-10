package com.example.rasheedfoundation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

public class MainActivity extends AppCompatActivity {
    Button LogInButton, RegisterButton,signInButton ;
    EditText Email, Password ;
    String EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String TempPassword = "NOT_FOUND" ;
    public static final String UserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //google
        LogInButton = (Button)findViewById(R.id.buttonLogin);

        RegisterButton = (Button)findViewById(R.id.buttonRegister);
        signInButton= (Button)findViewById(R.id.signInButton);
        Email = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);

        sqLiteHelper = new SQLiteHelper(this);

        //Adding click listener to log in button.
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling EditText is empty or no method.
                CheckEditTextStatus();

                // Calling login method.
                LoginFunction();


            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Opening new user registration activity using intent on button click.
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Opening new user registration activity using intent on button click.
                Intent intent = new Intent(MainActivity.this, google_signup.class);
                startActivity(intent);

            }
        });


    }
    public void LoginFunction(){

        if(EditTextEmptyHolder) {

            // Opening SQLite database write permission.
            sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

            // Adding search email query to cursor.
            cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

            while (cursor.moveToNext()) {

                if (cursor.isFirst()) {

                    cursor.moveToFirst();

                    // Storing Password associated with entered email.
                    TempPassword = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_Password));

                    // Closing cursor.
                    cursor.close();
                }
            }

            // Calling method to check final result ..
            CheckFinalResult();

        }
        else {

            Toast.makeText(MainActivity.this,"Please Enter UserName or Password.",Toast.LENGTH_LONG).show();

        }

    }

    public void CheckEditTextStatus(){
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        if( TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){

            EditTextEmptyHolder = false ;

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }

    public void CheckFinalResult(){

        if(TempPassword.equalsIgnoreCase(PasswordHolder))
        {

            Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();

            // Going to Dashboard activity after login success message.
            Intent intent = new Intent(MainActivity.this, dashboard.class);

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(UserEmail, EmailHolder);

            startActivity(intent);


        }
        else {

            Toast.makeText(MainActivity.this,"UserName or Password is Wrong, Please Try Again.", Toast.LENGTH_LONG).show();

        }
        TempPassword = "NOT_FOUND" ;

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}