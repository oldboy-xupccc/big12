# -*-coding:utf-8-*-
import pymysql

# 连接到数据库 ,注意使用127.0.0.1
conn = pymysql.Connect("127.0.0.1", "root", "root", "python")
# 得到游标
cur = conn.cursor()

# 执行语句
cur.execute("select version()")

# 提取第一条记录
result = cur.fetchone()
print result

print "================="
cur.execute("select * from t1")
list = cur.fetchall()
for r in list:
    print "%d/%s/%d\r\n"%(r[0],r[1],r[2]),

cur.execute("insert into t1(id ,name ,age) values(3000 ,'tttt' , 22)")
cur.execute("delete from t1 where id >= 1000")
conn.commit()
cur.close()
