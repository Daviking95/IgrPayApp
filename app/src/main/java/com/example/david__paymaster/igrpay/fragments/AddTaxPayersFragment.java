package com.example.david__paymaster.igrpay.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david__paymaster.igrpay.R;
import com.example.david__paymaster.igrpay.SplashLogin;
import com.example.david__paymaster.igrpay.constants.NavigationDrawerConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class AddTaxPayersFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    //    Calendar myCalendar = Calendar.getInstance();
//    EditText dob;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String TAG = "AddTaxPayer";
    final int REQUEST_PERMISSION_CAMERA = 1001;
    final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE =
            1002;
    int permissionCount = 0;
    final static  int mWidth = 200;
    final static int mheight = 200;

    EditText bookletNum, firstName, middleName, lastName, address, mobileNum, occupation, nin, unionNum, imageUpload;

    Spinner genderSpinner, unionSpinner;

    ImageView imageToUpload;

    Button uploadImage, buttonDOB, regButton;

    TextView dob;

    ProgressDialog mProgressDialog;

    DatePickerDialog dpd;
    Calendar c;

    private FirebaseDatabase fireData;
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthListener;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_ADD_TAX_PAYER);

        fireBaseCall();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_payers, container, false);

        setUpViews(view);

        datePickerListener();
        setUpListeners();

        return view;
    }

    private void setUpViews(View view) {
//        BUTTONS
        buttonDOB = (Button) view.findViewById(R.id.input_dob_button);
        uploadImage = (Button) view.findViewById(R.id.uploadImage);
        regButton = (Button) view.findViewById(R.id.register_tax_payer);

//        SPINNERS
        genderSpinner = (Spinner) view.findViewById(R.id.gender);
        unionSpinner = (Spinner) view.findViewById(R.id.input_union);

//        IMAGEVIEW
        imageToUpload = (ImageView) view.findViewById(R.id.imageUpload);

//        EDITTEXTS
        bookletNum = (EditText) view.findViewById(R.id.input_booklet_name);
        firstName = (EditText) view.findViewById(R.id.input_first_name);
        middleName = (EditText) view.findViewById(R.id.input_middle_name);
        lastName = (EditText) view.findViewById(R.id.input_last_name);
        address = (EditText) view.findViewById(R.id.input_address);
        mobileNum = (EditText) view.findViewById(R.id.input_mobile);
        occupation = (EditText) view.findViewById(R.id.input_occupation);
        nin = (EditText) view.findViewById(R.id.input_nin);
        unionNum = (EditText) view.findViewById(R.id.input_union_number);
        imageUpload = (EditText) view.findViewById(R.id.input_image_upload);

//        TEXTVIEWS
        dob = (TextView) view.findViewById(R.id.input_dob);

//        PROGRESSDIALOG
        mProgressDialog = new ProgressDialog(getContext());
    }

    private void setUpListeners() {
        imageToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageUpload.setText();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
//                Intent galleryIntent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                issueCameraIntent();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d (TAG, "onClick: Uploading Image.");
                mProgressDialog.setMessage("Uploading Image...");
                mProgressDialog.show();

                FirebaseUser user = fireAuth.getCurrentUser();
                String userID = user.getUid();

                String _bookletNum = bookletNum.getText().toString();
                String _firstName = firstName.getText().toString();
                String _middleName = middleName.getText().toString();
                String _lastName = lastName.getText().toString();
                String _address = address.getText().toString();
                String _mobileNum = mobileNum.getText().toString();
                String _occupation = occupation.getText().toString();
                String _nin = nin.getText().toString();
                String _unionNum = unionNum.getText().toString();
                String _dob = dob.getText().toString();
                String _genderSpinner = genderSpinner.getSelectedItem().toString();
                String _unionSpinner = unionSpinner.getSelectedItem().toString();

                if (!TextUtils.isEmpty(_bookletNum)) {
                    DatabaseReference ref = myRef.child(userID).child("tax registration").child(_bookletNum);
                    ref.child("bookletNum").setValue(_bookletNum);
                    ref.child("firstName").setValue(_firstName);
                    ref.child("middleName").setValue(_middleName);
                    ref.child("lastName").setValue(_lastName);
                    ref.child("address").setValue(_address);
                    ref.child("mobileNum").setValue(_mobileNum);
                    ref.child("occupation").setValue(_occupation);
                    ref.child("nin").setValue(_nin);
                    ref.child("unionNum").setValue(_unionNum);
                    ref.child("dob").setValue(_dob);
                    ref.child("genderSpinner").setValue(_genderSpinner);
                    ref.child("unionSpinner").setValue(_unionSpinner);

                    Toast.makeText(getActivity(), "Data Saved", Toast.LENGTH_SHORT).show();

                    bookletNum.setText("");
                    firstName.setText("");
                    middleName.setText("");
                    lastName.setText("");
                    address.setText("");
                    mobileNum.setText("");
                    occupation.setText("");
                    nin.setText("");
                    unionNum.setText("");
                    dob.setText("");
                    genderSpinner.setSelection(0, true);
                    unionSpinner.setSelection(0, true);
//                    bookletNum.setText("");
//                    bookletNum.setText("");

                    Fragment homeFragment = new HomeFragment();
                    showFragment(homeFragment);
                }
            }
        });
    }

    private void issueCameraIntent() {
        Intent cameraIntent = new Intent
                (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
//            Uri selectedImage = data.getData();
//            imageToUpload.setImageURI(selectedImage);
//        }

        if (requestCode == RESULT_LOAD_IMAGE &&
                resultCode == RESULT_OK && data != null) {
            Bitmap b = (data.getExtras()).getParcelable("data");
            imageToUpload.setImageBitmap(b);
        }
    }

    public void fireBaseCall(){
        fireAuth = FirebaseAuth.getInstance();
        fireData = FirebaseDatabase.getInstance();
        myRef = fireData.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        checkFilePermissions();

        addFilePath();

        fireAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Sign in logic here.
//                    Log.d(TAG)
                }
            }
        };

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = ActivityCompat.checkSelfPermission(getContext(),"Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += ActivityCompat.checkSelfPermission(getContext(), "Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0 ){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CAMERA);
            }
        }else {
            Log.d(TAG, "checkBTPermission: No need to check permission. SDK version < LOLLIPOP");
        }
    }

    private void addFilePath() {
        Log.d(TAG, "addFilePath: adding file paths.");
        String path = System.getenv("EXTERNAL_STORAGE");

//        loadImageFromStorage();
    }

//    private void loadImageFromStorage(){
//        try {
////            File f = new(path, "");{
//                Bitmap b = (data.getExtras()).getParcelable("data");
//                imageToUpload.setImageBitmap(b);
//        }
//    }

    private void datePickerListener() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dpd = new DatePickerDialog(getContext(), this, year, month, day);
        buttonDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        dob.setText(currentDateString);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED)
                    permissionCount++;
            default:
                break;
        }
    }


    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }



//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setCancelable(false);
//        builder.setMessage("Do you want to Exit?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user pressed "yes", then he is allowed to exit from application
//                finish();
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //if user select "No", just cancel this dialog and continue with app
//                dialog.cancel();
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
}
