package com.priyanka.dynamicformlibrary.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.priyanka.dynamicformlibrary.CameraActivity;
import com.priyanka.dynamicformlibrary.FormController;
import com.priyanka.dynamicformlibrary.FormModel;
import com.priyanka.dynamicformlibrary.R;
import com.priyanka.dynamicformlibrary.utils.ActionPerform;
import com.priyanka.dynamicformlibrary.utils.Utility;
import com.priyanka.dynamicformlibrary.validations.InputValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Priyankam on 11-05-2017.
 */

public class FloatingEditTextCameraController extends FloatingLabeledFieldController {

    public static Map<Integer, EditText> EditTextMap = new HashMap<>();
    static Context context;
    static FormModel saveFormModel;
    static String saveGetName;
    private final int editTextId = FormController.generateViewId();
    private final String placeholder;
    List<Bitmap> allImages = new ArrayList<>();
    private int inputType;
    private String label;
    private String Value;
    private String ViewID;
    private String refNo;
    private String editInputType;

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
    public FloatingEditTextCameraController(Context ctx, String viewFormID, String labelText, String placeholder, Set<InputValidator> validators, int inputType) {
        super(ctx, viewFormID, labelText, validators);
        context = ctx;
        this.placeholder = placeholder;
        this.inputType = inputType;
        this.label = labelText;


    }

    public FloatingEditTextCameraController(Context ctx, String viewID, String viewFormID, String labelText, String placeholder, String value, boolean isRequired, String refNo, String editinputType) {
        this(ctx, viewFormID, labelText, placeholder, isRequired, InputType.TYPE_CLASS_TEXT);
        context = ctx;
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
     * @param labelText   the label to display beside the field
     * @param placeholder a placeholder text to show when the input field is empty. If null, no placeholder is displayed
     * @param validators  contains the validations to process on the field
     */
    public FloatingEditTextCameraController(Context ctx, String viewFormID, String labelText, String placeholder, Set<InputValidator> validators) {
        this(ctx, viewFormID, labelText, placeholder, validators, InputType.TYPE_CLASS_TEXT);
        context = ctx;
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
    public FloatingEditTextCameraController(Context ctx, String viewFormID, String labelText, String placeholder, boolean isRequired, int inputType) {
        super(ctx, viewFormID, labelText, isRequired);
        context = ctx;
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
    public FloatingEditTextCameraController(Context ctx, String viewFormID, String labelText, String placeholder, boolean isRequired) {
        this(ctx, viewFormID, labelText, placeholder, isRequired, InputType.TYPE_CLASS_TEXT);
        context = ctx;
        this.label = labelText;
    }

    public FloatingEditTextCameraController(Context ctx, String viewFormID, String labelText, String placeholder, String value, boolean isRequired) {
        this(ctx, viewFormID, labelText, placeholder, isRequired, InputType.TYPE_CLASS_TEXT);
        context = ctx;
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
    public FloatingEditTextCameraController(Context ctx, String viewFormID, String labelText, String placeholder) {
        this(ctx, viewFormID, labelText, placeholder, false, InputType.TYPE_CLASS_TEXT);
        context = ctx;
        this.label = labelText;
    }

    /**
     * Constructs a new instance of an edit text field.
     *
     * @param ctx        the Android context
     * @param viewFormID the viewFormID of the field
     * @param labelText  the label to display beside the field
     */
    public FloatingEditTextCameraController(Context ctx, String viewFormID, String labelText) {
        this(ctx, viewFormID, labelText, null, false, InputType.TYPE_CLASS_TEXT);
        context = ctx;
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

        editText.setInputType(inputType);

        refresh(editText);

        if (!Value.isEmpty() && (Value != null)) {
            String[] keyValuePairs = Value.split("\\|(?![^\\[]*\\])"); //split the string to creat key-value pairs,excepts value in between array i.e[]

            String savedRemark = keyValuePairs[0].trim();
            String savedImages = keyValuePairs[1].trim();

            if (!savedImages.isEmpty() && (savedImages != null)) {
                savedImages = savedImages.substring(1, savedImages.length() - 1);  //remove curly brackets
                String[] imageOfDb = savedImages.split(","); //split the string to creat key-value pairs,excepts value in betwwen array i.e[]

                if (imageOfDb.length == 0) {
                    editText.setText("Files has deleted");
                } else {
                    String filePath = imageOfDb[0];
                    filePath = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                    filePath = filePath.substring(0, filePath.lastIndexOf("_"));
                    List fileList = fileFilter(filePath);

                    String[] strImageArray = new String[]{savedImages};
                    String resultValue = savedRemark + ActionPerform.SYMBOL_SPLIT + Arrays.toString(strImageArray);
                    Utility.saveMap.put(ViewID, resultValue);

                    for (int i = 0; i < fileList.size(); i++) {
                        //String resultValue = savedRemark + ActionPerform.SYMBOL_SPLIT + fileList.get(i);
                        System.out.println("values = " + resultValue);
                        //Utility.saveMap.put(ViewID, resultValue);
                    }


                    editText.setText("" + fileList.size() + " file attached.");
                }
            } else {
                editText.setText(Value);
            }

            getModel().setValue(getName(), editText.getText().toString());

        }

        EditTextMap.put(editTextId, editText);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFormModel = getModel();
                saveGetName = getName();
                selectImage(editText.getId(), textInputLayout.getHint().toString());
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    saveFormModel = getModel();
                    saveGetName = getName();
                    selectImage(editText.getId(), textInputLayout.getHint().toString());
                }
            }
        });

        return textInputLayout;
    }

