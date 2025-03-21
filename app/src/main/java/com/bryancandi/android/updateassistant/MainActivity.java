package com.bryancandi.android.updateassistant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bryancandi.android.updateassistant.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this); // enables edge-to-edge on old android versions
        super.onCreate(savedInstanceState);

        com.bryancandi.android.updateassistant.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {

            Toast.makeText(getApplicationContext(), getString(R.string.action_refresh_toast),
                    Toast.LENGTH_SHORT).show();
            final Handler rHandler1 = new Handler(Looper.getMainLooper());
            rHandler1.postDelayed(() -> {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 200);
            final Handler rHandler2 = new Handler(Looper.getMainLooper());
            rHandler2.postDelayed(this::recreate, 400);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Toast.makeText(getApplicationContext(), getString(R.string.action_refresh_toast),
                    Toast.LENGTH_SHORT).show();
            recreate();
            return true;
        }
        if (id == R.id.action_apps) {
            Intent playStoreLink = new Intent(android.content.Intent.ACTION_VIEW);
            playStoreLink.setData(Uri.parse("https://play.google.com/store/apps/dev?id=5180384152101978531"));
            startActivity(playStoreLink);
            return true;
        }
        if (id == R.id.action_about) {
            Toast.makeText(getApplicationContext(), getString(R.string.app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_exit) {
            ActivityCompat.finishAffinity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}