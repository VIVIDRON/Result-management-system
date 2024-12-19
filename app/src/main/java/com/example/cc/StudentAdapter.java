package com.example.cc;// StudentAdapter.java

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<StudentsData> studentList;

    public StudentAdapter(List<StudentsData> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentsData student = studentList.get(position);
        holder.rollNoTextView.setText("ID : "+student.getRollNo());
        holder.cgpaTextView.setText("Name : "+student.getStudentName()+", CGPA : "+student.getCGPA()+", Sem : "+student.getSemName());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView cgpaTextView;
        TextView rollNoTextView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            rollNoTextView = itemView.findViewById(R.id.rollNoTextView);
            cgpaTextView = itemView.findViewById(R.id.cgpaTextView);
        }
    }
}

