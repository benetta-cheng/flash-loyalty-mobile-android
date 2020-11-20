package com.ibm2105.loyaltyapp.prelogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ibm2105.loyaltyapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResetPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputLayout resetCodeTextInputLayout = view.findViewById(R.id.textInputResetCode);
        TextInputLayout passwordTextInputLayout = view.findViewById(R.id.textInputPassword);
        TextInputLayout confirmPasswordTextInputLayout = view.findViewById(R.id.textInputConfirmPassword);

        PreLoginViewModel viewModel = new ViewModelProvider(requireActivity()).get(PreLoginViewModel.class);

        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                if (status.equals(R.string.password_reset_successful)) {
                    Toast.makeText(getContext(), R.string.password_reset_successful, Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.login);
                } else if (status.equals(R.string.invalid_reset_code)) {
                    resetCodeTextInputLayout.setError(getString(R.string.invalid_reset_code));
                }
            }
        });

        Button changePasswordButton = view.findViewById(R.id.buttonChangePassword);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resetCode = resetCodeTextInputLayout.getEditText().getText().toString().trim();
                String password = passwordTextInputLayout.getEditText().getText().toString();
                String confirmPassword = confirmPasswordTextInputLayout.getEditText().getText().toString();
                boolean valid = true;

                if (resetCode.isEmpty()) {
                    resetCodeTextInputLayout.setError(getString(R.string.required_input));
                    valid = false;
                } else if (resetCodeTextInputLayout.isErrorEnabled()) {
                    resetCodeTextInputLayout.setErrorEnabled(false);
                }

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
                    viewModel.changePassword(getArguments().getString("username"), resetCode, password);
                }
            }
        });
    }
}