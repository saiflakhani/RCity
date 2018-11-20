package com.quicsolv.rcity.responses.LoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

@SerializedName("contact")
@Expose
private Contact contact;
@SerializedName("loyalty")
@Expose
private Loyalty loyalty;
@SerializedName("_id")
@Expose
private Integer id;
@SerializedName("firstName")
@Expose
private String firstName;
@SerializedName("lastName")
@Expose
private String lastName;
@SerializedName("gender")
@Expose
private String gender;
@SerializedName("age")
@Expose
private String age;
@SerializedName("password")
@Expose
private String password;
@SerializedName("isDeleted")
@Expose
private Boolean isDeleted;

public Contact getContact() {
return contact;
}

public void setContact(Contact contact) {
this.contact = contact;
}

public Loyalty getLoyalty() {
return loyalty;
}

public void setLoyalty(Loyalty loyalty) {
this.loyalty = loyalty;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getFirstName() {
return firstName;
}

public void setFirstName(String firstName) {
this.firstName = firstName;
}

public String getLastName() {
return lastName;
}

public void setLastName(String lastName) {
this.lastName = lastName;
}

public String getGender() {
return gender;
}

public void setGender(String gender) {
this.gender = gender;
}

public String getAge() {
return age;
}

public void setAge(String age) {
this.age = age;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public Boolean getIsDeleted() {
return isDeleted;
}

public void setIsDeleted(Boolean isDeleted) {
this.isDeleted = isDeleted;
}

}