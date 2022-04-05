package com.example.chat_app;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatListAdapter extends ArrayAdapter<Message> {
    private Activity activity;
    private ArrayList<Message> chatMessages;

    public ChatListAdapter(Activity context, int resource, ArrayList<Message> objects){
        super(context, resource, objects);
        this.activity = context;
        this.chatMessages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutResource;
        Message message = getItem(position);

        if(message.getUserName().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString())){
            layoutResource = R.layout.item_sent;
        }
        else
            layoutResource = R.layout.item_received;

        if(convertView != null){
  //          holder = (ViewHolder)convertView.getTag();
        }
        else{
            convertView = inflater.inflate(layoutResource, parent, false);
   //         holder = new ViewHolder(convertView);
    //        convertView.setTag(holder);
        }

   //     holder.message.setText(Message.getTextMessage());

        return convertView;
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public int getItemViewType(int position){
        return position % 2;
    }

    static class ViewHolder {
        private TextView message;

        public ViewHolder(View v){
            message = (TextView)v.findViewById(R.id.text);
        }
    }
}
