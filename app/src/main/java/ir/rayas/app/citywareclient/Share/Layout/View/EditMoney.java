package ir.rayas.app.citywareclient.Share.Layout.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.rayas.app.citywareclient.R;
import ir.rayas.app.citywareclient.Share.Enum.ValidationType;
import ir.rayas.app.citywareclient.Share.Helper.TypefaceSpan;
import ir.rayas.app.citywareclient.Share.Layout.Font.Font;
import ir.rayas.app.citywareclient.Share.Layout.Font.PersianReshape;
import ir.rayas.app.citywareclient.Share.Utility.Utility;

/**
 * Created by Programmer on 10/14/2018.
 */

public class EditMoney extends android.support.v7.widget.AppCompatEditText {
    private static final int MAX_LENGTH = 20;
    private static final int MAX_DECIMAL = 3;
    private boolean IsRequired = false;
    private boolean IsValidated = true;
    private String ValidationErrorMessage = "";
    private String Title = "";

    public EditMoney(Context context) {
        super(context);
        Init();
    }

    public EditMoney(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
        TypedArray ListOfTyped = context.obtainStyledAttributes(attrs, R.styleable.EditTextPersian, 0, 0);
        try {
            IsRequired = ListOfTyped.getBoolean(R.styleable.EditTextPersian_IsRequired, false);
            Title = ListOfTyped.getString(R.styleable.EditTextPersian_Title);
        } finally {
            ListOfTyped.recycle();
        }
    }

    public EditMoney(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
        TypedArray ListOfTyped = context.obtainStyledAttributes(attrs, R.styleable.EditTextPersian, 0, 0);
        try {
            IsRequired = ListOfTyped.getBoolean(R.styleable.EditTextPersian_IsRequired, false);
            Title = ListOfTyped.getString(R.styleable.EditTextPersian_Title);
        } finally {
            ListOfTyped.recycle();
        }

    }

    private void Init(){
        addTextChangedListener(new ThousandNumberTextWatcher(this));
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setSingleLine(true);
        this.setHint(PersianReshape.reshape(this.getHint().toString()));
        this.setTypeface(Font.MasterFont);
    }

    public boolean isValidated() {
        if (getText() != null) {
            CheckValidation(getText().toString().trim());
        } else {
            CheckValidation("");
        }
        return IsValidated;
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

    private static class ThousandNumberTextWatcher implements TextWatcher {

        private EditText mEditText;

        ThousandNumberTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String originalString = editable.toString();
            String cleanString = originalString.replaceAll("[,]", "");
            cleanString = cleanString.replaceAll("[٬]", "");
            if (cleanString.isEmpty()) {
                return;
            }
            String formattedString = getFormatString(cleanString);

            mEditText.removeTextChangedListener(this);
            mEditText.setText(formattedString);
            mEditText.setSelection(mEditText.getText().length());
            mEditText.addTextChangedListener(this);
        }

        /**
         * Return the format string
         */
        private String getFormatString(String cleanString) {
            if (cleanString.contains(".")) {
                return formatDecimal(cleanString);
            } else {
                return formatInteger(cleanString);
            }
        }

        private String formatInteger(String str) {
            BigDecimal parsed = new BigDecimal(str);
            DecimalFormat formatter;
            formatter = new DecimalFormat("#,###");
            return formatter.format(parsed);
        }

        private String formatDecimal(String str) {
            if (str.equals(".")) {
                return ".";
            }
            BigDecimal parsed = new BigDecimal(str);
            DecimalFormat formatter;
            formatter =
                    new DecimalFormat("#,###." + getDecimalPattern(str)); //example patter #,###.00
            return formatter.format(parsed);
        }

        /**
         * It will return suitable pattern for format decimal
         * For example: 10.2 -> return 0 | 10.23 -> return 00 | 10.235 -> return 000
         */
        private String getDecimalPattern(String str) {
            int decimalCount = str.length() - 1 - str.indexOf(".");
            StringBuilder decimalPattern = new StringBuilder();
            for (int i = 0; i < decimalCount && i < MAX_DECIMAL; i++) {
                decimalPattern.append("0");
            }
            return decimalPattern.toString();
        }
    }
}
