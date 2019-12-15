package com.simon.appmanager_vip.https.entity;

/**
 * 更新apk
 */
public class UpdateApkInfoInputBean {
    private String file;
    private String type;
    private String vresion;
    private String introduce;

    @Override
    public String toString() {
        return "UpdateApkInfoOutputBean{" +
                "file='" + file + '\'' +
                ", type='" + type + '\'' +
                ", vresion='" + vresion + '\'' +
                ", introduce='" + introduce + '\'' +
                '}';
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVresion() {
        return vresion;
    }

    public void setVresion(String vresion) {
        this.vresion = vresion;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
