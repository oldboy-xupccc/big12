# -*-coding:utf-8-*-
# oop，面向对象编程

if True:
    pass
    print 1000

def f1():
    #pass是占位符
    pass

# 定义类
class Emp:
    pass

# 创建对象
e1 = Emp()
print e1.__class__

# 定义静态变量
class BENZ:
    brand = "BENZ"      # 静态变量
    __color = "black"   # __开头是私有的
print BENZ.brand

# 构造函数
class Person:
    # 构造函数
    def __init__(self):
        print "创建了一个Person对象"
        self.name = "tom"
        self.age = 100
        #del self.name

p1 = Person()
print p1.name
# 变量可以删除
# x = 100
# print x
# del x
# print x


"""
成员函数：

"""
class Dog:
    # 类方法,是通过访问的静态方法
    @classmethod
    def add(cls,a,b):
        return a + b

    #静态方法,不通过类访问，直接访问的方法
    @staticmethod
    def sub(a,b):
        return a - b
    # 成员函数
    def watch(self,a,b):
        print a
        print b
        print "xx"

d1 = Dog()
d1.watch(1,2)

# 调用类方法
print Dog.add(1,2)
print Dog.sub(1,2)

import os
def allfiles(dir):
    print unicode(dir,"gb2312")
    if os.path.isdir(dir):
        subdirs = os.listdir(dir)
        for sub in subdirs:
            allfiles(dir + "\\" + sub)
#allfiles("d:\\")

'''
析构函数
'''
class Man:

    #构造函数
    def __init__(self,name):
        print "创建Man"
        self.name = name

    #析构函数
    def __del__(self):
        pass
        #print "销毁Man"

# m1 = Man("tomasss")
# m2=m1
# m1 = None

# 内置方法，操纵对象的属性
m1 = Man("xxxxxx")
print hasattr(m1,"age")
setattr(m1,"age",12)
delattr(m1,"age")
print getattr(m1,"age" , -1)

# __dict__
r = Man.__dict__


'''
考察多重继承，类可以有多个父类
'''
class Horse:
    def __init__(self,name):
        self.name = name

class Donkey:
    def __init__(self,age):
        self.age = age

class Luozi(Horse,Donkey):
    def __init__(self,name,age,color):
        Horse.__init__(self ,name)
        Donkey.__init__(self,age)
        self.color = color

lz1 = Luozi("ttt" , 12 , "black")
print lz1.name

'''
 异常处理
'''
try:
    print 1 / 0
except:
    print "0不能做除数"
finally:
    print "over"

try:
    print 1 / 0
finally:
    print "over"




