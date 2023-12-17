package com.bryancandi.android.updateassistant;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.gms.common.GoogleApiAvailability;

import com.bryancandi.android.updateassistant.databinding.FragmentMainBinding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainFragment extends Fragment {

    CardView versionCard;
    CardView securityCard;
    CardView gPlayUpdateCard;
    CardView gmsUpdatedCard;
    CardView playStoreUpdatedCard;
    TextView version;
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
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        version = view.findViewById(R.id.versionTV);
        version.setText(getString(R.string.android_version) + " " + androidVersion());
        apiLevel = view.findViewById(R.id.apiLevelTV);
        apiLevel.setText(getString(R.string.android_api_level) + " " + androidAPILevel());
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
        versionCard.setOnClickListener(v -> {
            Toast.makeText(getActivity(), R.string.android_version_toast,
                    Toast.LENGTH_SHORT).show();
        });

        securityCard = view.findViewById(R.id.security_card_view);
        securityCard.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("com.google.android.gms.update.SystemUpdateActivity");
                intent.setClassName("com.google.android.gms", "com.google.android.gms.update.SystemUpdateActivity");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.not_available,
                        Toast.LENGTH_LONG).show();
            }
        });

        gPlayUpdateCard = view.findViewById(R.id.google_play_update_card_view);
        gPlayUpdateCard.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("com.google.android.finsky.systemupdateactivity.SystemUpdateActivity");
                intent.setClassName("com.android.vending", "com.google.android.finsky.systemupdateactivity.SystemUpdateActivity");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.not_available,
                        Toast.LENGTH_LONG).show();
            }
        });

        gmsUpdatedCard = view.findViewById(R.id.gms_card_view);
        gmsUpdatedCard.setOnClickListener(v -> {
                String url = "https://play.google.com/store/apps/details?id=com.google.android.gms";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
        });

        playStoreUpdatedCard = view.findViewById(R.id.play_store_card_view);
        playStoreUpdatedCard.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("com.google.android.finsky.activities.MainActivity");
                intent.setClassName("com.android.vending", "com.google.android.finsky.activities.MainActivity");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.not_available,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private String androidVersion() {
        return Build.VERSION.RELEASE;
    }

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
        } catch (java.io.IOException e) {
            buildNumber.setVisibility(View.GONE);
        }
        return build;
    }

    private String securityUpdate() {
        String patchDate = Build.VERSION.SECURITY_PATCH;
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        Date newPatchDate = null;
        try {
            newPatchDate = spf.parse(patchDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String newPatchDateString;
        if (newPatchDate != null) {
            newPatchDateString = dateFormat.format(newPatchDate);
            return newPatchDateString;
        } else {
            return Build.VERSION.SECURITY_PATCH;
        }
    }

    private String gPlayUpdate() {
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        Date newPatchDate = null;
        try {
            String patchDate = requireActivity().getPackageManager().getPackageInfo("com.google.android.modulemetadata", 0).versionName;
            newPatchDate = spf.parse(patchDate);
        } catch (ParseException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String newPatchDateString;
        if (newPatchDate != null) {
            newPatchDateString = dateFormat.format(newPatchDate);
            return newPatchDateString;
        } else {
            return getString(R.string.not_available);
        }
    }

    private String gmsVersion() {
        try {
            return requireActivity().getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0 ).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return getString(R.string.not_available);
    }

    private String gmsUpdated() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm z");
        try {
            long lastUpdate = requireActivity().getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0 ).lastUpdateTime;
            return dateFormat.format( new Date( lastUpdate ) );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return getString(R.string.not_available);
    }

    private String playStoreVersion() {
        try {
            return requireActivity().getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_STORE_PACKAGE, 0 ).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return getString(R.string.not_available);
    }

    private String playStoreUpdated() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm z");
        try {
            long v = requireActivity().getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_STORE_PACKAGE, 0).lastUpdateTime;
            return dateFormat.format( new Date( v ) );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return getString(R.string.not_available);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}