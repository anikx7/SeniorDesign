package com.example.medapp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class medicationactivity extends AppCompatActivity {

    EditText mResultEt;
    ImageView mPreviewIv;
    Button add;
    Button view;
    DatabaseHelper db;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;


    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent (medicationactivity.this,homeactivity.class);
        startActivity (intent);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_medicationactivity);

        db= new DatabaseHelper (this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar ();
        actionBar.setSubtitle ("Click+ button to insert image");



        mResultEt = findViewById (R.id.resultEt);
        mPreviewIv= findViewById (R.id.imageIv);
        add= findViewById (R.id.add);
        view= findViewById (R.id.View);

        cameraPermission = new String[] {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE};


        view.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (medicationactivity.this,second.class);
                startActivity (intent);
            }
        });

        add.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String name = mResultEt.getText ().toString ();

                //////messed up here
                if(!name.equals ("") && db.insertData (name,"0","0") ) {


                    if (name.equals ("Clonodine"))


                    {
                        Cursor cursor = db.sr ("Propranolol");
                        if (cursor.getCount () > 0) {


                            AlertDialog.Builder bld = new AlertDialog.Builder (medicationactivity.this);
                            //   bld.setMessage ("Clonidine has inteartion with Propranolol. Consult a doctor.").setNegativeButton ("Continue",null);
                            bld.setMessage ("Clonidine has inteartion with Propranolol. Consult a doctor.")
                                    .setPositiveButton ("Continue", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText (medicationactivity.this, "ADDED", Toast.LENGTH_SHORT).show ();
                                            add.setText ("");
                                        }
                                    });


                            AlertDialog alt = bld.create ();
                            alt.show ();


                        }
                    } else {
                        Toast.makeText (medicationactivity.this, "ADDED", Toast.LENGTH_SHORT).show ();
                        add.setText ("");
                        //listItem.clear ();
                        //  viewData ();
                    }
                }
                else
                    Toast.makeText (medicationactivity.this,"not added",Toast.LENGTH_SHORT).show ();
            }
        });






    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if(id== R.id.addImage)
        {
            showImageImportDialog();

        }
        if(id== R.id.settings)
        {
            Toast.makeText(this,"settings",Toast.LENGTH_SHORT).show ();

        }

        return super.onOptionsItemSelected (item);
    }

    private void showImageImportDialog() {
        String[] items= {"Camera","Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder (this);
        dialog.setTitle ("select Image");
        dialog.setItems (items, new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0)
                {
                    if(!checkCameraPermission())
                    {
                        requestCameraPermission();
                    }
                    else {
                        pickCamera();
                    }

                }
                if(which==1)
                {
                    if(!checkStoragePermission())
                    {
                        requestStoragePermission();
                    }
                    else {
                        pickGallery();
                    }

                }

            }
        });
        dialog.create ().show ();
    }

    private void pickGallery() {
        Intent intent = new Intent (Intent.ACTION_PICK);
        intent.setType ("image/*");
        startActivityForResult (intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickCamera() {
        ContentValues values = new ContentValues ();
        values.put (MediaStore.Images.Media.TITLE, "NewPic");
        values.put (MediaStore.Images.Media.DESCRIPTION, "Image to Text");
        image_uri = getContentResolver ().insert (MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra (MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult (cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions (this,storagePermission, STORAGE_REQUEST_CODE);

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions (this,cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission (this, Manifest.permission.CAMERA)==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission (this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0)
                {
                    boolean cameraAccepted = grantResults[0]==
                            PackageManager.PERMISSION_GRANTED;

                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted)
                    {
                        pickCamera ();
                    }
                    else {
                        Toast.makeText (this,"permission denied", Toast.LENGTH_SHORT).show ();

                    }

                }
                break;


            case STORAGE_REQUEST_CODE:



                if(grantResults.length>0)
                {
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;


                    if( writeStorageAccepted)
                    {
                        pickGallery ();
                    }
                    else {
                        Toast.makeText (this,"permission denied", Toast.LENGTH_SHORT).show ();

                    }

                }

                break;



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK)
        {
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                CropImage.activity (data.getData ()).setGuidelines (CropImageView.Guidelines.ON).start (this);

            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){


                CropImage.activity (image_uri).setGuidelines (CropImageView.Guidelines.ON).start (this);


            }
        }


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult (data);
            if(resultCode== RESULT_OK){

                Uri resultUri = result.getUri ();
                mPreviewIv.setImageURI (resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable)mPreviewIv.getDrawable ();

                Bitmap bitmap = bitmapDrawable.getBitmap ();

                TextRecognizer recognizer = new TextRecognizer.Builder (getApplicationContext ()).build ();
                if(!recognizer.isOperational ()){
                    Toast.makeText (this, "Error" , Toast.LENGTH_SHORT).show ();


                }
                else {
                    Frame frame = new Frame.Builder ().setBitmap (bitmap).build ();
                    SparseArray<TextBlock> items = recognizer.detect (frame);
                    StringBuilder sb = new StringBuilder ();
                    for(int i =0;i<items.size ();i++){
                        TextBlock myItem = items.valueAt (i);
                        sb.append (myItem.getValue ());
                        sb.append ("\n");

                    }

                    mResultEt.setText (sb.toString ());





                }


            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError ();
                Toast.makeText (this,""+error, Toast.LENGTH_SHORT).show ();



            }

        }

    }

}

