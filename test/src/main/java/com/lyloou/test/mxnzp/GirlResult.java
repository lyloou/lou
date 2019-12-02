package com.lyloou.test.mxnzp;

import java.io.Serializable;
import java.util.List;

public class GirlResult implements Serializable {

    /**
     * code : 1
     * msg : 数据返回成功，现已提供app_id方式请求接口，不限速，不限流，不封IP，可在自建服务器调用api，欢迎升级使用，详情请访问：https://github.com/MZCretin/RollToolsApi#%E8%A7%A3%E9%94%81%E6%96%B0%E6%96%B9%E5%BC%8F
     * data : {"page":0,"totalCount":1563,"totalPage":157,"limit":10,"list":[{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqly1g04lsmmadlj31221vowz7.jpg","imageSize":"1370x2436","imageFileLength":774119},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg","imageSize":"1024x1319","imageFileLength":355653},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqgy1fxno2dvxusj30sf10nqcm.jpg","imageSize":"1023x1319","imageFileLength":367361},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg","imageSize":"978x1275","imageFileLength":205050},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqgy1fze94uew3jj30qo10cdka.jpg","imageSize":"960x1308","imageFileLength":174514},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqgy1fy58bi1wlgj30sg10hguu.jpg","imageSize":"1024x1313","imageFileLength":350731},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg","imageSize":"1024x1365","imageFileLength":257411},{"imageUrl":"http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg","imageSize":"828x1242","imageFileLength":228162},{"imageUrl":"https://ww1.sinaimg.cn/large/0065oQSqly1g2hekfwnd7j30sg0x4djy.jpg","imageSize":"1024x1192","imageFileLength":162237},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqgy1fxd7vcz86nj30qo0ybqc1.jpg","imageSize":"960x1235","imageFileLength":345840}]}
     */

    private int code;
    private String msg;
    private Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable {
        /**
         * page : 0
         * totalCount : 1563
         * totalPage : 157
         * limit : 10
         * list : [{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqly1g04lsmmadlj31221vowz7.jpg","imageSize":"1370x2436","imageFileLength":774119},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg","imageSize":"1024x1319","imageFileLength":355653},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqgy1fxno2dvxusj30sf10nqcm.jpg","imageSize":"1023x1319","imageFileLength":367361},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg","imageSize":"978x1275","imageFileLength":205050},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqgy1fze94uew3jj30qo10cdka.jpg","imageSize":"960x1308","imageFileLength":174514},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqgy1fy58bi1wlgj30sg10hguu.jpg","imageSize":"1024x1313","imageFileLength":350731},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg","imageSize":"1024x1365","imageFileLength":257411},{"imageUrl":"http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg","imageSize":"828x1242","imageFileLength":228162},{"imageUrl":"https://ww1.sinaimg.cn/large/0065oQSqly1g2hekfwnd7j30sg0x4djy.jpg","imageSize":"1024x1192","imageFileLength":162237},{"imageUrl":"https://ws1.sinaimg.cn/large/0065oQSqgy1fxd7vcz86nj30qo0ybqc1.jpg","imageSize":"960x1235","imageFileLength":345840}]
         */

        private int page;
        private int totalCount;
        private int totalPage;
        private int limit;
        private List<Girl> list;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public List<Girl> getList() {
            return list;
        }

        public void setList(List<Girl> list) {
            this.list = list;
        }

        public static class Girl implements Serializable {
            /**
             * imageUrl : https://ws1.sinaimg.cn/large/0065oQSqly1g04lsmmadlj31221vowz7.jpg
             * imageSize : 1370x2436
             * imageFileLength : 774119
             */

            private String imageUrl;
            private String imageSize;
            private int imageFileLength;

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getImageSize() {
                return imageSize;
            }

            public void setImageSize(String imageSize) {
                this.imageSize = imageSize;
            }

            public int getImageFileLength() {
                return imageFileLength;
            }

            public void setImageFileLength(int imageFileLength) {
                this.imageFileLength = imageFileLength;
            }
        }
    }
}
