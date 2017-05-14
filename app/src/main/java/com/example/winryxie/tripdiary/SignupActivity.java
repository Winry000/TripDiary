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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.winryxie.tripdiary.Model.User;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{


    private Button signupButton;
    private EditText editTextSignupEmail;
    private EditText editTextSignupPassword;
    private EditText editTextRepassword;
    private EditText editTextName;
    private TextView toLoginText;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    public static final String FB_DATABASE_PATH = "user";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        signupButton = (Button) findViewById(R.id.signupButton);
        editTextSignupEmail = (EditText) findViewById(R.id.editTextSignupEmail);
        editTextSignupPassword = (EditText)findViewById(R.id.editTextSignupPassword);
        editTextRepassword = (EditText) findViewById(R.id.editTextRepassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
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
        final String email = editTextSignupEmail.getText().toString().trim().toLowerCase();
        final String password = editTextSignupPassword.getText().toString().trim();
        final String repassword = editTextRepassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
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
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this,"Please enter a name",Toast.LENGTH_SHORT).show();
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
                            //start successful and start a new profil
                            User user = new User(name, email);
                            //save the imginfo to firedatabase
                            String userId = databaseReference.push().getKey();
                            databaseReference.child(userId).setValue(user);

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
