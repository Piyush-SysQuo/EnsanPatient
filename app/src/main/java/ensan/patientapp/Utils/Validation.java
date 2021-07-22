package ensan.patientapp.Utils;

import android.content.Context;

public class Validation {
    private Context applicationContext;

    public Validation(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

//
//    public boolean isValidateForLogin(ActivityMainBinding activityMainBinding) {
//
//        if (activityMainBinding.emailEt.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter your registered email id", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (activityMainBinding.passwordEt.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter your registered password", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    public boolean CreateEmployeeMethod(CreateEmployeeActivityBinding createEmployeeActivityBinding) {
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//        if (createEmployeeActivityBinding.empId.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter Employee Id", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.empId.getText().toString().trim().length() < 4 ||
//                createEmployeeActivityBinding.empId.getText().toString().trim().length() > 12) {
//            Toast.makeText(applicationContext, "Employee ID must be between 4 to 12 digits", Toast.LENGTH_SHORT).show();
//            return false;
//
////        }else if (createEmployeeActivityBinding.designation.getText().toString().trim().isEmpty()){
////            Toast.makeText(applicationContext, "Please enter Employee Designation", Toast.LENGTH_SHORT).show();
////            return false;
//        } else if (createEmployeeActivityBinding.employeeName.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter Employee Name", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.fatherName.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter Employee Father's Name", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.motherName.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter Employee Mother's Name", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.dob.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter Employee DOB", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.aadharCard.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter Employee Aadhar Card Number", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.aadharCard.getText().toString().trim().length() < 12) {
//            Toast.makeText(applicationContext, "Please enter valid Employee Aadhar Card Number", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.phoneNumber.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter Employee Contact number", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.phoneNumber.getText().toString().trim().length() < 10) {
//            Toast.makeText(applicationContext, "Please enter Valid Employee Contact number", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (createEmployeeActivityBinding.email.getText().toString().trim().isEmpty()) {
//            Toast.makeText(applicationContext, "Please enter Employee Email Id", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(createEmployeeActivityBinding.email.getText().toString().trim()).matches()) {
//            Toast.makeText(applicationContext, "Please enter valid Email Id", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
}
