package com.priyanka.dynamicformlibrary.controllers;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.priyanka.dynamicformlibrary.FormController;
import com.priyanka.dynamicformlibrary.R;
import com.priyanka.dynamicformlibrary.utils.ActionPerform;
import com.priyanka.dynamicformlibrary.utils.DecimalDigitsInputFilter;
import com.priyanka.dynamicformlibrary.utils.Utility;
import com.priyanka.dynamicformlibrary.validations.InputValidator;

import java.util.Set;

/**
 * Represents a field that allows free-form text.
 */
public class FloatingEditTextController extends FloatingLabeledFieldController {
    private final int editTextId = FormController.generateViewId();
    private final String placeholder;
    private int inputType;
    private String label;
    private String Value;
    private String ViewID;
    private String refNo;
    private String editInputType;


    public FloatingEditTextController(Context ctx, String viewID, String viewFormID, String labelText, String placeholder, String value, boolean isRequired, String refNo, String editinputType) {
        this(ctx, viewFormID, labelText, placeholder, isRequired, InputType.TYPE_CLASS_TEXT);
        this.label = labelText;
        this.Value = value;
        this.ViewID = viewID;
        this.refNo = refNo;
        this.editInputType = editinputType;

    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param ctx         the Android context
     * @param viewFormID  the viewFormID of the field
     * @param labelText   the label to display beside the field. Set to {@code null} to not show a label.
     * @param placeholder a placeholder text to show when the input field is empty. If null, no placeholder is displayed
     * @param validators  contains the validations to process on the field
     * @param inputType   the content type of the text box as a mask; possible values are defined by {@link InputType}.
     *                    For example, to enable multi-line, enable {@code InputType.TYPE_TEXT_FLAG_MULTI_LINE}.
     */
    public FloatingEditTextController(Context ctx, String viewFormID, String labelText, String placeholder, Set<InputValidator> validators, int inputType) {
        super(ctx, viewFormID, labelText, validators);
        this.placeholder = placeholder;
        this.inputType = inputType;
        this.label = labelText;
    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param ctx         the Android context
     * @param viewFormID  the viewFormID of the field
     * @param labelText   the label to display beside the field
     * @param placeholder a placeholder text to show when the input field is empty. If null, no placeholder is displayed
     * @param validators  contains the validations to process on the field
     */
    public FloatingEditTextController(Context ctx, String viewFormID, String labelText, String placeholder, Set<InputValidator> validators) {
        this(ctx, viewFormID, labelText, placeholder, validators, InputType.TYPE_CLASS_TEXT);
        this.label = labelText;
    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param ctx         the Android context
     * @param viewFormID  the viewFormID of the field
     * @param labelText   the label to display beside the field. Set to {@code null} to not show a label.
     * @param placeholder a placeholder text to show when the input field is empty. If null, no placeholder is displayed
     * @param isRequired  indicates if the field is required or not
     * @param inputType   the content type of the text box as a mask; possible values are defined by {@link InputType}.
     *                    For example, to enable multi-line, enable {@code InputType.TYPE_TEXT_FLAG_MULTI_LINE}.
     */
    public FloatingEditTextController(Context ctx, String viewFormID, String labelText, String placeholder, boolean isRequired, int inputType) {
        super(ctx, viewFormID, labelText, isRequired);
        this.placeholder = placeholder;
        this.inputType = inputType;
        this.label = labelText;
    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param ctx         the Android context
     * @param viewFormID  the viewFormID of the field
     * @param labelText   the label to display beside the field
     * @param placeholder a placeholder text to show when the input field is empty. If null, no placeholder is displayed
     * @param isRequired  indicates if the field is required or not
     */
    public FloatingEditTextController(Context ctx, String viewFormID, String labelText, String placeholder, boolean isRequired) {
        this(ctx, viewFormID, labelText, placeholder, isRequired, InputType.TYPE_CLASS_TEXT);
        this.label = labelText;
    }

    public FloatingEditTextController(Context ctx, String viewFormID, String labelText, String placeholder, String value, boolean isRequired) {
        this(ctx, viewFormID, labelText, placeholder, isRequired, InputType.TYPE_CLASS_TEXT);
        this.label = labelText;
        this.Value = value;
    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param ctx         the Android context
     * @param viewFormID  the viewFormID of the field
     * @param labelText   the label to display beside the field
     * @param placeholder a placeholder text to show when the input field is empty. If null, no placeholder is displayed
     */
    public FloatingEditTextController(Context ctx, String viewFormID, String labelText, String placeholder) {
        this(ctx, viewFormID, labelText, placeholder, false, InputType.TYPE_CLASS_TEXT);
        this.label = labelText;
    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param ctx        the Android context
     * @param viewFormID the viewFormID of the field
     * @param labelText  the label to display beside the field
     */
    public FloatingEditTextController(Context ctx, String viewFormID, String labelText) {
        this(ctx, viewFormID, labelText, null, false, InputType.TYPE_CLASS_TEXT);
        this.label = labelText;
    }


    /**
     * Returns the EditText view associated with this element.
     *
     * @return the EditText view associated with this element
     */
    public EditText getEditText() {

        return (EditText) getView().findViewById(editTextId);
    }

    /**
     * Returns a mask representing the content input type. Possible values are defined by {@link InputType}.
     *
     * @return a mask representing the content input type
     */
    public int getInputType() {
        return inputType;
    }

    private void setInputTypeMask(int mask, boolean enabled) {
        if (enabled) {
            inputType = inputType | mask;
        } else {
            inputType = inputType & ~mask;
        }
        if (isViewCreated()) {
            getEditText().setInputType(inputType);
        }
    }

    /**
     * Indicates whether this text box has multi-line enabled.
     *
     * @return true if this text box has multi-line enabled, or false otherwise
     */
    public boolean isMultiLine() {
        return (inputType | InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0;
    }

    /**
     * Enables or disables multi-line input for the text field. Default is false.
     *
     * @param multiLine if true, multi-line input is allowed, otherwise, the field will only allow a single line.
     */
    public void setMultiLine(boolean multiLine) {
        setInputTypeMask(InputType.TYPE_TEXT_FLAG_MULTI_LINE, multiLine);
    }

    /**
     * Indicates whether this text field hides the input text for security reasons.
     *
     * @return true if this text field hides the input text, or false otherwise
     */
    public boolean isSecureEntry() {
        return (inputType | InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0;
    }

    /**
     * Enables or disables secure entry for this text field. If enabled, input will be hidden from the user. Default is
     * false.
     *
     * @param isSecureEntry if true, input will be hidden from the user, otherwise input will be visible.
     */
    public void setSecureEntry(boolean isSecureEntry) {
        setInputTypeMask(InputType.TYPE_TEXT_VARIATION_PASSWORD, isSecureEntry);
    }

    @Override
    protected View createFieldView() {

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final TextInputLayout textInputLayout = new TextInputLayout(getContext());

        textInputLayout.setLayoutParams(lparams);
        //textInputLayout.setId(editTextId);
        textInputLayout.setFocusableInTouchMode(true);
        textInputLayout.setHintEnabled(true);
        textInputLayout.setHint(label);
        textInputLayout.setHintTextAppearance(R.style.Active);

        final EditText editText = new EditText(getContext());
        editText.setId(editTextId);
        editText.setSingleLine(!isMultiLine());

        editText.setLayoutParams(lparams);
        editText.setFocusableInTouchMode(true);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        textInputLayout.addView(editText);

        if (placeholder != null) {
            textInputLayout.setHint(label);
        }

        if (editInputType.equals(ActionPerform.EDIT_TEXT_NUMBER)) {

            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            try {
                int maxLength = 10;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                editText.setFilters(fArray);
                editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(ActionPerform.MAX_DIGITS_BEFORE_DOT, ActionPerform.MAX_DIGITS_AFTER_DOT, Long.MAX_VALUE)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_TIME)) {

            editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_DECIMAL)) {

            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            try {
                int maxLength = 10;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                editText.setFilters(fArray);
                editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(ActionPerform.MAX_DIGITS_BEFORE_DOT, ActionPerform.MAX_DIGITS_AFTER_DOT, Long.MAX_VALUE)});
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_EMAIL)) {

            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_DEFAULT)) {

            editText.setInputType(InputType.TYPE_CLASS_TEXT);

        } else {

            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }


        refresh(editText);

        if (!Value.isEmpty() && (Value != null)) {
            editText.setText(Value);
            getModel().setValue(getName(), editText.getText().toString());
            // Utility.saveMap.put(textInputLayout.getHint().toString(),textInputLayout.getEditText().getText().toString());
            Utility.saveMap.put(ViewID, textInputLayout.getEditText().getText().toString());
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                //Toast.makeText(getContext()," editText ="+ editText.getText().toString(),Toast.LENGTH_SHORT).show();
                getModel().setValue(getName(), editText.getText().toString());
                // Utility.saveMap.put(textInputLayout.getHint().toString(),textInputLayout.getEditText().getText().toString());
                Utility.saveMap.put(ViewID, textInputLayout.getEditText().getText().toString());
            }
        });

        return textInputLayout;
    }

    private void refresh(EditText editText) {
        Object value = getModel().getValue(getName());
        String valueStr = value != null ? value.toString() : "";
        if (!valueStr.equals(editText.getText().toString()))
            editText.setText(valueStr);
    }

    @Override
    public void refresh() {
        refresh(getEditText());
    }
}
