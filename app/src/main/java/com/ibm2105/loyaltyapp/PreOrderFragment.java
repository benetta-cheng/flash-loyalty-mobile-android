package com.ibm2105.loyaltyapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        PreOrderListData[] preOrderListDataArray = new PreOrderListData[]{
                new PreOrderListData(R.drawable.ic_launcher_background, 0, 10, "ABC"),
                new PreOrderListData(R.drawable.ic_launcher_background, 1, 8, "ABE"),
                new PreOrderListData(R.drawable.ic_launcher_background, 2, 76, "JSF"),
                new PreOrderListData(R.drawable.ic_launcher_background, 3, 12, "ASF")
        };
        RecyclerView preOrderRecycler = view.findViewById(R.id.preOrderRecycler);
        PreOrderListAdapter preOrderListAdapter = new PreOrderListAdapter(preOrderListDataArray);
        preOrderRecycler.setHasFixedSize(true);
        preOrderRecycler.setLayoutManager(new GridLayoutManager(requireContext(),2));
        preOrderRecycler.setAdapter(preOrderListAdapter);

        Button checkoutButton = view.findViewById(R.id.buttonCheckout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_pre_order_confirmation);
            }
        });
    }
}