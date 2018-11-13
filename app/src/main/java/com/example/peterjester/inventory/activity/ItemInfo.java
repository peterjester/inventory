package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.peterjester.inventory.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemInfo extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    static int runningId = 1;

    String mCurrentPhotoPath = null;
    Bitmap capturedImage = null;

    private EditText itemView = null;
    private EditText descriptionView = null;
    private EditText locationView = null;
    private EditText beaconView = null;
    private ImageView imageView = null;

    private Button addButton = null;
    private Button checkoutButton = null;
    private Button removeButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        itemView = findViewById(R.id.itemEditView);
        descriptionView = findViewById(R.id.descriptionEditView);
        locationView = findViewById(R.id.locationEditView);
        beaconView = findViewById(R.id.beaconEditView);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(this);

        removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener(this);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageView: dispatchTakePictureIntent(); break;
            case R.id.addButton: add(); break;
            case R.id.checkoutButton: checkout(); break;
            case R.id.removeButton: remove(); break;
        }

    }

    private void remove(){
        Toast.makeText(getApplicationContext(),"Removing item",Toast.LENGTH_LONG).show();
    }

    private void checkout(){
        Toast.makeText(getApplicationContext(),"Checking out item",Toast.LENGTH_LONG).show();
    }

    private void add() {

        boolean validItem = verifyFields();
        if(validItem) {
            Toast.makeText(getApplicationContext(),"Adding item " + itemView.getText().toString() ,Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Warning: Must enter a name and location",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * @brief In order to have a valid item entered into our db, we must have
     *          a name, and a description at minimum. This check verifies that
     * @return true if valid, otherwise false;
     */
    private boolean verifyFields(){
        boolean enteredValidItem = true;

        if(itemView.getText().toString().matches("")
            || locationView.getText().toString().matches("")){
            enteredValidItem = false;
        }

        return enteredValidItem;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedImage = (Bitmap) extras.get("data");
            imageView.setImageBitmap(capturedImage);
        }
    }


}
