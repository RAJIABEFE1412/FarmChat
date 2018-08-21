package com.wordpress.gistschool.raji.farmchat.Adapters;

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

public class commentsAdapter extends ArrayAdapter<comments> {



    public commentsAdapter(Context context, int resource, List<comments> studentList) {
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



        comments AddLec = getItem(position);

            sentMsg.setVisibility( View.GONE );
            sentTime.setVisibility( View.GONE );
            recTime.setVisibility( View.GONE );
            Glide.with( mRecImg.getContext() )
                    .load( AddLec.getmImage() )
                    .into( mRecImg );
            RecName.setText(AddLec.getmName());
            RecMsg.setText( AddLec.getmText() );


        return convertView;
    }
}
