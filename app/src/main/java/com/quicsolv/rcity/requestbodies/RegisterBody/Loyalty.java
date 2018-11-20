package com.quicsolv.rcity.requestbodies.RegisterBody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loyalty {

@SerializedName("balance")
@Expose
private String balance;
@SerializedName("redeemed")
@Expose
private String redeemed;

public String getBalance() {
return balance;
}

public void setBalance(String balance) {
this.balance = balance;
}

public String getRedeemed() {
return redeemed;
}

public void setRedeemed(String redeemed) {
this.redeemed = redeemed;
}

}