package com.info.web.copys.pojo;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserContacts {
    private static final Pattern EMOJI = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    private String id;
    private String userId;
    private String userName;
    private String contactName;
    private String contactPhone;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        if (StringUtils.isNotBlank(contactName)) {
            Matcher emojiMatcher = EMOJI.matcher(contactName);
            if (emojiMatcher.find()) {
                contactName = emojiMatcher.replaceAll("*");
            }
        }
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserContacts [contactName=" + contactName + ", contactPhone="
                + contactPhone + ", createTime=" + createTime + ", id=" + id
                + ", userId=" + userId + ", userName=" + userName + "]";
    }

}
