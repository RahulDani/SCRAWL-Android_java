package com.rad.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_activity extends AppCompatActivity {

    EditText emailEditText,passwordEdittext;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText =findViewById(R.id.email_editText);
        passwordEdittext=findViewById(R.id.password_editText);
        loginBtn=findViewById(R.id.login_btn);
        progressBar=findViewById(R.id.progress_bar);
        createAccBtnTextView=findViewById(R.id.createAcc_text_btn);

        loginBtn.setOnClickListener(view -> loginUser());
        createAccBtnTextView.setOnClickListener(view -> startActivity(new Intent(Login_activity.this,Create_acc.class)));


    }

    void loginUser(){
        String email=emailEditText.getText().toString();
        String password=passwordEdittext.getText().toString();


        boolean isValidedata=validateData(email,password);  // or if(!validateData(email,password,confirmPassword)
        if(!isValidedata){
            return;
        }

        loginAccInFireBase(email,password);

    }

    void loginAccInFireBase(String email,String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInProgressBar(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgressBar(false);
                if(task.isSuccessful()){

                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Login_activity.this,MainActivity.class));
                        finish();                                                                          //login suceesfull

                    }
                    else{
                        Toast.makeText(Login_activity.this, "Email is not verified , kindly verify you email", Toast.LENGTH_LONG).show(); //email if not verified

                    }
                }
                else{
                    Toast.makeText(Login_activity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show(); //login failure

                }

            }
        });

    }



    void changeInProgressBar(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is not valid please try again!");
            return false;
        }
        if(password.length()>6){
            passwordEdittext.setError("Please provide password length of 6");
            return false;
        }
        return true;

    }
}