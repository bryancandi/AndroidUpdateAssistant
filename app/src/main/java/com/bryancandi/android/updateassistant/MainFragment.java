package com.bryancandi.android.updateassistant;

import android.content.Intent;
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
import androidx.navigation.fragment.NavHostFragment;

import com.bryancandi.android.updateassistant.databinding.FragmentMainBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainFragment extends Fragment {

    CardView versionCard;
    CardView securityCard;
    TextView version;
    TextView apiLevel;
    TextView securityUpdate;

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
        securityUpdate = view.findViewById(R.id.securityUpdateTV);
        securityUpdate.setText(securityUpdate());

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

    }

    private String androidVersion() {
        return Build.VERSION.RELEASE;
    }

    private String androidAPILevel() {
        return String.valueOf(Build.VERSION.SDK_INT);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}