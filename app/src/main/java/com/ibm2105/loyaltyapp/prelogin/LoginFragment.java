package com.ibm2105.loyaltyapp.prelogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ibm2105.loyaltyapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    PreLoginViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment login.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(PreLoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputLayout usernameTextInputLayout = view.findViewById(R.id.textInputUsername);
        TextInputLayout passwordTextInputLayout = view.findViewById(R.id.textInputPassword);

        Button signInButton = view.findViewById(R.id.buttonSignIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTextInputLayout.getEditText().getText().toString().trim();
                String password = passwordTextInputLayout.getEditText().getText().toString();
                boolean empty = false;

                if (username.isEmpty()) {
                    usernameTextInputLayout.setError(getString(R.string.required_input));
                    empty = true;
                } else if (usernameTextInputLayout.isErrorEnabled()) {
                    usernameTextInputLayout.setErrorEnabled(false);
                }

                if (password.isEmpty()) {
                    passwordTextInputLayout.setError(getString(R.string.required_input));
                    empty = true;
                } else if (passwordTextInputLayout.isErrorEnabled()) {
                    passwordTextInputLayout.setErrorEnabled(false);
                }

                if (!empty) {
                    viewModel.login(username, password);
                }
            }
        });

        Button signUpButton = view.findViewById(R.id.buttonSignUp);
        signUpButton.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.signupFragment));

        Button forgotPasswordButton = view.findViewById(R.id.buttonForgotPassword);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTextInputLayout.getEditText().getText().toString().trim();

                if (username.isEmpty()) {
                    usernameTextInputLayout.setError(getString(R.string.required_input));
                } else {
                    if (usernameTextInputLayout.isErrorEnabled()) {
                        usernameTextInputLayout.setErrorEnabled(false);
                    }

                    Toast.makeText(getContext(), R.string.forget_password_message, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    Navigation.findNavController(view).navigate(R.id.resetPasswordFragment, bundle);
                }
            }
        });
    }
}