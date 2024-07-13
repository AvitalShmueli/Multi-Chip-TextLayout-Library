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
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class MultiChipTextLayout extends TextInputLayout {

    private TextInputLayout mc_textLayout;
    private TextInputEditText mc_TXT;
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

        mc_TXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Clear focus here from the text input
                    mc_TXT.clearFocus();
                    String strTagValue = String.valueOf(mc_TXT.getEditableText()).trim();
                    Chip chip = new Chip(view.getContext());
                    if (!strTagValue.isEmpty() && !arrTags.contains(strTagValue)) {
                        chip.setText(strTagValue);
                        setChipStyle(context, chip);
                        mc_chipGroup_tags.addView(chip);
                        arrTags.add(strTagValue);
                    }
                    mc_TXT.setText("");
                }
                return false;
            }
        });

        if (textWatcher != null)
            mc_TXT.addTextChangedListener(textWatcher);

    }

    private void findViews(View view) {
        mc_textLayout = view.findViewById(R.id.mc_textLayout);
        mc_TXT = view.findViewById(R.id.mc_TXT);
        mc_chipGroup_tags = view.findViewById(R.id.mc_chipGroup_tags);
    }

    public void setTextChangedListener(TextWatcher textChangedListener) {
        textWatcher = textChangedListener;
        if (textWatcher != null)
            mc_TXT.addTextChangedListener(textWatcher);
    }

    public void setChipOnCloseClickListener(OnClickListener listener) {
        chipCloseClickListener = listener;
    }

    private void handelAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiChipCombobox, 0, 0);
        try {
            // text input attributes
            textSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_textSize, 14);
            ems = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_ems, 10);
            strokeColor = typedArray.getColor(R.styleable.MultiChipCombobox_strokeColor, getContext().getColor(R.color.black));
            textColor = typedArray.getColor(R.styleable.MultiChipCombobox_textColor, getContext().getColor(R.color.black));
            hintText = typedArray.getString(R.styleable.MultiChipCombobox_hintText);
            startIconDrawable = typedArray.getDrawable(R.styleable.MultiChipCombobox_startIconDrawable);
            startIconTint = typedArray.getColor(R.styleable.MultiChipCombobox_startIconTint, getContext().getColor(R.color.black));

            // chip attributes
            chipIconDrawable = typedArray.getDrawable(R.styleable.MultiChipCombobox_chipIconDrawable);
            chipIconTint = typedArray.getColor(R.styleable.MultiChipCombobox_chipIconTint, getContext().getColor(R.color.black));
            chipStrokeColor = typedArray.getColor(R.styleable.MultiChipCombobox_chipStrokeColor, getContext().getColor(R.color.black));
            chipStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipStrokeWidth, 0);
            chipBackgroundColor = typedArray.getColor(R.styleable.MultiChipCombobox_chipBackgroundColor, Color.parseColor("#FFFFFF"));
            chipTextColor = typedArray.getColor(R.styleable.MultiChipCombobox_chipTextColor, getContext().getColor(R.color.black));
            chipCloseIconTint = typedArray.getColor(R.styleable.MultiChipCombobox_chipCloseIconTint, getContext().getColor(R.color.black));
            chipIconStartPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipIconStartPadding, 3);
            chipIconSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipIconSize, 50);
            chipCloseEnabled = typedArray.getBoolean(R.styleable.MultiChipCombobox_chipCloseEnabled, true);
            chipCheckable = typedArray.getBoolean(R.styleable.MultiChipCombobox_chipCheckable, false);
            chipClickable = typedArray.getBoolean(R.styleable.MultiChipCombobox_chipClickable, false);
            chipTextSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipTextSize, 12);
            chipTextStartPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipTextStartPadding, 20);
            chipTextEndPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipTextEndPadding, 20);
            chipPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipPaddingLeft, 20);
            chipPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipPaddingRight, 20);
            chipPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipPaddingTop, 5);
            chipPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MultiChipCombobox_chipPaddingBottom, 5);

            // apply attributes
            mc_textLayout.setHint(hintText);
            mc_textLayout.setHintTextColor(ColorStateList.valueOf(textColor));
            mc_textLayout.setBoxStrokeColor(strokeColor);
            mc_textLayout.setStartIconTintList(ColorStateList.valueOf(startIconTint));
            super.setStartIconDrawable(null);
            mc_textLayout.setStartIconDrawable(startIconDrawable);
            mc_TXT.setTextSize(textSize);
            mc_TXT.setEms(ems);
            mc_TXT.setTextColor(textColor);

        } catch (Exception e) {
            e.printStackTrace();
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
