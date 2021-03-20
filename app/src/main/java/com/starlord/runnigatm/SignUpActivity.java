package com.starlord.runnigatm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private EditText userName, email, password;
    private Button signUpBtn;
    private TextView signUpText;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email_singUp);
        password = findViewById(R.id.password_signUp);
        signUpBtn = findViewById(R.id.signUp);
        signUpText = findViewById(R.id.signIn_tv);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        signUpBtn.setOnClickListener(view -> {
            final String userNameValue = userName.getText().toString().trim();
            String emailValue = email.getText().toString().trim();
            String passwordValue = password.getText().toString().trim();

            if (TextUtils.isEmpty(userNameValue)) {
                userName.setError("Required field");
                return;
            }
            if (TextUtils.isEmpty(emailValue)) {
                email.setError("Required field");
                return;
            }
            if (TextUtils.isEmpty(passwordValue)) {
                password.setError("Required field");
                return;
            }
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            sendEmailVerification();
                            final FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            Map<String, Object> userDetails = new HashMap<>();
                            userDetails.put("name", userNameValue);
                            userDetails.put("email", emailValue);
                            userDetails.put("phone", "0987654321");

                            DocumentReference documentReference = db.collection("users").document(userID);
                            documentReference.set(userDetails).addOnSuccessListener(aVoid -> Log.d(TAG, "User profile updated."))
                                    .addOnFailureListener(e -> Log.d(TAG, "User profile not updated."));

                            //update DisplayName of the user
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userNameValue).build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d(TAG, "Display name updated.");
                                        }
                                    });

                            //After user registration
                            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign Up failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        });

        signUpText.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    // Email sent
                    Log.d(TAG, "Verification email sent");
                });
    }
}