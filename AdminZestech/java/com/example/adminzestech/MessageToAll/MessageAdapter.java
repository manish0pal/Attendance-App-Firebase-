package com.example.adminzestech.MessageToAll;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzestech.MessageToAll.messages_gone;
import com.example.adminzestech.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<com.example.adminzestech.MessageToAll.MessageAdapter.UserViewHolder> {

    private Context mCtx;
    private List<messages_gone> messages_goneList;

    public MessageAdapter(Context mCtx, List<messages_gone> messages_goneList) {
        this.mCtx = mCtx;
        this.messages_goneList = messages_goneList;
    }
    //1
    //2
    @NonNull
    @Override
    public com.example.adminzestech.MessageToAll.MessageAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.messages_layout, parent, false);
        return new com.example.adminzestech.MessageToAll.MessageAdapter.UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull com.example.adminzestech.MessageToAll.MessageAdapter.UserViewHolder holder, int position) {
        messages_gone messages_gone = messages_goneList.get(position);
        String headingwithmsg = messages_gone.msg;
        String msgdpt = messages_gone.department;
        String msgname = messages_gone.name;
        String msgtime = messages_gone.time;
        int indexofmsgStart = headingwithmsg.indexOf("~");
        String heading = headingwithmsg.substring(0,indexofmsgStart);
        String message = headingwithmsg.substring(indexofmsgStart+1);
            holder.HeadingMsg.setText(heading);
            holder.textViewMsg.setText(message);
            holder.textViewName.setText("Name: "+msgname);
            holder.textViewDpt.setText("Dpt: "+msgdpt);
            holder.textViewTime.setText("Time: "+msgtime);
    }
    //Set method of OnItemClickListener object
    @Override
    public int getItemCount() {
        return messages_goneList.size();
    }
    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMsg,HeadingMsg,textViewName,textViewDpt,textViewTime;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            HeadingMsg = itemView.findViewById(R.id.msg_heading);
            textViewMsg = itemView.findViewById(R.id.msg_gone);
            textViewDpt= itemView.findViewById(R.id.msgdpt);
            textViewName=itemView.findViewById(R.id.msgname);
            textViewTime=itemView.findViewById(R.id.msgtime);

        }
    }

}

