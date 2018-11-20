package com.quicsolv.rcity.requestbodies.RegisterBody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterBody {

    public RegisterBody() {
        Loyalty loyalty = new Loyalty();
        loyalty.setRedeemed("0");
        loyalty.setBalance("100");
        this.loyalty = loyalty;
        isDeleted = "false";
    }

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
@SerializedName("contact")
@Expose
private Contact contact;
@SerializedName("loyalty")
@Expose
private Loyalty loyalty;
@SerializedName("password")
@Expose
private String password;
@SerializedName("isDeleted")
@Expose
private String isDeleted;

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

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public String getIsDeleted() {
return isDeleted;
}

public void setIsDeleted(String isDeleted) {
this.isDeleted = isDeleted;
}

}