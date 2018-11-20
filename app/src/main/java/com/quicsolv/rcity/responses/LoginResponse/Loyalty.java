package com.quicsolv.rcity.responses.LoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loyalty {

@SerializedName("balance")
@Expose
private Integer balance;
@SerializedName("redeemed")
@Expose
private Integer redeemed;

public Integer getBalance() {
return balance;
}

public void setBalance(Integer balance) {
this.balance = balance;
}

public Integer getRedeemed() {
return redeemed;
}

public void setRedeemed(Integer redeemed) {
this.redeemed = redeemed;
}

}