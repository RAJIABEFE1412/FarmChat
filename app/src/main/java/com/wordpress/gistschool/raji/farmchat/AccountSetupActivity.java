package com.wordpress.gistschool.raji.farmchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetupActivity extends AppCompatActivity {
    private static final int RC_IMAGE_PICKER = 200;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    private EditText name;
    CircleImageView mPix;
    Uri mProfilePix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_account_setup );
        final Spinner spinner = findViewById( R.id.spinner );
        mAuth = FirebaseAuth.getInstance( );
        mFirebaseDatabase = FirebaseDatabase.getInstance( );
        name = findViewById( R.id.name );
        mPix = findViewById( R.id.profileImg );
        mReference = mFirebaseDatabase.getReference( ).child( "User" );
        final Button finishSetUp = findViewById( R.id.add_to_db );
        String[] jobs = {getString(R.string.farmer), getString(R.string.extension)};
        ImageView selectPfImg = findViewById( R.id.selectPf );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,
                android.R.layout.simple_spinner_dropdown_item,
                jobs );

        spinner.setAdapter( adapter );

        finishSetUp.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog( AccountSetupActivity.this );
                dialog.setMessage( "Finishing set up..." );
                dialog.setCancelable( false );
                dialog.show();
                final String user_id = mAuth.getCurrentUser( ).getUid( );
                final DatabaseReference current_user_db = mReference.child( user_id );

                final StorageReference ref = FirebaseStorage.getInstance().getReference().child("profileUi").child(mProfilePix.getLastPathSegment());
                final UploadTask uploadTask = ref.putFile(mProfilePix);
                Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>( ) {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                } ).addOnCompleteListener( new OnCompleteListener<Uri>( ) {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful( )) {
                            Uri downloadUri = task.getResult( );
                            current_user_db.child( "name" ).setValue( name.getText( ).toString( ) );

                            current_user_db.child( "profession" ).setValue( spinner.getSelectedItem( ).toString( ) );
                            current_user_db.child( "uId" ).setValue( user_id );
                            current_user_db.child( "image" ).setValue( downloadUri.toString() );
                            current_user_db.child( "presence" ).setValue( "Online" )

                                    .addOnSuccessListener( new OnSuccessListener<Void>( ) {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    String occup = spinner.getSelectedItem( ).toString( );
                                    if (occup.equals( getString( R.string.farmer ) )){
                                        startActivity( new Intent( AccountSetupActivity.this, FarmerActivity.class ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                                        finish();
                                    }else if(occup.equals(  getString( R.string.extension ) )){
                                        startActivity( new Intent( AccountSetupActivity.this, ExpertActivity.class ).addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                                        finish();
                                    }
                                }
                            } );
                        }
                    }
                    } );






            }
            });

        selectPfImg.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
                intent.setType( "image/*" );
                intent.putExtra( Intent.EXTRA_LOCAL_ONLY, true );
                startActivityForResult( Intent.createChooser( intent, "Complete action using" ), RC_IMAGE_PICKER );
            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK) {
            //Toast.makeText( this, data.getDataString( ) + "Video picked", Toast.LENGTH_SHORT ).show( );

            if (data.getData( ) != null) {
                mProfilePix = data.getData( );
                Glide.with( this)
                        .load( mProfilePix )
                        .into( mPix );

            } else {
                mProfilePix = Uri.parse( "android.resource://com.wordpress.gistschool.raji.farmchat/drawable/golden" );
            }
        }
    }
}