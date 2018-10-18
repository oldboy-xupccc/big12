# -*-coding:utf-8-*-
# 运算符

print 1 / 2
print 1 % 2

# 幂函数,2^3
print 2 ** 3

# 浮点数除法
print 1.0 / 2
# 整除
print 1.0 // 2

# 进制变换
print hex(97)   # 转换成16进制
print oct(97)   # 转换8进制
print chr(97)   # 转换成字符

# 位运算
print "=======位运算========="
print 1 & 2
print 1 | 2
print 1 ^ 2
print ~1
print 1 << 1
# 有符号右移
print -1 >> 1

# 逻辑运算
# and
print (1 < 2 and 2 > 3)
print (1 < 2 or 2 > 3)
print (not 1 < 2)

# 身份运算
a = [1,2,3,4,5]
b = a[0:3]
# 判断是否是同一对象
print a is b
# 判断内容是否相同
print a == b


# 条件运算
age =15
if age < 18:
    print "少年"
elif age >60:
    print "老年"
else:
    print "中年"


