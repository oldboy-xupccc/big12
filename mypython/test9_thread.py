# -*-coding:utf-8-*-
# 线程
import time
import thread
import threading

#
# def sayHello(str):
#     print str
# thread.start_new_thread(sayHello , ("hello world",))
# 休眠秒数
#time.sleep(5)

#玩家
# class Player(threading.Thread):
#     def run(self):
#         print "%s 出发了"%(self.name)
#         time.sleep(self.time)
#         print "%s 到了"%(self.name)
#
#     def __init__(self,name, time):
#         threading.Thread.__init__(self)
#         self.name = name
#         self.time = time
# p1= Player("p1" , 3)
# p2= Player("p2" , 1)
# p3= Player("p3" , 2)
# p4= Player("p4" , 4)
#
# p1.start()
# p2.start()
# p3.start()
# p4.start()
#
# p1.join()
# p2.join()
# p3.join()
# p4.join()
# print("开局!!")
#
tickets = 100

#取票方法,保证线程安全性
lock = threading.Lock()
def getTicket():
    global  tickets
    lock.acquire()
    tmp = tickets
    if (tickets > 0):
        tickets -= 1
        lock.release()
        return tmp
    else:
        lock.release()
        return -1


# 售票员
class Saler(threading.Thread):
    def run(self):
        while (True):
            tmp = getTicket()
            if tmp != -1:
                print "%s : %d\r\n"%(self.name ,tmp),
            else:
                break ;

    def __init__(self, name):
        threading.Thread.__init__(self)
        self.name = name

s2 = Saler("s2")
s1 = Saler("s1")
s1.start()
s2.start()
s1.join()
s2.join()
print "over"