package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.peterjester.inventory.model.dao.ItemPersistence;
import com.example.peterjester.inventory.model.entity.Item;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    // File dir
    String fileDir = null;

    // SQLite
    ItemPersistence itemPersistence = null;

    // Photo flags and variables
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath = null;
    Bitmap capturedImage = null;

    // Running Id for DB insertion
    static int runningId = 1;

    // view initializations
    private EditText itemView = null;
    private EditText descriptionView = null;
    private EditText locationView = null;
    private EditText beaconView = null;
    private ImageView imageView = null;

    private Button addButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        itemView = findViewById(R.id.itemEditView);
        descriptionView = findViewById(R.id.descriptionEditView);
        locationView = findViewById(R.id.locationEditView);
        beaconView = findViewById(R.id.beaconEditView);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        itemPersistence = new ItemPersistence(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageView: dispatchTakePictureIntent(); break;
            case R.id.addButton: add(); break;
        }

    }

    private void add() {

        boolean validItem = verifyFields();
        if(validItem) {
            Toast.makeText(getApplicationContext(),"Adding item " + itemView.getText().toString() ,Toast.LENGTH_LONG).show();
            insertItemIntoDb();
            startActivity(new Intent(AddItemActivity.this, ViewAllActivity.class));
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

    private void insertItemIntoDb() {

        // Based on previous checks, we know these are valid
        String itemName = itemView.getText().toString();
        String location = locationView.getText().toString();

        String description = getDescription();
        String beacon = getBeacon(); // unused currently

        Item item = new Item(runningId++, itemName, description, location, mCurrentPhotoPath);

        itemPersistence.insert(item);
    }

    /**
     * @brief fill in description if its there, default if it isnt
     * @return description
     */
    private String getDescription() {
        String description = "No description";

        if(!descriptionView.getText().toString().matches("" )) {
            description = descriptionView.getText().toString();
        }
        return description;
    }

    /**
     * @brief fill in beacon if available, default if it isnt
     * @warning this is primarily a placeholder, unless we hit our stretch goal
     *          of implementing the beacon technoloy
     * @return beacon
     */
    private String getBeacon() {
        String beacon = "No description";

        if(!beaconView.getText().toString().matches("" )) {
            beacon = beaconView.getText().toString();
        }
        return beacon;
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
            capturedImage = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imageView.setImageBitmap(capturedImage);
        }
    }


}
