package com.wordpress.gistschool.raji.farmchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.gistschool.raji.farmchat.Adapters.Chat;
import com.wordpress.gistschool.raji.farmchat.Adapters.ChatAdapter;
import com.wordpress.gistschool.raji.farmchat.Adapters.blogHome;
import com.wordpress.gistschool.raji.farmchat.Adapters.homeAdapter;
import com.wordpress.gistschool.raji.farmchat.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView userImg;
    EditText message;
    TextView userName,mProfession;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private ChatAdapter adapter;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        mAuth = FirebaseAuth.getInstance(  );
        final EditText message = findViewById( R.id.edittext_chatbox );
        Bundle getExtraValues = getIntent().getExtras();
        String name = getExtraValues.get( "name" ).toString();
        String prof = getExtraValues.get( "prof" ).toString();
        String uId = getExtraValues.get( "uId" ).toString();
        final String img = getExtraValues.get( "img" ).toString();
        userName = findViewById(R.id.user_name);
        mProfession = findViewById( R.id.profession );
        userImg = findViewById( R.id.userImg );
        userName.setText( name );
        mProfession.setText( prof );
        Glide.with( ChatActivity.this)
                .load( img )
                .into( userImg );




        ListView recyclerView = findViewById( R.id.reyclerview_message_list);

        userImg.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder( ChatActivity.this);
                View mView = getLayoutInflater().inflate( R.layout.profile_img,null);

                ImageView mProfileImage = mView.findViewById( R.id.profileImg );

                Glide.with(ChatActivity.this)
                        .load(img)
                        .into(mProfileImage);

                mBuilder.setView( mView );
                mBuilder.setCancelable( true );
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        } );

//        mFirebaseDatabase = FirebaseDatabase.getInstance(); //.setPersistenceEnabled(true);;
//        mDatabaseReference = mFirebaseDatabase.getReference().child( "Chats" );


        final DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Chats").child( mAuth.getUid()+uId  );
        final DatabaseReference mReferenceSecond = FirebaseDatabase.getInstance().getReference().child("Chats").child( uId+mAuth.getUid()  );
        final String user_id = mAuth.getCurrentUser( ).getUid( );
        List<Chat> studentList = new ArrayList<>(  );
        adapter = new ChatAdapter( getApplicationContext(), R.layout.chat_layout, studentList );

        recyclerView.setAdapter( adapter );
        childEventListener = new ChildEventListener( ) {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat AddLec = dataSnapshot.getValue(Chat.class);
//
                adapter.add(AddLec);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener( childEventListener );

        Button send = findViewById( R.id.button_chatbox_send );


        send.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                DatabaseReference userdetails = FirebaseDatabase.getInstance().getReference().child( "User" ).child( user_id );
                userdetails.addValueEventListener( new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.child( "name" ).getValue().toString() ;
                        String msg = message.getText().toString();

                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new java.util.Date(  ));

                        String img = dataSnapshot.child( "image"  ).getValue().toString();

                        Chat chatContent = new Chat(name,
                                msg,
                                img,
                                ServerValue.TIMESTAMP.toString(),
                                user_id
                        );

                        mReference.push().setValue( chatContent ).addOnSuccessListener( new OnSuccessListener<Void>( ) {

                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText( ChatActivity.this, "sent", Toast.LENGTH_SHORT ).show( );
                                message.setText( "" );
                            }
                        } );
                        mReferenceSecond.push().setValue( chatContent ).addOnSuccessListener( new OnSuccessListener<Void>( ) {
                            @Override
                            public void onSuccess(Void aVoid) {
                                message.setText( "" );
                            }
                        } );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
            }
        } );





    }

}
