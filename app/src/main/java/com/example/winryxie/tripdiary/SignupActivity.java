package com.example.winryxie.tripdiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{


    private Button signupButton;
    private EditText editTextSignupEmail;
    private EditText editTextSignupPassword;
    private EditText editTextRepassword;
    private TextView toLoginText;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signupButton = (Button) findViewById(R.id.signupButton);
        editTextSignupEmail = (EditText) findViewById(R.id.editTextSignupEmail);
        editTextSignupPassword = (EditText)findViewById(R.id.editTextSignupPassword);
        editTextRepassword = (EditText) findViewById(R.id.editTextRepassword);
        toLoginText = (TextView) findViewById(R.id.toLogInText);

        signupButton.setOnClickListener(this);
        toLoginText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signupButton) {
            signupUser();
        }
        if (v == toLoginText) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void signupUser() {
        String email = editTextSignupEmail.getText().toString().trim();
        String password = editTextSignupPassword.getText().toString().trim();
        String repassword = editTextRepassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(repassword)) {
            Toast.makeText(this,"Please repeat enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repassword)) {
            Toast.makeText(this,"Password doesn't match, please try again..",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Sign up user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            //start successful and start a new profile
                            Toast.makeText(SignupActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            return;
                        } else {
                            Toast.makeText(SignupActivity.this, "Could not sign in, please try again...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }
}
