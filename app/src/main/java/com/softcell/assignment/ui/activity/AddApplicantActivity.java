package com.softcell.assignment.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.softcell.assignment.R;
import com.softcell.assignment.api.presenter.ApplicantPresenter;
import com.softcell.assignment.api.view.AddApplicantView;
import com.softcell.assignment.base.BaseActivity;
import com.softcell.assignment.model.User;
import com.softcell.assignment.networking.NetworkError;
import com.softcell.assignment.utils.CustomRangeFilter;
import com.softcell.assignment.utils.Regex;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class AddApplicantActivity extends BaseActivity implements AddApplicantView {

    @BindView(R.id.first_name)
    TextInputEditText firstName;
    @BindView(R.id.first_name_layout)
    TextInputLayout firstNameLayout;
    @BindView(R.id.last_name)
    TextInputEditText lastName;
    @BindView(R.id.last_name_layout)
    TextInputLayout lastNameLayout;
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.loan_amount)
    TextInputEditText loanAmount;
    @BindView(R.id.loan_amount_layout)
    TextInputLayout loanAmountLayout;
    @BindView(R.id.pan_number)
    TextInputEditText panNumber;
    @BindView(R.id.pan_number_layout)
    TextInputLayout panNumberLayout;
    @BindView(R.id.aadhaar_number)
    TextInputEditText aadhaarNumber;
    @BindView(R.id.aadhaar_layout)
    TextInputLayout aadhaarLayout;
    @BindView(R.id.voter_id)
    TextInputEditText voterId;
    @BindView(R.id.voter_id_layout)
    TextInputLayout voterIdLayout;
    @BindView(R.id.login_layout)
    LinearLayout loginLayout;
    @BindView(R.id.add_applicant)
    Button addApplicant;

    private double lat, lng;
    private ApplicantPresenter applicantPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_applicant);
        ButterKnife.bind(this);
        applicantPresenter = new ApplicantPresenter(this, this);
        loanAmount.setFilters(new InputFilter[]{new CustomRangeFilter(1, 100000)});
    }

    @OnClick(R.id.add_applicant)
    public void onViewClicked() {
        if (formValid()) {
            submitForm();
        }
    }


    private void submitForm() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            getLtLng();
        } else {
            EasyPermissions.requestPermissions(this, "", 1, Manifest.permission.ACCESS_FINE_LOCATION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onViewClicked();
    }

    private boolean formValid() {
        boolean valid = true;

        if (TextUtils.isEmpty(firstName.getText()) || !Pattern.compile(Regex.NAME_REGEX).matcher(Objects.requireNonNull(firstName.getText()).toString()).matches()) {
            firstNameLayout.setErrorEnabled(true);
            firstNameLayout.setError("Name should be min 3 to max 30 chars long.");
            valid = false;
        } else {
            firstNameLayout.setError("");
            firstNameLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(lastName.getText()) || !Pattern.compile(Regex.NAME_REGEX).matcher(Objects.requireNonNull(lastName.getText()).toString()).matches()) {
            lastNameLayout.setErrorEnabled(true);
            lastNameLayout.setError("Name should be min 3 to max 30 chars long.");
            valid = false;
        } else {
            lastNameLayout.setError("");
            lastNameLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(email.getText()) || !Pattern.compile(Patterns.EMAIL_ADDRESS.pattern()).matcher(Objects.requireNonNull(email.getText()).toString()).matches()) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError(getString(R.string.enter_valid_email));
            valid = false;
        } else {
            emailLayout.setError(null);
            emailLayout.setErrorEnabled(false);

        }

        if (!TextUtils.isEmpty(this.loanAmount.getText())) {
            int loanAmount = Integer.parseInt(String.valueOf(this.loanAmount.getText()));
            loanAmountLayout.setError(null);
            loanAmountLayout.setErrorEnabled(false);


            if (loanAmount < 30000) {
                //one of pan/aadhaar/voter
                if (!TextUtils.isEmpty(panNumber.getText())) {

                    valid = validatePan();

                } else if (!TextUtils.isEmpty(aadhaarNumber.getText())) {
                    valid = validateAadhaar();
                } else if (!TextUtils.isEmpty(voterId.getText())) {
                    valid = validateVoterId();
                } else {
                    Toast.makeText(this, "Please enter value for at least one: PAN/ AADHAAR/ VOTER ID", Toast.LENGTH_LONG).show();
                }
            } else if (loanAmount < 50000) {
                //pan/aadhaar
                if (!TextUtils.isEmpty(panNumber.getText())) {
                    valid = validatePan();
                } else if (!TextUtils.isEmpty(aadhaarNumber.getText())) {
                    valid = validateAadhaar();
                } else {
                    Toast.makeText(this, "Please enter value for at least one: PAN/ AADHAAR", Toast.LENGTH_LONG).show();
                }
            } else if (!TextUtils.isEmpty(panNumber.getText())) {
                //PAN
                valid = validatePan();
            } else {
                Toast.makeText(this, "Please enter value for PAN card", Toast.LENGTH_LONG).show();
            }

        } else {
            loanAmountLayout.setErrorEnabled(true);
            loanAmountLayout.setError(getString(R.string.enter_loan_amount));
            valid = false;
        }

        if (valid) {
            clearAllErrors();
        }

        return valid;
    }

    private void clearAllErrors() {
        firstNameLayout.setErrorEnabled(false);
        lastNameLayout.setErrorEnabled(false);
        emailLayout.setErrorEnabled(false);
        loanAmountLayout.setErrorEnabled(false);
        panNumberLayout.setErrorEnabled(false);
        aadhaarLayout.setErrorEnabled(false);
        voterIdLayout.setErrorEnabled(false);
    }

    private void getLtLng() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please provide location permission", Toast.LENGTH_LONG).show();
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(AddApplicantActivity.this, location -> {
            lat = location.getLatitude();
            lng = location.getLongitude();

            User user = new User();

            user.setFirstName(Objects.requireNonNull(firstName.getText()).toString());
            user.setLastName(Objects.requireNonNull(lastName.getText()).toString());
            user.setEmail(Objects.requireNonNull(email.getText()).toString());
            user.setLoanAmount(Integer.parseInt(String.valueOf(loanAmount.getText())));
            user.setPanCardNumber(Objects.requireNonNull(panNumber.getText()).toString());
            user.setAadhaarCardNumber(Objects.requireNonNull(aadhaarNumber.getText()).toString());
            user.setVoterIdNumber(Objects.requireNonNull(voterId.getText()).toString());

            user.setLatitude(lat);
            user.setLongitude(lng);

            applicantPresenter.addApplicant(user);
        });
    }

    private boolean validatePan() {
        boolean valid = true;
        if (!Pattern.compile(Regex.PAN_CARD_REGEX).matcher(Objects.requireNonNull(panNumber.getText()).toString()).matches()) {
            panNumberLayout.setErrorEnabled(true);
            panNumberLayout.setError(getString(R.string.enter_valid_pan_card_number));
            valid = false;
        } else {
            panNumberLayout.setErrorEnabled(false);
            panNumberLayout.setError(null);
        }
        return valid;
    }

    private boolean validateAadhaar() {
        boolean valid = true;
        if (!Pattern.compile(Regex.AADHAAR_CARD_REGEX).matcher(Objects.requireNonNull(aadhaarNumber.getText()).toString()).matches()) {
            aadhaarLayout.setErrorEnabled(true);
            aadhaarLayout.setError(getString(R.string.enter_valid_aadhaar_card_number));
            valid = false;
        }
        return valid;
    }

    private boolean validateVoterId() {
        boolean valid = true;
        if (!Pattern.compile(Regex.VOTER_ID_REGEX).matcher(Objects.requireNonNull(voterId.getText()).toString()).matches()) {
            voterIdLayout.setErrorEnabled(true);
            voterIdLayout.setError(getString(R.string.enter_valid_voter_card_number));
            valid = false;
        }
        return valid;
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void onApplicantAdded(User user) {
        finish();
    }

    @Override
    public void onApplicantsReceived(ArrayList<User> users) {

    }

    @Override
    public void failed(NetworkError error) {

    }
}
