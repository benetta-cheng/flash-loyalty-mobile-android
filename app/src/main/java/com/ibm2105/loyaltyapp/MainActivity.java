package com.ibm2105.loyaltyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;
import com.ibm2105.loyaltyapp.notifications.NotificationsDialogFragment;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Force open the database to allow pre-populating of data
        LoyaltyDatabase.getDatabase(this).getOpenHelper().getWritableDatabase();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        drawerLayout = findViewById(R.id.drawer_layout);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_news, R.id.nav_pre_order, R.id.nav_branch, R.id.nav_points, R.id.nav_profile).setOpenableLayout(drawerLayout).build();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navigationView);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        // Set the title for the first time as the title is not set according to the fragment until there is a fragment change/selection
        getSupportActionBar().setTitle(navController.getCurrentDestination().getLabel());

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getAccountLiveData().observe(this, account -> {
            ImageView profileImageView = findViewById(R.id.imageViewUser);
            TextView usernameTextView = findViewById(R.id.textViewUsername);

            usernameTextView.setText(account.getUsername());
            if (account.getImage() != null) {
                byte[] decodedString = Base64.decode(account.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profileImageView.setImageBitmap(bitmap);
            }
        });

        viewModel.getStatus().observe(this, status -> {
            if (status != null) {
                if (status.equals(R.string.logout_successful)) {
                    Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
                    navHostFragment.getNavController().navigate(R.id.preLoginActivity);

                    // To prevent getting back to the post-login screen from pressing back after logging in
                    finish();
                }
            }
        });

        Button logoutButton = navigationView.getHeaderView(0).findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.logout();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_action_notification) {
            // Add code to display notification dialog
            openDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openDialog() {
        NotificationsDialogFragment notificationsDialogFragment = new NotificationsDialogFragment();
        notificationsDialogFragment.show(getSupportFragmentManager(), "Example");
    }
}