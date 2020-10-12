package com.ibm2105.loyaltyapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] STATES = new String[]{"Kuala Lumpur", "Putrajaya", "Labuan", "Perlis", "Kedah", "Terengganu", "Pahang", "Perak", "Kelantan", "Penang", "Selangor", "Negeri Sembilan", "Johor", "Malacca", "Sabah", "Sarawak"};

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_item, STATES);
        AutoCompleteTextView stateInput = view.findViewById(R.id.autoCompleteTextInputState);
        stateInput.setAdapter(stateAdapter);

        TextInputEditText textInputEditTextDOB = view.findViewById(R.id.textInputEditTextDOB);
        textInputEditTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                materialDatePickerBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
                materialDatePickerBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);

                CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder();
                calendarConstraints.setEnd(MaterialDatePicker.todayInUtcMilliseconds());
                calendarConstraints.setOpenAt(MaterialDatePicker.todayInUtcMilliseconds());
                calendarConstraints.setValidator(DateValidatorPointBackward.now());

                materialDatePickerBuilder.setCalendarConstraints(calendarConstraints.build());
                MaterialDatePicker<Long> materialDatePicker = materialDatePickerBuilder.build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Date date = new Date(selection);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    textInputEditTextDOB.setText(dateFormat.format(date));
                });

                materialDatePicker.show(getChildFragmentManager(), materialDatePicker.toString());

            }
        });
    }

}