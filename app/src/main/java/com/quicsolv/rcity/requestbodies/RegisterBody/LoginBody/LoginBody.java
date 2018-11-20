package com.quicsolv.rcity.requestbodies.RegisterBody.LoginBody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBody {

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