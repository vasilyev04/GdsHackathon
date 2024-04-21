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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vasilyev.gdshackathon.R;
import com.vasilyev.gdshackathon.databinding.BottomSheetInfoBinding;

public class BottomSheetInfoFragment extends BottomSheetDialogFragment {
     private BottomSheetInfoBinding binding;

     public BottomSheetInfoFragment() {
     }

     @Override
     public void onCreate(@Nullable Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
     }

     @Nullable
     @Override
     public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          binding = BottomSheetInfoBinding.inflate(inflater, container, false);
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
