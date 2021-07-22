package ensan.patientapp.connection;


import org.json.JSONArray;
import ensan.patientapp.Utils.Constants;
import ensan.patientapp.view.activity.invoice.model.TransactionDetailsResponse;
import ensan.patientapp.view.activity.paymentMethod.model.CheckOutIdResponse;
import ensan.patientapp.view.activity.paymentMethod.model.GetSaveCard;
import ensan.patientapp.view.activity.paymentMethod.model.VerifyDetailsResponse;
import ensan.patientapp.view.activity.bookingdetails.model.BookingDetailsResponse;
import ensan.patientapp.view.activity.bookingdetails.model.CareGiverListResponse;
import ensan.patientapp.view.activity.bookingdetails.model.SaveBookingBody;
import ensan.patientapp.view.activity.bookingdetails.model.SaveBookingResponse;
import ensan.patientapp.view.activity.caregiverprofile.model.CareGiverProfileResponse;
import ensan.patientapp.view.activity.choosedatetime.model.BookingRequest;
import ensan.patientapp.view.activity.choosedatetime.model.FrequencyResponse;
import ensan.patientapp.view.activity.choosedatetime.model.LanguagesResponse;
import ensan.patientapp.view.activity.completeprofile.model.InsuranceDocResponse;
import ensan.patientapp.view.activity.completeprofile.model.ProfileResponse;
import ensan.patientapp.view.activity.completeprofile.model.VerificationDocResponse;
import ensan.patientapp.view.activity.editmedicalhistory.model.UpdateProfileInfoResponse;
import ensan.patientapp.view.activity.familymember.model.FamilyMemberListResponse;
import ensan.patientapp.view.activity.familymember.model.FamilyMemberResponse;
import ensan.patientapp.view.activity.forgotpassword.model.ForgotPassResponse;
import ensan.patientapp.view.activity.forgotpassword.model.ResetPwdResponse;
import ensan.patientapp.view.activity.home.model.CategoryResponse;
import ensan.patientapp.view.activity.home.model.SubCategoryResponse;
import ensan.patientapp.view.activity.idProof.model.IdProofResponse;
import ensan.patientapp.view.activity.idinsurance.model.InsuranceResponse;
import ensan.patientapp.view.activity.notification.model.NotificationResponse;
import ensan.patientapp.view.activity.personaldetail.model.UpdateProfileResponse;
import ensan.patientapp.view.activity.privacypolicy.model.PrivacySecurityResp;
import ensan.patientapp.view.activity.refundpolicy.model.RefundPolicyResp;
import ensan.patientapp.view.activity.savemedicalHistory.model.ActivityResponse;
import ensan.patientapp.view.activity.savemedicalHistory.model.AllergyResponse;
import ensan.patientapp.view.activity.savemedicalHistory.model.DietResponse;
import ensan.patientapp.view.activity.savemedicalHistory.model.EmploymentResponse;
import ensan.patientapp.view.activity.savemedicalHistory.model.MedicalHistoryResponse;
import ensan.patientapp.view.fragment.about.model.AboutResponse;
import ensan.patientapp.view.fragment.addresses.model.DeleteAddressResponse;
import ensan.patientapp.view.fragment.addresses.model.GetAddressesResponse;
import ensan.patientapp.view.fragment.addresses.model.SaveAddressResponse;
import ensan.patientapp.view.fragment.booking.model.CancelBookingResponse;
import ensan.patientapp.view.fragment.booking.model.CurrentBookingResponse;
import ensan.patientapp.view.fragment.personal.model.CoverPicResponse;
import ensan.patientapp.view.activity.login.model.LoginResponse;
import ensan.patientapp.view.activity.signup.model.SignUpResponse;
import ensan.patientapp.view.activity.verification.model.OtpResponse;
import ensan.patientapp.view.fragment.personal.model.ProfilePicResponse;
import ensan.patientapp.view.fragment.personal.model.UserProfileResponse;
import ensan.patientapp.view.fragment.setting.model.ChangeEmailResponse;
import ensan.patientapp.view.fragment.setting.model.ChangeLanguageResp;
import ensan.patientapp.view.activity.review.model.RatingResponse;
import ensan.patientapp.view.fragment.wallet.model.GetTransactionHistoryResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiDataService {

    // login api
    @FormUrlEncoded
    @POST("api/auth/login")
    Call<LoginResponse> GetValidLogin(@Header("Accept") String header, @Field(Constants.KEY_EMAIL) String email, @Field(Constants.KEY_PASSWORD) String password, @Field(Constants.DEVICE_TOKEN) String deviceToken,  @Field(Constants.KEY_LANGUAGE) String language, @Field(Constants.KEY_ROLE) String role);

    // signUp api
    @FormUrlEncoded
    @POST("api/auth/register")
    Call<SignUpResponse> signUpApi(@Header("Accept") String header, @Field(Constants.KEY_FIRST_NAME) String first_name, @Field(Constants.KEY_FAMILY_NAME) String last_name, @Field(Constants.KEY_EMAIL) String email, @Field(Constants.KEY_MOBILE_NUMBER) String phone, @Field(Constants.KEY_PASSWORD) String password, @Field(Constants.KEY_ROLE) String role,
                                   @Field(Constants.KEY_DOB) String dob, @Field(Constants.KEY_LANGUAGE) String language,@Field(Constants.KEY_ID_NUMBER) String id_number, @Field(Constants.KEY_COUNTRY_CODE) String countryCode);


    // otp verification api
    @FormUrlEncoded
    @POST("api/auth/validate_otp")
    Call<OtpResponse> otpVerification(@Header("Accept") String header, @Field(Constants.KEY_OTP) String otp, @Field(Constants.KEY_MOBILE_NUMBER) String phone);

    // otp update profile api
    @FormUrlEncoded
    @POST("api/update_profile")
    Call<ProfileResponse> completeFirstProfile(@Header("Accept") String header,  @Header("Authorization") String Authorization, @Field(Constants.KEY_GENDER) String gender, @Field(Constants.KEY_NATIONALITY) String nationality, @Field(Constants.KEY_MARITAL_STATUS) String marital_status);

    // save family member api
    @FormUrlEncoded
    @POST("api/update_profile_details")
    Call<ProfileResponse> completeSecondProfile(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_NO_OF_CHILD) String no ,@Field(Constants.KEY_HEIGHT) String height, @Field(Constants.KEY_WEIGHT) String weight, @Field(Constants.KEY_BLOOD_GROUP) String b_group, @Field(Constants.KEY_EMERGENCY_NUMBER) String emergency_no/*, @Field(Constants.KEY_FAMILY_NUMBER) String family_id*/);

    // save address screen api
    @Multipart
    @POST("api/save_address")
    Call<ProfileResponse> saveAddress(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part(Constants.KEY_ADDRESS) RequestBody address, @Part(Constants.KEY_PO_BOX) RequestBody poBox, @Part(Constants.KEY_CITY) RequestBody city, @Part(Constants.KEY_COUNTRY) RequestBody country, @Part(Constants.KEY_FAMILY_MEMBER_ID) RequestBody familyNumberId, @Part(Constants.KEY_LONGITUDE) RequestBody longitude, @Part(Constants.KEY_LATITUDE) RequestBody latitude, @Part(Constants.KEY_TAG) RequestBody tag);

    // save family number api
    @FormUrlEncoded
    @POST("api/save_family_member")
    Call<FamilyMemberResponse> saveFamilyMember(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_NAME) String name, @Field(Constants.KEY_RELATION) String relation, @Field(Constants.KEY_MOBILE_NUMBER) String phone, @Field(Constants.KEY_TYPE) String type);

    // get user details api
    @GET("api/auth/user")
    Call<UserProfileResponse> getUserDetails(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // get user details api
    @GET("api/get-family-members")
    Call<FamilyMemberListResponse> getFamilyMember(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // update cover pic api
    @Multipart
    @POST("api/upload-cover-pic")
    Call<CoverPicResponse> updateCoverPic(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part MultipartBody.Part image);

    // update profile pic api
    @Multipart
    @POST("api/upload-profile-pic")
    Call<ProfilePicResponse> updateProfilePic(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part MultipartBody.Part image);

    // verification doc api
    @Multipart
    @POST("api/upload-varification-doc")
    Call<VerificationDocResponse> uploadVerificationPic(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part MultipartBody.Part image);

    // insurance doc api
    @Multipart
    @POST("api/upload-insurance-doc")
    Call<InsuranceDocResponse> uploadInsurancePic(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part MultipartBody.Part image);

    // save medical history
    @Multipart
    @POST("api/save-medical-history")
    Call<MedicalHistoryResponse> saveMedicalHistory(@Header("Accept") String header, @Header("Authorization") String Authorization,@Part(Constants.KEY_MEDICAL) RequestBody keyMedical, @Part(Constants.KEY_DESCRIPTION) RequestBody description , @Part MultipartBody.Part image,  @Part(Constants.KEY_ID) RequestBody id,  @Part(Constants.KEY_FAMILY_NUMBER) RequestBody familyId);

    // add diet
    @FormUrlEncoded
    @POST("api/add-diet")
    Call<DietResponse> addDiet(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_DIET) JSONArray diet,  @Field(Constants.KEY_FAMILY_NUMBER) String familyId);

    // add activity
    @FormUrlEncoded
    @POST("api/add-activity")
    Call<ActivityResponse> addActivity(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_SMOKE) String smoke, @Field(Constants.KEY_EXERCISE) String exercise, @Field(Constants.KEY_FAMILY_NUMBER) String familyId);

    // add Employee
    @FormUrlEncoded
    @POST("api/add-employment")
    Call<EmploymentResponse> addEmployee(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_WORKING) String working, @Field(Constants.KEY_DETAIL) String details, @Field(Constants.KEY_FAMILY_NUMBER) String familyId);

    // add allergy
    @FormUrlEncoded
    @POST("api/add-allergy")
    Call<AllergyResponse> addAllergy(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_ALLERGY) String allergy, @Field(Constants.KEY_DESCRIPTION) String description, @Field(Constants.KEY_FAMILY_NUMBER) String familyId);

    // update patient profile
    @FormUrlEncoded
    @POST("api/update-patient-profile")
    Call<UpdateProfileResponse> updateProfile(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_S_EMAIL) String email, @Field(Constants.KEY_PHONE) String phone,@Field(Constants.KEY_MARITAL_STATUS) String maritalStatus, @Field(Constants.KEY_EMERGENCY_NUMBER) String emergencyNo, @Field(Constants.KEY_CHILDREN) String children, @Field(Constants.KEY_GENDER) String gender,  @Field(Constants.KEY_NATIONALITY) String nationality);

    // reset password api
    @FormUrlEncoded
    @POST("api/auth/reset-password")
    Call<ResetPwdResponse> resetPassword(@Header("Accept") String header, @Field(Constants.KEY_EMAIL) String email, @Field(Constants.KEY_PASSWORD) String password);

    // forgot password api
    @FormUrlEncoded
    @POST("api/auth/forgot-password")
    Call<ForgotPassResponse> forgotPassword(@Header("Accept") String header, @Field(Constants.KEY_EMAIL) String email, @Field(Constants.KEY__WRONG_LANGUAGE) String language, @Field(Constants.KEY_ROLE) String role);

    // category api
    @GET("api/get_categories")
    Call<CategoryResponse> getCategory(@Header("Accept") String header, @Header("Authorization") String Authorization);


    // sub-category api
    @FormUrlEncoded
    @POST("api/get_sub_categories")
    Call<SubCategoryResponse> getSubCategory(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_CAT_ID) String categoryId);

    // frequency api
    @GET("api/get_all_frequency")
    Call<FrequencyResponse> getFrequency(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // all languages api
    @FormUrlEncoded
    @POST("api/all-languages")
    Call<LanguagesResponse> getAllLanguages(@Header("Accept") String header, @Header("Authorization") String Authorization,  @Field(Constants.KEY_LANGUAGE) String language);

    // caregiver list api
    @FormUrlEncoded
    @POST("api/get-care-givers")
    Call<CareGiverListResponse> getCareGiverList(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_BOOKING_FOR) String bookingFor, @Field(Constants.KEY_CAT_ID) String catID, @Field(Constants.KEY_SUB_CAT_ID) JSONArray sub_catID, @Field(Constants.KEY_FROM_DATE) String fromDate, @Field(Constants.KEY_TO_DATE) String toDate
            , @Field(Constants.KEY_TIME) String time, @Field(Constants.KEY_FREQUENCY_ID) String freqID);

    // caregiver list api
    @POST("api/get-care-givers")
    Call<CareGiverListResponse> getCareGiverList(@Header("Accept") String header, @Header("Authorization") String Authorization, @Body BookingRequest bookingRequest);

    // booking api
    @POST("api/save_booking")
    Call<SaveBookingResponse> saveBooking(@Header("Accept") String header, @Header("Authorization") String Authorization,  @Body SaveBookingBody bookingBody);

    // view caregiver profile api
    @FormUrlEncoded
    @POST("api/care-giver-profile")
    Call<CareGiverProfileResponse> getCareGiverProfile(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_CARE_GIVER_ID) String careGiverId);


  /*  // get user details api
    @GET("api/get-family-members")
    Call<FamilyMemberListResponse> getFamilyMember(@Header("Accept") String header, @Header("Authorization") String Authorization);
*/

    // get current booking api
    @GET("api/get-patient-bookings")
    Call<CurrentBookingResponse> getPatientBooking(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // get cancel booking api
    @FormUrlEncoded
    @POST("api/cancel-booking-patient")
    Call<CancelBookingResponse> cancelBooking(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_BOOKING_ID) String bookingId, @Field(Constants.KEY_NOTES) String notes);

    // get past booking api
    @GET("api/get-patient-bookings-history")
    Call<CurrentBookingResponse> getPatientPastBooking(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // get booking detail api
    @FormUrlEncoded
    @POST("api/booking-detail-patient")
    Call<BookingDetailsResponse> getBookingDetail(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_BOOKING_ID) String patientId);

    // get notifications api
    @GET("api/notifications")
    Call<NotificationResponse> getNotification(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // get insurance document api
    @GET("api/get-insurance-doc")
    Call<InsuranceResponse> getInsurance(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // get IdProof api
    @GET("api/get-varification-doc")
    Call<IdProofResponse> getIdProof(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // get medical history
    @FormUrlEncoded
    @POST("api/get-medical-history")
    Call<ensan.patientapp.view.activity.medicalhistory.model.MedicalHistoryResponse> getFamilyMedicalHistory(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_FAMILY_MEMBER_ID) String familyMemberId);

    // save documents api
    @Multipart
    @POST("api/saveCarePatientDocs")
    Call<LoginResponse> saveDocs(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part MultipartBody.Part[] insurance_doc, @Part MultipartBody.Part[] verification_doc,  @Part MultipartBody.Part profile);

    // resend OTP api
    @GET("api/auth/resene_otp")
    Call<OtpResponse> resendOTP(@Header("Accept") String header,@Header("Authorization") String Authorization);

    // change Number api
    @FormUrlEncoded
    @POST("api/auth/change_number")
    Call<OtpResponse> changeNumber(@Header("Accept") String header,@Header("Authorization") String Authorization, @Field(Constants.KEY_MOBILE_NUMBER) String phone, @Field(Constants.KEY_COUNTRY_CODE) String country_code);


    // get about text
    @FormUrlEncoded
    @POST("api/about-app")
    Call<AboutResponse> getAboutText(@Header("Accept") String header, @Header("Authorization") String Authorization,@Field(Constants.KEY__WRONG_LANGUAGE) String language);

    // get privacy and security text
    @FormUrlEncoded
    @POST("api/privacy-policy")
    Call<PrivacySecurityResp> getPrivacyText(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY__WRONG_LANGUAGE) String language);

    // get terms text
    @FormUrlEncoded
    @POST("api/terms-conditions")
    Call<PrivacySecurityResp> getTermsText(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY__WRONG_LANGUAGE) String language);


    // change language api
    @FormUrlEncoded
    @POST("api/change-language")
    Call<ChangeLanguageResp> setLanguage(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_LANGUAGE) String language);

    // get refund policy text
    @FormUrlEncoded
    @POST("api/refund-policy")
    Call<RefundPolicyResp> refundPolicyText(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY__WRONG_LANGUAGE) String language);

    // save address screen api
    @Multipart
    @POST("api/save_address")
    Call<ProfileResponse> saveAddress(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part(Constants.KEY_ADDRESS) RequestBody address, @Part(Constants.KEY_PO_BOX) RequestBody poBox, @Part(Constants.KEY_CITY) RequestBody city, @Part(Constants.KEY_COUNTRY) RequestBody country, @Part(Constants.KEY_FAMILY_MEMBER_ID) RequestBody familyNumberId, @Part(Constants.KEY_LONGITUDE) RequestBody longitude, @Part(Constants.KEY_LATITUDE) RequestBody latitude, @Part(Constants.KEY_LANGUAGES) RequestBody jsonArray
            ,@Part(Constants.KEY_NEIGHBOUR) RequestBody landmark);

    @GET("api/getAddresses")
    Call<GetAddressesResponse> getAddresses(@Header("Accept") String header, @Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST("api/delete_address")
    Call<DeleteAddressResponse> deleteAddress(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_ID) int id);

    @FormUrlEncoded
    @POST("api/save_address")
    Call<SaveAddressResponse> saveNewAddress(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_FAMILY_MEMBER_ID) String family_member_id, @Field(Constants.KEY_ADDRESS) String address, @Field(Constants.KEY_PO_BOX) String po_box, @Field(Constants.KEY_CITY) String city, @Field(Constants.KEY_COUNTRY) String country, @Field(Constants.KEY_LONGITUDE) String longitude, @Field(Constants.KEY_LATITUDE) String latitude, @Field(Constants.KEY_NEIGHBOUR) String neighbour, @Field(Constants.KEY_PRIMARY_ADD) String primary_add, @Field(Constants.KEY_ADD_MORE) String add_more, @Field(Constants.KEY_TAG) String tag);

    // set address as default
    @FormUrlEncoded
    @POST("api/set_default_address")
    Call<SaveAddressResponse> setAddressDefault(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_ADDRESS_ID) String catId);

    // update bloodgroup,height and weight
    @FormUrlEncoded
    @POST("api/updateProfileInfo")
    Call<UpdateProfileInfoResponse> updateProfileInfo(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_BLOOD_GROUP) String bloodGroup, @Field(Constants.KEY_HEIGHT) String height, @Field(Constants.KEY_WEIGHT) String weight);

    // change email
    @FormUrlEncoded
    @POST("api/change_email")
    Call<ChangeEmailResponse> changeEmail(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_EMAIL) String email);

    // validate phone otp
    @FormUrlEncoded
    @POST("api/validateChangeAccount")
    Call<ChangeEmailResponse> phoneOtp(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_MOBILE_NUMBER) String number, @Field(Constants.KEY_OTP) String otp);

    // validate email otp
    @FormUrlEncoded
    @POST("api/validateChangeAccount")
    Call<ChangeEmailResponse> emailOtp(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_EMAIL) String number, @Field(Constants.KEY_OTP) String otp);

    // delete family number api
    @FormUrlEncoded
    @POST("api/delete-family-member")
    Call<FamilyMemberResponse> deleteFamilyMember(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_ID) String id);

    // get payment checkoutId
    @FormUrlEncoded
    @POST("api/checkoutId")
    Call<CheckOutIdResponse> getCheckoutId(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_CARD_TYPE) String cardType);

    // verify card details
    @FormUrlEncoded
    @POST("api/verifyCardDetails")
    Call<VerifyDetailsResponse> verifyCardDetail(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_RESOURCE_PATH) String resourcePath,@Field(Constants.KEY_CARD_TYPE) String cardType);

    // get save card
    @GET("api/getCardDetails")
    Call<GetSaveCard> getSaveCard(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // payment api
    @FormUrlEncoded
    @POST("api/doPayment")
    Call<CheckOutIdResponse> doPayment(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_AMOUNT) String amount, @Field(Constants.KEY_CARD_TOKEN) String cardToken, @Field(Constants.KEY_BOOKING_ID) String bookingId);

    // read notification api
    @FormUrlEncoded
    @POST("api/readNotification")
    Call<NotificationResponse> readNotification(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_NOTIFICATION_ID) String notificationId);

    // save Review api
    @FormUrlEncoded
    @POST("api/saveReview")
    Call<RatingResponse> rating(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_RATING_CAREGIVER_ID) String caregiverId, @Field(Constants.KEY_MESSAGE) String message, @Field(Constants.KEY_RATING) String rating, @Field(Constants.KEY_BOOKING_ID) String bookingId);

    // get payment status
    @FormUrlEncoded
    @POST("api/paymentStatus")
    Call<GetSaveCard> getPaymentStatus(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_RESOURCE_PATH) String resourcePath, @Field(Constants.KEY_BOOKING_ID) String bookingId, @Field(Constants.KEY_CARD_TOKEN) String cardToken,  @Field(Constants.KEY_RE_BOOKING_FOR) String bookingFor);

    // add id proof
    @Multipart
    @POST("api/upload-varification-doc")
    Call<IdProofResponse> saveIdProof(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part MultipartBody.Part[] document);

    // add insurance
    @Multipart
    @POST("api/upload-insurance-doc")
    Call<InsuranceResponse> saveInsurance(@Header("Accept") String header, @Header("Authorization") String Authorization, @Part MultipartBody.Part[] document);

    // Delete Insurance Card
    @FormUrlEncoded
    @POST("api/delete_insurance_doc")
    Call<InsuranceResponse> deleteInsuranceCard(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_ID) String id);

    // Delete Id  Card
    @FormUrlEncoded
    @POST("api/delete_verification_doc")
    Call<IdProofResponse> deleteIDCard(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_ID) String id);


    // get transaction history
    @GET("api/getPatientTransaction")
    Call<GetTransactionHistoryResponse> getTransactionHistory(@Header("Accept") String header, @Header("Authorization") String Authorization);

    // Delete Id  Card
    @FormUrlEncoded
    @POST("api/delete_card")
    Call<VerifyDetailsResponse> deletePaymentCard(@Header("Accept") String header, @Header("Authorization") String Authorization, @Field(Constants.KEY_ID) String id);


    // get transaction details
    @FormUrlEncoded
    @POST("api/getTransactionDetail")
    Call<TransactionDetailsResponse> getTransactionDetails(@Header("Accept") String header, @Header("Authorization") String Authorization,  @Field(Constants.KEY_BOOKING_ID) String bookingID);

    // resend OTP api
    @FormUrlEncoded
    @POST("/api/auth/forgot-resend")
    Call<OtpResponse> resend( @Field(Constants.KEY_EMAIL) String email, @Field(Constants.KEY__WRONG_LANGUAGE) String language, @Field(Constants.KEY_ROLE) String role);

    // verify login OTP
    @FormUrlEncoded
    @POST("api/verify_login")
    Call<LoginResponse> getLoginVerifyOTP(@Header("Accept") String header, @Header("Authorization") String Authorization,  @Field(Constants.KEY_OTP) String otp);

}
