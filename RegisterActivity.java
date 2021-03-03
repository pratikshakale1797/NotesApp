package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.Model.User;
import com.example.noteapp.Utils.Constant;
import com.example.noteapp.Utils.DatabaseHandler;

public class RegisterActivity extends AppCompatActivity {


    EditText registration_email,registration_password,registration_confirmpassword;

    Button registration_button,registration_loginbutton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        registration_email=(EditText)findViewById(R.id.registration_email);
        registration_password=(EditText)findViewById(R.id.registration_password);
        registration_confirmpassword=(EditText)findViewById(R.id.registration_confirmpassword);
        registration_button=(Button)findViewById(R.id.registration_button);
        registration_loginbutton=(Button)findViewById(R.id.registration_loginbutton);
        registration_button.setOnClickListener(v -> checkValidation());
        registration_loginbutton.setOnClickListener(v -> {
            Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }

    private void checkValidation(){
        String email=registration_email.getText().toString();
        String password=registration_password.getText().toString();
        String confirmPassword=registration_confirmpassword.getText().toString();

        if(Constant.isEmptyOrNull(email)){
            registration_email.requestFocus();
            registration_email.setError("Please enter email");
        }else if(Constant.isEmptyOrNull(password)){
            registration_password.requestFocus();
            registration_password.setError("Please enter password");
        }else if(Constant.isEmptyOrNull(confirmPassword)){
            registration_confirmpassword.requestFocus();
            registration_confirmpassword.setError("Please enter confirm password");
        }else if(!Constant.isValidEmail(email)){
            registration_email.requestFocus();
            registration_email.setError("Please enter valid email");
        }else if(password.length()<6){
            registration_password.requestFocus();
            registration_password.setError("Password length should be greater then 6");
        }else if(confirmPassword.length()<6){
            registration_confirmpassword.requestFocus();
            registration_confirmpassword.setError("Confirm password length should be greater then 6");
        }else if(!password.equals(confirmPassword)){
            registration_confirmpassword.requestFocus();
            registration_confirmpassword.setError("Password and confirm password should be same");
        }else{
            addUserToDB(email,password);
        }
    }

    private void addUserToDB(String email,String password){
        boolean isAvailable=db.checkUniqueEmail(email);
        if(isAvailable) {
            Toast.makeText(this, "This email already exist.Try another email" , Toast.LENGTH_LONG).show();
        }
        else{
            User user=new User();
            user.setEmail(email);
            user.setPassword(password);
            long value=db.insertUserData(user);
            Toast.makeText(this, "User register successfully" , Toast.LENGTH_LONG).show();
            if(value>0){
                Intent intent=new Intent(RegisterActivity.this,RecyclerActivity.class);
                intent.putExtra("login_user_email",email);
                startActivity(intent);
            }
        }
    }
}