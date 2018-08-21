package com.wordpress.gistschool.raji.farmchat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.gistschool.raji.farmchat.Adapters.comments;
import com.wordpress.gistschool.raji.farmchat.Adapters.commentsAdapter;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class commentsActivity extends AppCompatActivity {
    CircleImageView userImg;
    EditText message;
    private FirebaseAuth mAuth;
    private commentsAdapter adapter;
    private ChildEventListener childEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_comments );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        mAuth = FirebaseAuth.getInstance(  );
        final EditText message = findViewById( R.id.edittext_chatbox );
        Bundle getExtraValues = getIntent().getExtras();
        String commentsUrl = getExtraValues.get( "UriComments" ).toString();
        ListView recyclerView = findViewById( R.id.reyclerview_message_list);

        recyclerView.setBackgroundColor( Color.rgb( 255,254,254) );

        final DatabaseReference mReference = FirebaseDatabase.getInstance().getReferenceFromUrl( commentsUrl ).child( "comments" );
        //final DatabaseReference mReferenceSecond = FirebaseDatabase.getInstance().getReference().child("Chats").child( uId+mAuth.getUid()  );
        final String user_id = mAuth.getCurrentUser( ).getUid( );
        List<comments> studentList = new ArrayList<>(  );
        adapter = new commentsAdapter( getApplicationContext(), R.layout.chat_layout, studentList );

        recyclerView.setAdapter( adapter );
        childEventListener = new ChildEventListener( ) {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                comments AddLec = dataSnapshot.getValue(comments.class);
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

                        String img = dataSnapshot.child( "image"  ).getValue().toString();

                        comments chatContent = new comments(name,
                                msg,
                                img
                        );

                        mReference.push().setValue( chatContent ).addOnSuccessListener( new OnSuccessListener<Void>( ) {

                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText( ChatActivity.this, "sent", Toast.LENGTH_SHORT ).show( );
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
