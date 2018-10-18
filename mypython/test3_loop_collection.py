# -*-coding:utf-8-*-

# 循环
list = [1,2,3,4,5,6]
for e in list:
    print e

# 99表格
rows = [1,2,3,4,5,6,7,8,9]
cols = [1,2,3,4,5,6,7,8,9]

rows = range(1,12,1)
print "=========99============="
for r in rows:
    cols = range(1 , r + 1)
    for c in cols:
        if c <= r:
            print "%dx%d=%d\t"%(c ,r , (c * r)),
    print

# while循环实现99表
print "================="
i = 1
while(i < 10):
    j = 1
    while(j <= i):
        print str(j) + "x" + str(i) + "=" + str(i * j) ,
        j += 1
    print
    i +=1

# 字符串转换函数
str(1000)

# in操作表示在集合中是否存在
list = [1,2,3]
print (2 in list)

# range 快速构造数字序列,[)区间
r = range(1,10)     #步长1,必须是整数
r = range(1,10 ,2)  #步长2
r = range(10,1 ,-1) #步长-1
print r


# 元组，使用()表示，元组不可变
print "========= 元组操作 ========="
t = (1,"tom" , 12)
# t[0] = 100 错误，元素不能修改
print t
print t[0:-1]
for e in t:
    print e


# 字典，类似于java的map，kv对
print "===== 字典,使用{}表示 ========"
d = {1:"tom1" , 2:"tom2" , 3:"tom3" , 4:"tom4"}
d[1]
d[1] = 100
# 包含k操作
print 3 in d
# 删除元素
d.pop(1)
print d

# 遍历字典,迭代所有的key
for e in d.keys() :
    print e
# 遍历字典,迭代所有的value
for e in d.values() :
    print e
# 迭代所有的元组,字典看作是元组的集合。
for e in d.items():
    print str(e[0]) + ":" + e[1]
# 反解析字典每个元素的内容到kv中
for (k,v) in d.items():
    print str(k) + ":::::" + v























