package com.example.multichipcomboboxlibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class MultiChipTextLayout extends TextInputLayout {

    private TextInputLayout mc_textLayout;
    private ArrayList<String> arrTags;
    private ChipGroup mc_chipGroup_tags;
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
    private AppCompatMultiAutoCompleteTextView mc_multi;
    private SpannableStringBuilder spannableStringBuilder;
    private List<String> dropdownItems;
    private boolean enableAddValuesManually;
    private boolean enableMultipleSelection;
    private MultiChipTextLayout.eChipsLayoutMode selectedChipsLayoutMode;
    public enum eChipsLayoutMode {BELOW, INLINE, COMBO_BOX}

    private boolean focusedTouch = false;

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


        switch (selectedChipsLayoutMode) {
            case BELOW:
                initLayoutBelow();
                break;
            case INLINE:
                spannableStringBuilder = new SpannableStringBuilder();
                initLayoutInline();
                break;
            case COMBO_BOX:
                spannableStringBuilder = new SpannableStringBuilder();
                initLayoutComboBox(context);
                break;
        }

        mc_multi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

    }


    private void initLayoutBelow(){
        setEndIconMode(TextInputLayout.END_ICON_NONE);
        mc_multi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mc_multi.clearFocus();
                    String strTagValue = mc_multi.getText().toString().trim();
                    addChipToGroup(strTagValue);
                }
                return false;
            }
        });

        if (textWatcher != null)
            mc_multi.addTextChangedListener(textWatcher);
    }


    private void initLayoutInline(){
        handleNewChip();
        handelChipDrawableDeletion();
    }


    private void initLayoutComboBox(Context context){
        mc_textLayout.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);
