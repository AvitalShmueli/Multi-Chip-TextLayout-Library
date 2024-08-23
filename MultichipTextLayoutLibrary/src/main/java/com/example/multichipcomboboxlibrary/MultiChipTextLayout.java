package com.example.multichipcomboboxlibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class MultiChipTextLayout extends TextInputLayout {

    private TextInputLayout mc_textLayout;
    private ArrayList<String> arrTags;
    private ChipGroup mc_chipGroup_tags;
    private float textSize;
    private int ems;
    private int strokeColor, textColor, startIconTint;
    private String hintText;
    private Drawable startIconDrawable;
    private Drawable chipIconDrawable;
    private int chipIconTint;
    private float chipIconStartPadding;
    private boolean chipCloseEnabled, chipCheckable, chipClickable;
    private float chipTextSize;
    private float chipTextStartPadding, chipTextEndPadding, chipIconSize;
    private int chipPaddingLeft, chipPaddingRight, chipPaddingTop, chipPaddingBottom;
    private int chipStrokeColor, chipBackgroundColor, chipTextColor, chipCloseIconTint;
    private int chipStrokeWidth;
    private TextWatcher textWatcher;
    private OnClickListener chipCloseClickListener;
    private MaterialAutoCompleteTextView mc_autoCompleteTextView;
    private eChipsLayoutMode selectedChipsLayoutMode;
    private List<String> dropdownItems;

    public enum eChipsLayoutMode {BELOW, INLINE, COMBO_BOX}


    public MultiChipTextLayout(Context context) {
        this(context, null);
    }


    public MultiChipTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.multichip_layout, this, true);
        arrTags = new ArrayList<>();
        textWatcher = null;
        chipCloseClickListener = null;

        findViews(view);
        handelAttributes(attrs);

        switch (selectedChipsLayoutMode)
        {
            case BELOW:
                initLayoutBelow(context);
                break;
            case INLINE:
                initLayoutInline(context);
                break;
            case COMBO_BOX:
                initLayoutComboBox(context);
                break;

        }
    }


    private void initLayoutBelow(Context context){
        setEndIconMode(TextInputLayout.END_ICON_NONE);
        mc_autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mc_autoCompleteTextView.clearFocus();
                    String strTagValue = mc_autoCompleteTextView.getText().toString().trim();
                    addChip(strTagValue);
                }
                return false;
            }
        });

        if (textWatcher != null)
            mc_autoCompleteTextView.addTextChangedListener(textWatcher);
    }


    private void initLayoutInline(Context context){
        setEndIconMode(TextInputLayout.END_ICON_NONE);
    }


    private void initLayoutComboBox(Context context){
        dropdownItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, dropdownItems);
        mc_autoCompleteTextView.setAdapter(adapter);
        mc_textLayout.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);
        mc_autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            addChip(selectedItem);
            mc_autoCompleteTextView.clearFocus();
        });
        if (textWatcher != null)
            mc_autoCompleteTextView.addTextChangedListener(textWatcher);
    }


    public void setDropdownItems(List<String> items) {
        this.dropdownItems = items;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, dropdownItems);
        mc_autoCompleteTextView.setAdapter(adapter);
    }


    private void addChip(String text) {
        Chip chip = new Chip(getContext());
        if (!arrTags.contains(text)) {
            chip.setText(text);
            setChipStyle(getContext(), chip);
            mc_chipGroup_tags.addView(chip);
            arrTags.add(text);
        }
        mc_autoCompleteTextView.setText(""); // Clear the input after selection
    }


    private void findViews(View view) {
        mc_textLayout = view.findViewById(R.id.mc_textLayout);
        mc_autoCompleteTextView = findViewById(R.id.mc_autoCompleteTextView);
        mc_chipGroup_tags = view.findViewById(R.id.mc_chipGroup_tags);
    }


    public void setTextChangedListener(TextWatcher textChangedListener) {
        textWatcher = textChangedListener;
        if (textWatcher != null)
            mc_autoCompleteTextView.addTextChangedListener(textWatcher);
    }


    public void setChipOnCloseClickListener(OnClickListener listener) {
        chipCloseClickListener = listener;
    }


    private void handelAttributes(AttributeSet attrs) {
        try (TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiChipTextLayout, 0, 0)) {
            try {
                // text input attributes
                textSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_textSize, 14);
                ems = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_ems, 10);
                strokeColor = typedArray.getColor(R.styleable.MultiChipTextLayout_strokeColor, getContext().getColor(R.color.black));
                textColor = typedArray.getColor(R.styleable.MultiChipTextLayout_textColor, getContext().getColor(R.color.black));
                hintText = typedArray.getString(R.styleable.MultiChipTextLayout_hintText);
                startIconDrawable = typedArray.getDrawable(R.styleable.MultiChipTextLayout_startIconDrawable);
                startIconTint = typedArray.getColor(R.styleable.MultiChipTextLayout_startIconTint, getContext().getColor(R.color.black));

                // chip attributes
                chipIconDrawable = typedArray.getDrawable(R.styleable.MultiChipTextLayout_chipIconDrawable);
                chipIconTint = typedArray.getColor(R.styleable.MultiChipTextLayout_chipIconTint, getContext().getColor(R.color.black));
                chipStrokeColor = typedArray.getColor(R.styleable.MultiChipTextLayout_chipStrokeColor, getContext().getColor(R.color.black));
                chipStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipStrokeWidth, 0);
                chipBackgroundColor = typedArray.getColor(R.styleable.MultiChipTextLayout_chipBackgroundColor, Color.parseColor("#FFFFFF"));
                chipTextColor = typedArray.getColor(R.styleable.MultiChipTextLayout_chipTextColor, getContext().getColor(R.color.black));
                chipCloseIconTint = typedArray.getColor(R.styleable.MultiChipTextLayout_chipCloseIconTint, getContext().getColor(R.color.black));
                chipIconStartPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipIconStartPadding, 3);
                chipIconSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipIconSize, 50);
                chipCloseEnabled = typedArray.getBoolean(R.styleable.MultiChipTextLayout_chipCloseEnabled, true);
                chipCheckable = typedArray.getBoolean(R.styleable.MultiChipTextLayout_chipCheckable, false);
                chipClickable = typedArray.getBoolean(R.styleable.MultiChipTextLayout_chipClickable, false);
                chipTextSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipTextSize, 12);
                chipTextStartPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipTextStartPadding, 20);
                chipTextEndPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipTextEndPadding, 20);
                chipPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipPaddingLeft, 20);
                chipPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipPaddingRight, 20);
                chipPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipPaddingTop, 5);
                chipPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipPaddingBottom, 5);

                selectedChipsLayoutMode = eChipsLayoutMode.values()[typedArray.getInt(R.styleable.MultiChipTextLayout_chipsLayoutMode,0)];

                // apply attributes
                mc_textLayout.setHint(hintText);
                mc_textLayout.setHintTextColor(ColorStateList.valueOf(textColor));
                mc_textLayout.setBoxStrokeColor(strokeColor);
                mc_textLayout.setStartIconTintList(ColorStateList.valueOf(startIconTint));
                super.setStartIconDrawable(null);
                mc_textLayout.setStartIconDrawable(startIconDrawable);

                mc_autoCompleteTextView.setTextSize(textSize);
                mc_autoCompleteTextView.setEms(ems);
                mc_autoCompleteTextView.setTextColor(textColor);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setChipStyle(Context context, Chip chip) {
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry);
        chip.setChipDrawable(chipDrawable);
        chip.setCheckable(chipCheckable);
        chip.setClickable(chipClickable);
        chip.setChipBackgroundColor(ColorStateList.valueOf(chipBackgroundColor));
        chip.setChipStrokeColor(ColorStateList.valueOf(chipStrokeColor));
        chip.setChipStrokeWidth(chipStrokeWidth);
        chip.setTextColor(chipTextColor);
        chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, chipTextSize);
        chip.setPadding(chipPaddingLeft, chipPaddingTop, chipPaddingRight, chipPaddingBottom);
        chip.setTextEndPadding(chipTextEndPadding);
        chip.setTextStartPadding(chipTextStartPadding);
        chip.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);

        if (chipIconDrawable != null)
            chip.setChipIcon(chipIconDrawable);
        chip.setIconStartPadding(chipIconStartPadding);
        chip.setChipIconTint(ColorStateList.valueOf(chipIconTint));
        chip.setChipIconSize(chipIconSize);

        chip.setCloseIconTint(ColorStateList.valueOf(chipCloseIconTint));
        chip.setCloseIconVisible(chipCloseEnabled);

        chip.setOnCloseIconClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                arrTags.remove(chip.getText().toString());
                mc_chipGroup_tags.removeView(chip);
                if (chipCloseClickListener != null)
                    chipCloseClickListener.onClick(v);
            }
        });

        mc_chipGroup_tags.setChipSpacingVertical(0);
    }


    /**
     * @return current array of tags
     */
    public ArrayList<String> getChipsArray() {
        return arrTags;
    }


    /**
     * Clears all tags
     */
    public void clearChips() {
        arrTags.clear();
        mc_chipGroup_tags.removeAllViews();
    }

}