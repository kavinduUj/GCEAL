package com.appflavorsz.gceal.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.appflavorsz.gceal.R;
import com.appflavorsz.gceal.activities.LoadUrl;
import com.appflavorsz.gceal.service.other.SharePref;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class MenuPage extends BottomSheetDialogFragment {

    private View v;
    private ImageView addProfileImage;
    private CardView privacy,terms,help,about,rate;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private Uri imageUri;
    private SharePref pref;

    private String pri = "file:///android_asset/gce.html";
    private String condi = "file:///android_asset/condition.html";
    private String APP_PNAME = "com.education.gceal";



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.menu_page,container,false);
        variableCasting();



        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        privacy.setOnClickListener((View vv)->{
            Intent i = new Intent(getActivity(), LoadUrl.class);
            i.putExtra("title","Privacy Policy");
            i.putExtra("htmlUrl",pri);
            startActivity(i);
        });

        terms.setOnClickListener((View vv)->{
            Intent i = new Intent(getActivity(), LoadUrl.class);
            i.putExtra("title","Terms & Condition");
            i.putExtra("htmlUrl",condi);
            startActivity(i);
        });

        about.setOnClickListener((View vv)->{
            Intent i = new Intent(getActivity(), LoadUrl.class);
            i.putExtra("title","About us");
            i.putExtra("htmlUrl",pri);
            startActivity(i);
        });

        rate.setOnClickListener((View vv)->{

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APP_PNAME)));

        });
        help.setOnClickListener((View v)->{

            new AlertDialog.Builder(getActivity())
                    .setTitle("Help")
                    .setMessage("If you have any problem this app,you can click Get help and contact our team member ")
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_baseline_live_help_24)
                    .setPositiveButton("Get help", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "edu.gceal@gmail.com"));
                            intent.putExtra(Intent.EXTRA_SUBJECT, "get help for GCEAL app");
                            startActivity(intent);
                        }
                    }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
                    .show();
        });


        return v;
    }


    private void selectImage() {

        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), 1);
    }
    private String getImages(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void uploadImage() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference storageReference22 = FirebaseStorage.getInstance().getReference("users");
        StorageReference ref = storageReference22.child(System.currentTimeMillis()+" "+ getImages(imageUri));
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isSuccessful());

                Uri uri1 = uri.getResult();

                if (uri.isSuccessful()){

                    HashMap<String,Object> map = new HashMap<>();

                    map.put("proUrl",uri1.toString());

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("users");
                    databaseReference.child(user.getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getActivity(),"URL updated",Toast.LENGTH_SHORT).show();
                            Log.i("getId", String.valueOf(databaseReference.child(user.getUid())));
                            progressDialog.dismiss();
                        }
                    });

                }


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+(int) progress + "%");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK){

            if (requestCode == 1){
                imageUri = data.getData();
                uploadImage();
            }

        }

    }

    private void variableCasting(){

        privacy = (CardView)v.findViewById(R.id.mPrivacy);
        terms = (CardView)v.findViewById(R.id.mTems);
        help = (CardView)v.findViewById(R.id.mHelp);
        about = (CardView)v.findViewById(R.id.mAboutLay);
        rate = (CardView)v.findViewById(R.id.mRateusLay);
        pref = new SharePref(getActivity());

    }

//    private void setPrefValues(){
//        String getFName = Prefs.getString("firstName","");
//        String getLName = Prefs.getString("lastName","");
//        String getEmail = Prefs.getString("email","");
//        String getUid = Prefs.getString("userId","");
//        String getProUrl = Prefs.getString("profileUrl","");
//
//        name.setText(getFName + " " + getLName);
//        email.setText(getEmail);
//
//        if (getProUrl.isEmpty()){
//
//        }else {
//            Glide.with(this).load(getProUrl).into(setProfileImage);
//        }
//
//        Log.i("pritUserData",getFName + "\n"+ getLName + "\n" + getEmail);
//
//
//    }


}
