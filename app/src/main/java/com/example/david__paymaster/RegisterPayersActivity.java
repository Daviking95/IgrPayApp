package com.example.david__paymaster;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.*;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.utils.Tools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterPayersActivity extends AppCompatActivity {

    public static final String FIRST_NAME = "first_name";
    public static final String MIDDLE_NAME = "middle_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String OCCUPATION = "occupation";
    public static final String AGE = "age";
    public static final String UNION = "union";
    public static final String AGENT_ID = "agent_id";
    public static final String DATE_OF_REGISTRATION = "date_of_registration";
    public static final String PHOTO_URL = "photo_url";
    public static final String GENDER = "gender";
    ImageView imageview;
    FloatingActionButton uploadImage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private View parent_view;
    ProgressBar progressBar ;

    private Uri filePath;

    Calendar c;

    static final int RESULT_LOAD_IMAGE = 1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText firstNameEd;
    EditText middleNameEd, lastNameEd, phoneNoEd, emailEd, addressEd, cityEd, stateEd, occupationEd, ageEd, unionEd;
    TextView time;
    Spinner genderSpinner;
    private String TAG = "RegisterPayersActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_payers);
        parent_view = findViewById(android.R.id.content);
        initToolbar();
        setUpViews();
        setUpListeners();
        progressBar.setVisibility(View.GONE);
//        onDateSet();

        ((View) findViewById(R.id.register_payer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build();
                db.setFirestoreSettings(settings);

                String androidId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                c = Calendar.getInstance();
                String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
//                String currentDateString = Tools.getFormattedDateEvent();
                time.setText(currentDateString);

                String firstNameText = firstNameEd.getText().toString();
                String middleNameText = middleNameEd.getText().toString();
                String lastNameText = lastNameEd.getText().toString();
                String phoneText = phoneNoEd.getText().toString();
                String emailText = emailEd.getText().toString();
                String addressText = addressEd.getText().toString();
                String cityText = cityEd.getText().toString();
                String stateText = stateEd.getText().toString();
                String occupationText = occupationEd.getText().toString();
                String ageText = ageEd.getText().toString();
                String unionText = unionEd.getText().toString();
                String agentIdText = androidId.toString();
                String _genderSpinner = genderSpinner.getSelectedItem().toString();
                String dateText = time.getText().toString();

                String imagePath = "payersImages/" + agentIdText + "/" + firstNameText + "/" + UUID.randomUUID() + ".png";

                Map<String, Object> user = new HashMap<>();
                user.put(FIRST_NAME, firstNameText);
                user.put(MIDDLE_NAME, middleNameText);
                user.put(LAST_NAME, lastNameText);
                user.put(PHONE, phoneText);
                user.put(EMAIL, emailText);
                user.put(ADDRESS, addressText);
                user.put(CITY, cityText);
                user.put(STATE, stateText);
                user.put(OCCUPATION, occupationText);
                user.put(AGE, ageText);
                user.put(UNION, unionText);
                user.put(AGENT_ID, agentIdText);
                user.put(GENDER, _genderSpinner);
                user.put(DATE_OF_REGISTRATION, dateText);
                user.put(PHOTO_URL, imagePath);

                // Add a new document with a generated ID
                db.collection("igrpayweb/add_tax_payers/tax_payers")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(RegisterPayersActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getBaseContext(), ViewPayersActivity.class));
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                uploadImage(imagePath);

            }

        });


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register Tax Payers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.pink_900);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //    SETUP VIEWS
    private void setUpViews(){
        uploadImage = findViewById(R.id.uploadImageButton);
        firstNameEd = findViewById(R.id.first_name);
        middleNameEd = findViewById(R.id.middle_name);
        lastNameEd = findViewById(R.id.last_name);
        phoneNoEd = findViewById(R.id.phone);
        emailEd = findViewById(R.id.email);
        addressEd = findViewById(R.id.address);
        cityEd = findViewById(R.id.city);
        stateEd = findViewById(R.id.state);
        occupationEd = findViewById(R.id.occupation);
        ageEd = findViewById(R.id.age);
        unionEd = findViewById(R.id.union);
        progressBar = findViewById(R.id.progressbar);
        imageview = findViewById(R.id.imageUploadView);
        time = findViewById(R.id.time);
        genderSpinner = findViewById(R.id.gender);
    }

    private void setUpListeners(){
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
    }

    //    ##################### SETUP IMAGE OPERATIONS
    private void imageOperations(){
        if (!hasCamera()){
            uploadImage.setEnabled(false);
        }
    }

    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void launchCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void uploadImage(String imagePath) {

        imageview.setDrawingCacheEnabled(true);
        imageview.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageview.setImageBitmap(bitmap);
        byte[] data = baos.toByteArray();

        StorageReference imageRef = storage.getReference(imagePath);

//        imageRef.getDownloadUrl()
        progressBar.setVisibility(View.VISIBLE);
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);
                taskSnapshot.getMetadata();
                String Uri = taskSnapshot.getUploadSessionUri().toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(RegisterPayersActivity.this, "Upload Failed." + exception,
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Upload Failed", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_IMAGE){
            imageview.setDrawingCacheEnabled(true);
            imageview.buildDrawingCache();
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageview.setImageBitmap(photo);
            byte[] dataB = baos.toByteArray();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//    ##################### END IMAGE OPERATIONS
}
