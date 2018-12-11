package com.example.lingxuan925.anif;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentUser extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ArrayList<Option> optionList = new ArrayList<>();
    private Button logoutBtn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private TextView name, whatsup;
    private TextView user_email;
    ImageView profile_pic;
    Dialog myDialog;
    DatabaseReference databaseRef;

    public FragmentUser() {
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


        myDialog = new Dialog(getContext());
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
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        logoutBtn = view.findViewById(R.id.signout);
        logoutBtn.setOnClickListener(this);

        final String cur_user_key = current_user.getUid();

        name = view.findViewById(R.id.user_name);
        whatsup = view.findViewById(R.id.whats_up);
        user_email = view.findViewById(R.id.user_id);
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

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(cur_user_key)) {
                        name.setText(ds.getValue(AppUser.class).getName());
                        whatsup.setText(ds.getValue(AppUser.class).getWhatsup());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                Pix.start(getFragmentManager().findFragmentByTag("android:switcher:" + R.id.fragment_frame + ":2"),
                        1001,
                        1);
                break;
            case "Change nickname":
                showChangeNamePopUp(view);
                break;
            case "Upcoming events":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                showUpcomingEvents();
                break;
            case "Details":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                editDetails();
                break;
            case "Settings":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                break;
            case "Feedback":
                Toast.makeText(getActivity(), text + " is clicked!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showUpcomingEvents() {
        startActivity(new Intent(getActivity(), UpcomingEvents.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void editDetails() {
        startActivity(new Intent(getActivity(), Details.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showChangeNamePopUp(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.change_nickname_popup, null);

        final EditText nicknameField = mView.findViewById(R.id.change_nickname);
        Button mSave = mView.findViewById(R.id.btn_save);
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String editNickName = nicknameField.getText().toString();
                if (!editNickName.isEmpty()) {
                    updateNickname(editNickName);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(),
                            "field is empty!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateNickname(String newNickName) {
        try {
            databaseRef.child(mAuth.getCurrentUser().getUid()).child("name").setValue(newNickName);
            name.setText(newNickName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            profile_pic.setColorFilter(Color.TRANSPARENT);
            profile_pic.setImageURI(resultUri);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(getActivity(), cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (resultCode == RESULT_OK && (requestCode == 1001 || requestCode == 1002)) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Toast.makeText(getActivity(), returnValue.get(0), Toast.LENGTH_SHORT).show();
            Uri selectedImage = Uri.fromFile(new File(returnValue.get(0)));
            UCrop.Options options = new UCrop.Options();
            options.setFreeStyleCropEnabled(true);
            options.setCircleDimmedLayer(true);
            options.setShowCropGrid(false);
            UCrop.of(selectedImage, selectedImage)
                    .withOptions(options)
                    .withAspectRatio(16, 9)
                    .start(getContext(), this, UCrop.REQUEST_CROP);
        }
    }
}
