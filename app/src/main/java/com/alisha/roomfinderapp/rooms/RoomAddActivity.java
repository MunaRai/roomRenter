package com.alisha.roomfinderapp.rooms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.alisha.roomfinderapp.utils.GPSTracker;
import com.alisha.roomfinderapp.utils.ImageManager;
import com.alisha.roomfinderapp.utils.Permissions;
import com.alisha.roomfinderapp.utils.SharedPreferenceHelper;
import com.alisha.roomfinderapp.utils.UniversalImageLoader;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RoomAddActivity extends AppCompatActivity {
    private static final String TAG = "RoomAddActivity";

    private static final int VERIFY_PERMISSION_REQUEST = 100;
    private Context mContext = RoomAddActivity.this;

    private TextInputEditText name, desc, price, no_of_rooms, location, owner_name, contact_no;
    private ImageView image;
    private Button btn_save;

    private String s_name, s_desc, s_price, s_no_of_rooms, s_location, image_link, s_owner_name,
            s_contact_no, s_room_services;
    private boolean valid;

    private FirebaseHelper mFirebaseHelper;
    private ProgressDialog dialog;
    private String keyId;
    private int mPhotoUploadProgress = 0;
    private Spinner perUnitsSpinner;
    private Spinner districtsSpinner;

    private String exactLocation;
    private long longitude, latitude;

    private Room post;
    private ArrayAdapter<CharSequence> districtsAdapter;
    private ArrayAdapter<CharSequence> perAdapter;
    private boolean isEditPost = false;
    private boolean isImageChanged = false;
    private LocationManager locationManager;
    private ArrayAdapter<CharSequence> roomTypeAdapter, payTypeAdapter;
    private Spinner roomTypeSpinner, payment_type_spinner;
    private EditText room_services;
    private boolean requestingLocationUpdates;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_add);

        setupWidgets();
        checkCallingIntent();
        setupToolbar();
        initImageLoader();
        mFirebaseHelper = new FirebaseHelper(mContext);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }

            ;
        };
        setupProgressDialog();
        initData();
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */
                setupLocationService();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

    }

    private void checkCallingIntent() {
        Intent in = getIntent();
        if (in.hasExtra(mContext.getString(R.string.calling_room_edit))) {


            post = in.getParcelableExtra(mContext.getString(R.string.calling_room_edit));

            name.setText(post.getName().toString());
            desc.setText(post.getDesc().toString());
            int spinnerPosition = perAdapter.getPosition(post.getPerUnits());
            perUnitsSpinner.setSelection(spinnerPosition);

            location.setText(post.getLocation());
            UniversalImageLoader.setImage(post.getImage(), image, null, "");
            price.setText(post.getPrice() + "");

            int districtPostion = districtsAdapter.getPosition(post.getDistrict());
            districtsSpinner.setSelection(spinnerPosition);
            no_of_rooms.setText(post.getNo_of_rooms() + "");
            owner_name.setText(post.getOwner_name());
            contact_no.setText(String.format("%d", post.getContact_no()));

            image_link = post.getImage();
            isEditPost = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationClient != null)
            stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void setupLocationService() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        GPSTracker gps = new GPSTracker(RoomAddActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            setupLocationFromLatLong(longitude, latitude);
            this.longitude = (long) latitude;
            this.latitude = (long) longitude;
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }

    private void setupLocationFromLatLong(double longitude, double latitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); //
            Log.d(TAG, "setupLocationFromLatLong: "+addresses);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            exactLocation = String.format("%s, %s, %s", address, city, state);
            location.setText(exactLocation);
        } catch (IOException e) {
            Toast.makeText(mContext, "Error occured while get location. Please enter by self", Toast.LENGTH_SHORT).show();
        }
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupProgressDialog() {
        dialog = new ProgressDialog(mContext); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {

        owner_name.setText(SharedPreferenceHelper.getInstance(getApplicationContext()).getUserInfo().getUsername());

        if (!isEditPost)
            image_link = "";
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissionsArray(Permissions.PERMISSIONS)) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, VERIFY_PERMISSION_REQUEST);
                } else {
                    verifyPermissionsArray(Permissions.PERMISSIONS);
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_name = name.getText().toString();
                s_desc = desc.getText().toString();
                s_location = location.getText().toString();
                s_price = price.getText().toString();
                s_no_of_rooms = no_of_rooms.getText().toString();
                s_owner_name = owner_name.getText().toString();
                s_contact_no = contact_no.getText().toString();
                s_room_services = room_services.getText().toString();

                if (!validateForm()) {
                    Toast.makeText(mContext, "All fields are not set correctly", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressDialog();
                    savePictureToStorageNSave();
                }
            }
        });
    }

    private boolean validateForm() {
        valid = true;

        if (s_name.isEmpty()) {
            name.setError(mContext.getString(R.string.empty_field));
            valid = false;
        }
        if (s_price.isEmpty()) {
            price.setError(mContext.getString(R.string.empty_field));
            valid = false;
        }
        if (image_link.isEmpty()) {
            Toast.makeText(mContext, "Set an image", Toast.LENGTH_SHORT).show();
            valid = false;
        }


        return valid;
    }

    private void addRoomToDatabase(Uri downloadUrl) {


        String date = DateTimeUtils.formatDate(new Date());

        Room post = new Room();
        post.setCategory_name((String) roomTypeSpinner.getSelectedItem());
        post.setId(keyId);
        post.setName(s_name);
        post.setDesc(s_desc);
        post.setLocation(s_location);
        post.setImage(downloadUrl.toString());
        post.setPrice(s_price.toString());
        post.setPerUnits((String) perUnitsSpinner.getSelectedItem());
        post.setNo_of_rooms(Integer.parseInt(s_no_of_rooms));
        post.setOwner_name(s_owner_name);
        post.setOwner_id(mFirebaseHelper.getAuth().getCurrentUser().getUid());
        post.setContact_no(Long.parseLong(s_contact_no));
        post.setOwnerRules("This is owner rules part");
        post.setDistrict((String) districtsSpinner.getSelectedItem());
        post.setExactLocation(exactLocation);
        post.setDate_added(date);
        post.setUpdated_at(date);
        post.setDeleted_at(null);
        post.setLattitude(latitude);
        post.setLongitutde(longitude);
        post.setViewCount(0);
        post.setOnlinePaymentType((String) payment_type_spinner.getSelectedItem());
        post.setServices(s_room_services);


        assert keyId != null;
        mFirebaseHelper.getMyRef().child(FilePaths.ROOM)
                .child(keyId)
                .setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private void savePictureToStorageNSave() {
        if (isEditPost) {
            checkImageChangeAndUpload();

        } else {
            keyId = mFirebaseHelper.getMyRef().child(FilePaths.ROOM).push().getKey();
            final StorageReference storageReference = mFirebaseHelper.getmStorageReference()
                    .child(FilePaths.ROOM + "/" + "room" + keyId);
            InputStream is = null;
            try {
                is = mContext.getContentResolver().openInputStream(Uri.parse(image_link));

                Bitmap bitmap = BitmapFactory.decodeStream(is);
                byte[] data = ImageManager.getBytesFromBitmap(bitmap, 60);
                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(mContext, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        if (progress - 30 > mPhotoUploadProgress) {
                            Toast.makeText(mContext, "Photo upload progress: " + String.format("%.0f", progress), Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, "onProgress: progress: " + String.format("%.0f", progress) + " % done!");

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Task<Uri> firebaseUrl = storageReference.getDownloadUrl();
                        while (!firebaseUrl.isSuccessful()) ;

                        Uri downloadUrl = firebaseUrl.getResult();

                        SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance(mContext);
                        sharedPreferenceHelper.setAvatar(downloadUrl.toString());
                        Toast.makeText(mContext, "Upload success", Toast.LENGTH_SHORT).show();

                        addRoomToDatabase(downloadUrl);


                    }
                });
            } catch (FileNotFoundException e) {
                //            e.printStackTrace();
                Toast.makeText(mContext, "File not found.", Toast.LENGTH_SHORT).show();
            } finally {

                try {
                    assert is != null;
                    is.close();
                } catch (IOException e) {
                    //                e.printStackTrace();
                    Toast.makeText(mContext, "Exception while closing file", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void checkImageChangeAndUpload() {
        if (isImageChanged) {
            keyId = post.getId();
            final StorageReference storageReference = mFirebaseHelper.getmStorageReference()
                    .child(FilePaths.ROOM + "/" + "room" + post.getId());
            InputStream is = null;
            try {
                is = mContext.getContentResolver().openInputStream(Uri.parse(image_link));

                Bitmap bitmap = BitmapFactory.decodeStream(is);
                byte[] data = ImageManager.getBytesFromBitmap(bitmap, 60);
                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(mContext, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        if (progress - 30 > mPhotoUploadProgress) {
                            Toast.makeText(mContext, "Photo upload progress: " + String.format("%.0f", progress), Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, "onProgress: progress: " + String.format("%.0f", progress) + " % done!");

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Task<Uri> firebaseUrl = storageReference.getDownloadUrl();
                        while (!firebaseUrl.isSuccessful()) ;

                        Uri downloadUrl = firebaseUrl.getResult();

                        SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance(mContext);
                        sharedPreferenceHelper.setAvatar(downloadUrl.toString());
                        Toast.makeText(mContext, "Upload success", Toast.LENGTH_SHORT).show();

                        addRoomToDatabase(downloadUrl);


                    }
                });
            } catch (FileNotFoundException e) {
                //            e.printStackTrace();
                Toast.makeText(mContext, "File not found.", Toast.LENGTH_SHORT).show();
            } finally {

                try {
                    assert is != null;
                    is.close();
                } catch (IOException e) {
                    //                e.printStackTrace();
                    Toast.makeText(mContext, "Exception while closing file", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            keyId = post.getId();
            addRoomToDatabase(Uri.parse(post.getImage()));
        }
    }

    private void updateRoomImageUrl(Uri downloadUrl) {
        mFirebaseHelper.getMyRef().child(FilePaths.ROOM)
                .child(keyId)
                .child("image")
                .setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
            }
        });
    }


    private void hideProgressDialog() {
        dialog.hide();
    }

    private void showProgressDialog() {
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            image_link = targetUri.toString();
            Bitmap bitmap;
            try {
                isImageChanged = true;
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void setupWidgets() {
        roomTypeSpinner = findViewById(R.id.roomTypeSpinner);
        roomTypeSpinner = findViewById(R.id.roomTypeSpinner);
        payment_type_spinner = findViewById(R.id.payment_type_spinner);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        districtsSpinner = findViewById(R.id.districtsSpinner);
        location = findViewById(R.id.location);
        image = findViewById(R.id.image);
        price = findViewById(R.id.price);
        perUnitsSpinner = findViewById(R.id.perUnits);
        no_of_rooms = findViewById(R.id.no_of_rooms);
        btn_save = findViewById(R.id.btn_save);
        owner_name = findViewById(R.id.owner_name);
        contact_no = findViewById(R.id.contact_no);
        room_services = findViewById(R.id.room_services);

        // Create an ArrayAdapter using the string array and a default spinner layout
        districtsAdapter = ArrayAdapter.createFromResource(this,
                R.array.districts_array, android.R.layout.simple_spinner_item);
        districtsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtsSpinner.setAdapter(districtsAdapter);

        perAdapter = ArrayAdapter.createFromResource(this,
                R.array.choice_arrays, android.R.layout.simple_spinner_item);
        perAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        perUnitsSpinner.setAdapter(perAdapter);

        roomTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.room_type_array, android.R.layout.simple_spinner_item);
        roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(roomTypeAdapter);

        payTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.pay_type_array, android.R.layout.simple_spinner_item);
        payTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payment_type_spinner.setAdapter(payTypeAdapter);
    }


    /**
     * check an array of permission
     * check if user has given permissions to the app to perform certain activites like camera permissions
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissionsArray(String[] permissions) {

        for (String permission : permissions) {

            if (!checkPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * check a single permisssion
     *
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {

        int permissionRequest = ActivityCompat.checkSelfPermission(mContext, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: Permission not granted for: " + permission);
            return false;
        }
        return true;
    }

    private void verifyPermissionsArray(String[] permissions) {
        Log.d(TAG, "verifyPermissionsArray: verifying permissions");

        ActivityCompat.requestPermissions(
                this,
                permissions,
                VERIFY_PERMISSION_REQUEST
        );
//        finish();

    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();

    }
}
