package com.yandex.mandrik.launcher.welcomeactivity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.yandex.mandrik.launcher.R;

public class WelcomeViewPageFragment extends Fragment {

    private int page;

    public static WelcomeViewPageFragment newInstance(int page) {
        WelcomeViewPageFragment fragmentPage = new WelcomeViewPageFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);

        fragmentPage.setArguments(args);
        return fragmentPage;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView;
        switch(page) {
            case 0:
                rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_wel_first_page, container, false);
                break;
            case 1:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.fragment_wel_second_page, container, false);
                break;
            case 2:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.fragment_wel_third_page, container, false);
                break;
            case 3:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.fragment_wel_fourth_page, container, false);
                setClickAdapterSize(rootView);
                break;
            case 4:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.fragment_wel_fifth_page, container, false);
                setClickAdapterTheme(rootView);
                break;
            default:
                rootView = null;
        }
        rootView.setTag("rootView_" + page);
        return rootView;
    }

    private void setClickAdapterSize(ViewGroup rootView) {
        final View size4x6 = rootView.findViewById(R.id.image_chooser_size_4x6);
        final View size5x7 = rootView.findViewById(R.id.image_chooser_size_5x7);
        final CheckBox chkBoxSize4x6 = (CheckBox) rootView.findViewById(R.id.check_box_size_4x6);
        final CheckBox chkBoxSize5x7 = (CheckBox) rootView.findViewById(R.id.check_box_size_5x7);
        size4x6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chkBoxSize4x6.isChecked()) {
                    chkBoxSize4x6.setVisibility(View.VISIBLE);
                    chkBoxSize4x6.setChecked(true);
                    chkBoxSize5x7.setVisibility(View.GONE);
                    chkBoxSize5x7.setChecked(false);
                }
            }
        });

        size5x7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chkBoxSize5x7.isChecked()) {
                    chkBoxSize5x7.setVisibility(View.VISIBLE);
                    chkBoxSize5x7.setChecked(true);
                    chkBoxSize4x6.setVisibility(View.GONE);
                    chkBoxSize4x6.setChecked(false);

                }
            }
        });
    }

    private void setClickAdapterTheme(ViewGroup rootView) {
        final View lightTheme = rootView.findViewById(R.id.image_chooser_light_theme);
        final View darkTheme = rootView.findViewById(R.id.image_chooser_dark_theme);
        final CheckBox chkBoxLightTheme = (CheckBox) rootView.findViewById(R.id.check_box_light_theme);
        final CheckBox chkBoxDarkTheme = (CheckBox) rootView.findViewById(R.id.check_box_dark_theme);
        lightTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chkBoxLightTheme.isChecked()) {
                    chkBoxLightTheme.setVisibility(View.VISIBLE);
                    chkBoxLightTheme.setChecked(true);
                    chkBoxDarkTheme.setVisibility(View.GONE);
                    chkBoxDarkTheme.setChecked(false);

                }
            }
        });

        darkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chkBoxDarkTheme.isChecked()) {
                    chkBoxDarkTheme.setVisibility(View.VISIBLE);
                    chkBoxDarkTheme.setChecked(true);
                    chkBoxLightTheme.setVisibility(View.GONE);
                    chkBoxLightTheme.setChecked(false);

                }
            }
        });
    }
}
