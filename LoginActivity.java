package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapp.Utils.Constant;
import com.example.noteapp.Utils.DatabaseHandler;

public class LoginActivity extends AppCompatActivity {
    EditText login_email,login_password;
    Button login_button,reg_button;

    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();


        login_email=(EditText)findViewById(R.id.login_email);
        login_password=(EditText)findViewById(R.id.login_password);
        login_button=(Button)findViewById(R.id.login_button);
        reg_button=(Button)findViewById(R.id.reg_button);
        login_button.setOnClickListener(v -> checkValidation());
        reg_button.setOnClickListener(v -> {
            Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    private void checkValidation(){
    String email=login_email.getText().toString();
    String password=login_password.getText().toString();

    if(Constant.isEmptyOrNull(email)){
    login_email.requestFocus();
    login_email.setError("Please enter email");
    }else if(Constant.isEmptyOrNull(password)){
        login_password.requestFocus();
        login_password.setError("Please enter password");
    }else if(!Constant.isValidEmail(email)){
        login_email.requestFocus();
        login_email.setError("Please enter valid email");
    }else if(password.length()<6){
        login_password.requestFocus();
        login_password.setError("Password length should be greater then 6");
    }else{
        checkUserData(email,password);

    }
    }

    private void checkUserData(String email,String password){
        boolean isAvailable=db.checkUser(email,password);
        if(isAvailable){
            Toast.makeText(this,"Login successfully",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(LoginActivity.this, RecyclerActivity.class);
            intent.putExtra("login_user_email",email);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Invalid login credential",Toast.LENGTH_LONG).show();
        }
    }
}