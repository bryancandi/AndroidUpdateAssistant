package com.bryancandi.android.updateassistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bryancandi.android.updateassistant.databinding.FragmentMainBinding;

import java.util.Objects;

public class MainFragment extends Fragment {

    CardView securityCard;

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

        securityCard = view.findViewById(R.id.security_card_view);
        securityCard.setOnClickListener(v -> {
            Toast.makeText(requireActivity(), "Button was Clicked", Toast.LENGTH_LONG).show();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}