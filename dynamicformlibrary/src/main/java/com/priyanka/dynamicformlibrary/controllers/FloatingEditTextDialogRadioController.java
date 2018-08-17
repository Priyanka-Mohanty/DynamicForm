package com.priyanka.dynamicformlibrary.controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.priyanka.dynamicformlibrary.CameraActivity;
import com.priyanka.dynamicformlibrary.FormController;
import com.priyanka.dynamicformlibrary.R;
import com.priyanka.dynamicformlibrary.utils.ActionPerform;
import com.priyanka.dynamicformlibrary.utils.MessageUtil;
import com.priyanka.dynamicformlibrary.utils.Utility;
import com.priyanka.dynamicformlibrary.validations.InputValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FloatingEditTextDialogRadioController extends FloatingLabeledFieldController {
    public static Dialog selectionDialog = null;
    static Context context;
    private final int editTextId = FormController.generateViewId();
    private final DataSource dataSource;
    private final FloatingEditTextDialogRadioController.LoadItemsTask loadItemsTask;
    private List<String> items = new ArrayList<>();
    private ProgressDialog loadingIndicator;
    private HashMap<String, String> actionsItems = new HashMap<>();
    private HashMap<String, List<String>> newHashMap = new HashMap<>();
    private String label = "";
    private String Value = "";
    private String ViewID = "";
    private String refNo = "";
    private int itemTobeChecked = 0;
    private CharSequence defaultCharSequence = "";
    private String editInputType;

    /**
     * Creates a new instance of a selection field.
     *
     * @param ctx        the Android context
     * @param viewFormID the viewFormID of the field
     * @param labelText  the label to display beside the field. Set to {@code null} to not show a label.
     * @param hint
     * @param isRequired indicates if the field is required or not
     * @param dataSource the data source that provides the list of items to display
     */
    public FloatingEditTextDialogRadioController(Context ctx, String viewFormID, String labelText, String hint, boolean isRequired, DataSource dataSource) {
        super(ctx, viewFormID, labelText, isRequired);
        this.dataSource = dataSource;
        this.label = labelText;
        loadItemsTask = new FloatingEditTextDialogRadioController.LoadItemsTask();
        loadItemsTask.execute();
    }

    public FloatingEditTextDialogRadioController(Context ctx, String viewFormID, String labelText, String hint, String value, boolean isRequired, DataSource dataSource) {
        super(ctx, viewFormID, labelText, isRequired);
        this.dataSource = dataSource;
        this.label = labelText;
        this.Value = value;
        loadItemsTask = new FloatingEditTextDialogRadioController.LoadItemsTask();
        loadItemsTask.execute();
    }

    public FloatingEditTextDialogRadioController(Context ctx, String viewID, String viewFormID, String labelText, String hint, String value, boolean isRequired, DataSource dataSource, String refNo, String editinputType) {
        super(ctx, viewFormID, labelText, isRequired);
        this.dataSource = dataSource;
        this.label = labelText;
        this.Value = value;
        this.ViewID = viewID;
        this.refNo = refNo;
        this.editInputType = editinputType;
        loadItemsTask = new FloatingEditTextDialogRadioController.LoadItemsTask();
        loadItemsTask.execute();
    }

    /**
     * Creates a new instance of a selection field.
     *
     * @param ctx        the Android context
     * @param viewFormID the viewFormID of the field
     * @param labelText  the label to display beside the field. Set to {@code null} to not show a label.
     * @param validators contains the validations to process on the field
     * @param dataSource the data source that provides the list of items to display
     */
    public FloatingEditTextDialogRadioController(Context ctx, String viewFormID, String labelText, Set<InputValidator> validators, DataSource dataSource) {
        super(ctx, viewFormID, labelText, validators);
        this.dataSource = dataSource;
        this.label = labelText;
        loadItemsTask = new FloatingEditTextDialogRadioController.LoadItemsTask();
        loadItemsTask.execute();

    }

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
        //editText.setHint(label);
        editText.setSingleLine(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setKeyListener(null);
        textInputLayout.addView(editText);

        refresh(editText);

        if (!Value.isEmpty() && (Value != null)) {

            String resultValue = Value;
            String showResult = ActionPerform.NOT_APPLICABLE;
            String input = Value;// "ok|034556|image" "selectedOption|remark|images"
            String[] parts = input.split("\\|");
            System.out.println("Input   : '" + input + "'");
            System.out.println("Size: " + parts.length);
            String savedOption = parts[0]; // selectedOption

            if (parts.length > 1) {
                String savedRemark = parts[1]; // remark
                String savedImages = parts[2]; // 034556 images

                //if part3 is not empty
                if (!(savedImages.isEmpty()) && (savedImages != null) && (!savedImages.equals(ActionPerform.NOT_APPLICABLE))) {

                    savedImages = savedImages.substring(1, savedImages.length() - 1);  //remove curly brackets
                    String[] imageOfDb = savedImages.split(","); //split the string to creat key-value pairs,excepts value in betwwen array i.e[]
                    if (imageOfDb.length == 0) {

                        showResult = savedOption + " (Files has deleted.)";

                    } else {
                        String filePath = imageOfDb[0];
                        filePath = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                        filePath = filePath.substring(0, filePath.lastIndexOf("_"));
                        List fileList = fileFilter(filePath);

                        String[] strImageArray = new String[]{savedImages};
                        resultValue = savedOption + ActionPerform.SYMBOL_SPLIT + savedRemark + ActionPerform.SYMBOL_SPLIT + Arrays.toString(strImageArray);
                        //  Utility.saveMap.put(ViewID, resultValue);
                        for (int i = 0; i < fileList.size(); i++) {
                            //String resultValue = savedRemark + ActionPerform.SYMBOL_SPLIT + fileList.get(i);
                            //  Utility.saveMap.put(ViewID, resultValue);
                        }
                        showResult = savedOption + " ( " + fileList.size() + " File attached.)";

                    }
                } else {

                    if (!savedRemark.equals(ActionPerform.NOT_APPLICABLE)) {
                        showResult = savedOption + " ( " + savedRemark + ")";
                    } else {
                        showResult = savedOption;
                    }

                }

            } else {

                resultValue = savedOption + ActionPerform.SYMBOL_SPLIT + ActionPerform.NOT_APPLICABLE + ActionPerform.SYMBOL_SPLIT + ActionPerform.NOT_APPLICABLE;
                showResult = savedOption;

                // Utility.saveMap.put(ViewID, textInputLayout.getEditText().getText().toString());
            }

            editText.setText(showResult);

            Utility.saveMap.put(ViewID, resultValue);

            getModel().setValue(getName(), editText.getText().toString());

        }


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog(getContext(), textInputLayout, editText);

            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSelectionDialog(getContext(), textInputLayout, editText);
                }
            }
        });


        return textInputLayout;
    }

    private void showSelectionDialog(final Context context, final TextInputLayout textInputLayout, final EditText editText) {

        String savedOption = null;
           /* if(itemTobeChecked == 0){
                //first check editText is empty or not, if edittext is not empty then, by default editetxt string  will checked.
                String input = Value;// "ok|034556|image" "selectedOption|remark|images"
                String[] splits = input.split("\\|");
                savedOption = splits[0]; // selectedOption
            }*/
        try {
            if (!editText.getText().toString().trim().isEmpty()) {
                String input = editText.getText().toString().trim();// "ok(sdwewew)"
                String[] splits = input.split("\\(");
                if (splits.length > 1) {
                    savedOption = splits[0];
                } else {
                    savedOption = editText.getText().toString().trim();
                }
            }

            if (items == null) {
                assert (loadItemsTask.getStatus() != AsyncTask.Status.FINISHED);
                loadItemsTask.runTaskOnFinished(new Runnable() {
                    @Override
                    public void run() {
                        showSelectionDialog(context, textInputLayout, editText);
                    }
                });

                if (loadingIndicator == null) {
                    loadingIndicator = MessageUtil.newProgressIndicator("Getting required data", context);
                    loadingIndicator.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            loadItemsTask.runTaskOnFinished(null);
                        }
                    });
                }

                loadingIndicator.show();
            } else if (selectionDialog == null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getLabel());

                final View customView = LayoutInflater.from(context).inflate(R.layout.form_radio, null);

                final RadioGroup radioGroup = (RadioGroup) customView.findViewById(R.id.radio_group);
                final EditText editRemark = (EditText) customView.findViewById(R.id.edit_remarks);

                checkEditRemarkInputType(editRemark, editInputType);

                final TextWatcher mTextEditorWatcher = new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        try {
                            //Submit Button Visibility
                            EnableDisableSubmitButton(editRemark, selectionDialog, s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        try {
                            //Submit Button Visibility
                            EnableDisableSubmitButton(editRemark, selectionDialog, s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    public void afterTextChanged(Editable s) {
                        try {
                            //Submit Button Visibility
                            EnableDisableEditableSubmitButton(editRemark, selectionDialog, s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                };
                editRemark.addTextChangedListener(mTextEditorWatcher);

                for (int row = 0; row < 1; row++) {
                    RadioGroup rg = new RadioGroup(getContext());
                    rg.setOrientation(LinearLayout.VERTICAL);
                    List<String> itemsPerLabel = new ArrayList<>();
                    itemsPerLabel = newHashMap.get(label);

                    if (!itemsPerLabel.isEmpty() && (!newHashMap.get(label).isEmpty())) {
                        for (int i = 0; i < itemsPerLabel.size(); i++) {
                            RadioButton rdbtn = new RadioButton(getContext());
                            rdbtn.setId((row * 2) + i);
                            //Log.i("radio buttion id", "radio buttion id=  " + rdbtn.getId());

                            String string = itemsPerLabel.get(i);// "PI|OK"
                            String[] parts = string.split("\\|");
                            String part1 = parts[0]; // PI -flag
                            String part2 = parts[1]; // OK - value

                            actionsItems.put(part2, part1);

                            //item checked by default
                       /* if (part2.equals(savedOption.trim())) {
                            itemTobeChecked = rdbtn.getId();
                            //radioGroup.check(rdbtn.getId());//here saved option only display selection
                        }*/
                            rdbtn.setText(part2);

                            radioGroup.addView(rdbtn);
                        }
                    }

                    // ((RadioGroup) customView.findViewById(R.id.radio_group)).addView(rg);
                    radioGroup.addView(rg);

                    // radioGroup.check(itemTobeChecked);

                }

                //checkByDefaultEditRemarkVisibility(editRemark, newHashMap.get(label));

                builder.setView(customView);
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (radioGroup.getCheckedRadioButtonId() != -1) {
                            int id = radioGroup.getCheckedRadioButtonId();
                            View radioButton = radioGroup.findViewById(id);
                            int radioId = radioGroup.indexOfChild(radioButton);
                            RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);

                            String selection = (String) btn.getText();

                            itemTobeChecked = btn.getId();

                            String remark = editRemark.getText().toString();
                            String showResult = ActionPerform.NOT_APPLICABLE;
                            if (editRemark.isShown()) {

                                showResult = selection + "(" + editRemark.getText().toString() + ")";

                            } else {
                                if (remark.isEmpty()) {
                                    if (CameraActivity.multiMapFileName.get(String.valueOf(editTextId)).size() > 0) {

                                        showResult = selection + "(" + String.valueOf(CameraActivity.multiMapFileName.get(String.valueOf(editTextId)).size()) + " file attached.)";

                                    } else {
                                        showResult = selection;

                                    }

                                }

                            }

                            System.out.println("Size: " + CameraActivity.multiMapFileName.keys().count(String.valueOf(editTextId)));

                            editText.setText(showResult);

                            getModel().setValue(getName(), editText.getText().toString());
                            //Toast.makeText(getContext()," editText ="+ textInputLayout.getEditText().getText().toString()+"\nhint"+ textInputLayout.getHint().toString(),Toast.LENGTH_SHORT).show();
                            if (editRemark.isShown()) {
                                //Utility.saveMap.put(textInputLayout.getHint().toString(),selection);
                                String savedRemark = editRemark.getText().toString();
                                String resultValue = selection + ActionPerform.SYMBOL_SPLIT + savedRemark + ActionPerform.SYMBOL_SPLIT + ActionPerform.NOT_APPLICABLE;

                                Utility.saveMap.put(ViewID, resultValue);

                            } else if (!editRemark.isShown()) {

                                if (editRemark.getText().toString().isEmpty()) {

                                    try {
                                        if ((CameraActivity.multiMapFileName.get(String.valueOf(editTextId)).size() > 0) && (CameraActivity.mapCameraRemark.size() > 0)) {
                                            // Utility.saveMap.put(textInputLayout.getHint().toString(),CameraActivity.multiMap.get(String.valueOf(editTextId)).toString());
                                            //  CameraActivity.mapCameraRemark.get(String.valueOf(editTextId)).toString()
                                            // Log.i("TAG", CameraActivity.mapCameraRemark.get(String.valueOf(editTextId)).toString());
                                            // Log.i("TAG", CameraActivity.multiMapFileName.get(String.valueOf(editTextId)).toString());


                                            String resultValue = selection + ActionPerform.SYMBOL_SPLIT + CameraActivity.mapCameraRemark.get(String.valueOf(editTextId)) + ActionPerform.SYMBOL_SPLIT + CameraActivity.multiMapFileName.get(String.valueOf(editTextId)).toString();

                                            Utility.saveMap.put(ViewID, resultValue);
                                        } else {
                                            // Utility.saveMap.put(textInputLayout.getHint().toString(), selection);
                                            String resultValue = selection + ActionPerform.SYMBOL_SPLIT + ActionPerform.NOT_APPLICABLE + ActionPerform.SYMBOL_SPLIT + ActionPerform.NOT_APPLICABLE;
                                            Utility.saveMap.put(ViewID, resultValue);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                            selectionDialog.dismiss();

                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectionDialog.dismiss();
                    }
                });
                selectionDialog = builder.create();
                selectionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        selectionDialog = null;
                    }
                });
                selectionDialog.show();

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        final String selectedValue = ((RadioButton) customView.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                        //Toast.makeText(getContext(), selectedValue, Toast.LENGTH_SHORT).show();

                        itemTobeChecked = radioGroup.getCheckedRadioButtonId();

                        if (actionsItems.containsKey(selectedValue)) {
                            //Log.i("containsValue", "containsValue  " + checkedId);
                        }
                        for (Map.Entry<String, String> entry : actionsItems.entrySet()) {
                            if (entry.getKey().equals(selectedValue)) {
                                // System.out.println("containsValue =" + entry.getValue());

                                switch (entry.getValue().trim()) {


                                    case ActionPerform.POSITIVE_RADIO:

                                        editRemark.setVisibility(View.GONE);

                                        //remove for the particular component's image if it choose other options
                                        if (CameraActivity.multiMapFileName.containsKey(String.valueOf(editTextId))) {
                                            CameraActivity.multiMapFileName.removeAll(String.valueOf(editTextId));
                                            System.out.println("Size: " + CameraActivity.multiMapFileName.size());
                                        }
                                        //Submit Button Visibility
                                        EnableDisableSubmitButton(editRemark, selectionDialog, defaultCharSequence);

                                        break;

                                    case ActionPerform.NEGATIVE_RADIO:

                                        editRemark.setVisibility(View.GONE);

                                        //remove for the particular component's image if it choose other options
                                        if (CameraActivity.multiMapFileName.containsKey(String.valueOf(editTextId))) {
                                            CameraActivity.multiMapFileName.removeAll(String.valueOf(editTextId));
                                            System.out.println("Size: " + CameraActivity.multiMapFileName.size());
                                        }

                                        //Submit Button Visibility
                                        EnableDisableSubmitButton(editRemark, selectionDialog, defaultCharSequence);


                                        break;

                                    case ActionPerform.POSITIVE_REMARK:

                                        editRemark.setVisibility(View.VISIBLE);
                                        //clear the previous activity's /form images
                                        //remove for the particular component's image if it choose other options
                                        if (CameraActivity.multiMapFileName.containsKey(String.valueOf(editTextId))) {
                                            CameraActivity.multiMapFileName.removeAll(String.valueOf(editTextId));
                                        }

                                        //Submit Button Visibility
                                        EnableDisableSubmitButton(editRemark, selectionDialog, defaultCharSequence);


                                        break;

                                    case ActionPerform.NEGATIVE_REMARK:

                                        editRemark.setVisibility(View.VISIBLE);
                                        //clear the previous activity's /form images

                                        //remove for the particular component's image if it choose other options
                                        if (CameraActivity.multiMapFileName.containsKey(String.valueOf(editTextId))) {
                                            CameraActivity.multiMapFileName.removeAll(String.valueOf(editTextId));
                                        }

                                        //Submit Button Visibility
                                        EnableDisableSubmitButton(editRemark, selectionDialog, defaultCharSequence);


                                        break;

                                    case ActionPerform.POSITIVE_CAMERA:

                                        editRemark.setVisibility(View.GONE);

                                        callCameraActivity(context, String.valueOf(editTextId), label, ActionPerform.OPTION_RADIO, ActionPerform.REMARK_REQUIRED, ViewID, Value, refNo);

                                        //Submit Button Visibility
                                        EnableDisableSubmitButton(editRemark, selectionDialog, defaultCharSequence);


                                        break;

                                    case ActionPerform.NEGATIVE_CAMERA:

                                        editRemark.setVisibility(View.GONE);

                                        callCameraActivity(context, String.valueOf(editTextId), label, ActionPerform.OPTION_RADIO, ActionPerform.REMARK_REQUIRED, ViewID, Value, refNo);

                                        //Submit Button Visibility
                                        EnableDisableSubmitButton(editRemark, selectionDialog, defaultCharSequence);

                                        break;

                                    case ActionPerform.POSITIVE_IMAGE:

                                        editRemark.setVisibility(View.GONE);

                                        callCameraActivity(context, String.valueOf(editTextId), label, ActionPerform.OPTION_RADIO, ActionPerform.REMARK_NOT_REQUIRED, ViewID, Value, refNo);

                                        //Submit Button Visibility
                                        EnableDisableSubmitButton(editRemark, selectionDialog, defaultCharSequence);

                                        break;

                                    case ActionPerform.NEGATIVE_IMAGE:

                                        editRemark.setVisibility(View.GONE);

                                        callCameraActivity(context, String.valueOf(editTextId), label, ActionPerform.OPTION_RADIO, ActionPerform.REMARK_NOT_REQUIRED, ViewID, Value, refNo);

                                        //Submit Button Visibility
                                        EnableDisableSubmitButton(editRemark, selectionDialog, defaultCharSequence);

                                        break;

                                    default:
                                        break;


                                }

                            }
                        }

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void checkEditRemarkInputType(EditText editRemark, String editInputType) {
        if (editInputType.equals(ActionPerform.EDIT_TEXT_NUMBER)) {

            editRemark.setInputType(InputType.TYPE_CLASS_NUMBER);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_TIME)) {

            editRemark.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_DECIMAL)) {

            editRemark.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_EMAIL)) {

            editRemark.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_DEFAULT)) {

            editRemark.setInputType(InputType.TYPE_CLASS_TEXT);

        } else {

            editRemark.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private void checkByDefaultEditRemarkVisibility(EditText editRemark, List<String> itemsPerLabel) {

        String string = itemsPerLabel.get(0);// "PI|OK"
        String[] parts = string.split("\\|");
        String part1 = parts[0]; // PI -flag
        String part2 = parts[1]; // OK - value

        switch (part1) {

            case ActionPerform.POSITIVE_RADIO:

                editRemark.setVisibility(View.GONE);

                break;

            case ActionPerform.NEGATIVE_RADIO:

                editRemark.setVisibility(View.GONE);

                break;

            case ActionPerform.POSITIVE_REMARK:

                editRemark.setVisibility(View.VISIBLE);

                break;

            case ActionPerform.NEGATIVE_REMARK:

                editRemark.setVisibility(View.VISIBLE);

                break;

            case ActionPerform.POSITIVE_CAMERA:

                editRemark.setVisibility(View.GONE);

                break;

            case ActionPerform.NEGATIVE_CAMERA:

                editRemark.setVisibility(View.GONE);


                break;

            case ActionPerform.POSITIVE_IMAGE:

                editRemark.setVisibility(View.GONE);

                break;

            case ActionPerform.NEGATIVE_IMAGE:

                editRemark.setVisibility(View.GONE);

                break;

            default:
                break;


        }
    }

    private void EnableDisableSubmitButton(EditText editRemark, Dialog selectionDialog, CharSequence s) {

        try {
            // Initially  disable the submit button
            if (editRemark.isShown()) {
                if (TextUtils.isEmpty(s) && s.length() < 3) {
                    if ((s.toString() != null) && (s.toString().trim().equals(""))) {
                        // Disable ok button
                        ((AlertDialog) selectionDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        editRemark.setError("Remark must required!");
                    }

                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) selectionDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            } else {
                ((AlertDialog) selectionDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void EnableDisableEditableSubmitButton(EditText editRemark, Dialog selectionDialog, Editable s) {

        try {
            // Initially  disable the submit button
            if (editRemark.isShown()) {
                if (TextUtils.isEmpty(s) && s.length() < 3) {
                    if ((s.toString() != null) && (s.toString().trim().equals(""))) {
                        // Disable ok button
                        ((AlertDialog) selectionDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        editRemark.setError("Remark must required!");
                    }

                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) selectionDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            } else {
                ((AlertDialog) selectionDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void callCameraActivity(Context context, String editTextId, String label, String optionFlag, String remarkFlag, String viewID, String value, String refNo) {

        Intent intent = new Intent(context, CameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ActionPerform.STRING_EDIT_TEXT_ID, String.valueOf(editTextId));
        intent.putExtra(ActionPerform.STRING_EDIT_TEXT_HINT_NAME, label);
        intent.putExtra(ActionPerform.OPTION_FLAG, optionFlag);
        intent.putExtra(ActionPerform.REMARK_FLAG, remarkFlag);
        intent.putExtra(ActionPerform.VIEW_ID, viewID);

        String input = value;// "004|remark|images"
        String[] parts = input.split("\\|");
        if (parts.length > 1) {
            intent.putExtra(ActionPerform.STRING_STORE_VALUE, Value);
        } else {
            intent.putExtra(ActionPerform.STRING_STORE_VALUE, "");
        }

        intent.putExtra(ActionPerform.STRING_REF_NO, refNo);
        intent.putExtra(ActionPerform.EDIT_INPUT_TYPE, editInputType);
        context.startActivity(intent);

    }

    private EditText getEditText() {
        return (EditText) getView().findViewById(editTextId);
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

    /**
     * An interface that provides the list of items to display for the {@link FloatingEditTextDialogRadioController}.
     */
    public interface DataSource {
        /**
         * Returns a list of all the items that can be selected or searched. This method will be called by the
         * {@link FloatingEditTextDialogRadioController} in a background thread.
         *
         * @return a list of all the items that can be selected or searched.
         */
        HashMap<String, List<String>> getItems();
    }

    private class LoadItemsTask extends AsyncTask<Void, Void, HashMap<String, List<String>>> {

        Runnable doneRunnable;

        @Override
        protected HashMap<String, List<String>> doInBackground(Void... params) {
            return dataSource.getItems();
        }

        @Override
        protected void onPostExecute(HashMap<String, List<String>> results) {
            newHashMap = results;
            if (loadingIndicator != null) {
                loadingIndicator.dismiss();
                loadingIndicator = null;
            }

            if (doneRunnable != null) {
                doneRunnable.run();
            }
        }

        protected void runTaskOnFinished(Runnable runnable) {
            doneRunnable = runnable;
        }
    }


}
