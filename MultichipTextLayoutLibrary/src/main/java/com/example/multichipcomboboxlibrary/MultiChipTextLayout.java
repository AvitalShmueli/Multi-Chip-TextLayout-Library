package com.example.multichipcomboboxlibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    //    private TextInputEditText mc_TXT;
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

    private AppCompatMultiAutoCompleteTextView mc_multi;
    private List<String> dropdownItems;

    private SpannableStringBuilder spannableStringBuilder;
    private int totalChipsWidthInCurrentLine = 0, lines = 1;
    private int chipSpacing = 16;
    private eChipsLayoutMode selectedChipsLayoutMode;
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

        switch (selectedChipsLayoutMode) {
            case BELOW:
                initLayoutBelow(context);
                break;
            case INLINE:
                spannableStringBuilder = new SpannableStringBuilder();
                initLayoutInline(context);
                break;
            case COMBO_BOX:
                spannableStringBuilder = new SpannableStringBuilder();
                initLayoutComboBox(context);
                break;

        }

    }


    private void initLayoutBelow(Context context){
        setEndIconMode(TextInputLayout.END_ICON_NONE);
//        mc_TXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        mc_multi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    mc_TXT.clearFocus();
//                    String strTagValue = mc_TXT.getText().toString().trim();
                    mc_multi.clearFocus();
                    String strTagValue = mc_multi.getText().toString().trim();
                    addChipToGroup(strTagValue);
                }
                return false;
            }
        });

        if (textWatcher != null)
            mc_multi.addTextChangedListener(textWatcher);
