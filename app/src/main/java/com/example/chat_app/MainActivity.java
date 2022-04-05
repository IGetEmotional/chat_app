package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final int SIGN_IN_CODE = 1;
    private RelativeLayout activity_main;
    private FirebaseListAdapter<Message> adapter;
    private FloatingActionButton sendBtn;
    private EditText textField;
  //  private ChatListAdapter adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE){
            if(resultCode == RESULT_OK){
                Snackbar.make(activity_main, "You are authorized", Snackbar.LENGTH_SHORT).show();
                displayAllMessages();
            }
            else{
                Snackbar.make(activity_main, "You are not authorized", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = findViewById(R.id.activity_main);
        sendBtn = findViewById(R.id.btnSend);
        textField = findViewById(R.id.message_field);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textField.getText().toString().equals(""))
                    return;
                FirebaseDatabase.getInstance(" https://chat-68bc6-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference().push()
                        .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(), textField.getText().toString())
                        ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("SENDING", "SEND FAILURE");
                    }
                });
                textField.setText("");
            }
        });

        //User not authorized yet
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        }
        else
            Snackbar.make(activity_main, "You are authorized", Snackbar.LENGTH_SHORT).show();

        displayAllMessages();
        }




        private void displayAllMessages(){
            ListView list_of_messages = findViewById(R.id.list_of_messages);
            FirebaseListOptions<Message> options_1 =
                    new FirebaseListOptions.Builder<Message>()
                    .setQuery(FirebaseDatabase.getInstance( "https://chat-68bc6-default-rtdb.asia-southeast1.firebasedatabase.app").getReference(), Message.class)
                   .setLayout(R.layout.list_item)
                    .build();
//проблема с layout
            adapter = new FirebaseListAdapter<Message>(options_1) {
                @Override
                protected void populateView( View v,  Message model, int position) {
                    TextView mess_user, mess_time;
                    BubbleTextView mess_text;
  //                  if(model.getUserName().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString())) {
                        mess_user = v.findViewById(R.id.message_user);
                        mess_time = v.findViewById(R.id.message_time);
                        mess_text = v.findViewById(R.id.message_text_mine);
   //                 }
    //                else {
    //                    mess_user = v.findViewById(R.id.message_user_received);
    //                    mess_time = v.findViewById(R.id.message_time_received);
     //                   mess_text = v.findViewById(R.id.message_text_received);
      //              }
                    mess_user.setText(model.getUserName());
                    mess_text.setText(model.getTextMessage());
                    mess_time.setText(DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getMessageTime()));
                }
            };
            list_of_messages.setAdapter(adapter);
        }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
