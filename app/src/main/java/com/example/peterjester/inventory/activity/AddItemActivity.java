package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.example.peterjester.inventory.model.entity.MapLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    private ImageView imageView = null;

    private Button addButton = null;

    private StorageReference storageRef;
    private StorageReference imageStorageReference;
    String currentImageFileName = null;

    private FusedLocationProviderClient mFusedLocationClient;
    private MapLocation currentGeolocation = null;

    final int userAgreePermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        itemView = findViewById(R.id.itemEditView);
        descriptionView = findViewById(R.id.descriptionEditView);
        locationView = findViewById(R.id.locationEditView);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);

        storageRef = FirebaseStorage.getInstance().getReference();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

    }

    @Override
    protected void onStart() {
        super.onStart();
        itemPersistence = new ItemPersistence();
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
            startActivity(new Intent(AddItemActivity.this, MainActivity.class));
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

        Item item = new Item(runningId++, itemName, description, location, currentImageFileName, currentGeolocation);

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
        currentImageFileName = imageFileName();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                currentImageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String imageFileName() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            capturedImage = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imageView.setImageBitmap(capturedImage);

            addBitmapToFirebaseStroage();
        }
    }

    private void addBitmapToFirebaseStroage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        imageStorageReference = storageRef.child("images/" + currentImageFileName);

        UploadTask uploadTask = imageStorageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

    private void getCurrentLocation() {

        int currentPermission = ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
        if( currentPermission!=PackageManager.PERMISSION_GRANTED ) ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, userAgreePermissionCode);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentGeolocation = new MapLocation(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                        }
                    }
                });

        mFusedLocationClient.getLastLocation()
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // on fail
                    }
                });

    }

}
