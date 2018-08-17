package com.example.priyankam.dynamicform;

import android.os.Environment;

import com.priyanka.dynamicformlibrary.utils.Utility;

import java.io.File;

public class Configuration {
    public static final String IMAGE_FILE_PATH = Utility.FILE_PATH + "/";
    public static final String FILE_DIRECTORY_NAME = "DynamicForm";
    public static final String IMAGE_DIRECTORY_NAME = Environment.getExternalStorageDirectory() + File.separator + FILE_DIRECTORY_NAME + File.separator + "Images";
    public static final String IMAGE_NAME = "UserProfile" + ".jpg";
    //url
    public static final String URL_MEDIA_UPLOAD_PATH = "dynamicformResponse.php";
    public static final String URL_MEDIA_UPLOAD_PATH_IMAGE = "dynamicformResponseImage.php";
    public static final String URL_PARENT_PATH = "http://10.1.1.206/Projects/php_upload/";


    //dynamic form
    public static final String JSON_VIEW_ID = "viewID";
    public static final String JSON_VIEW = "view";
    public static final String JSON_HINT = "hint";
    public static final String JSON_MANDATORY_OPTION = "mandatory";
    public static final String JSON_INPUT_TYPE = "inputType";
    public static final String JSON_ITEMS = "items";
    public static final String JSON_NAME = "name";
    public static final String STRING_EDIT_TEXT = "edittext";
    public static final String STRING_EDIT_TEXT_RADIO = "edittextRadio";
    public static final String STRING_EDIT_TEXT_CAMERA = "edittextCamera";


    public static final String TAKE_PHOTO = "Take Photo";
    public static final String CHOOSE_FILE = "Choose from File";
    public static final String CANCEL = "Cancel";
    public static final String MODE_DYNAMIC_FORM = "DynamicForm";
    public static final String MODE_POST_DYNAMIC_FORM = "PostDynamicForm";
    public static final int TAB_IDENTIFIER_DEFAULT = 99;
    public static final String JSON_UPLOADED_FILE = "FileName";//"uploaded_file";
    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Fail";
    public static final String MESSAGE = "message";
    public static final String STATUS = "status";
    public static final String DATA_NOT_AVL = "Data Not Available";
    public static final String MODE_USER = "UserData";
    public static final String JSON_USER = "Users";
    public static final String JSON_USER_ID = "userID";
    public static final String JSON_USER_NAME = "userName";
    public static final String JSON_USER_EMAIL = "email";
    public static final String JSON_DYNAMIC_FORM = "DynamicForm";
    public static final String BLANK_ARRAY = "[]";
    public static final String STRING_NULL = "null";
    public static final String FORM_FRAGMENT_KEY = "nd_form";
    public static final String LOG_ID = "logID";
    public static final String FORM_DATA = "FormData";
    public static final String TAG_LOG = "LOG_MESSAGE";
    public static final int INTERNET_TIME_OUT = 1200000;
    public static final String STRING_ZERO = "0";
    public static final String STRING_NA = "NA";
    public static final String STRING_EMPTY = "";
    public static final String RESPONSE = "response";
    public static boolean sSwipeRefreshFlag = false;

}
