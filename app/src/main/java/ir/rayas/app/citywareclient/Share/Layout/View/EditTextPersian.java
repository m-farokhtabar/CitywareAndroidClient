package ir.rayas.app.citywareclient.Share.Layout.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Enum.ValidationType;
import ir.rayas.app.citywareclient.Share.Helper.TypefaceSpan;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.Font.PersianReshape;
import ir.rayas.app.citywareclient.Share.Utility.Utility;

/**
 * کامپوننت تکست باکس فارسی
 */
public class EditTextPersian extends android.support.v7.widget.AppCompatEditText {

    private boolean IsRequired = false;
    private ValidationType TypeOfValidation = ValidationType.None;
    private boolean IsValidated = true;
    private String ValidationErrorMessage = "";
    private String Title = "";

    public String getTitle() {
        return Title;
    }

    public boolean isValidated() {
        if (getText() != null) {
            CheckValidation(getText().toString().trim());
        } else {
            CheckValidation("");
        }
        return IsValidated;
    }

    public String getValidationErrorMessage() {
        return ValidationErrorMessage;
    }

    public void setTypeOfValidation(ValidationType typeOfValidation) {
        TypeOfValidation = typeOfValidation;
    }

    public ValidationType getTypeOfValidation() {
        return TypeOfValidation;
    }


    public boolean isRequired() {
        return IsRequired;
    }

    public void setRequired(boolean required) {
        IsRequired = required;
    }


    public EditTextPersian(Context context) {
        super(context);
        SetDefaultField();
        this.setHint(PersianReshape.reshape(this.getHint().toString()));
        this.setTypeface(Font.MasterFont);
    }

    public EditTextPersian(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetDefaultField();
        TypedArray ListOfTyped = context.obtainStyledAttributes(attrs, R.styleable.EditTextPersian, 0, 0);
        try {
            IsRequired = ListOfTyped.getBoolean(R.styleable.EditTextPersian_IsRequired, false);
            Title = ListOfTyped.getString(R.styleable.EditTextPersian_Title);
            TypeOfValidation = ValidationType.values()[ListOfTyped.getInt(R.styleable.EditTextPersian_ValidationType, 0)];

        } finally {
            ListOfTyped.recycle();
        }
        this.setHint(PersianReshape.reshape(this.getHint().toString()));
        this.setTypeface(Font.MasterFont);
    }

    public EditTextPersian(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetDefaultField();
        TypedArray ListOfTyped = context.obtainStyledAttributes(attrs, R.styleable.EditTextPersian, 0, 0);
        try {
            IsRequired = ListOfTyped.getBoolean(R.styleable.EditTextPersian_IsRequired, false);
            Title = ListOfTyped.getString(R.styleable.EditTextPersian_Title);
            TypeOfValidation = ValidationType.values()[ListOfTyped.getInt(R.styleable.EditTextPersian_ValidationType, 0)];
        } finally {
            ListOfTyped.recycle();
        }
        this.setHint(PersianReshape.reshape(this.getHint().toString()));
        this.setTypeface(Font.MasterFont);
    }

    public void ShowError() {
        if (ValidationErrorMessage != null && ValidationErrorMessage.trim() != "" && !IsValidated) {
            SpannableString SpannableString = new SpannableString(ValidationErrorMessage);
            SpannableString.setSpan(new TypefaceSpan(Font.MasterFont), 0, SpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            requestFocus();
            Utility.ShowKeyboard(this);
            setError(SpannableString, null);
        }
    }

    private void SetDefaultField() {
        IsRequired = false;
        TypeOfValidation = ValidationType.None;
        IsValidated = true;
        ValidationErrorMessage = "";
        Title = "";
    }

    private void CheckValidation(String Text) {
        IsValidated = true;
        ValidationErrorMessage = "";

        if (IsRequired) {
            if (Text.trim().equals("")) {
                IsValidated = false;
                ValidationErrorMessage = getResources().getString(R.string.field_is_required);
                if (Title.trim().equals(""))
                    ValidationErrorMessage = ValidationErrorMessage.replace("{0}", getResources().getString(R.string.value));
                else
                    ValidationErrorMessage = ValidationErrorMessage.replace("{0}", Title);
            }
        }
        if (IsRequired || !Text.trim().equals(""))
        if (TypeOfValidation != null && TypeOfValidation.getRegularExpression() != "" && IsValidated) {
            Pattern CurrentPattern = Pattern.compile(TypeOfValidation.getRegularExpression());
            Matcher CurrentMatcher = CurrentPattern.matcher(Text);
            if (!CurrentMatcher.find()) {
                IsValidated = false;
                ValidationErrorMessage = getResources().getString(TypeOfValidation.getResourceStringId());
            }
        }
    }

    @Override
    public Editable getText() {
        return super.getText();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null)
            super.setText(PersianReshape.reshape(text.toString()), type);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    protected void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
    }


    public static String ConvertNumber(String Text) {

        String answer = Text;
        answer = answer.replace("١", "1");
        answer = answer.replace("٢", "2");
        answer = answer.replace("٣", "3");
        answer = answer.replace("٤", "4");
        answer = answer.replace("٥", "5");
        answer = answer.replace("٦", "6");
        answer = answer.replace("٧", "7");
        answer = answer.replace("٨", "8");
        answer = answer.replace("٩", "9");
        answer = answer.replace("٠", "0");
        return answer;

//        char[] chars = new char[Text.length()];
//        for(int i=0;i<Text.length();i++) {
//            char ch = Text.charAt(i);
//
//
//            if (ch >= 0x0660 && ch <= 0x0669)
//                ch -= 0x0660 - '0';
//            else if (ch >= 0x06f0 && ch <= 0x06F9)
//                ch -= 0x06f0 - '0';
//            chars[i] = ch;
//        }
       // return new String(chars);
    }

}
