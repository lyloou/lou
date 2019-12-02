package com.lyloou.test.mxnzp;

import java.io.Serializable;
import java.util.List;

public class JokeResult implements Serializable {

    /**
     * code : 1
     * msg : 数据返回成功，现已提供app_id方式请求接口，不限速，不限流，不封IP，可在自建服务器调用api，欢迎升级使用，详情请访问：https://github.com/MZCretin/RollToolsApi#%E8%A7%A3%E9%94%81%E6%96%B0%E6%96%B9%E5%BC%8F
     * data : {"page":55,"totalCount":35899,"totalPage":3590,"limit":10,"list":[{"content":"\u200d\u200d皇上久闻李白才名。 皇上：\u201c李爱卿，朕命你写诗助兴。\u201d 李白：\u201c皇上，写诗可以，还请贵妃为我磨墨。\u201d 皇上：\u201c哈哈，才子就是才子，要求都这么独特！来人那，上馍馍，贵妃你就喂李白吃几个吧！\u201d 李白：\u201c\u2026\u2026\u201d\u200d\u200d","updateTime":"2019-11-30 16:59:07"},{"content":"\u200d\u200d一个书生，一个将军，一个富翁，一个乞丐，四人分别做了句诗。 书生：\u201c大雪纷纷落地。\u201d 将军：\u201c是我皇家福气。\u201d 富翁：\u201c下它三日何妨。\u201d 乞丐：\u201c放你妈的狗屁。\u201d\u200d\u200d","updateTime":"2019-11-30 16:59:07"},{"content":"\u200d\u200d\u200d\u200d一新兵去潜水艇里服役，初来，有点不适应，四周黑漆漆的一片更让他感到压抑。 于是顺着某个地方开始摸索着些什么。 长官恰好从他身边经过，就向他问道:\u201c你在干什么？\u201d 新兵捂着胸口立马答道：\u201c报告长官，我有点气闷，想打开窗户透透气！\u201d\u200d\u200d\u200d\u200d","updateTime":"2019-11-30 16:59:06"},{"content":"\u200d\u200d老公对体育赛事一向不怎么感冒。 但是我想奥运会应该可以另当别论吧。 于是刚才很兴奋地问他：\u201c后天晚上要不要一起看开幕式？\u201d 他一脸蓦然地看着我：\u201c开谁的墓？\u201d\u200d\u200d","updateTime":"2019-11-30 16:59:05"},{"content":"\u200d\u200d\u200d\u200d过马路，没红绿灯。 上班高峰期，奈何我们这种屌丝没车。 足足等了数分钟过不了马路，突见一白衣大侠不顾车流，显身拉着我就走。 大爷，谢谢啊。\u200d\u200d\u200d\u200d","updateTime":"2019-11-30 16:59:04"},{"content":"\u200d\u200d每次看完西游记我都会想：\u201c为什么女儿国的水一喝就怀孕呢？\u201d 现在再想想，我觉得那应该不是水。 我是不是知道的太多了。\u200d\u200d","updateTime":"2019-11-30 16:59:04"},{"content":"\u200d\u200d老姐1.63米，60kg。 决定要减肥，坚持了半个月，腰和腿还是不变，胸明显小了。 想想既然减不了别处，还是吃回来吧！ 于是脱缰的吃了一个星期，腰和腿粗了，胸还是没回来。 已经一天没出房间了。\u200d\u200d","updateTime":"2019-11-30 16:59:03"},{"content":"\u200d\u200d\u200d\u200d二科和他女友分手半年了，一直在疗伤。 结果今早看见前女友的QQ签名改成了：\u201c今天是和老公一周年纪念日，我们去HAPPY！\u201d\u200d\u200d\u200d\u200d","updateTime":"2019-11-30 16:59:02"},{"content":"\u200d\u200d\u200d\u200d哥们给介绍一对象，见面前特意叮嘱要穿好一点。 我问他：\u201c怎么穿好一点？\u201d 他说：\u201c穿最贵那件。\u201d 第二天我穿着我那件波司登羽绒服就去了。 结果那女的见了我转身就走了，我都没摸清情况。 这么热的天，我热得跟狗一样。 你倒好，招呼都不打就走了，也太没礼礼貌了。\u200d\u200d\u200d\u200d","updateTime":"2019-11-30 16:59:01"},{"content":"\u200d\u200d一参加面试的女生信步走进了考场。 主考官开始向她宣读面试的注意事项，并微笑着安慰道：\u201c你不要紧张！\u201d 女生闻听，自信地接话道：\u201c我不会紧张的！培训时老师说了，面试的时候，就当面前坐着的面试官是一群猪！\u201d\u200d\u200d","updateTime":"2019-11-30 16:59:01"}]}
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
         * page : 55
         * totalCount : 35899
         * totalPage : 3590
         * limit : 10
         * list : [{"content":"\u200d\u200d皇上久闻李白才名。 皇上：\u201c李爱卿，朕命你写诗助兴。\u201d 李白：\u201c皇上，写诗可以，还请贵妃为我磨墨。\u201d 皇上：\u201c哈哈，才子就是才子，要求都这么独特！来人那，上馍馍，贵妃你就喂李白吃几个吧！\u201d 李白：\u201c\u2026\u2026\u201d\u200d\u200d","updateTime":"2019-11-30 16:59:07"},{"content":"\u200d\u200d一个书生，一个将军，一个富翁，一个乞丐，四人分别做了句诗。 书生：\u201c大雪纷纷落地。\u201d 将军：\u201c是我皇家福气。\u201d 富翁：\u201c下它三日何妨。\u201d 乞丐：\u201c放你妈的狗屁。\u201d\u200d\u200d","updateTime":"2019-11-30 16:59:07"},{"content":"\u200d\u200d\u200d\u200d一新兵去潜水艇里服役，初来，有点不适应，四周黑漆漆的一片更让他感到压抑。 于是顺着某个地方开始摸索着些什么。 长官恰好从他身边经过，就向他问道:\u201c你在干什么？\u201d 新兵捂着胸口立马答道：\u201c报告长官，我有点气闷，想打开窗户透透气！\u201d\u200d\u200d\u200d\u200d","updateTime":"2019-11-30 16:59:06"},{"content":"\u200d\u200d老公对体育赛事一向不怎么感冒。 但是我想奥运会应该可以另当别论吧。 于是刚才很兴奋地问他：\u201c后天晚上要不要一起看开幕式？\u201d 他一脸蓦然地看着我：\u201c开谁的墓？\u201d\u200d\u200d","updateTime":"2019-11-30 16:59:05"},{"content":"\u200d\u200d\u200d\u200d过马路，没红绿灯。 上班高峰期，奈何我们这种屌丝没车。 足足等了数分钟过不了马路，突见一白衣大侠不顾车流，显身拉着我就走。 大爷，谢谢啊。\u200d\u200d\u200d\u200d","updateTime":"2019-11-30 16:59:04"},{"content":"\u200d\u200d每次看完西游记我都会想：\u201c为什么女儿国的水一喝就怀孕呢？\u201d 现在再想想，我觉得那应该不是水。 我是不是知道的太多了。\u200d\u200d","updateTime":"2019-11-30 16:59:04"},{"content":"\u200d\u200d老姐1.63米，60kg。 决定要减肥，坚持了半个月，腰和腿还是不变，胸明显小了。 想想既然减不了别处，还是吃回来吧！ 于是脱缰的吃了一个星期，腰和腿粗了，胸还是没回来。 已经一天没出房间了。\u200d\u200d","updateTime":"2019-11-30 16:59:03"},{"content":"\u200d\u200d\u200d\u200d二科和他女友分手半年了，一直在疗伤。 结果今早看见前女友的QQ签名改成了：\u201c今天是和老公一周年纪念日，我们去HAPPY！\u201d\u200d\u200d\u200d\u200d","updateTime":"2019-11-30 16:59:02"},{"content":"\u200d\u200d\u200d\u200d哥们给介绍一对象，见面前特意叮嘱要穿好一点。 我问他：\u201c怎么穿好一点？\u201d 他说：\u201c穿最贵那件。\u201d 第二天我穿着我那件波司登羽绒服就去了。 结果那女的见了我转身就走了，我都没摸清情况。 这么热的天，我热得跟狗一样。 你倒好，招呼都不打就走了，也太没礼礼貌了。\u200d\u200d\u200d\u200d","updateTime":"2019-11-30 16:59:01"},{"content":"\u200d\u200d一参加面试的女生信步走进了考场。 主考官开始向她宣读面试的注意事项，并微笑着安慰道：\u201c你不要紧张！\u201d 女生闻听，自信地接话道：\u201c我不会紧张的！培训时老师说了，面试的时候，就当面前坐着的面试官是一群猪！\u201d\u200d\u200d","updateTime":"2019-11-30 16:59:01"}]
         */

        private int page;
        private int totalCount;
        private int totalPage;
        private int limit;
        private List<Joke> list;

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

        public List<Joke> getList() {
            return list;
        }

        public void setList(List<Joke> list) {
            this.list = list;
        }

        public static class Joke implements Serializable {
            /**
             * content : ‍‍皇上久闻李白才名。 皇上：“李爱卿，朕命你写诗助兴。” 李白：“皇上，写诗可以，还请贵妃为我磨墨。” 皇上：“哈哈，才子就是才子，要求都这么独特！来人那，上馍馍，贵妃你就喂李白吃几个吧！” 李白：“……”‍‍
             * updateTime : 2019-11-30 16:59:07
             */

            private String content;
            private String updateTime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
