package com.example.adminzestech.MeetingList1;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzestech.R;

import java.util.List;

public class StudentAttendAdapter extends RecyclerView.Adapter<StudentAttendAdapter.UserViewHolder> {

    private Context mCtx;
    private List<Student> StudentList;

    public StudentAttendAdapter(Context mCtx, List<Student> StudentList) {
        this.mCtx = mCtx;
        this.StudentList = StudentList;
    }

    @NonNull
    @Override
    public StudentAttendAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.stdattends_layout, parent, false);
        return new StudentAttendAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAttendAdapter.UserViewHolder holder, int position) {
        Student student = StudentList.get(position);
        holder.textViewName.setText(student.name);
        holder.textViewClass.setText("Class: " + student.std);
        holder.textViewRoll.setText("Roll no: " + student.rollno);

    }

    @Override
    public int getItemCount() {
        return StudentList.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewClass, textViewRoll;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewClass = itemView.findViewById(R.id.text_view_class);
            textViewRoll = itemView.findViewById(R.id.text_view_rollno);

        }
    }

}

