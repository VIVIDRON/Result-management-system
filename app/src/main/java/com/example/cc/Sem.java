package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class Sem extends AppCompatActivity {

    private EditText maths, maths_internal, phy, phy_internal, chem, chem_internal, mechanical, mechanical_internal, BEE, BEE_internal, workShop, Sname;
    private TextView title, subtitle;
    private Button send;
    private LinearLayout math1Layout, phy1Layout, che1Layout, mechLayout, beeLayout, workshopLayout, NameLayout;
    DatabaseReference databaseReference;
    private float sum = 0F;
    private String result = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sem);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Sem1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        maths = findViewById(R.id.m1_sem);
        maths_internal = findViewById(R.id.m1_internal);
        phy = findViewById(R.id.p1_sem);
        phy_internal = findViewById(R.id.p1_internal);
        chem = findViewById(R.id.c1_sem);
        chem_internal = findViewById(R.id.c1_internal);
        mechanical = findViewById(R.id.mech_sem);
        mechanical_internal = findViewById(R.id.mech_internal);
        BEE = findViewById(R.id.bee_sem);
        BEE_internal = findViewById(R.id.bee_internal);
        workShop = findViewById(R.id.workshop);
        send = findViewById(R.id.submit_btn);
        Sname = findViewById(R.id.Name);

        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);

        math1Layout = findViewById(R.id.math1Layout);
        phy1Layout = findViewById(R.id.phy1Layout);
        che1Layout = findViewById(R.id.che1Layout);
        mechLayout = findViewById(R.id.mechLayout);
        beeLayout = findViewById(R.id.beeLayout);
        workshopLayout = findViewById(R.id.workshopLayout);
        NameLayout = findViewById(R.id.Name_layout);

        Intent intent = getIntent();
        String semName = intent.getStringExtra("semName");
        String receivedString = intent.getStringExtra("myStringKey");
        subtitle.setText("Enter the details below of Student for " + semName);
        title.setText("ID : " + receivedString);

        switch (Objects.requireNonNull(semName)) {
            case ("Sem 1"):

                send.setEnabled(true);
                math1Layout.setVisibility(View.VISIBLE);
                phy1Layout.setVisibility(View.VISIBLE);
                che1Layout.setVisibility(View.VISIBLE);
                mechLayout.setVisibility(View.VISIBLE);
                beeLayout.setVisibility(View.VISIBLE);
                workshopLayout.setVisibility(View.VISIBLE);
                NameLayout.setVisibility(View.VISIBLE);
                break;

            case ("Sem 2"):
                send.setEnabled(false);
                break;

            case ("Sem 3"):
                send.setEnabled(false);
                break;

            // bhaiyo aage ke sem banate time extend karenge
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Objects.requireNonNull(semName)) {
                    case "Sem 1":
                        try {
                            sum += Calculations.sem1.for100(Integer.parseInt(maths.getText().toString()));
                            sum += Calculations.sem1.for25M(Integer.parseInt(maths_internal.getText().toString()));
                            sum += Calculations.sem1.for75(Integer.parseInt(phy.getText().toString()));
                            sum += Calculations.sem1.for25(Integer.parseInt(phy_internal.getText().toString()));
                            sum += Calculations.sem1.for75(Integer.parseInt(chem.getText().toString()));
                            sum += Calculations.sem1.for25(Integer.parseInt(chem_internal.getText().toString()));
                            sum += Calculations.sem1.for100(Integer.parseInt(mechanical.getText().toString()));
                            sum += Calculations.sem1.for50(Integer.parseInt(mechanical_internal.getText().toString()));
                            sum += Calculations.sem1.for100(Integer.parseInt(BEE.getText().toString()));
                            sum += Calculations.sem1.for50(Integer.parseInt(BEE_internal.getText().toString()));
                            sum += Calculations.sem1.for50(Integer.parseInt(workShop.getText().toString()));
                            sum = sum / 18;
                            result = String.format("%.2f", sum);
                        } catch (Exception e) {
                            Toast.makeText(Sem.this, "Empty TextField!", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Sem 2":
                        break;
                    case "Sem 3":
                        break;
                }
                try {
                    HashMap<String, String> markHashMap = new HashMap<>();
                    markHashMap.put("CGPA", result);
                    markHashMap.put("Name", Sname.getText().toString());
                    markHashMap.put("Sem", semName);
                    markHashMap.put("maths marks:", maths.getText().toString());
                    databaseReference.child(receivedString).setValue(markHashMap);
                    Toast.makeText(Sem.this, "Send Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(Sem.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}