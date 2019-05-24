package com.yijia.common_yijia.bean;

/**
 * @author JiaLei
 * Created on 2019/5/24 16:35
 * ━━━━━━神兽出没━━━━━━
 * 　　┏┓　　　┏┓+ +
 * 　┏┛┻━━━┛┻┓ + +
 * 　┃　　　　　　　┃
 * 　┃　　　━　　　┃ ++ + + +
 * ████━████ ┃+
 * 　┃　　　　　　　┃ +
 * 　┃　　　┻　　　┃
 * 　┃　　　　　　　┃ + +
 * 　┗━┓　　　┏━┛
 * 　　　┃　　　┃
 * 　　　┃　　　┃ + + + +
 * 　　　┃　　　┃
 * 　　　┃　　　┃ +  神兽保佑
 * 　　　┃　　　┃    代码无bug
 * 　　　┃　　　┃　　+
 * 　　　┃　 　　┗━━━┓ + +
 * 　　　┃ 　　　　　　　┣┓
 * 　　　┃ 　　　　　　　┏┛
 * 　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　┃┫┫　┃┫┫
 * 　　　　┗┻┛　┗┻┛+ + + +
 * ━━━━━━感觉萌萌哒━━━━━━
 */
public class VersionCheckBean extends BaseBean {
    /**
     * data : {"versionCode":2,"versionName":"1.0.1","hasNewVersion":1,"isForce":2,"updateUrl":"www.hao123.com"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * versionCode : 2
         * versionName : 1.0.1
         * hasNewVersion : 1
         * isForce : 2
         * updateUrl : www.hao123.com
         */

        private long versionCode;
        private String versionName;
        private int hasNewVersion;
        private int isForce;
        private String updateUrl;

        public long getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(long versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getHasNewVersion() {
            return hasNewVersion;
        }

        public void setHasNewVersion(int hasNewVersion) {
            this.hasNewVersion = hasNewVersion;
        }

        public int getIsForce() {
            return isForce;
        }

        public void setIsForce(int isForce) {
            this.isForce = isForce;
        }

        public String getUpdateUrl() {
            return updateUrl;
        }

        public void setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
        }
    }
}
