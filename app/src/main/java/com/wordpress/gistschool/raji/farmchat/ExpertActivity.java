package com.wordpress.gistschool.raji.farmchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpertActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_expert );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        changeFragment(new HomeFragment());
        NavigationView navigationView = findViewById( R.id.nav_view );
        mAuth = FirebaseAuth.getInstance(  );
        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header_expert);


        final ImageView mProfilePix =  headerLayout.findViewById(R.id.profileImg );
        final TextView name = headerLayout.findViewById(R.id.name );
        TextView email =  headerLayout.findViewById(R.id.email );
        String user_id = mAuth.getUid();

        email.setText( mAuth.getCurrentUser().getEmail() );
        DatabaseReference current_user_ref = FirebaseDatabase.getInstance().getReference().child( "User" ).child( user_id );
        current_user_ref.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText( dataSnapshot.child( "name" ).getValue().toString() );

                Glide.with( ExpertActivity.this)
                        .load( dataSnapshot.child("image").getValue().toString() )
                        .into( mProfilePix );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState( );

        //NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed( );
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId( );

        if (id == R.id.nav_home) {
            // Handle the camera action
            changeFragment(new HomeFragment());
        } else if (id == R.id.nav_add) {
            startActivity( new Intent( ExpertActivity.this,AddPostActivity.class ) );
        } else if (id == R.id.nav_log_out) {
            setPref();
            mAuth.signOut();
            startActivity( new Intent( ExpertActivity.this,LoginActivity.class ) );
            finish();

        } else if (id == R.id.nav_me) {
            changeFragment(new DashBoardFragment());

        }else if (id == R.id.nav_chats_farmers) {
            changeFragment(new whoToChatUpExpertActivity());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.frameContainer, fragment)
                .commit();
    }
    private void setPref(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("prof", "");
        editor.putString("uId", "");
        editor.commit();


    }
}
