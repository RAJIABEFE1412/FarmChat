package com.wordpress.gistschool.raji.farmchat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.wordpress.gistschool.raji.farmchat.Adapters.Friends;
import com.wordpress.gistschool.raji.farmchat.Adapters.FriendsAdapter;
import com.wordpress.gistschool.raji.farmchat.Adapters.blogHome;
import com.wordpress.gistschool.raji.farmchat.Adapters.homeAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link whoToChatUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class whoToChatUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    Query query = FirebaseDatabase.getInstance( )
            .getReference( )
            .child( "User" );
    private ChildEventListener childEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FriendsAdapter adapter;

    public whoToChatUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate( R.layout.fragment_who_to_chat_up_expert, container, false );
        ListView recyclerView = view.findViewById( R.id.who_to_chat_up_expert );
        mFirebaseDatabase = FirebaseDatabase.getInstance(); //.setPersistenceEnabled(true);;
        mDatabaseReference = mFirebaseDatabase.getReference().child( "LecturersId" );
        List<Friends> studentList = new ArrayList<>(  );
        adapter = new FriendsAdapter( getContext( ), R.layout.who_to_chatter, studentList );


        Animation animation = AnimationUtils.loadAnimation( getContext( ), R.anim.fade_in );
        recyclerView.setAnimation( animation );
        recyclerView.setAdapter( adapter );

        recyclerView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name =  adapter.getItem( position ).getName();
                String uId =  adapter.getItem( position ).getuId();
                String prof =  adapter.getItem( position ).getProfession();
                String img =  adapter.getItem( position ).getImage();

                Intent chatIntent = new Intent( getContext(),ChatActivity.class );
                chatIntent.putExtra( "name",name);
                chatIntent.putExtra( "uId", uId );
                chatIntent.putExtra( "prof",  prof);
                chatIntent.putExtra( "img", img);
                getContext().startActivity( chatIntent );

            }
        } );

        childEventListener = new ChildEventListener( ) {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Friends AddLec = dataSnapshot.getValue(Friends.class);

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

    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    public void onDetach() {
        super.onDetach( );
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
