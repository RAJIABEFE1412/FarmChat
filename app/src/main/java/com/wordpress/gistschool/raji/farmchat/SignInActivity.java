package com.wordpress.gistschool.raji.farmchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
        private FirebaseAuth mAuth;
        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference userReference;
        private EditText email,pw,cmf_pw;
        private ProgressDialog mProgress;
        private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_in );
        email = findViewById( R.id.name );
        pw = findViewById( R.id.pw );
        mAuth = FirebaseAuth.getInstance(  );
        cmf_pw = findViewById( R.id.cmf_pw );
        TextView login = findViewById( R.id.login );

        login.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( SignInActivity.this, LoginActivity.class ) );
            }
        } );

        mProgress = new ProgressDialog( this );

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//            if (mAuth.getCurrentUser() != null){
//                if(true) {
//                    startActivity( new Intent( this, whereTo ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
//                }else if (true){
//                    startActivity( new Intent( this, whereto ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
//                }

            //}
            }
        };

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userReference = mFirebaseDatabase.getReference().child( "Users" );
        Button sigUp = (Button) findViewById( R.id.sign_up );
        sigUp.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                signUp();
            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart( );
        mAuth.addAuthStateListener( mAuthListener );
    }

    private void signUp(){
        mProgress.setMessage( "Signing Up..." );

        if(!TextUtils.isEmpty( email.getText().toString().trim() )){
            if(!TextUtils.isEmpty( pw.getText().toString().trim() )){
                if(!TextUtils.isEmpty( cmf_pw.getText().toString().trim() )){
                    if(TextUtils.equals( pw.getText().toString().trim(),cmf_pw.getText().toString().trim() )){

                        mProgress.show();
                        mProgress.setCancelable( false );
                        mAuth.createUserWithEmailAndPassword( email.getText().toString().trim(),pw.getText().toString().trim() )
                                .addOnCompleteListener( new OnCompleteListener<AuthResult>( ) {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()){
                                            mProgress.hide();

                                            Intent i = new Intent( getApplicationContext(), AccountSetupActivity.class );
                                            i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                            startActivity( i );

                                        }else if (task.isCanceled()){
                                            mProgress.hide();
                                            Toast.makeText( SignInActivity.this, "error signing Up", Toast.LENGTH_SHORT ).show( );
                                        }
                                    }
                                } );
                    }else{
                        cmf_pw.setError( "password not matched!" );
                        mProgress.dismiss();
                    }

                }else{
                    cmf_pw.setError( "Please fill this!" );
                    mProgress.dismiss();
                }
            }else{
                pw.setError( "Please fill this!" );
                mProgress.dismiss();
            }

        }else{
            email.setError( "Please fill this!" );
            mProgress.dismiss();
        }

    }

    private void addDb(){

    }
}
