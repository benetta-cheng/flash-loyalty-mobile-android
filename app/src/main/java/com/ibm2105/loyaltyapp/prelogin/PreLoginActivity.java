package com.ibm2105.loyaltyapp.prelogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.widget.Toast;

import com.ibm2105.loyaltyapp.R;

public class PreLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        PreLoginViewModel viewModel = new ViewModelProvider(this).get(PreLoginViewModel.class);
        viewModel.getStatus().observe(this, status -> {
            if (status != null) {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show();

                if (status.equals(R.string.login_successful)) {
                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_prelogin);
                    navHostFragment.getNavController().navigate(R.id.mainActivity);

                    // To prevent getting back to the pre-login screen from pressing back after logging in
                    finish();
                }
            }
        });
    }
}