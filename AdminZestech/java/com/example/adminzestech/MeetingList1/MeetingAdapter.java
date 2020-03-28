package com.example.adminzestech.MeetingList1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzestech.R;

import java.util.ArrayList;
import java.util.List;


public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.UserViewHolder> {

    private Context mCtx;
    private List<meetingdone> meetingdoneList;
    public RecycleviewOnclick recycleviewOnclick;



    public MeetingAdapter(Context mCtx, List<meetingdone> meetingdoneList) {
        this.mCtx = mCtx;
        this.meetingdoneList = meetingdoneList;
    }
    //1
   /* public ViewHolder(View v) {
        super(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When item view is clicked, trigger the itemclicklistener
                //Because that itemclicklistener is indicated in MainActivity
                recycleviewOnclick.onItemClick(v,position);
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //When item view is clicked long, trigger the itemclicklistener
                //Because that itemclicklistener is indicated in MainActivity
                recycleviewOnclick.onItemLongClick(v,position);
                return true;
            }
        });  */
    //2
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.meetinglis_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        meetingdone meetingdone = meetingdoneList.get(position);
        holder.position=position;
        holder.textViewTime.setText(meetingdone.time);
        holder.textViewVenue.setText("Venue: " + meetingdone.venue);
        holder.textViewAgenda.setText("as: " + meetingdone.agenda);
        holder.textViewVenue.setText("Venue: " + meetingdone.venue);
        holder.textViewAgenda.setText("Agenda: " + meetingdone.agenda);
        holder.textViewDepartment.setText(("Department: "+ meetingdone.department));
        holder.textViewName.setText("Name: " + meetingdone.uname);
    }
    //Set method of OnItemClickListener object
    public void setOnItemClickListener(RecycleviewOnclick recyclerViewItemClickListener){
        this.recycleviewOnclick=recyclerViewItemClickListener;
    }

    @Override
    public int getItemCount() {
        return meetingdoneList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        public int position=0;
        TextView textViewTime, textViewVenue, textViewAgenda,textViewDepartment,textViewName;

        public UserViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemview) {
                    //When item view is clicked, trigger the itemclicklistener
                    //Because that itemclicklistener is indicated in MainActivity
                    recycleviewOnclick.onItemClick(itemview,position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View itemview) {
                    //When item view is clicked long, trigger the itemclicklistener
                    //Because that itemclicklistener is indicated in MainActivity
                    recycleviewOnclick.onItemLongClick(itemView,position);
                    return true;
                }
            });

            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewVenue = itemView.findViewById(R.id.text_view_venue);
            textViewAgenda = itemView.findViewById(R.id.text_view_agenda);
            textViewDepartment = itemView.findViewById(R.id.text_view_dpt);
            textViewName = itemView.findViewById(R.id.text_view_name);
        }
    }
}
