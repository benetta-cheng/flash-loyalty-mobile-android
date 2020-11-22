package com.ibm2105.loyaltyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    SimpleDateFormat dateFormat;

    public ProfileFragment() {
        // Required empty public constructor
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView profileImageView = view.findViewById(R.id.imageViewProfile);
        TextInputLayout usernameTextInputLayout = view.findViewById(R.id.textInputUsername);
        TextInputLayout fullNameTextInputLayout = view.findViewById(R.id.textInputFullName);
        TextInputLayout emailTextInputLayout = view.findViewById(R.id.textInputEmail);
        TextInputLayout dobTextInputLayout = view.findViewById(R.id.textInputDOB);
        TextInputLayout stateTextInputLayout = view.findViewById(R.id.textInputState);

        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                if (status.equals(R.string.username_exists)) {
                    usernameTextInputLayout.setError(getString(R.string.username_exists));
                } else {
                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getAccountLiveData().observe(getViewLifecycleOwner(), account -> {

            viewModel.setAccount(account);

            usernameTextInputLayout.getEditText().setText(account.getUsername());
            emailTextInputLayout.getEditText().setText(account.getEmail());

            if (account.getFullName() != null) {
                fullNameTextInputLayout.getEditText().setText(account.getFullName());
            }

            if (account.getDateOfBirth() != null) {
                dobTextInputLayout.getEditText().setText(account.getDateOfBirth());
            }

            if (account.getState() != null) {
                AutoCompleteTextView stateAutoCompleteTextView = (AutoCompleteTextView) stateTextInputLayout.getEditText();
                stateAutoCompleteTextView.setText(account.getState(), false);
            }

            if (account.getImage() != null) {
                byte[] decodedString = Base64.decode(account.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profileImageView.setImageBitmap(bitmap);
            }
        });

        String[] STATES = new String[]{"Kuala Lumpur", "Putrajaya", "Labuan", "Perlis", "Kedah", "Terengganu", "Pahang", "Perak", "Kelantan", "Penang", "Selangor", "Negeri Sembilan", "Johor", "Malacca", "Sabah", "Sarawak"};

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_item, STATES);
        AutoCompleteTextView stateInput = view.findViewById(R.id.autoCompleteTextInputState);
        stateInput.setAdapter(stateAdapter);

        stateInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                stateInput.clearFocus();
            }
        });

        TextInputEditText textInputEditTextDOB = view.findViewById(R.id.textInputEditTextDOB);
        textInputEditTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dob = dobTextInputLayout.getEditText().getText().toString().trim();

                MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder();

                if (dob.isEmpty()) {
                    materialDatePickerBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
                    calendarConstraints.setOpenAt(MaterialDatePicker.todayInUtcMilliseconds());
                } else {
                    try {
                        long date = dateFormat.parse(dob).getTime();
                        materialDatePickerBuilder.setSelection(date);
                        calendarConstraints.setOpenAt(date);
                    } catch (ParseException exception) {
                        materialDatePickerBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
                    }
                }
                materialDatePickerBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);

                calendarConstraints.setEnd(MaterialDatePicker.todayInUtcMilliseconds());
                calendarConstraints.setValidator(DateValidatorPointBackward.now());

                materialDatePickerBuilder.setCalendarConstraints(calendarConstraints.build());
                MaterialDatePicker<Long> materialDatePicker = materialDatePickerBuilder.build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Date date = new Date(selection);
                    textInputEditTextDOB.setText(dateFormat.format(date));
                });

                materialDatePicker.show(getChildFragmentManager(), materialDatePicker.toString());

            }
        });

        Button saveButton = view.findViewById(R.id.buttonSaveChanges);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username = usernameTextInputLayout.getEditText().getText().toString().trim();
                String email = emailTextInputLayout.getEditText().getText().toString().trim();

                boolean valid = true;

                if (username.isEmpty()) {
                    usernameTextInputLayout.setError(getString(R.string.required_input));
                    valid = false;
                } else if (usernameTextInputLayout.isErrorEnabled()) {
                    usernameTextInputLayout.setErrorEnabled(false);
                }

                if (email.isEmpty()) {
                    emailTextInputLayout.setError(getString(R.string.required_input));
                    valid = false;
                } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (emailTextInputLayout.isErrorEnabled()) {
                        emailTextInputLayout.setErrorEnabled(false);
                    }
                } else {
                    emailTextInputLayout.setError(getString(R.string.invalid_email));
                    valid = false;
                }

                if (valid) {
                    viewModel.update(username, fullNameTextInputLayout.getEditText().getText().toString().trim(), dobTextInputLayout.getEditText().getText().toString().trim(), stateTextInputLayout.getEditText().getText().toString().trim(), email);
                }
            }
        });

        Button changePasswordButton = view.findViewById(R.id.buttonChangePassword);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordDialogFragment changePasswordDialogFragment = new ChangePasswordDialogFragment();
                changePasswordDialogFragment.show(getChildFragmentManager(), "");
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1001);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap image = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                viewModel.updateImage(encodedImage);
                cursor.close();
            } else {
                Toast.makeText(getContext(), "Image Selection Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}