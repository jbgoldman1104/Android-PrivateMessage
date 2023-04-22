package com.parasgautam.flashchatnewfirebase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {


    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private chatListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        setupDisplayName();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // Link the Views in the layout to the Java code
        mInputText = findViewById(R.id.messageInput);
        mSendButton =  findViewById(R.id.sendButton);
        mChatListView = findViewById(R.id.chat_list_view);



            mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    sendMessage();
                    return true;
                }
            });


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }


        private void setupDisplayName(){
            SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS,MODE_PRIVATE);
            mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
            if(mDisplayName==null) mDisplayName="Anonymous";

        }

    private void sendMessage() {


            String input = mInputText.getText().toString();
            if(!input.equals("")){
                InstantMessage instantMessage = new InstantMessage(input,mDisplayName);
                mDatabaseReference.child("message").push().setValue(instantMessage);
                mInputText.setText("");
            }
    }




    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new chatListAdapter(this,mDatabaseReference,mDisplayName);
        mChatListView.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.cleanUp();

    }

}
