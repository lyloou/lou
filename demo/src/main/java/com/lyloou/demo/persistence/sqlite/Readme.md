
## LouSQLite用法
```java
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
phrase.setContent(phrase.getContent() + " 嘿嘿嘿");
LouSQLite.update(MyCallBack.TABLE_NAME_PHRASE, phrase, PhraseEntry.COLEUM_NAME_ID + "=?", new String[]{phrase.getId()});


// 从数据库中删除
LouSQLite.delete(MyCallBack.TABLE_NAME_PHRASE, MyCallBack.COLEUM_NAME_ID + "=?", new String[]{phrase.getId()});


// 执行一条sql语句
LouSQLite.execSQL("UPDATE " + MyCallBack.TABLE_NAME_PHRASE
                + " SET " + PhraseEntry.COLEUM_NAME_FAVORITE + "=1 "
                + "WHERE " + PhraseEntry.COLEUM_NAME_FAVORITE + "=0");
```
## 常用SQL操作语句
```
- insert into tableName (id, name, age) values (?, ?, ?)
- delete from tableName
- delete from tableName where age > ?
- delete from tableName where mac in ('24:71:89:0A:DD:82', '24:71:89:0A:DD:83','24:71:89:0A:DD:84')
- update tableName set name = ? where id = ?
- select * from tableName where age > ?
```