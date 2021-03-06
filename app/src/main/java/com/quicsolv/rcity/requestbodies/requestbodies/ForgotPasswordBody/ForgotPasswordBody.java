package com.quicsolv.rcity.requestbodies.requestbodies.ForgotPasswordBody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordBody {

@SerializedName("emailId")
@Expose
private String emailId;
@SerializedName("password")
@Expose
private String password;

public String getEmailId() {
return emailId;
}

public void setEmailId(String emailId) {
this.emailId = emailId;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

}