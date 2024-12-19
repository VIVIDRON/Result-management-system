package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentList extends AppCompatActivity {

    RecyclerView studentsList;
    Button createStudentButton;
    EditText studentID;
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;
    private List<StudentsData> studentList = new ArrayList<>();
    DatabaseReference databaseReference;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Get reference to the Spinner
        spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_items, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        studentsList = findViewById(R.id.rv_studentsList);
        createStudentButton = findViewById(R.id.btn_createStudentButton);
        studentID = findViewById(R.id.ev_studentIDStudentList);

        recyclerView = findViewById(R.id.rv_studentsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(studentList);
        recyclerView.setAdapter(studentAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference(); // Replace with your path

        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String rollNo = snapshot.getKey(); // Get roll number (key)
                    String cgpa = snapshot.child("CGPA").getValue(String.class); // Get CGPA
                    String studentName = snapshot.child("Name").getValue(String.class); // Get CGPA
                    String semName = snapshot.child("Sem").getValue(String.class); // Get CGPA
                    studentList.add(new StudentsData(rollNo, cgpa, studentName, semName)); // Create a new Student object
                }
                studentAdapter.notifyDataSetChanged(); // Refresh RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudentList.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });


        createStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = spinner.getSelectedItem().toString();
                if (txt.equals("Select Sem")) {
                    Toast.makeText(StudentList.this, "Select the Sem", Toast.LENGTH_LONG).show();
                } else {
                    String myString = studentID.getText().toString();
                    if (myString.isEmpty()) {
                        Toast.makeText(StudentList.this, "Empty ID", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(StudentList.this, Sem.class);
                        intent.putExtra("myStringKey", myString);
                        intent.putExtra("semName", txt);
                        startActivity(intent);
                    }
                }
            }
        });

    }
}