//            mc_TXT.addTextChangedListener(textWatcher);
    }


    private void initLayoutInline(Context context){
//        mc_multi.setInputType(InputType.TYPE_CLASS_TEXT);
        handleNewChip();
        handelChipDrawableDeletion();

//        if (textWatcher != null)
//            mc_TXT.addTextChangedListener(textWatcher);


    }


    private void initLayoutComboBox(Context context){
        mc_textLayout.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);
        spannableStringBuilder = new SpannableStringBuilder();

        dropdownItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, dropdownItems);
        mc_multi.setAdapter(adapter);

        mc_multi.setThreshold(1);
        mc_multi.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mc_multi.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            if (!selectedItem.isEmpty() && !arrTags.contains(selectedItem)) {
                arrTags.add(selectedItem);
                createChip(selectedItem);
            } else {
                int startIndex = mc_multi.getText().length() - selectedItem.length() - 2;
                int endIndex = mc_multi.getText().length();
                mc_multi.getEditableText().delete(startIndex,endIndex);
            }
            mc_multi.clearFocus();
        });

        handleNewChip();
        handelChipDrawableDeletion();

        if (textWatcher != null)
            mc_multi.addTextChangedListener(textWatcher);
    }

    private void handleNewChip(){
        mc_multi.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    mc_multi.clearFocus();
                    int endIndex = mc_multi.getEditableText().length();
                    mc_multi.getEditableText().delete(endIndex,endIndex);
                    String strTagValue = String.valueOf(mc_multi.getEditableText()).trim();
                    if (!strTagValue.isEmpty() && !arrTags.contains(strTagValue)) {
                        arrTags.add(strTagValue);
                        createChip(strTagValue);
                    }
                    else{
                        int startIndex = mc_multi.getText().length() - strTagValue.length() ;
                        endIndex = mc_multi.getText().length();
                        mc_multi.getEditableText().delete(startIndex,endIndex);
                    }
                    return true;
                }
                return false;
            }
        });

        mc_multi.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE ) {
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
                    }
                    return false;
                }
        );
    }


    private void handelChipDrawableDeletion(){
//        mc_TXT.addTextChangedListener(new TextWatcher() {
        mc_multi.addTextChangedListener(new TextWatcher() {
            private boolean backspaceDetected = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                backspaceDetected = count == 1 && after == 0; // Backspace detected
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (backspaceDetected) {
//                    int position = mc_TXT.getSelectionStart();
                    int position = mc_multi.getSelectionStart();
                    ImageSpan[] spans = spannableStringBuilder.getSpans(position, position + 1, ImageSpan.class);

                    if (spans.length > 0) {
                        ImageSpan spanToRemove = spans[0];
                        String tagToRemove = spanToRemove.getSource();
                        arrTags.remove(tagToRemove);

                        Log.d("TEST","tag removed: "+tagToRemove);

                        int startSpan = spannableStringBuilder.getSpanStart(spanToRemove);
                        int endSpan = spannableStringBuilder.getSpanEnd(spanToRemove);
                        spannableStringBuilder.removeSpan(spanToRemove);
                        spannableStringBuilder.delete(startSpan, endSpan);

                        // Check and remove any extra newline characters around the deleted span
                        if (startSpan > 0 && spannableStringBuilder.charAt(startSpan - 1) == '\n') {
                            spannableStringBuilder.delete(startSpan - 1, startSpan); // Remove the newline
                            startSpan = startSpan - 1;
                        }

//                        mc_TXT.setText(spannableStringBuilder);  // Update the TextInputEditText with the new spannable content
//                        mc_TXT.setSelection(startSpan); // Update cursor position
                        mc_multi.setText(spannableStringBuilder);  // Update the TextInputEditText with the new spannable content
                        mc_multi.setSelection(startSpan); // Update cursor position
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void addChipToGroup(String text) {
        Chip chip = new Chip(getContext());
        if (!text.isEmpty() && !arrTags.contains(text)) {
            chip.setText(text);
            setChipStyle(getContext(), chip);
            mc_chipGroup_tags.addView(chip);
            arrTags.add(text);
        }
//        mc_TXT.setText(""); // Clear the input after selection
        mc_multi.getEditableText().clear(); // Clear the input after selection
    }


    private void createChip(String text) {
        ChipDrawable chipDrawable = setChipStyleInline(text);
        ImageSpan imageSpan =  new ImageSpan(chipDrawable) {
            @Override
            public String getSource() {
                return text;  // Store the tag text as the source
            }
        };

        spannableStringBuilder.append(" "); // Append a space to avoid merging with text
        int start = spannableStringBuilder.length() - 1; // Before the space
        spannableStringBuilder.setSpan(imageSpan, start, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
        //mc_TXT = view.findViewById(R.id.mc_TXT);
        mc_multi = findViewById(R.id.mc_multi);
        mc_chipGroup_tags = view.findViewById(R.id.mc_chipGroup_tags);
    }

    public void setTextChangedListener(TextWatcher textChangedListener) {
        textWatcher = textChangedListener;
        if (textWatcher != null)
            mc_multi.addTextChangedListener(textWatcher);
//            mc_TXT.addTextChangedListener(textWatcher);
    }

    public void setChipOnCloseClickListener(OnClickListener listener) {
        chipCloseClickListener = listener;
    }

    private void handelAttributes(AttributeSet attrs) {
        try (TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiChipTextLayout, 0, 0)) {
            try {
                // text input attributes
                textSize = typedArray.getDimensionPixelSize(R.styleable.MultiChipTextLayout_textSize, (int) getResources().getDimension(R.dimen.textSize));
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

                selectedChipsLayoutMode = eChipsLayoutMode.values()[typedArray.getInt(R.styleable.MultiChipTextLayout_chipsLayoutMode, 0)];

                // apply attributes
                mc_textLayout.setHint(hintText);
                mc_textLayout.setHintTextColor(ColorStateList.valueOf(textColor));
                mc_textLayout.setBoxStrokeColor(strokeColor);
                mc_textLayout.setStartIconTintList(ColorStateList.valueOf(startIconTint));
                super.setStartIconDrawable(null);
                mc_textLayout.setStartIconDrawable(startIconDrawable);
                //            mc_TXT.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                //            mc_TXT.setEms(ems);
                //            mc_TXT.setTextColor(textColor);

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

//        if (chipIconDrawable != null)
//            chipDrawable.setChipIcon(chipIconDrawable);
//        chipDrawable.setIconStartPadding(chipIconStartPadding);
//        chipDrawable.setChipIconTint(ColorStateList.valueOf(chipIconTint));
//        chipDrawable.setChipIconSize(chipIconSize);

//        chipDrawable.setCloseIconTint(ColorStateList.valueOf(chipCloseIconTint));
//        chipDrawable.setCloseIconVisible(chipCloseEnabled);

        chipDrawable.setBounds(chipSpacing, chipSpacing, chipDrawable.getIntrinsicWidth()+ chipSpacing, chipDrawable.getIntrinsicHeight() + chipSpacing);
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
        //mc_TXT.setText("");
        mc_multi.getEditableText().clear();
        lines = 1;
        totalChipsWidthInCurrentLine = 0;
    }

    public SpannableStringBuilder getSpannableStringBuilder(){
        return spannableStringBuilder;
    }
}