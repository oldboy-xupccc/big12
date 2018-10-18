# -*-coding:utf-8-*-
# 类型转换


# 转换成字符串 str(x)
print str(100) + str(2)

# 转换成整数 int("100")
print int("100") + 2

# 转换成float类型
print float("123.456")
print float(123)

# 将字符串解析成数学表达式进行计算
express = "1 + 2 - 3 * 4 / 2"
print eval(express)

# set()不重复集合
s = set([1,1,1,2,2,3])
print s

#
print str([1,2,2,3,4,5])
print str((1,23,45))
print str({1:"tom1",2:"tom2"})
