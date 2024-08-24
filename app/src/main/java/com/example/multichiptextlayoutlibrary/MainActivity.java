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

public class MainActivity extends AppCompatActivity {
    private MultiChipTextLayout main_multi_chip;
    private MaterialButton main_BTN_showTags;
    private MaterialTextView main_txt_selectedTags;
    private MaterialButton main_BTN_clearTags;
    private boolean showTags = false;

//    private AppCompatMultiAutoCompleteTextView main_multi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        findViews();

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
        main_multi_chip.setDropdownItems(items);


//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
//        main_multi.setAdapter(adapter);
//        main_multi.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        main_multi_chip.setTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(showTags)
                    main_txt_selectedTags.setText(main_multi_chip.getChipsArray().toString());
            }
        });

        main_multi_chip.setChipOnCloseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showTags)
                    main_txt_selectedTags.setText(main_multi_chip.getChipsArray().toString());
            }
        });

        main_BTN_showTags.setOnClickListener(v -> {
            showTags = true;
            main_txt_selectedTags.setText(main_multi_chip.getChipsArray().toString());
        });

        main_BTN_clearTags.setOnClickListener(v -> {
            main_multi_chip.clearChips();
            main_txt_selectedTags.setText("");
            showTags = false;
        });


    }

    private void findViews() {
        main_multi_chip = findViewById(R.id.main_multi_chip);
        main_BTN_showTags = findViewById(R.id.main_BTN_showTags);
        main_txt_selectedTags = findViewById(R.id.main_txt_selectedTags);
        main_BTN_clearTags = findViewById(R.id.main_BTN_clearTags);
//        main_multi = findViewById(R.id.main_multi);
    }

}