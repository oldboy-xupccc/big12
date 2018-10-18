# -*-coding:utf-8-*-
# 函数 ,有返回语句，不写是None
def add(a,b) :
    if __name__ =="__main__":
        print "xxx : " + __name__
    # 有返回语句
    print "a : " + str(a)
    print "b : " + str(b)
    return a + b

print add(b=1,a=2)

"""
定义函数，有return语句，否则返回None
*a :变长参数
*args : 固定写法，表示任何多个无名参数，它是一个tuple
**kwargs:固定写法，关键字参数，它是一个dict
此种方式类似于java的反射中的Method类，能够提取函数的运行时信息。
"""
def f1(a , *args):
    print args
    for e in a:
        print e
# 调用函数，传递变长参数
f1((1,2,3,4,5))

def f2(a,b,c , *args):
    print str(args)
f2(1,2,3,4)

#
def foo(x,*args, **kwargs):
    print 'args=', args
    print 'kwargs=', kwargs
    print '**********************'
foo(1, 2, 3, 4)
foo(1000 ,a=1, b=2, c=3)
foo(1, 2,  a=4, b=5, c=100)



