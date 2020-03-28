package com.example.manishauth;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.UserViewHolder> {
    private Context mCtx;
    private List<Meeting> MeetingList;
    public RecycleviewOnclick recycleviewOnclick;


    public MeetingAdapter(Context mCtx, List<Meeting> MeetingList) {
        this.mCtx = mCtx;
        this.MeetingList = MeetingList;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.meetinglist_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Meeting Meeting = MeetingList.get(position);
        holder.textViewTime.setText(Meeting.time);
        holder.textViewVenue.setText("Venue: " + Meeting.venue);
        holder.textViewAgenda.setText("Agenda: " + Meeting.agenda);
        holder.textViewDepartment.setText(("Department: "+ Meeting.department));
        holder.textViewName.setText("Name: " + Meeting.uname);
    }
    //Set method of OnItemClickListener object
    public void setOnItemClickListener(RecycleviewOnclick recyclerViewItemClickListener){
        this.recycleviewOnclick=recyclerViewItemClickListener;
    }

    @Override
    public int getItemCount() {
        return MeetingList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public int position=0;
        TextView textViewTime, textViewVenue, textViewAgenda,textViewDepartment,textViewName;

        public UserViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemview) {
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
