
// 初始化
LouSQLite.init(mContext, new MyCallBack());

//  查找
List<Phrase> lists = LouSQLite.query(MyCallBack.TABLE_NAME_PHRASE, "select * from " + MyCallBack.TABLE_NAME_PHRASE, null);

// 插入一个数据到数据库
Phrase phrase = new Phrase("青青子衿，悠悠我心");
LouSQLite.insert(MyCallBack.TABLE_NAME_PHRASE, phrase);


// 插入一组数据
List<Phrase> lists =  Arrays.asList(
new Phrase("窈窕淑女，君子好逑"),
new Phrase("海上生明月，天涯共此时"),
new Phrase("青青子衿，悠悠我心"),
new Phrase("人生若只如初见")
);
LouSQLite.insert(MyCallBack.TABLE_NAME_PHRASE, lists);


// 更新到数据库
LouSQLite.update(MyCallBack.TABLE_NAME_PHRASE, phrase, PhraseEntry.COLEUM_NAME_ID + "=?", new String[]{phrase.getId()});




// 从数据库中删除
LouSQLite.delete(MyCallBack.TABLE_NAME_PHRASE, MyCallBack.COLEUM_NAME_ID + "=?", new String[]{phrase.getId()});


## 常用SQL语句操作

