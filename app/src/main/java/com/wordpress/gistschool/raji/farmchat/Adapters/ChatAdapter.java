package com.wordpress.gistschool.raji.farmchat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.wordpress.gistschool.raji.farmchat.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends ArrayAdapter<Chat> {



    public ChatAdapter(Context context, int resource, List<Chat> studentList) {
        super( context, resource, studentList );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.chat_layout, parent, false);
        }

        TextView sentMsg = (TextView) convertView.findViewById( R.id.sent_body);
        TextView RecMsg = (TextView) convertView.findViewById( R.id.recieved_message);
        TextView RecName = (TextView) convertView.findViewById( R.id.recieved_message_name);
        CircleImageView mRecImg = (CircleImageView) convertView.findViewById( R.id.image_message_profile );
        TextView recTime = (TextView) convertView.findViewById( R.id.recieved_message_time);
        TextView sentTime = (TextView) convertView.findViewById( R.id.text_message_time);



        Chat AddLec = getItem(position);
        if (AddLec.getmUid().equals( FirebaseAuth.getInstance().getCurrentUser().getUid() )){
            sentMsg.setText( AddLec.getmText() );
            sentTime.setText( AddLec.getmTime() );
            recTime.setVisibility( View.GONE );
            mRecImg.setVisibility( View.GONE );
            RecName.setVisibility( View.GONE );
            RecMsg.setVisibility( View.GONE );
        }else {
            sentMsg.setVisibility( View.GONE );
            sentTime.setVisibility( View.GONE );
            recTime.setText( AddLec.getmTime() );
            Glide.with( mRecImg.getContext() )
                    .load( AddLec.getmImage() )
                    .into( mRecImg );
            RecName.setText(AddLec.getName());
            RecMsg.setText( AddLec.getmText() );

        }

        return convertView;
    }
}
