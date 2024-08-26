package com.example.multichiptextlayoutlibrary;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multichipcomboboxlibrary.MultiChipTextLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private MultiChipTextLayout main_multi_chip_below, main_multi_chip_inline, main_multi_chip_combobox, main_multi_chip_single;
    private MaterialButton main_BTN_showTags;
    private MaterialTextView main_txt_selectedTags_below, main_txt_selectedTags_inline, main_txt_selectedTags_combobox, main_txt_selectedTags_single;
    private MaterialButton main_BTN_clearTags;
    private boolean showTags = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        findViews();

        main_BTN_showTags.setOnClickListener(v -> {
            showTags = true;
            main_txt_selectedTags_below.setText(main_multi_chip_below.getChipsArray().toString());
            main_txt_selectedTags_inline.setText(main_multi_chip_inline.getChipsArray().toString());
            main_txt_selectedTags_combobox.setText(main_multi_chip_combobox.getChipsArray().toString());
            main_txt_selectedTags_single.setText(main_multi_chip_single.getChipsArray().toString());

            main_txt_selectedTags_below.setVisibility(View.VISIBLE);
            main_txt_selectedTags_inline.setVisibility(View.VISIBLE);
            main_txt_selectedTags_combobox.setVisibility(View.VISIBLE);
            main_txt_selectedTags_single.setVisibility(View.VISIBLE);

        });

        main_BTN_clearTags.setOnClickListener(v -> {
            main_multi_chip_below.clearChips();
            main_multi_chip_inline.clearChips();
            main_multi_chip_combobox.clearChips();
            main_multi_chip_single.clearChips();
            showTags = false;

            main_txt_selectedTags_below.setVisibility(View.GONE);
            main_txt_selectedTags_inline.setVisibility(View.GONE);
            main_txt_selectedTags_combobox.setVisibility(View.GONE);
            main_txt_selectedTags_single.setVisibility(View.GONE);
        });

        setListenersForMultiChipLayouts();

        List<String> items = Arrays.asList(
                "Mercury",
                "Venus",
                "Earth",
                "Mars",
                "Jupiter",
                "Saturn",
                "Uranus",
                "Neptune",
                "Pluto"
        );
        main_multi_chip_combobox.setDropdownItems(items);
        main_multi_chip_single.setDropdownItems(items);

    }

    private void findViews() {
        main_multi_chip_below = findViewById(R.id.main_multi_chip_below);
        main_multi_chip_inline = findViewById(R.id.main_multi_chip_inline);
        main_multi_chip_combobox = findViewById(R.id.main_multi_chip_combobox);
        main_multi_chip_single = findViewById(R.id.main_multi_chip_single);
        main_BTN_showTags = findViewById(R.id.main_BTN_showTags);
        main_BTN_clearTags = findViewById(R.id.main_BTN_clearTags);
        main_txt_selectedTags_below = findViewById(R.id.main_txt_selectedTags_below);
        main_txt_selectedTags_inline = findViewById(R.id.main_txt_selectedTags_inline);
        main_txt_selectedTags_combobox = findViewById(R.id.main_txt_selectedTags_combobox);
        main_txt_selectedTags_single = findViewById(R.id.main_txt_selectedTags_single);
    }

    private void setListenersForMultiChipLayouts(){

        main_multi_chip_below.setTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(showTags)
                    main_txt_selectedTags_below.setText(main_multi_chip_below.getChipsArray().toString());
            }
        });

        main_multi_chip_below.setChipOnCloseClickListener(v -> {
            if(showTags)
                main_txt_selectedTags_below.setText(main_multi_chip_below.getChipsArray().toString());
        });


        main_multi_chip_inline.setTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(showTags)
                    main_txt_selectedTags_inline.setText(main_multi_chip_inline.getChipsArray().toString());
            }
        });

        main_multi_chip_inline.setChipOnCloseClickListener(v -> {
            if(showTags)
                main_txt_selectedTags_inline.setText(main_multi_chip_inline.getChipsArray().toString());
        });


        main_multi_chip_combobox.setTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(showTags)
                    main_txt_selectedTags_combobox.setText(main_multi_chip_combobox.getChipsArray().toString());
            }
        });

        main_multi_chip_combobox.setChipOnCloseClickListener(v -> {
            if(showTags)
                main_txt_selectedTags_combobox.setText(main_multi_chip_combobox.getChipsArray().toString());
        });

        main_multi_chip_single.setTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(showTags)
                    main_txt_selectedTags_single.setText(main_multi_chip_single.getChipsArray().toString());
            }
        });

        main_multi_chip_single.setChipOnCloseClickListener(v -> {
            if(showTags)
                main_txt_selectedTags_single.setText(main_multi_chip_single.getChipsArray().toString());
        });
    }

}