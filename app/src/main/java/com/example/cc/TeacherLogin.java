package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class TeacherLogin extends AppCompatActivity {

    EditText teacherID, teacherPass;
    Button signInButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.teacher_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        teacherID = findViewById(R.id.ev_teacherID);
        teacherPass = findViewById(R.id.ev_teacherPass);
        signInButton = findViewById(R.id.btn_signIn);

        mAuth = FirebaseAuth.getInstance();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = teacherID.getText().toString();
                String password = teacherPass.getText().toString();

                if (id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(TeacherLogin.this, "Please enter both ID and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Sign-in success logic (for demonstration purposes)
                    mAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(TeacherLogin.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                updateUI(user);
                            } else {
                                Toast.makeText(TeacherLogin.this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            Intent intent = new Intent(TeacherLogin.this, StudentList.class);
            startActivity(intent);
        }
    }

}
