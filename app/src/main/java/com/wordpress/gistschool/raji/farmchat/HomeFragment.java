package com.wordpress.gistschool.raji.farmchat;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.wordpress.gistschool.raji.farmchat.Adapters.blogHome;
import com.wordpress.gistschool.raji.farmchat.Adapters.homeAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Query query = FirebaseDatabase.getInstance( )
            .getReference( )
            .child( "blogPosts" );
    private ChildEventListener childEventListener;
    private homeAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_home_farmer, container, false );

        ListView recyclerView = view.findViewById( R.id.recycler_view );

        FirebaseDatabase.getInstance().setPersistenceEnabled( true );

        //mFirebaseDatabase = FirebaseDatabase.getInstance(); //.setPersistenceEnabled(true);;
        //mDatabaseReference = mFirebaseDatabase.getReference().child( "LecturersId" );
        List<blogHome> studentList = new ArrayList<>(  );
        adapter = new homeAdapter( getContext( ), R.layout.home_content, studentList );


        FloatingActionButton fab = view.findViewById( R.id.fab );


        fab.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                getContext( ).startActivity( new Intent( getContext( ), AddPostActivity.class ) );
            }
        } );


        Animation animation = AnimationUtils.loadAnimation( getContext( ), R.anim.fade_in );
        recyclerView.setAnimation( animation );
        recyclerView.setAdapter( adapter );
        childEventListener = new ChildEventListener( ) {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              blogHome AddLec = dataSnapshot.getValue(blogHome.class);
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
        query.addChildEventListener( childEventListener );
        query.keepSynced( true );
        return view;
    }

    
}