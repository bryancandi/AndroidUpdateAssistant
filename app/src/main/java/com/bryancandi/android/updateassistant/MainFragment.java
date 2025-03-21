package com.bryancandi.android.updateassistant;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bryancandi.android.updateassistant.databinding.FragmentMainBinding;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainFragment extends Fragment {

    CardView versionCard;
    CardView securityCard;
    CardView gPlayUpdateCard;
    CardView gmsUpdatedCard;
    CardView playStoreUpdatedCard;
    TextView version;
    TextView codename;
    TextView apiLevel;
    TextView buildNumber;
    TextView securityUpdate;
    TextView gPlayUpdate;
    TextView gmsVersion;
    TextView gmsUpdated;
    TextView playStoreVersion;
    TextView playStoreUpdated;

    private FragmentMainBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        version = view.findViewById(R.id.versionTV);
        version.setText(getString(R.string.android_version, androidVersion()));
        codename = view.findViewById(R.id.codenameTV);
        if (Objects.equals(Build.VERSION.CODENAME, "REL")) {
            codename.setVisibility(View.GONE);
        } else {
            codename.setText(getString(R.string.android_codename, Build.VERSION.CODENAME));
        }
        apiLevel = view.findViewById(R.id.apiLevelTV);
        apiLevel.setText(getString(R.string.android_api_level, androidAPILevel()));
        buildNumber = view.findViewById(R.id.buildNumberTV);
        buildNumber.setText(buildNumber());
        securityUpdate = view.findViewById(R.id.securityUpdateTV);
        securityUpdate.setText(securityUpdate());
        gPlayUpdate = view.findViewById(R.id.gPlayUpdateTV);
        gPlayUpdate.setText(gPlayUpdate());
        gmsVersion = view.findViewById(R.id.gmsVersionTV);
        gmsVersion.setText(gmsVersion());
        gmsUpdated = view.findViewById(R.id.gmsUpdateTV);
        gmsUpdated.setText(gmsUpdated());
        playStoreVersion = view.findViewById(R.id.playStoreVersionTV);
        playStoreVersion.setText(playStoreVersion());
        playStoreUpdated = view.findViewById(R.id.playStoreUpdateTV);
        playStoreUpdated.setText(playStoreUpdated());
        versionCard = view.findViewById(R.id.version_card_view);
        versionCard.setOnClickListener(v -> Toast.makeText(getActivity(), R.string.android_version_toast,
                Toast.LENGTH_SHORT).show());

        securityCard = view.findViewById(R.id.security_card_view);
        securityCard.setOnClickListener(v -> {
            try {
                Intent systemUpdateIntent = new Intent("com.google.android.gms.update.SystemUpdateActivity");
                systemUpdateIntent.setClassName("com.google.android.gms", "com.google.android.gms.update.SystemUpdateActivity");
                systemUpdateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(systemUpdateIntent);
            } catch (Exception e) {
                Log.e("systemUpdateIntent", "Android Security Update activity", e);
                Toast.makeText(getActivity(), R.string.not_available,
                        Toast.LENGTH_LONG).show();
            }
        });

        gPlayUpdateCard = view.findViewById(R.id.google_play_update_card_view);
        gPlayUpdateCard.setOnClickListener(v -> {
            try {
                Intent gPlaySystemUpdateIntent = new Intent("com.google.android.finsky.systemupdateactivity.SystemUpdateActivity");
                gPlaySystemUpdateIntent.setClassName("com.android.vending", "com.google.android.finsky.systemupdateactivity.SystemUpdateActivity");
                gPlaySystemUpdateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(gPlaySystemUpdateIntent);
            } catch (Exception e) {
                Log.e("gPlaySystemUpdateIntent", "Google Play System Update activity", e);
                Toast.makeText(getActivity(), R.string.not_available,
                        Toast.LENGTH_LONG).show();
            }
        });

        gmsUpdatedCard = view.findViewById(R.id.gms_card_view);
        gmsUpdatedCard.setOnClickListener(v -> {
            String marketUrl = "market://details?id=com.google.android.gms";
            String webUrl = "https://play.google.com/store/apps/details?id=com.google.android.gms";
            try {
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl));
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(marketIntent);
            } catch (Exception e) {
                Log.e("marketIntent", "Market app is not installed", e);
                try {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                    webIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(webIntent);
                } catch (Exception webE) {
                    Log.e("webIntent", "Unable to open GMS web link", e);
                    Toast.makeText(getActivity(), R.string.not_available,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        playStoreUpdatedCard = view.findViewById(R.id.play_store_card_view);
        playStoreUpdatedCard.setOnClickListener(v -> {
            try {
                Intent playStoreIntent = new Intent("com.google.android.finsky.activities.MainActivity");
                playStoreIntent.setClassName("com.android.vending", "com.google.android.finsky.activities.MainActivity");
                playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(playStoreIntent);
            } catch (Exception e) {
                Log.e("playStoreIntent", "Google Play Store activity", e);
                Toast.makeText(getActivity(), R.string.not_available,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String androidVersion() {
        return Build.VERSION.RELEASE;
    }

    @NonNull
    private String androidAPILevel() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    private String buildNumber() {
        String build = null;
        try {
            Process ifc = Runtime.getRuntime().exec("getprop " + "ro.build.display.id");
            BufferedReader bis = new BufferedReader(new InputStreamReader(ifc.getInputStream()));
            build = bis.readLine();
            ifc.destroy();
            bis.close();
        } catch (Exception e) {
            Log.e("Reader", "Unable to get property: \"ro.build.display.id\"", e);
            buildNumber.setVisibility(View.GONE);
        }
        return build;
    }

    private String securityUpdate() {
        String patchDate = Build.VERSION.SECURITY_PATCH;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date newPatchDate = null;
        try {
            newPatchDate = formatter.parse(patchDate);
        } catch (ParseException e) {
            Log.e("Build", "Security Patch date unavailable", e);
        }
        String newPatchDateString;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        if (newPatchDate != null) {
            newPatchDateString = dateFormat.format(newPatchDate);
            return newPatchDateString;
        } else {
            return Build.VERSION.SECURITY_PATCH;
        }
    }

    private String gPlayUpdate() {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM", Locale.US);
        Date newPatchDate = null;
        try {
            String patchDate = requireActivity().getPackageManager().getPackageInfo("com.google.android.modulemetadata", 0).versionName;
            assert patchDate != null;
            newPatchDate = formatter1.parse(patchDate);
        } catch (ParseException | PackageManager.NameNotFoundException e) {
            Log.e("GMS", "Google Play System Update date format yyyy-MM-dd unavailable", e);
        }
        try {
            String patchDate = requireActivity().getPackageManager().getPackageInfo("com.google.android.modulemetadata", 0).versionName;
            assert patchDate != null;
            newPatchDate = formatter2.parse(patchDate);
        } catch (ParseException | PackageManager.NameNotFoundException e) {
            Log.e("GMS", "Google Play System Update date format yyyy-MM unavailable", e);
        }
        String newPatchDateString;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        if (newPatchDate != null) {
            newPatchDateString = dateFormat.format(newPatchDate);
            return newPatchDateString;
        } else {
            return getString(R.string.not_available);
        }
    }

    private String gmsVersion() {
        try {
            return requireActivity().getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GMS", "Google Play Services unavailable", e);
        }
        return getString(R.string.not_available);
    }

    private String gmsUpdated() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy HH:mm z", Locale.US);
        try {
            long lastUpdate = requireActivity().getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0).lastUpdateTime;
            return formatter.format(new Date(lastUpdate));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GMS", "Google Play Services date unavailable", e);
        }
        return getString(R.string.not_available);
    }

    private String playStoreVersion() {
        try {
            return requireActivity().getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_STORE_PACKAGE, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("PlayStore", "Google Play Store unavailable", e);
        }
        return getString(R.string.not_available);
    }

    private String playStoreUpdated() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy HH:mm z", Locale.US);
        try {
            long v = requireActivity().getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_STORE_PACKAGE, 0).lastUpdateTime;
            return formatter.format(new Date(v));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("PlayStore", "Google Play Store date unavailable", e);
        }
        return getString(R.string.not_available);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}