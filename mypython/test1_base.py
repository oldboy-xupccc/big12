# -*-coding:utf-8-*-
print "hello world中国人"
x = 10
print x

# 缩进作为代码段
if True:
    print 1
    print 2
else:
    print 0

# 手动换行
x = 1 + \
    2 + \
    3

"""
this is a commnent ;
多行!!!
"""
s = u"hello \u0061"
print s

# 字符串操作
s = "hello world"
print s[3]      #指定位置的字符
print s[3:5]    #指定范围
print s[3:]
print s[:5]
print s[1:-1]
# *表示重复次数
print s * 2

# strip滤除两端的空白符,==trim(),如果
# 如果是指定的字符开头，就滤除。
print "==============="
print "hello world  ".strip("h")
print " hello world  ".lstrip()
print " hello world  ".rstrip()

# 字符串格式化操作
info = "name is %s and age is %d"%("tom" , 12)
print info
# 如果变量多次出现，可以使用format方式
info = "name is {x} , welcome for {x}".format(x="tom")
print info

# 变量必须初始化
y = 0

# 同时定义多个变量
a,b,c = 1,2,3
print a
print b
print c
