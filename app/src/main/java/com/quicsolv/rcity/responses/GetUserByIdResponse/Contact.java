package com.quicsolv.rcity.responses.GetUserByIdResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contact {

@SerializedName("phoneNo")
@Expose
private String phoneNo;
@SerializedName("emailId")
@Expose
private String emailId;

public String getPhoneNo() {
return phoneNo;
}

public void setPhoneNo(String phoneNo) {
this.phoneNo = phoneNo;
}

public String getEmailId() {
return emailId;
}

public void setEmailId(String emailId) {
this.emailId = emailId;
}

}