//        spannableStringBuilder = new SpannableStringBuilder();

        dropdownItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, dropdownItems);
        mc_multi.setAdapter(adapter);

        mc_multi.setThreshold(1);
        mc_multi.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mc_multi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (!selectedItem.isEmpty() && !arrTags.contains(selectedItem)) {
                    if (!enableMultipleSelection)
                        removeChip(0,1);
                    arrTags.add(selectedItem);
                    createChip(selectedItem);
                } else {
                    int startIndex = mc_multi.getText().length() - selectedItem.length() - 2;
                    int endIndex = mc_multi.getText().length();
                    mc_multi.getEditableText().delete(startIndex,endIndex);
                }
                mc_multi.clearFocus();
                focusedTouch = false;
            }
        });

        if (enableAddValuesManually && enableMultipleSelection) {
            handleNewChip();
        }

        handelChipDrawableDeletion();

        if (textWatcher != null)
            mc_multi.addTextChangedListener(textWatcher);
    }


    private void handleNewChip(){
        mc_multi.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        mc_multi.clearFocus();
                        String strTagValue = String.valueOf(mc_multi.getEditableText()).trim();
                        if (!strTagValue.isEmpty() && !arrTags.contains(strTagValue)) {
                            arrTags.add(strTagValue);
                            createChip(strTagValue);
                        }
                        else{
                            int startIndex = mc_multi.getText().length() - strTagValue.length() ;
                            int endIndex = mc_multi.getText().length();
                            mc_multi.getEditableText().delete(startIndex,endIndex);
                        }
                        focusedTouch = selectedChipsLayoutMode != eChipsLayoutMode.COMBO_BOX;
                    }
                    return false;
                }
        );
    }


    private void handelChipDrawableDeletion(){
        mc_multi.addTextChangedListener(new TextWatcher() {
            private boolean backspaceDetected = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                backspaceDetected = count == 1 && after == 0; // Backspace detected
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (backspaceDetected) {
                    int position = mc_multi.getSelectionStart();
                    removeChip(position, position + 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mc_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipCloseEnabled) {
                    if(focusedTouch || mc_multi.length() == 0) {
                        removeChip(mc_multi.getSelectionStart(), mc_multi.getSelectionEnd());
                    }
                    else focusedTouch = true;
                }
            }
        });
    }


    private void removeChip(int startPos, int endPos){
        if (startPos != -1 && endPos != -1) {
            ImageSpan[] spans = spannableStringBuilder.getSpans(startPos,  endPos, ImageSpan.class);
            if (spans.length > 0) {
                ImageSpan spanToRemove = spans[0];
                String tagToRemove = spanToRemove.getSource();
                arrTags.remove(tagToRemove);

                int startSpan = spannableStringBuilder.getSpanStart(spanToRemove);
                int endSpan = spannableStringBuilder.getSpanEnd(spanToRemove);
                spannableStringBuilder.removeSpan(spanToRemove);
                spannableStringBuilder.delete(startSpan, endSpan);

                mc_multi.setText(spannableStringBuilder);  // Update the TextInputEditText with the new spannable content
                mc_multi.setSelection(startSpan); // Update cursor position

                focusedTouch = false;
                mc_multi.clearFocus();
            }
        }
    }


    private void addChipToGroup(String text) {
        Chip chip = new Chip(getContext());
        if (!text.isEmpty() && !arrTags.contains(text)) {
            chip.setText(text);
            setChipStyle(getContext(), chip);
            mc_chipGroup_tags.addView(chip);
            arrTags.add(text);
        }
        mc_multi.setText(""); // Clear the input after selection
    }


    private void createChip(String text) {
        ChipDrawable chipDrawable = setChipStyleInline(text);

        ImageSpan imageSpan = new CenteredImageSpan(chipDrawable){
            @Override
            public String getSource() {
                return text;  // Store the tag text as the source
            }
        };

        int start = spannableStringBuilder.length(); // Before the space
        spannableStringBuilder.append(" "); // Append a space to avoid merging with text
        int end = spannableStringBuilder.length();

        spannableStringBuilder.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mc_multi.setText(spannableStringBuilder); // Update the TextInputEditText with the new spannable content
        mc_multi.setSelection(spannableStringBuilder.length()); // Move cursor to the end

    }


    public void setDropdownItems(List<String> items) {
        this.dropdownItems = items;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, dropdownItems);
        mc_multi.setAdapter(adapter);
    }


    private void findViews(View view) {
        mc_textLayout = view.findViewById(R.id.mc_textLayout);
        mc_multi = findViewById(R.id.mc_multi);
        mc_chipGroup_tags = view.findViewById(R.id.mc_chipGroup_tags);
        mc_chipGroup_tags.setChipSpacingVertical(0); // minimal spacing
    }


    public void setTextChangedListener(TextWatcher textChangedListener) {
        textWatcher = textChangedListener;
        if (textWatcher != null)
            mc_multi.addTextChangedListener(textWatcher);
    }


    public void setChipOnCloseClickListener(OnClickListener listener) {
        chipCloseClickListener = listener;
    }


    private void handelAttributes(AttributeSet attrs) {
        try (TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiChipTextLayout, 0, 0)) {
            try {
                // text input attributes
                float textSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_textSize, (int) getResources().getDimension(R.dimen.textSize));
                int ems = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_ems, 10);
                int strokeColor = typedArray.getColor(R.styleable.MultiChipTextLayout_strokeColor, getContext().getColor(R.color.black));
                int textColor = typedArray.getColor(R.styleable.MultiChipTextLayout_textColor, getContext().getColor(R.color.black));
                String hintText = typedArray.getString(R.styleable.MultiChipTextLayout_hintText);
                Drawable startIconDrawable = typedArray.getDrawable(R.styleable.MultiChipTextLayout_startIconDrawable);
                int startIconTint = typedArray.getColor(R.styleable.MultiChipTextLayout_startIconTint, getContext().getColor(R.color.black));

                // chip attributes
                chipIconDrawable = typedArray.getDrawable(R.styleable.MultiChipTextLayout_chipIconDrawable);
                chipIconTint = typedArray.getColor(R.styleable.MultiChipTextLayout_chipIconTint, getContext().getColor(R.color.black));
                chipStrokeColor = typedArray.getColor(R.styleable.MultiChipTextLayout_chipStrokeColor, getContext().getColor(R.color.black));
                chipStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipStrokeWidth, (int) getResources().getDimension(R.dimen.chip_StrokeWidth));
                chipBackgroundColor = typedArray.getColor(R.styleable.MultiChipTextLayout_chipBackgroundColor, Color.parseColor("#FFFFFF"));
                chipTextColor = typedArray.getColor(R.styleable.MultiChipTextLayout_chipTextColor, getContext().getColor(R.color.black));
                chipCloseIconTint = typedArray.getColor(R.styleable.MultiChipTextLayout_chipCloseIconTint, getContext().getColor(R.color.black));
                chipIconStartPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipIconStartPadding, (int) getResources().getDimension(R.dimen.chip_IconStartPadding));
                chipIconSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipIconSize, (int) getResources().getDimension(R.dimen.chip_IconSize));
                chipCloseEnabled = typedArray.getBoolean(R.styleable.MultiChipTextLayout_chipCloseEnabled, true);
                chipCheckable = typedArray.getBoolean(R.styleable.MultiChipTextLayout_chipCheckable, false);
                chipClickable = typedArray.getBoolean(R.styleable.MultiChipTextLayout_chipClickable, false);
                chipTextSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipTextSize, (int) getResources().getDimension(R.dimen.chip_TextSize));
                chipTextStartPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipTextStartPadding, (int) getResources().getDimension(R.dimen.chip_TextStartPadding));
                chipTextEndPadding = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipTextEndPadding, (int) getResources().getDimension(R.dimen.chip_TextEndPadding));
                chipPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipPaddingLeft, (int) getResources().getDimension(R.dimen.chip_PaddingLeft));
                chipPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipPaddingRight, (int) getResources().getDimension(R.dimen.chip_PaddingRight));
                chipPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipPaddingTop, (int) getResources().getDimension(R.dimen.chip_PaddingTop));
                chipPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_chipPaddingBottom, (int) getResources().getDimension(R.dimen.chip_PaddingBottom));
                enableAddValuesManually = typedArray.getBoolean(R.styleable.MultiChipTextLayout_enableAddValuesManually,false);
                enableMultipleSelection = typedArray.getBoolean(R.styleable.MultiChipTextLayout_enableMultipleSelection,true);

                selectedChipsLayoutMode = eChipsLayoutMode.values()[typedArray.getInt(R.styleable.MultiChipTextLayout_chipsLayoutMode, 0)];

                // apply attributes
                mc_textLayout.setHint(hintText);
                mc_textLayout.setHintTextColor(ColorStateList.valueOf(textColor));
                mc_textLayout.setBoxStrokeColor(strokeColor);
                mc_textLayout.setStartIconTintList(ColorStateList.valueOf(startIconTint));
                super.setStartIconDrawable(null);
                mc_textLayout.setStartIconDrawable(startIconDrawable);
                mc_multi.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                mc_multi.setEms(ems);
                mc_multi.setTextColor(textColor);

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
        chip.setTextSize(TypedValue.COMPLEX_UNIT_PX,chipTextSize);
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


    private ChipDrawable setChipStyleInline(String text){
        // Create a ChipDrawable
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry);
        chipDrawable.setText(text);
        chipDrawable.setChipMinHeight((int) getResources().getDimension(R.dimen.chip_Height));
        chipDrawable.setChipBackgroundColor(ColorStateList.valueOf(chipBackgroundColor));
        chipDrawable.setCheckable(chipCheckable);
        chipDrawable.setChipStrokeColor(ColorStateList.valueOf(chipStrokeColor));
        chipDrawable.setChipStrokeWidth(chipStrokeWidth);
        chipDrawable.setTextColor(chipTextColor);
        chipDrawable.setTextSize(chipTextSize);
        chipDrawable.setPadding(chipPaddingLeft, chipPaddingTop, chipPaddingRight, chipPaddingBottom);
        chipDrawable.setTextEndPadding(chipTextEndPadding);
        chipDrawable.setTextStartPadding(chipTextStartPadding);
        if( getContext().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            chipDrawable.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else chipDrawable.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        if (chipIconDrawable != null)
            chipDrawable.setChipIcon(chipIconDrawable);
        chipDrawable.setIconStartPadding(chipIconStartPadding);
        chipDrawable.setChipIconTint(ColorStateList.valueOf(chipIconTint));
        chipDrawable.setChipIconSize(chipIconSize);

        chipDrawable.setCloseIconTint(ColorStateList.valueOf(chipCloseIconTint));
        chipDrawable.setCloseIconVisible(chipCloseEnabled);

        int spacing = 16;
        chipDrawable.setBounds(spacing, 0, chipDrawable.getIntrinsicWidth()+spacing, chipDrawable.getIntrinsicHeight());

        return chipDrawable;
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
        if(spannableStringBuilder != null)
            spannableStringBuilder.clear();
        mc_chipGroup_tags.removeAllViews();
        mc_multi.setText("");
        mc_multi.clearFocus();
    }

}
