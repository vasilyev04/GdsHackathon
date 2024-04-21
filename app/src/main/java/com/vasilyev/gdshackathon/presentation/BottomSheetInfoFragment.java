package com.vasilyev.gdshackathon.presentation;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vasilyev.gdshackathon.R;
import com.vasilyev.gdshackathon.databinding.BottomSheetInfoBinding;
import com.vasilyev.gdshackathon.domain.entity.Place;
import com.vasilyev.gdshackathon.presentation.main.MainState;
import com.vasilyev.gdshackathon.presentation.main.MainViewModel;
import com.vasilyev.gdshackathon.presentation.main.MainViewModelFactory;
import com.vasilyev.gdshackathon.presentation.map.MapActivity;

public class BottomSheetInfoFragment extends BottomSheetDialogFragment {
     private BottomSheetInfoBinding binding;
     private MainViewModel viewModel;

     private Place place;


     private void initView(Place place) {

          binding.titleNameTv.setText(place.getName());
          binding.addressTv.setText(place.getAddress());
          binding.scheduleTv.setText(place.getWorkSchedule());
          binding.descriptionTv.setText(place.getDescription());
          binding.contactsContactTv.setText(place.getContacts());

          Glide.with(requireContext()).load(place.getImage()).into(binding.ivInfo);
     }

     public BottomSheetInfoFragment() {
     }

     @Override
     public void onCreate(@Nullable Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          viewModel = new ViewModelProvider(this, new MainViewModelFactory(requireActivity().getApplication())).get(MainViewModel.class);

          int placeId = getArguments().getInt("EXTRA_PLACE");
          viewModel.getPlace(placeId);

          viewModel.getMainState().observe(this, mainState -> {
               if (mainState instanceof MainState.PlaceReceived){
                    place = ((MainState.PlaceReceived) mainState).getPlace();
                    initView(place);
               }
          });

          setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
     }

     @Nullable
     @Override
     public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          binding = BottomSheetInfoBinding.inflate(inflater, container, false);
          binding.findRouteButton.setOnClickListener(view ->{
               requireActivity().startActivity(MapActivity.Companion.newIntent(requireContext(), place.getId()));
          });
          return binding.getRoot();
     }

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
          Dialog dialog = super.onCreateDialog(savedInstanceState);
          if (dialog.getWindow() != null) {
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          }
          return dialog;
     }
}
