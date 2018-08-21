package com.wordpress.gistschool.raji.farmchat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.gistschool.raji.farmchat.R;
import com.wordpress.gistschool.raji.farmchat.commentsActivity;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class homeAdapter extends ArrayAdapter<blogHome> {



    public homeAdapter(Context context, int resource, List<blogHome> studentList) {
        super( context, resource, studentList );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate( R.layout.home_content, parent, false);
        }


        TextView lecturerTextView = (TextView) convertView.findViewById(R.id.lecturerName);
        TextView blogPost = (TextView) convertView.findViewById(R.id.blogPost);
        TextView blogPostDate = (TextView) convertView.findViewById(R.id.datePublished);
        ImageView iv_home = convertView.findViewById(R.id.iv_home);
        final ImageView likes = convertView.findViewById( R.id.like_post );
        final ImageView bounce = convertView.findViewById( R.id.double_tap );
        TextView comment = convertView.findViewById( R.id.comments );


        CircleImageView mLecImg = (CircleImageView) convertView.findViewById( R.id.lecturerImage );




        final blogHome AddLec = getItem(position);

        Glide.with(mLecImg.getContext())
                .load( AddLec.getPosterImg())
                .into(mLecImg);
        final String likesUrl = AddLec.getmPostKey();
        DatabaseReference checkLikes = FirebaseDatabase.getInstance().getReferenceFromUrl( likesUrl ).child( "likes" );
        final String user_id = FirebaseAuth.getInstance().getUid();
        checkLikes.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild( user_id )){
                likes.setColorFilter( Color.rgb( 255,0,0 ) );
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        final DatabaseReference checkDisLikes = FirebaseDatabase.getInstance().getReferenceFromUrl( likesUrl ).child( "likes" );
        checkDisLikes.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild( user_id )){

                    likes.setColorFilter( Color.rgb( 255,0,0 ) );
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        comment.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent commentsIntent = new Intent( getContext(), commentsActivity.class);
                commentsIntent.putExtra( "UriComments", AddLec.getmPostKey());
                getContext().startActivity( commentsIntent );

            }
        } );


        blogPostDate.setText( AddLec.getDatePosted() );
        lecturerTextView.setText(AddLec.getPosterName());
        blogPost.setText( AddLec.getPostDescription() );

        likes.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    likes.setColorFilter( Color.rgb( 255,0,0 ) );
                    DatabaseReference likes = FirebaseDatabase.getInstance().getReferenceFromUrl( likesUrl ).child( "likes" ).child( user_id ).child( "likes" );
                    likes.setValue( "likes" );

                }

            }
        } );



        Glide.with(iv_home.getContext())
                .load( AddLec.getDocPosted())
                .into(iv_home);

        final GestureDetector detector = new GestureDetector( getContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Animation animation = AnimationUtils.loadAnimation( getContext(),R.anim.fade_in_bounce_out );
                bounce.setVisibility( View.VISIBLE );
                bounce.setAnimation( animation);
                likes.setAnimation( animation );
                likes.setColorFilter( Color.rgb( 255,0,0 ) );
                bounce.setColorFilter( Color.rgb( 255,0,0 ) );
                bounce.setVisibility( View.INVISIBLE );
                String likesUrl = AddLec.getmPostKey();
                String user_id = FirebaseAuth.getInstance().getUid();
                        DatabaseReference likes = FirebaseDatabase.getInstance().getReferenceFromUrl( likesUrl ).child( "likes" ).child( user_id ).child( "likes" );
                        likes.setValue( "likes" );

                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp( e );
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress( e );
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return super.onScroll( e1, e2, distanceX, distanceY );
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling( e1, e2, velocityX, velocityY );
            }

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress( e );
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent( e );
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed( e );
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                return super.onContextClick( e );
            }
        } );
        iv_home.setOnTouchListener( new View.OnTouchListener( ) {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent( event );
            }
        } );

        return convertView;
    }

}
