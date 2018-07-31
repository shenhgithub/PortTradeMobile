package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/3/24.
 */

/**
 * 个人信息的数据结构
 *
 * @author 超悟空
 * @version 1.0 2015/3/24
 * @since 1.0
 */
public class PersonalInfo {

    /**
     * 用户唯一标识符
     */
    private String userID = null;

    /**
     * 用户电话
     */
    private String phone = null;

    /**
     * 用户手机
     */
    private String mobile = null;

    /**
     * 用户邮箱
     */
    private String email = null;

    /**
     * 设置用户标识符
     *
     * @param userID 用户标识字符串
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 获取用户ID
     *
     * @return 用户标识字符串
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户电话
     *
     * @param phone 电话号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 设置用户手机
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 设置用户邮箱
     *
     * @param email 邮箱号
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取用户电话
     *
     * @return 电话号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 获取用户手机
     *
     * @return 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 获取用户邮箱
     *
     * @return 邮箱号
     */
    public String getEmail() {
        return email;
    }
}
