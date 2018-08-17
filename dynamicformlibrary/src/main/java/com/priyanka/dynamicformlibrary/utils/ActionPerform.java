package com.priyanka.dynamicformlibrary.utils;

/**
 * Created by Priyankam on 17-05-2017.
 */

public class ActionPerform {
    public static final String POSITIVE_RADIO = "P";     //only option is required (No image and remark are required here.)
    public static final String POSITIVE_REMARK = "PR";  //only option and remark are required (No image is required here)
    public static final String POSITIVE_CAMERA = "PC"; //only option ,remark and image are required.(all required here.)
    public static final String POSITIVE_IMAGE = "PI"; //only option and image are required (No remark is required)

    public static final String NEGATIVE_RADIO = "N";     //only option is required (No image and remark are required here.)
    public static final String NEGATIVE_REMARK = "NR";  //only option and remark are required (No image is required here)
    public static final String NEGATIVE_CAMERA = "NC"; //only option ,remark and image are required.(all required here.)
    public static final String NEGATIVE_IMAGE = "NI"; //only option and image are required (No remark is required)

    public static final String OPTION_CAMERA = "camera";
    public static final String OPTION_RADIO = "radio";
    public static final String REMARK_REQUIRED = "RemarkRequired";
    public static final String REMARK_NOT_REQUIRED = "RemarkNotRequired";

    public static final String OPTION_FLAG = "OptionFlag";
    public static final String REMARK_FLAG = "RemarkFlag";

    public static final String STRING_EDIT_TEXT_ID = "editTextId";
    public static final String STRING_EDIT_TEXT_HINT_NAME = "editTextHintName";
    public static final String VIEW_ID = "ViewId";
    public static final String STRING_STORE_VALUE = "StoreValue";
    public static final String STRING_REF_NO = "refNo";
    public static final String EDIT_INPUT_TYPE = "editinputType";

    public static final String NOT_APPLICABLE = "NA";
    public static final String SYMBOL_SPLIT = "|";


    public static final String EDIT_TEXT_DEFAULT = "text";
    public static final String EDIT_TEXT_NUMBER = "number";
    public static final String EDIT_TEXT_TIME = "time";
    public static final String EDIT_TEXT_DECIMAL = "decimal";
    public static final String EDIT_TEXT_EMAIL = "email";

    public static final int MAX_DIGITS_BEFORE_DOT = 10;
    public static final int MAX_DIGITS_AFTER_DOT = 2;
}
