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
import android.widget.Button;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PreOrderFragment extends Fragment {

    public PreOrderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PreOrderFragment newInstance() {
        PreOrderFragment fragment = new PreOrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PreOrderViewModel viewModel = new ViewModelProvider(requireActivity()).get(PreOrderViewModel.class);

        RecyclerView preOrderRecycler = view.findViewById(R.id.preOrderRecycler);
        PreOrderListAdapter preOrderListAdapter = new PreOrderListAdapter(new ArrayList<PreOrderListData>());
        preOrderRecycler.setHasFixedSize(true);
        preOrderRecycler.setLayoutManager(new GridLayoutManager(requireContext(),2));
        preOrderRecycler.setAdapter(preOrderListAdapter);

        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            preOrderListAdapter.setListData(items);
            preOrderListAdapter.notifyDataSetChanged();
        });

        Button checkoutButton = view.findViewById(R.id.buttonCheckout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_pre_order_confirmation);
            }
        });
    }
}