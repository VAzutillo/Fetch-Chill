// Generated by view binder compiler. Do not edit!
package com.example.fetchchill.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.fetchchill.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentAccountInformationBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final FrameLayout EditFrame;

  @NonNull
  public final ImageView backToSetting;

  @NonNull
  public final TextView emailTextView;

  private FragmentAccountInformationBinding(@NonNull FrameLayout rootView,
      @NonNull FrameLayout EditFrame, @NonNull ImageView backToSetting,
      @NonNull TextView emailTextView) {
    this.rootView = rootView;
    this.EditFrame = EditFrame;
    this.backToSetting = backToSetting;
    this.emailTextView = emailTextView;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentAccountInformationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentAccountInformationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_account_information, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentAccountInformationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.EditFrame;
      FrameLayout EditFrame = ViewBindings.findChildViewById(rootView, id);
      if (EditFrame == null) {
        break missingId;
      }

      id = R.id.backToSetting;
      ImageView backToSetting = ViewBindings.findChildViewById(rootView, id);
      if (backToSetting == null) {
        break missingId;
      }

      id = R.id.emailTextView;
      TextView emailTextView = ViewBindings.findChildViewById(rootView, id);
      if (emailTextView == null) {
        break missingId;
      }

      return new FragmentAccountInformationBinding((FrameLayout) rootView, EditFrame, backToSetting,
          emailTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
