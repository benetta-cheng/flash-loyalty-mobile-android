package com.ibm2105.loyaltyapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordDialogFragment extends DialogFragment {

    public ChangePasswordDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordDialogFragment newInstance(String param1, String param2) {
        ChangePasswordDialogFragment fragment = new ChangePasswordDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getParentFragment().getLayoutInflater();
        ProfileViewModel viewModel = new ViewModelProvider(getParentFragment()).get(ProfileViewModel.class);

        builder.setView(inflater.inflate(R.layout.fragment_change_password_dialog, null))
                .setTitle(R.string.change_password)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextInputLayout passwordTextInputLayout = dialog.findViewById(R.id.textInputPassword);
                        TextInputLayout confirmPasswordTextInputLayout = dialog.findViewById(R.id.textInputConfirmPassword);

                        String password = passwordTextInputLayout.getEditText().getText().toString();
                        String confirmPassword = confirmPasswordTextInputLayout.getEditText().getText().toString();
                        boolean valid = true;

                        if (password.isEmpty()) {
                            passwordTextInputLayout.setError(getString(R.string.required_input));
                            valid = false;
                        } else if (passwordTextInputLayout.isErrorEnabled()) {
                            passwordTextInputLayout.setErrorEnabled(false);
                        }

                        if (confirmPassword.isEmpty()) {
                            confirmPasswordTextInputLayout.setError(getString(R.string.required_input));
                            valid = false;
                        } else if (password.equals(confirmPassword)) {
                            if (confirmPasswordTextInputLayout.isErrorEnabled()) {
                                confirmPasswordTextInputLayout.setErrorEnabled(false);
                            }
                        } else {
                            confirmPasswordTextInputLayout.setError(getString(R.string.input_mismatch));
                            valid = false;
                        }

                        if (valid) {
                            viewModel.changePassword(password);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return dialog;
    }
}