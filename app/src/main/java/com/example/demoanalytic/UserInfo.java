package com.example.demoanalytic;


        import java.io.Serializable;

public class UserInfo implements Serializable {

    /**
     * birthday : 2018-2-5
     * phone : 110
     * sex : 0
     * address : 北京
     * nickName : 12
     * userId : minApp100001
     * headPortrait : null
     */

    private String birthday;
    private String phone;
    private String sex;
    private String address;
    private String nickName;
    private String userId;
    private String headPortrait;
    private String ClientId;

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
}
