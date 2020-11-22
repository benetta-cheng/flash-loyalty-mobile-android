package com.ibm2105.loyaltyapp;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
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

import com.ibm2105.loyaltyapp.database.Account;
import com.ibm2105.loyaltyapp.database.Code;
import com.ibm2105.loyaltyapp.database.Reward;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class PointsFragment extends Fragment {

    //ADDED
    List<Reward> rewardList = new ArrayList<>();
    PointsViewModel pointsViewModel;
    PointsListAdapter pointsListAdapter;
    TextView tvtotalPoints;

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

        pointsViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                if (status.equals(R.string.reward_successful)) {
                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                    String id = "Channel 1";
                    String description = "Rewards";
                    int importance=NotificationManager.IMPORTANCE_LOW;

                    if (Build.VERSION.SDK_INT >= 26){
                        NotificationChannel channel = new NotificationChannel(id, description,importance);
                        notificationManager.createNotificationChannel(channel);

                        Notification notification = new Notification.Builder(getContext(),id)
                                .setCategory(Notification.CATEGORY_MESSAGE)
                                .setSmallIcon(R.drawable.ic_logo)
                                .setContentTitle("Reward redeemed!")
                                .setContentText("You have successfully redeemed a reward!")
                                .setAutoCancel(true)
                                .build();
                        notificationManager.notify(1,notification);
                    }
                   else {
                        Notification notification = new Notification.Builder(getContext())
                                .setSmallIcon(R.drawable.ic_logo)
                                .setContentTitle("Reward redeemed!")
                                .setContentText("You have successfully redeemed a reward!")
                                .setAutoCancel(true)
                                .build();
                        notificationManager.notify(1, notification);
                    }
                }
                else if (status.equals(R.string.reward_fail)) {
                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                }
                else if (status.equals(R.string.code_successful)) {
                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                    String id = "Channel 1";
                    String description = "Code";
                    int importance=NotificationManager.IMPORTANCE_LOW;

                    if (Build.VERSION.SDK_INT >= 26){
                        NotificationChannel channel = new NotificationChannel(id, description,importance);
                        notificationManager.createNotificationChannel(channel);

                        Notification notification = new Notification.Builder(getContext(),id)
                                .setCategory(Notification.CATEGORY_MESSAGE)
                                .setSmallIcon(R.drawable.ic_logo)
                                .setContentTitle("Code redeemed!")
                                .setContentText("You have successfully redeemed a code!")
                                .setAutoCancel(true)
                                .build();
                        notificationManager.notify(1,notification);
                    }
                    else {
                        Notification notification = new Notification.Builder(getContext())
                                .setSmallIcon(R.drawable.ic_logo)
                                .setContentTitle("Code redeemed!")
                                .setContentText("You have successfully redeemed a code!")
                                .setAutoCancel(true)
                                .build();
                        notificationManager.notify(1, notification);
                    }
                }
                else if (status.equals(R.string.code_fail)) {
                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Display total points
        pointsViewModel.getAccount().observe(getViewLifecycleOwner(), new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                tvtotalPoints= view.findViewById(R.id.pointsNumber);
                tvtotalPoints.setText(account.getTotalPoints() + " points");
            }
        });

        RecyclerView recyclerView=(RecyclerView)getView().findViewById(R.id.points_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));

        final PointsListAdapter pointsListAdapter=new PointsListAdapter(pointsViewModel);
        recyclerView.setAdapter(pointsListAdapter);

        pointsViewModel.getAllRewards().observe(getViewLifecycleOwner(), new Observer<List<Reward>>() {
            @Override
            public void onChanged(List<Reward> rewards) {
                pointsListAdapter.setListReward(rewards);
                pointsListAdapter.notifyDataSetChanged();
            }
        });

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
                redeemDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                redeemDialog.show();
                ImageView  redeemButtonDialogClose=(ImageView)redeemDialog.findViewById(R.id.closeIconButton);
                Button  redeemButtonSubmit=(Button)redeemDialog.findViewById(R.id.buttonRedeemSubmit);
                EditText redeemInput=(EditText)redeemDialog.findViewById(R.id.redeemCodeInput);

                //ENTER CODE AND SUBMIT
                redeemButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int code = Integer.parseInt(redeemInput.getText().toString());
                        pointsViewModel.redeemCode(code);
                        redeemDialog.dismiss();
                    }
                });

                //CLOSE DIALOG BUTTON
                redeemButtonDialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redeemDialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pointsViewModel=new ViewModelProvider(this).get(PointsViewModel.class);
    }
}