package com.nguyenoanh.chats.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.nguyenoanh.chats.Model.User;
import com.nguyenoanh.chats.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView imageProfile;
    private TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ArrayList<User> list;

    StorageReference storageReference;
    private static  final int IMAGE_REQUEST=1;
    private static  final int RESULT_OK=1;
    private Uri imageUri;
    private StorageTask uploadTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_profile, container, false);

        imageProfile = view.findViewById (R.id.profileImage);
        username = view.findViewById (R.id.tvUserName);

        storageReference = FirebaseStorage.getInstance ().getReference ("uploads");

        firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();
        reference = FirebaseDatabase.getInstance ().getReference ("Users").child (firebaseUser.getUid ());

        reference.addValueEventListener (new ValueEventListener () {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue (User.class);
                username.setText (user.getUserName ());

                //check image avatar
                String url = user.getImageURL();
                if (url != null && url.equals("default")) {
                    imageProfile.setImageResource (R.drawable.anh);
                }else {
                    Glide.with (getContext ()).load (user.getImageURL ())
                            .into (imageProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageProfile.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                openIamge();
            }
        });

        return view;
    }

    private void openIamge(){
        Intent intent = new Intent ();
        intent.setType ("image/*");
        intent.setAction (Intent.ACTION_GET_CONTENT);
        startActivityForResult (intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext ().getContentResolver ();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton ();

        return mimeTypeMap.getExtensionFromMimeType (contentResolver.getType (uri));
    }

    //handling upload image in profile
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog (getContext ());
        pd.setMessage ("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child (System.currentTimeMillis ()
                    +"."+ getFileExtension (imageUri));

            uploadTask = fileReference.putFile (imageUri);
            uploadTask.continueWithTask (new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful ()){
                        throw  task.getException ();
                    }
                    return fileReference.getDownloadUrl ();
                }
            }).addOnCompleteListener (new OnCompleteListener<Uri> () {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful ()){
                        Uri dowloadUri = task.getResult ();
                        String mUri = dowloadUri.toString ();

                        reference = FirebaseDatabase.getInstance ().getReference ("Users").child (firebaseUser.getUid ());
                        HashMap<String, Object> hashMap = new HashMap<> ();

                        hashMap.put("imageURL", mUri);
                        reference.updateChildren (hashMap);

                        pd.dismiss ();
                    }else {
                        Toast.makeText (getContext (), "Failed!", Toast.LENGTH_SHORT).show ();
                        pd.dismiss ();
                    }
                }
            }).addOnFailureListener (new OnFailureListener () {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText (getContext (), e.getMessage (), Toast.LENGTH_SHORT).show ();
                    pd.dismiss ();
                }
            });
        }else {
            Toast.makeText (getContext (),"No image selection", Toast.LENGTH_SHORT ).show ();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult (requestCode, resultCode,data);

        if(requestCode == IMAGE_REQUEST && requestCode == RESULT_OK
                && data != null && data.getData () != null){
            imageUri = data.getData ();

            if(uploadTask != null && uploadTask.isInProgress ()){
                Toast.makeText (getContext (), "Upload in progress", Toast.LENGTH_SHORT).show ();
            }else {
                uploadImage ();
            }
        }
    }
}
