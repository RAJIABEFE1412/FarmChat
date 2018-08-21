package com.wordpress.gistschool.raji.farmchat;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;


    public DashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_dash_board_farm, container, false );
        Button sign_out =  view.findViewById( R.id.sign_out );
        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser( ).getUid( );
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference( ).child( "User" ).child( user_id );

        final TextView userName = view.findViewById( R.id.UserNameText );
        final TextView userEmail = view.findViewById( R.id.UserEmailText );
        final TextView userId = view.findViewById( R.id.UseridText );

        final CircleImageView imageView = view.findViewById( R.id.userImg );

        mReference.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child( "name" ).getValue().toString() ;
                String userID = mAuth.getUid();
                String user_Email = mAuth.getCurrentUser().getEmail();
                final String profileUri = dataSnapshot.child( "image").getValue().toString();
                userName.setText( name );
                userId.setText( userID );
                userEmail.setText( user_Email );
                Glide.with(imageView.getContext())
                        .load(profileUri)
                        .into(imageView);
                imageView.setOnClickListener( new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder( getContext());
                        View mView =((Activity) getContext()).getLayoutInflater().inflate( R.layout.profile_img,null);

                        ImageView mProfileImage = mView.findViewById( R.id.profileImg );

                        Glide.with(getContext())
                                .load( profileUri)
                                .into(mProfileImage);

                        mBuilder.setView( mView );
                        mBuilder.setCancelable( true );
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();
                    }
                } );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        sign_out.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                setPref();
                mAuth.signOut();
                startActivity( new Intent( getContext(),LoginActivity.class ) );
                getActivity().finish();

            }
        } );


        return view;
    }
    private void setPref(){
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("prof", "");
        editor.putString("uId", "");
        editor.commit();


    }

}
