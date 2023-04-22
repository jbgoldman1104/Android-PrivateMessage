package com.parasgautam.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;

public class chatListAdapter extends BaseAdapter {
    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public chatListAdapter(Activity activity, DatabaseReference databaseReference, String displayName) {
        mActivity = activity;
        mDatabaseReference = databaseReference.child("message");
        databaseReference.addChildEventListener(mListener);
        mDisplayName = displayName;
        mSnapshotList = new ArrayList<>();
    }
        static class ViewHolder{
            TextView authorName,body;
            LinearLayout.LayoutParams params;
        }
    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int i) {
        DataSnapshot snapshot = mSnapshotList.get(i);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row,viewGroup,false);
            final ViewHolder holder = new ViewHolder();
            holder.authorName = view.findViewById(R.id.author);
            holder.body = view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams)holder.authorName.getLayoutParams();
            view.setTag(holder);
        }
        final InstantMessage instantMessage = getItem(i);
        final ViewHolder holder =  (ViewHolder) view.getTag();
        boolean isMe = instantMessage.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe,holder);
        String author = instantMessage.getAuthor();
        holder.authorName.setText(author);

        String message = instantMessage.getMessage();
        holder.body.setText(message);

        return view;
    }

    private void setChatRowAppearance(boolean isItMe,ViewHolder holder){
            if(isItMe){
                holder.params.gravity = Gravity.END;
                holder.authorName.setTextColor(Color.GREEN);
                holder.body.setBackgroundResource(R.drawable.bubble2);
            }
            else{
                holder.params.gravity = Gravity.START;
                holder.authorName.setTextColor(Color.BLUE);
                holder.body.setBackgroundResource(R.drawable.bubble1);
            }
            holder.authorName.setLayoutParams(holder.params);
            holder.body.setLayoutParams(holder.params);
    }

    public void cleanUp(){
        mDatabaseReference.removeEventListener(mListener);
    }
}
