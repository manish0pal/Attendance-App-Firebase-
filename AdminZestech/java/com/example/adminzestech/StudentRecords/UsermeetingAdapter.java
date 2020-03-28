package com.example.adminzestech.StudentRecords;
//Recycle view holder

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

public class UsermeetingAdapter extends RecyclerView.Adapter<UsermeetingAdapter.UserViewHolder> {

    private Context mCtx;
    private List<userinmeeting> userinmeetingList;
    public RecycleviewOnclick1 recycleviewOnclick;
    public UsermeetingAdapter(Context mCtx, List<userinmeeting> userinmeetingList) {
        this.mCtx = mCtx;
        this.userinmeetingList = userinmeetingList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        userinmeeting userinmeeting = userinmeetingList.get(position);
        holder.position=position;
        holder.textViewName.setText(userinmeeting.name);
        holder.Tuid.setText(userinmeeting.uid);
        holder.textViewClass.setText("Class: " + userinmeeting.std);
        holder.textViewRoll.setText("Roll no: " + userinmeeting.rollno);
        holder.textViewDepatment.setText("Department: "+ userinmeeting.department);


    }
    public void setOnItemClickListener(RecycleviewOnclick1 recyclerViewItemClickListener){
        this.recycleviewOnclick=recyclerViewItemClickListener;
    }

    @Override
    public int getItemCount() {
        return userinmeetingList.size();
    }


    public void filterlist(ArrayList<userinmeeting> filteredlist) {
        userinmeetingList = filteredlist;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewClass, textViewRoll,textViewDepatment,Tuid;
        public int position=0;
        public UserViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemview) {
                    //When item view is clicked, trigger the itemclicklistener
                    //Because that itemclicklistener is indicated in MainActivity
                    recycleviewOnclick.onItemClick(itemview, textViewName.getText().toString(),Tuid.getText().toString());
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
            Tuid=itemView.findViewById(R.id.uidgone);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewClass = itemView.findViewById(R.id.text_view_class);
            textViewRoll = itemView.findViewById(R.id.text_view_rollno);
            textViewDepatment = itemView.findViewById(R.id.text_view_dpt);

        }
    }
}
