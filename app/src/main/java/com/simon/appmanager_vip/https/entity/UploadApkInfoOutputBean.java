package com.simon.appmanager_vip.https.entity;

/**
 * 上传apk
 */
public class UploadApkInfoOutputBean {

    /**
     * status : 200
     * code : 200
     * state : 200
     * msg : 请求成功
     * name : 157632582588487.apk
     * old_name : hongqi.apk
     * pic : https://open.faw-hongqiacademy.com/Uploads/Website/157632582588487.apk
     * url : https://open.faw-hongqiacademy.com/Uploads/Website/157632582588487.apk
     * data : {"url":"https://open.faw-hongqiacademy.com/Uploads/Website/157632582588487.apk"}
     */

    private int status;
    private int code;
    private int state;
    private String msg;
    private String name;
    private String old_name;
    private String pic;
    private String url;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOld_name() {
        return old_name;
    }

    public void setOld_name(String old_name) {
        this.old_name = old_name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * url : https://open.faw-hongqiacademy.com/Uploads/Website/157632582588487.apk
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
