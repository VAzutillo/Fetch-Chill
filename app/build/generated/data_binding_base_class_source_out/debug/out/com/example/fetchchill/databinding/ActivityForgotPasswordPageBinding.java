// Generated by view binder compiler. Do not edit!
package com.example.fetchchill.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.fetchchill.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityForgotPasswordPageBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton Forgotpassbtn;

  @NonNull
  public final ImageView backToLoginPage;

  @NonNull
  public final TextInputEditText confirmPasswordTxt;

  @NonNull
  public final TextInputEditText emailTxt;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextInputEditText passwordTxt;

  @NonNull
  public final TextInputLayout textInputLayout2;

  @NonNull
  public final TextInputLayout textInputLayout8;

  private ActivityForgotPasswordPageBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialButton Forgotpassbtn, @NonNull ImageView backToLoginPage,
      @NonNull TextInputEditText confirmPasswordTxt, @NonNull TextInputEditText emailTxt,
      @NonNull ConstraintLayout main, @NonNull TextInputEditText passwordTxt,
      @NonNull TextInputLayout textInputLayout2, @NonNull TextInputLayout textInputLayout8) {
    this.rootView = rootView;
    this.Forgotpassbtn = Forgotpassbtn;
    this.backToLoginPage = backToLoginPage;
    this.confirmPasswordTxt = confirmPasswordTxt;
    this.emailTxt = emailTxt;
    this.main = main;
    this.passwordTxt = passwordTxt;
    this.textInputLayout2 = textInputLayout2;
    this.textInputLayout8 = textInputLayout8;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityForgotPasswordPageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityForgotPasswordPageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_forgot_password_page, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityForgotPasswordPageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Forgotpassbtn;
      MaterialButton Forgotpassbtn = ViewBindings.findChildViewById(rootView, id);
      if (Forgotpassbtn == null) {
        break missingId;
      }

      id = R.id.backToLoginPage;
      ImageView backToLoginPage = ViewBindings.findChildViewById(rootView, id);
      if (backToLoginPage == null) {
        break missingId;
      }

      id = R.id.confirmPasswordTxt;
      TextInputEditText confirmPasswordTxt = ViewBindings.findChildViewById(rootView, id);
      if (confirmPasswordTxt == null) {
        break missingId;
      }

      id = R.id.emailTxt;
      TextInputEditText emailTxt = ViewBindings.findChildViewById(rootView, id);
      if (emailTxt == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.passwordTxt;
      TextInputEditText passwordTxt = ViewBindings.findChildViewById(rootView, id);
      if (passwordTxt == null) {
        break missingId;
      }

      id = R.id.textInputLayout2;
      TextInputLayout textInputLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (textInputLayout2 == null) {
        break missingId;
      }

      id = R.id.textInputLayout8;
      TextInputLayout textInputLayout8 = ViewBindings.findChildViewById(rootView, id);
      if (textInputLayout8 == null) {
        break missingId;
      }

      return new ActivityForgotPasswordPageBinding((ConstraintLayout) rootView, Forgotpassbtn,
          backToLoginPage, confirmPasswordTxt, emailTxt, main, passwordTxt, textInputLayout2,
          textInputLayout8);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
