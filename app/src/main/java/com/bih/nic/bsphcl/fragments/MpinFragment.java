package com.bih.nic.bsphcl.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bih.nic.bsphcl.setraapp.MainActivity;
import com.bih.nic.bsphcl.setraapp.R;

import in.arjsna.passcodeview.PassCodeView;

/**
 * Created by NIC2 on 4/27/2018.
 */

public class MpinFragment extends Fragment {
    private final String PASSCODE = "1234";
    private PassCodeView passCodeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_mpin, container, false);
        passCodeView = (PassCodeView) mRootView.findViewById(R.id.pass_code_view);
        TextView promptView = (TextView) mRootView.findViewById(R.id.promptview);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "font/Font-Bold.ttf");
        passCodeView.setTypeFace(typeFace);
        passCodeView.setKeyTextColor(R.color.black_shade);
        passCodeView.setEmptyDrawable(R.drawable.empty_dot);
        passCodeView.setFilledDrawable(R.drawable.filled_dot);
        promptView.setTypeface(typeFace);
        bindEvents();
        return mRootView;
    }

    private void bindEvents() {
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override public void onTextChanged(String text) {
                if (text.length() == 4) {
                    if (text.equals(PASSCODE)) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        passCodeView.setError(true);
                    }
                }
            }
        });
    }


}
