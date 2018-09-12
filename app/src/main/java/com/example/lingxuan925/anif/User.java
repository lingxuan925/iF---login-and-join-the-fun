package com.example.lingxuan925.anif;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class User extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ArrayList<Option> optionList = new ArrayList<>();
    private Button logoutBtn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private TextView name;
    private TextView user_email;
    private ImageView profile_pic;

    public User() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOptions();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        OptionAdapter adapter = new OptionAdapter(view.getContext(), R.layout.option_item, optionList);
        ListView listView = view.findViewById(R.id.options_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser current_user = mAuth.getCurrentUser();
        logoutBtn = view.findViewById(R.id.signout);
        logoutBtn.setOnClickListener(this);

        name = view.findViewById(R.id.user_name);
        user_email = view.findViewById(R.id.user_id);
        name.setText(current_user.getDisplayName());
        user_email.setText(current_user.getEmail());

        profile_pic = view.findViewById(R.id.avatar);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getActivity(), SignInPage.class));
                }
            }
        };
        return view;
    }

    private void initOptions() {
        Option option1 = new Option("Change avatar", R.drawable.profile_icon);
        optionList.add(option1);
        Option option2 = new Option("Change nickname", R.drawable.pencil_icon);
        optionList.add(option2);
        Option option3 = new Option("Upcoming events", R.drawable.calendar_icon);
        optionList.add(option3);
        Option option4 = new Option("Details", R.drawable.details_icon);
        optionList.add(option4);
        Option option5 = new Option("Settings", R.drawable.wrench_icon);
        optionList.add(option5);
        Option option6 = new Option("Feedback", R.drawable.send_icon);
        optionList.add(option6);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signout) mAuth.signOut();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String text = (String) ((TextView) view.findViewById(R.id.option_name)).getText();
        switch (text) {
            case "Change avatar":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                System.out.println("change avatar is clicked");
                checkAndroidVersion();
                break;
            case "Change nickname":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                System.out.println("change nickname is clicked");
                break;
            case "Upcoming events":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                System.out.println("Upcoming events is clicked");
                break;
            case "Details":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                System.out.println("Details is clicked");
                break;
            case "Settings":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                System.out.println("Settings is clicked");
                break;
            case "Feedback":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                System.out.println("Feedback is clicked");
                break;
        }
    }

    public void checkAndroidVersion() {
        //REQUEST PERMISSION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
            } catch (Exception e) {

            }
        } else {
            pickImage();
        }
    }

    //PICK IMAGE METHOD
    public void pickImage() {
        CropImage.startPickImageActivity(getActivity());
    }

    //FOR ACTIVITY RESULT PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 555 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            checkAndroidVersion();
        }
    }

    //CROP REQUEST JAVA
    private void croprequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getActivity());
    }

    //FOR ACTIVITY RESULT
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //RESULT FROM SELECTED IMAGE
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);
            croprequest(imageUri);
        }

        //RESULT FROM CROPING ACTIVITY
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());

                    profile_pic.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

