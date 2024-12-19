package com.example.cc;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;

public class FetchCGPAActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText studentIDFetchCGPA;
    Button fetch, downloadPDF;
    TextView cgpaShow, nameLabel, semLabel, cgpaLabel;
    String studentName, cgpa;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fetch_cgpaactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        studentIDFetchCGPA = findViewById(R.id.ev_studentIDFetchCGPA);


        fetch = findViewById(R.id.btn_fetch);
        cgpaShow = findViewById(R.id.tv_cgpaShow);

        nameLabel = findViewById(R.id.nameLabel);
        semLabel = findViewById(R.id.semLabel);
        cgpaLabel = findViewById(R.id.cgpaLabel);
        downloadPDF = findViewById(R.id.btn_download);

        downloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(studentIDFetchCGPA.getText().toString().isEmpty())) {
                    if (nameLabel.getText().toString().equals("Name : XXXXXXXXXX") || semLabel.getText().toString().equals("Sem : XX") || cgpaLabel.getText().toString().equals("SGPA : X")) {
                        Toast.makeText(FetchCGPAActivity.this, "Wrong ID!", Toast.LENGTH_SHORT).show();
                    } else {
                        PdfDocument pdfDocument = new PdfDocument();

                        // Define the page info
                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

                        // Start a page
                        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                        // Canvas to draw on the page
                        Canvas canvas = page.getCanvas();
                        Paint paint = new Paint();

                        // Add text to the PDF
                        paint.setTextSize(14);
                        canvas.drawText("Atharva's CGPA Calculator", 10, 25, paint);
                        canvas.drawText("Student Name: " + studentName, 10, 50, paint);
                        canvas.drawText("You have obtained: " + cgpa, 10, 75, paint);

                        // Finish the page
                        pdfDocument.finishPage(page);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            // For Android 10 and above, use MediaStore API

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "my_file.pdf");
                            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS); // Save to Downloads folder

                            Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                            try {
                                if (uri != null) {
                                    try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                                        pdfDocument.writeTo(outputStream);
                                        Toast.makeText(FetchCGPAActivity.this, "PDF saved to Downloads folder!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (IOException e) {
                                Toast.makeText(FetchCGPAActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        // Close the document
                        pdfDocument.close();
                    }
                } else {
                    Toast.makeText(FetchCGPAActivity.this, "ID is Empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child(studentIDFetchCGPA.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        //databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                studentName = snapshot.child("Name").getValue(String.class);
                                String semName = snapshot.child("Sem").getValue(String.class);
                                cgpa = snapshot.child("CGPA").getValue(String.class);
                                nameLabel.setText("Name : " + studentName);
                                semLabel.setText("Sem : " + semName);
                                cgpaLabel.setText("SGPA : " + cgpa);

                            } else {
                                Toast.makeText(FetchCGPAActivity.this, "No student found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(FetchCGPAActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(FetchCGPAActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}