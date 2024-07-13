# Multi-Chip-TextLayout-Library

Home Assignment Exercise for Android B course, in Afeka - the academic college of Engineering in Tel Aviv.

Library to insert a text input layout that contains chips.

## Usage
##### MultiChipTextLayout Constructor:
```java  
main_multi_chip = findViewById(R.id.main_multi_chip);  
```  

##### MultiChipTextLayout xml parameters:

```xml  
<com.example.multichipcomboboxlibrary.MultiChipTextLayout    
  android:id="@+id/main_multi_chip"    
  android:layout_width="match_parent"    
  android:layout_height="wrap_content"    
  android:layout_marginTop="32dp"    
  android:layout_marginHorizontal="16dp"    
  app:chipBackgroundColor="@android:color/transparent"    
  app:chipCloseEnabled="true"    
  app:chipCloseIconTint="@color/teal800"    
  app:chipIconDrawable="@drawable/baseline_tag_24"    
  app:chipIconTint="@color/teal800"    
  app:chipStrokeColor="@color/teal800"    
  app:chipStrokeWidth="1dp"    
  app:chipTextColor="@color/teal800"    
  app:hintText="Hint text - test"    
  app:startIconDrawable="@drawable/baseline_tag_24"    
  app:startIconTint="@color/teal800"    
  app:strokeColor="@color/teal800"    
  app:textColor="@color/indigo900" />  
  ```

### Other attributes
**textSize** :  the size of the text in the TextInputEditText.
**ems** :  makes the TextView be exactly this many ems wide.
**strokeColor** :   stroke color of the TextInputEditText.
**strokeColor** :  stroke width of the TextInputEditText.
**textColor** :  the color of the text in the TextInputEditText.
**hintText** :  the hint text of the TextInputLayout.
**startIconDrawable** :  start icon of the TextInputLayout.
**chipIconDrawable** :  the icon of the chip.
**startIconTint** :  the color of the start icon of the TextInputLayout.
**chipIconTint** :  the color of the chip's icon.
**chipIconStartPadding** :  padding from the start of the chip.
**chipIconSize** :  size of the chip's icon.
**chipCheckable** :  is the chip checkable (default false).
**chipClickable** :  is the chip clickable (default false).
**chipCloseEnabled** :  is the close icon enabled (default true).
**chipTextSize** :  the size of the text in the chip.
**chipTextStartPadding** :  the padding from the chip's start.
**chipTextEndPadding** :  the padding from the chip's end.
**chipPaddingLeft** :  chip's padding left.
**chipPaddingRight** :  chip's padding right.
**chipPaddingTop** :  chip's padding top.
**chipPaddingBottom** :  chip's padding bottom.
**chipStrokeColor** :  stroke color of the chip.
**chipStrokeWidth** :  stroke width of the chip.
**chipBackgroundColor** :  background color of the chip.
**chipTextColor** :   the color of the text in the chip.
**chipCloseIconTint** :the color of the chip's close icon.


## Methods
1. getChipsArray() - returns all chips existing at the moment.
   ##### usage example
   ```java
   main_BTN_showTags.setOnClickListener(v -> {  
       showTags = true;  
       main_txt_selectedTags.setText(main_multi_chip.getChipsArray().toString());  
   });
   ```
2. clearChips() - clears all chips and resets the TextInputLayout.
   ##### usage example
   ```java
   main_BTN_clearTags.setOnClickListener(v -> {  
       main_multi_chip.clearChips();  
       main_txt_selectedTags.setText("");  
       showTags = false;  
   });
   ```
3. setTextChangedListener(TextWatcher textChangedListener) - set text change listener on the TextInputEditText.
   ##### usage example
   ```java
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
   ```

4. setChipOnCloseClickListener(OnClickListener listener) - set on click listener on the chip close icon,
   ##### usage example
   ```java
   main_multi_chip.setChipOnCloseClickListener(new View.OnClickListener() {  
       @Override  
     public void onClick(View v) {  
           if(showTags)  
               main_txt_selectedTags.setText(main_multi_chip.getChipsArray().toString());  
       }  
   });