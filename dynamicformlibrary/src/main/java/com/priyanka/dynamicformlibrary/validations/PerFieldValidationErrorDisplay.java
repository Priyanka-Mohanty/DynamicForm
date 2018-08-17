package com.priyanka.dynamicformlibrary.validations;

import android.content.Context;
import android.content.res.Resources;

import com.priyanka.dynamicformlibrary.FormController;
import com.priyanka.dynamicformlibrary.FormElementController;
import com.priyanka.dynamicformlibrary.controllers.FormSectionController;

import java.util.List;

public class PerFieldValidationErrorDisplay implements ValidationErrorDisplay {
    private final Context context;
    private final FormController controller;

    public PerFieldValidationErrorDisplay(Context context, FormController controller) {
        this.context = context;
        this.controller = controller;
    }

    @Override
    public void resetErrors() {
        for (FormSectionController section : controller.getSections()) {
            for (FormElementController elementController : section.getElements()) {
                elementController.setError(null);
            }
        }
    }

    @Override
    public void showErrors(List<ValidationError> errors) {
        Resources res = context.getResources();
        FormElementController element;
        for (ValidationError error : errors) {
            element = controller.getElement(error.getFieldName());
            element.setError(error.getMessage(res));
        }
    }
}