    private void selectImage(int editTextId, String label) {
        // Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //     context.startActivity(intent);
        Intent intent = new Intent(context, CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ActionPerform.STRING_EDIT_TEXT_ID, String.valueOf(editTextId));
        intent.putExtra(ActionPerform.STRING_EDIT_TEXT_HINT_NAME, label);
        intent.putExtra(ActionPerform.OPTION_FLAG, ActionPerform.OPTION_CAMERA);
        intent.putExtra(ActionPerform.REMARK_FLAG, ActionPerform.REMARK_REQUIRED);
        intent.putExtra(ActionPerform.VIEW_ID, ViewID);
        intent.putExtra(ActionPerform.STRING_STORE_VALUE, Value);
        intent.putExtra(ActionPerform.STRING_REF_NO, refNo);
        intent.putExtra(ActionPerform.EDIT_INPUT_TYPE, editInputType);
        context.startActivity(intent);
    }

    private void refresh(EditText editText) {
        Object value = getModel().getValue(getName());
        String valueStr = value != null ? value.toString() : "";
        if (!valueStr.equals(editText.getText().toString())) {
            editText.setText(valueStr);
        }


    }

    @Override
    public void refresh() {
        refresh(getEditText());
    }

    public void setValueCamera(String editTextIds, String editTextHintName, String viewID) {

        System.out.println("Size Camera: " + CameraActivity.multiMapFileName.keys().count(String.valueOf(editTextIds)));

        String resultValue = CameraActivity.mapCameraRemark.get(String.valueOf(editTextIds)) + ActionPerform.SYMBOL_SPLIT + CameraActivity.multiMapFileName.get(String.valueOf(editTextIds)).toString();

        EditText editText = EditTextMap.get(Integer.parseInt(editTextIds));
        String remark = "";
        if (remark.isEmpty()) {
            // remark = String.valueOf(CameraActivity.multiMap.get(String.valueOf(editTextIds)).size());
            remark = String.valueOf(CameraActivity.multiMapFileName.get(String.valueOf(editTextIds)).size());
        }
        try {
            editText.setText("" + remark + " file attached.");
            saveFormModel.setValue(saveGetName, editText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Utility.saveMap.put(viewID, CameraActivity.multiMapFileName.get(String.valueOf(editTextIds)).toString());
        Utility.saveMap.put(viewID, resultValue);
    }

    private List fileFilter(String fileHeader) {
        List fileList = new ArrayList();
        try {
            File path = new File(Utility.FILE_PATH);
            if (path.exists()) {
                String[] fileNames = path.list();
                if (fileNames != null) {
                    for (int j = 0; j < fileNames.length; j++) {
                        if (fileNames[j].startsWith(fileHeader)) {
                            fileList.add(Utility.FILE_PATH + "/" + fileNames[j]);
                            System.out.println(fileNames[j]);
                        }

                    }
                } else {
                    Log.e("fileNames is null", "fileNames is null");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

}
