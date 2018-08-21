package com.wordpress.gistschool.raji.farmchat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wordpress.gistschool.raji.farmchat.R;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends ArrayAdapter<Friends> {



    public FriendsAdapter(Context context, int resource, List<Friends> studentList) {
        super( context, resource, studentList );
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate( R.layout.who_to_chatter, parent, false);
        }

        TextView lecturerTextView = (TextView) convertView.findViewById( R.id.lecturerName);
        final CircleImageView mLecImg = (CircleImageView) convertView.findViewById( R.id.lecturerImage );
        TextView AddButton = convertView.findViewById( R.id.add_lec );



        final Friends AddLec = getItem(position);
        AddButton.setText( AddLec.getProfession() );
        Glide.with(mLecImg.getContext())
                .load( AddLec.getImage())
                .into(mLecImg);

        mLecImg.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( getContext());
                LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
                View mView = inflater.inflate( R.layout.profile_img,null);

                ImageView mProfileImage = mView.findViewById( R.id.profileImg );

                Glide.with(getContext())
                        .load( AddLec.getImage())
                        .into(mProfileImage);

                mBuilder.setView( mView );
                mBuilder.setCancelable( true );
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        } );



        lecturerTextView.setText(AddLec.getName());


        return convertView;
    }
}
