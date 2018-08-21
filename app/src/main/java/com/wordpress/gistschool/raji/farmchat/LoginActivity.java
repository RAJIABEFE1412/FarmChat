package com.wordpress.gistschool.raji.farmchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email, pw;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );


        email = findViewById( R.id.name );
        pw = findViewById( R.id.pw );
        Button login = findViewById( R.id.lgIn );
        mFirebaseDatabase = FirebaseDatabase.getInstance( );
        mReference = mFirebaseDatabase.getReference( ).child( "User" );

        //mReference.keepSynced( true );
        mAuth = FirebaseAuth.getInstance( );
        // initialize progress dialog
        mProgress = new ProgressDialog( this );
        mProgress.setMessage( "Logging in..." );
        Button createAcc = findViewById( R.id.new_acc );
        createAcc.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( LoginActivity.this, SignInActivity.class ) );
            }
        } );

        login.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                String emailString = email.getText( ).toString( ).trim( );
                String pw_string = pw.getText( ).toString( ).trim( );
                if (!TextUtils.isEmpty( emailString )) {
                    if (!TextUtils.isEmpty( pw_string )) {
                        checkLogin( emailString, pw_string );
                    } else {
                        pw.setError( "please fill this field" );
                    }
                } else {
                    email.setError( "please fill this field" );
                }
            }
        } );
    }

    private void checkLogin(String emailString, String pw_string) {
        mProgress.show( );
        mProgress.setCancelable( false );

        mAuth.signInWithEmailAndPassword( emailString, pw_string )
                .addOnCompleteListener( new OnCompleteListener<AuthResult>( ) {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful( )) {

                            checkIfExit( );

                            mProgress.dismiss( );

                        } else {
                            mProgress.dismiss( );
                            //Toast.makeText( LoginActivity.this, "error!!!!!!!!!!!", Toast.LENGTH_SHORT ).show( );
                            startActivity( new Intent( LoginActivity.this,
                                    SignInActivity.class )
                                    .addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                        }
                    }
                } );


    }

    @Override
    protected void onStart() {
        super.onStart( );
        getPref();
    }

    private void getPref() {
        SharedPreferences pref = getSharedPreferences( "MyPref",MODE_PRIVATE );

        String uId = pref.getString( "uId","" );
        String prof = pref.getString( "prof","" );
        //Toast.makeText( this, uId+"\n"+prof, Toast.LENGTH_SHORT ).show( );
        if (uId.equals( getString(R.string.farmer))){
            startActivity( new Intent( LoginActivity.this, FarmerActivity.class ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
            finish();
        }else if(uId.equals( getString( R.string.extension ) )){
            startActivity( new Intent( LoginActivity.this, ExpertActivity.class ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
            finish();
        }


    }

    private void setPref(String prof, String uId){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("prof", prof);
        editor.putString("uId", uId);
        editor.commit();


    }

    private void checkIfExit() {
        if (mAuth.getCurrentUser( ) != null) {
            final String user_id = mAuth.getCurrentUser( ).getUid( );

            mReference.addValueEventListener( new ValueEventListener( ) {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild( user_id )) {
                        String occup = dataSnapshot.child( user_id ).child( "profession" ).getValue( ).toString( );

                        Toast.makeText( LoginActivity.this, occup, Toast.LENGTH_SHORT ).show( );

                        setPref( user_id,occup );

                        if (occup.equals( getString(R.string.farmer))){
                            startActivity( new Intent( LoginActivity.this, FarmerActivity.class ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                            finish();
                        }else if(occup.equals( getString( R.string.extension ) )){
                            startActivity( new Intent( LoginActivity.this, ExpertActivity.class ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                            finish();
                        }

                    } else {
                        Toast.makeText( LoginActivity.this, "Please register, match not found", Toast.LENGTH_SHORT ).show( );
                        startActivity( new Intent( LoginActivity.this, SignInActivity.class ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText( LoginActivity.this, databaseError.getMessage( ), Toast.LENGTH_SHORT ).show( );

                }
            } );
        } else {
            startActivity( new Intent( LoginActivity.this, AccountSetupActivity.class )
                    .addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
            finish();
        }

    }
}
