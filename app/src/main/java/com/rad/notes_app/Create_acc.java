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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Create_acc extends AppCompatActivity {
    EditText emailEditText,passwordEdittext,confirmPasswordEditText;
    Button createAccBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        emailEditText =findViewById(R.id.email_editText);
        passwordEdittext=findViewById(R.id.password_editText);
        confirmPasswordEditText=findViewById(R.id.confirmpassword_editText);
        createAccBtn=findViewById(R.id.create_btn);
        progressBar=findViewById(R.id.progress_bar);
        loginBtnTextView=findViewById(R.id.login_text_btn);

        createAccBtn.setOnClickListener(view-> createAcc());
        loginBtnTextView.setOnClickListener(view ->startActivity(new Intent(Create_acc.this,Login_activity.class)) ); // for now we r dealing with




    }
    void createAcc(){
        String email=emailEditText.getText().toString();
        String password=passwordEdittext.getText().toString();
        String confirmPassword=confirmPasswordEditText.getText().toString();

        boolean isValidedata=validateData(email,password,confirmPassword);  // or if(!validateData(email,password,confirmPassword)
        if(!isValidedata){
            return;
        }

        createAccInFireBase(email,password);

    }

    void createAccInFireBase(String email,String password){
        changeInProgressBar(true);

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Create_acc.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgressBar(false);
                if (task.isSuccessful()) {
                    Toast.makeText(Create_acc.this, "Account created successfully, Check mail to verify", Toast.LENGTH_LONG).show();  //for sccuessfully creating an acc
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }
                else {
                    Toast.makeText(Create_acc.this,task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show(); //exception means the rason for which acc isnt created

                }
            }
        });


    }

    void changeInProgressBar(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password,  String confirmPassword){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is not valid please try again!");
            return false;
        }
        if(password.length()>6){
            passwordEdittext.setError("Please provide password length of 6");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Opps! Password is not matching \n TRY AGAIN!!");
            return false;
        }
        return true;

    }

}