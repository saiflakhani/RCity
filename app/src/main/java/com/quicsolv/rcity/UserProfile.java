package com.quicsolv.rcity;

import java.io.Serializable;

class UserProfile implements Serializable {
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

        public String getFullName() {
            return firstName + " " + lastName;
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

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getBalancePoints() {
            return balancePoints;
        }

        public void setBalancePoints(String balancePoints) {
            this.balancePoints = balancePoints;
        }

        public String getRedeemedPoints() {
            return redeemedPoints;
        }

        public void setRedeemedPoints(String redeemedPoints) {
            this.redeemedPoints = redeemedPoints;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        UserProfile() {
            userId = "NaN";
            firstName = "John";
            lastName = "Doe";
            gender = "";
            age = "969";
            emailId = "";
            phoneNo = "";
            balancePoints = "";
            redeemedPoints = "";

        }

        private String userId;
        private String firstName;
        private String lastName;
        private String fullName;
        private String gender;
        private String age;
        private String emailId;
        private String phoneNo;
        private String balancePoints;
        private String redeemedPoints;

    }