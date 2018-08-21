package com.wordpress.gistschool.raji.farmchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wordpress.gistschool.raji.farmchat.Adapters.blogHome;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private int RC_VIDEO_PICKER= 222;
    FirebaseAuth mAuth;
    ImageView addFile;
    EditText posts;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    private Uri videoStorage  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_post );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        addFile = (ImageView) findViewById( R.id.blogPost );
        mAuth =FirebaseAuth.getInstance(  );
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child( "Video_Pdf" );
        databaseReference = firebaseDatabase.getReference().child( "blogPosts" );
        Button sendpost = (Button) findViewById( R.id.sendPost);
        posts = (EditText) findViewById( R.id.addPostText);
        progressDialog =  new ProgressDialog( AddPostActivity.this );


        posts.addTextChangedListener( new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );


        addFile.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType( "image/*" );
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_VIDEO_PICKER);
            }
        } );


        sendpost.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(posts.getText( ).toString( ) )) {
                    progressDialog.setCancelable( false );
                    progressDialog.setMessage( "Posting...." );
                    progressDialog.show();
                    onSendToFbDb();

                }
        }


    });

    }
    private void onSendToFbDb() {
        final StorageReference ref = storageReference.child(videoStorage.getLastPathSegment());
        final UploadTask uploadTask = ref.putFile(videoStorage);

        final String user_id = mAuth.getCurrentUser( ).getUid( );

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference( ).child( "User" ).child( user_id );
        mReference.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child( "name" ).getValue().toString() ;
                final String profileImg = dataSnapshot.child("image").getValue().toString();
                //Toast.makeText( AddPostActivity.this, name, Toast.LENGTH_SHORT ).show( );

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();


                            DatabaseReference key = databaseReference.push( );
                            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new java.util.Date(  ));
                            blogHome homeFragmentStudent =

                                    new blogHome(name,
                                            currentDate,
                                            downloadUri.toString( ),
                                            profileImg,
                                            posts.getText( ).toString( ),
                                            key.getRef().toString());

                            key.setValue( homeFragmentStudent ).addOnSuccessListener( new OnSuccessListener<Void>( ) {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddPostActivity.this, "Uploaded", Toast.LENGTH_SHORT ).show( );

                                    posts.setText( "" );
                                    progressDialog.dismiss();
                                }
                            } ).addOnFailureListener( new OnFailureListener( ) {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText( AddPostActivity.this, "Failed to Upload", Toast.LENGTH_SHORT ).show( );
                                    progressDialog.dismiss();
                                }
                            } );
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );





        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == RC_VIDEO_PICKER && resultCode == RESULT_OK){
            Toast.makeText( this, data.getDataString()+"Video picked", Toast.LENGTH_SHORT ).show( );
            videoStorage = data.getData();
            if(data.getData() != null) {
                addFile.setImageURI( videoStorage );

            }else{
                Toast.makeText( this, "Unable to pick video", Toast.LENGTH_SHORT ).show( );
            }

        }

    }

}
