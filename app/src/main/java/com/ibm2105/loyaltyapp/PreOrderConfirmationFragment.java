package com.ibm2105.loyaltyapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.ibm2105.loyaltyapp.database.CartItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

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

        PreOrderViewModel viewModel = new ViewModelProvider(requireActivity()).get(PreOrderViewModel.class);

        String[] BRANCHES = new String[]{"Shah Alam", "Subang Jaya", "Kuala Lumpur"};

        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_item, BRANCHES);
        AutoCompleteTextView branchInput = view.findViewById(R.id.autoCompleteTextInputBranch);
        branchInput.setAdapter(branchAdapter);

        branchInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.updateBranch((String) adapterView.getItemAtPosition(i));
            }
        });

        TextInputEditText textInputEditTextPreOrderDate = view.findViewById(R.id.textInputEditTextPreOrderDate);
        textInputEditTextPreOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder();
                String preOrderDate = textInputEditTextPreOrderDate.toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                if (preOrderDate.isEmpty()) {
                    materialDatePickerBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
                    calendarConstraints.setOpenAt(MaterialDatePicker.todayInUtcMilliseconds());
                } else {
                    try {
                        long date = dateFormat.parse(preOrderDate).getTime();
                        materialDatePickerBuilder.setSelection(date);
                        calendarConstraints.setOpenAt(date);
                    } catch (ParseException exception) {
                        materialDatePickerBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
                    }
                }

                materialDatePickerBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);

                calendarConstraints.setStart(MaterialDatePicker.todayInUtcMilliseconds());
                calendarConstraints.setValidator(DateValidatorPointForward.now());

                materialDatePickerBuilder.setCalendarConstraints(calendarConstraints.build());
                MaterialDatePicker<Long> materialDatePicker = materialDatePickerBuilder.build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Date date = new Date(selection);
                    String formattedDate = dateFormat.format(date);
                    viewModel.updateCollectionDate(formattedDate);
                    textInputEditTextPreOrderDate.setText(formattedDate);
                });

                materialDatePicker.show(getChildFragmentManager(), materialDatePicker.toString());

            }
        });

        RecyclerView preOrderRecycler = view.findViewById(R.id.preOrderRecycler);
        PreOrderConfirmationListAdapter preOrderConfirmationListAdapter = new PreOrderConfirmationListAdapter(new ArrayList<PreOrderListData>(), viewModel);
        preOrderRecycler.setHasFixedSize(true);
        preOrderRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        preOrderRecycler.setAdapter(preOrderConfirmationListAdapter);

        viewModel.getCart().observe(getViewLifecycleOwner(), cart -> {
            if (cart != null) {
                viewModel.initCartItems(cart);
                viewModel.getCartItems(cart.getId()).observe(getViewLifecycleOwner(), cartItems -> {
                    viewModel.makeConfirmationPreOrderListData(cartItems);
                });

                if (cart.getLocation() != null) {
                    branchInput.setText(cart.getLocation(), false);
                } else {
                    branchInput.setText(BRANCHES[0], false);
                    viewModel.updateBranch(BRANCHES[0]);
                }

                if (cart.getCollectionDate() != null) {
                    textInputEditTextPreOrderDate.setText(cart.getCollectionDate());
                }
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                viewModel.createCart(dateFormat.format(MaterialDatePicker.todayInUtcMilliseconds()));
            }
        });

        LiveData<List<PreOrderListData>> confirmationItemsLiveData = viewModel.getConfirmationItems();
        confirmationItemsLiveData.observe(getViewLifecycleOwner(), new Observer<List<PreOrderListData>>() {
            @Override
            public void onChanged(List<PreOrderListData> items) {
                preOrderConfirmationListAdapter.setListData(items);
                preOrderConfirmationListAdapter.notifyDataSetChanged();
            }
        });

        TextView totalPriceTextView = view.findViewById(R.id.textViewOrderPrice);
        viewModel.getTotalPrice().observe(getViewLifecycleOwner(), totalPrice -> {
            totalPriceTextView.setText(String.format("%.2f", totalPrice));
        });

        Button backButton = view.findViewById(R.id.buttonPreOrderBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_pre_order);
            }
        });

        Button confirmButton = view.findViewById(R.id.buttonPreOrderConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.completePreOrder();
                Toast.makeText(getContext(), "Preorder Completed", Toast.LENGTH_SHORT).show();
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                String id = "Channel 1";
                String description = "Preorders";
                int importance=NotificationManager.IMPORTANCE_LOW;

                if (Build.VERSION.SDK_INT >= 26){
                    NotificationChannel channel = new NotificationChannel(id, description,importance);
                    notificationManager.createNotificationChannel(channel);

                    Notification notification = new Notification.Builder(getContext(),id)
                            .setCategory(Notification.CATEGORY_MESSAGE)
                            .setSmallIcon(R.drawable.ic_logo)
                            .setContentTitle("Preorder Complete")
                            .setContentText("You have successfully placed a preorder.")
                            .setAutoCancel(true)
                            .build();
                    notificationManager.notify(1,notification);
                }
                else {
                    Notification notification = new Notification.Builder(getContext())
                            .setSmallIcon(R.drawable.ic_logo)
                            .setContentTitle("Preorder Complete")
                            .setContentText("You have successfully placed a preorder.")
                            .setAutoCancel(true)
                            .build();
                    notificationManager.notify(1, notification);
                }
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