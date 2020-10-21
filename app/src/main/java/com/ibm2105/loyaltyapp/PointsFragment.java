package com.ibm2105.loyaltyapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PointsFragment extends Fragment {
    public PointsFragment() {
    }

    public static PointsFragment newInstance() {
        PointsFragment fragment = new PointsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_points,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //REDEEM CODE DIALOG BUTTON TOGGLE
        final Dialog redeemDialog;
        final Button redeemButtonDialog;
        final ImageView redeemButtonDialogClose;

        redeemButtonDialog=(Button)view.findViewById(R.id.buttonRedeemCode);
        redeemDialog= new Dialog(view.getContext());

        redeemButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRedeemDialog();
            }
            public void ShowRedeemDialog(){
              redeemDialog.setContentView(R.layout.points_dialogbox);
              ImageView  redeemButtonDialogClose=(ImageView)redeemDialog.findViewById(R.id.closeIconButton);
              Button  redeemButtonSubmit=(Button)redeemButtonDialog.findViewById(R.id.buttonRedeemSubmit);
              TextView  redeemContent=(TextView)redeemButtonDialog.findViewById(R.id.redeemPointsContent);
              EditText redeemInput=(EditText)redeemButtonDialog.findViewById(R.id.redeemCodeInput);

                redeemButtonDialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redeemDialog.dismiss();
                    }
                });

                redeemDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                redeemDialog.show();
            }
        });



        //POINTS ITEM CODE
        final PointsListItem[]pointsListItems=new PointsListItem[]{
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_stars_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_account_circle_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_stars_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_account_circle_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_stars_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_account_circle_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_stars_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_account_circle_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_stars_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_account_circle_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_stars_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_account_circle_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_stars_24,100),
                new PointsListItem("Free Brownie!",R.drawable.ic_baseline_account_circle_24,100),

        };

        final RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.points_recyclerview);
        PointsListAdapter pointsListAdapter=new PointsListAdapter(pointsListItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(pointsListAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}