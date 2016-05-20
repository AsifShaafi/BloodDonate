package com.example.shaafi.blooddonate;

/**
 * Created by Shaafi on 31-Jan-16, 2016.
 */
public class MemberInfo {

    private String userFirstName, userLastName, userPhone, userAge, userBlood, userLocation, userActive;


    public MemberInfo(String userFirstName, String userLastName, String userPhone, String userAge, String userBlood, String userLocation, String userActive) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userPhone = userPhone;
        this.userAge = userAge;
        this.userBlood = userBlood;
        this.userLocation = userLocation;
        this.userActive = userActive;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserBlood() {
        return userBlood;
    }

    public void setUserBlood(String userBlood) {
        this.userBlood = userBlood;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserActive() {
        return userActive;
    }

    public void setUserActive(String userActive) {
        this.userActive = userActive;
    }
}
