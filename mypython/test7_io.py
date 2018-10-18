# -*-coding:utf-8-*-
# io
f = open("d:\\java\\1.txt")
# 读取所有行
lines = f.readlines()
for l in lines:
    print l,

# 读取下一行
print
print "============="
f = open("d:\\java\\1.txt")
while(True):
    line = f.readline()
    if line != '':
        print line ,
    else:
        break

# None，类似于java中null的表示不存在。
x = None
print x

# 文件写操作
f = open("d:\\java\\11.txt" , mode="wb")
f.write("hello world 中国人wwwwwwwwww??")
f.close()

# 文件重命名
# 源文件必须存在
import os
# os.renames("d:\\java\\2222.txt", "d:\\java\\1111.txt")

# 删除文件
# os.remove("d:\\java\\1111.txt")

# 删除空目录。
# os.removedirs("D:\\java\\hbdaxue")

# 列出目录的元素
files = os.listdir("d:\\downloads")

# 判断文件和还是目录
def mylistdir(dir):
    print dir
    subdir = os.listdir(dir)
    for d in subdir:
        if dir.endswith("\\"):
            mylistdir(dir  + d)
        else :
            mylistdir(dir + "\\" + d)



# 导入python模块，每个python文件就是一个模块
import test6_fuction
test6_fuction.add(1,2)
print __name__

if __name__ == "__main__":
    print 10000

# 提取脚本的参数
import sys
r = sys.argv
print r[0]
print r[1]

#
s = "xxx"
s.__len__()
# 返回对象的指定属性，没有的话，可以指定默认值
r = getattr(s , "__len__" , "no this attr")
print r

list = [1,2,3]

# 导入时间库
import datetime
datetime.datetime.now()
print datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

class Cat:
    # 类方法,是通过访问的静态方法
    @classmethod
    def add(cls, a, b):
        return a + b

    # 静态方法,不通过类访问，直接访问的方法
    @staticmethod
    def sub(a, b):
        return a - b

    # 成员函数
    def watch(self, a, b):
        print a
        print b
        print "xx"