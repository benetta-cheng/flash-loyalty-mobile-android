package com.ibm2105.loyaltyapp.news;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm2105.loyaltyapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullNewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FullNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullNewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FullNewsFragment newInstance(String param1, String param2) {
        FullNewsFragment fragment = new FullNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView textViewNewsTitle;
    ImageView imageViewNewsPicture;
    TextView textViewFullDescription;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewNewsTitle = view.findViewById(R.id.textViewNewsTitle);
        textViewNewsTitle.setText("Celebrate your birthday with us!");

        imageViewNewsPicture = view.findViewById(R.id.imageViewNewsPicture);
        imageViewNewsPicture.setImageResource(R.drawable.img_brownies);

        textViewFullDescription = view.findViewById(R.id.textViewNewsDescription);
        textViewFullDescription.setText("Come claim a free cuboid brownie if your birthday is on July! \n\nJust present any form of member identification to" +
                "one of our friendly staff member and they will present you with your free birthday brownie. \n\nYou may select any brownie flavour from our diverse options! \n\n" +
                "*Offer lasts till the end of your birthday month.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_news, container, false);
    }
}