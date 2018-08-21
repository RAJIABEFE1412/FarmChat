package com.wordpress.gistschool.raji.farmchat;

import android.content.Context;
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
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
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


public class HomeFarmerFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public HomeFarmerFragment() {
        // Required empty public constructor
    }

    Query query = FirebaseDatabase.getInstance( )
            .getReference( )
            .child( "blogPosts" );
    private ChildEventListener childEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private homeAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_home_farmer, container, false );

        ListView recyclerView = view.findViewById( R.id.recycler_view );

        mFirebaseDatabase = FirebaseDatabase.getInstance(); //.setPersistenceEnabled(true);;
        mDatabaseReference = mFirebaseDatabase.getReference().child( "LecturersId" );
        List<blogHome> studentList = new ArrayList<>(  );
        adapter = new homeAdapter( getContext( ), R.layout.home_content, studentList );

        //listView.setEmptyView( R.id.pb_home);



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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart( );
        //adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString( )
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    public static class ChatHolder extends RecyclerView.ViewHolder{
        TextView posts,postUserName,datePushed;
        CircleImageView posterImg;
        ImageView imageView;
        public ChatHolder(View itemView) {
            super( itemView );
             posts =  itemView.findViewById( R.id.blogPost );
            posterImg =  itemView.findViewById( R.id.lecturerImage );
            postUserName =  itemView.findViewById( R.id.lecturerName);
            imageView = itemView.findViewById( R.id.iv_home );
            datePushed = itemView.findViewById( R.id.datePublished );

        }
    }

    @Override
    public void onDetach() {
        super.onDetach( );
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
