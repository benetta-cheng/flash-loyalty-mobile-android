package com.ibm2105.loyaltyapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.ibm2105.loyaltyapp.database.CartItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PreOrderConfirmationFragment extends Fragment {

    public PreOrderConfirmationFragment() {
        // Required empty public constructor
    }

    public static PreOrderConfirmationFragment newInstance() {
        PreOrderConfirmationFragment fragment = new PreOrderConfirmationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] BRANCHES = new String[]{"Branch1", "Branch2", "Branch3"};

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_item, BRANCHES);
        AutoCompleteTextView stateInput = view.findViewById(R.id.autoCompleteTextInputBranch);
        stateInput.setAdapter(stateAdapter);

        TextInputEditText textInputEditTextPreOrderDate = view.findViewById(R.id.textInputEditTextPreOrderDate);
        textInputEditTextPreOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                materialDatePickerBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
                materialDatePickerBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);

                CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder();
                calendarConstraints.setOpenAt(MaterialDatePicker.todayInUtcMilliseconds());
                calendarConstraints.setValidator(DateValidatorPointForward.now());

                materialDatePickerBuilder.setCalendarConstraints(calendarConstraints.build());
                MaterialDatePicker<Long> materialDatePicker = materialDatePickerBuilder.build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Date date = new Date(selection);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    textInputEditTextPreOrderDate.setText(dateFormat.format(date));
                });

                materialDatePicker.show(getChildFragmentManager(), materialDatePicker.toString());

            }
        });

        //PreOrderListData[] preOrderListDataArray = new PreOrderListData[]{};
        PreOrderViewModel viewModel = new ViewModelProvider(requireActivity()).get(PreOrderViewModel.class);

        RecyclerView preOrderRecycler = view.findViewById(R.id.preOrderRecycler);
        PreOrderConfirmationListAdapter preOrderConfirmationListAdapter = new PreOrderConfirmationListAdapter(new ArrayList<PreOrderListData>());
        preOrderRecycler.setHasFixedSize(true);
        preOrderRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        preOrderRecycler.setAdapter(preOrderConfirmationListAdapter);

        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            viewModel.initCartItems(items, dateFormat.format(MaterialDatePicker.todayInUtcMilliseconds()));
        });

        viewModel.getCart().observe(getViewLifecycleOwner(), cart -> {
            System.out.println("Fragment Test");
            if (cart != null) {
                System.out.println("Got the cart");
                viewModel.getCartItems(cart.getId()).observe(getViewLifecycleOwner(), cartItems -> {
                    System.out.println("Got the cart items " + cartItems.size());
                    viewModel.makeConfirmationPreOrderListData(cartItems);
                });
            }
        });

        viewModel.getConfirmationItems().observe(getViewLifecycleOwner(), items -> {
            preOrderConfirmationListAdapter.setListData(items);
            preOrderConfirmationListAdapter.notifyDataSetChanged();
        });

        Button checkoutButton = view.findViewById(R.id.buttonPreOrderBack);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_pre_order);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_order_confirmation, container, false);
    }

}