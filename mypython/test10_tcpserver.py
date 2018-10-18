# -*-coding:utf-8-*-
'''
tcp协议的socker编程，server端
'''
import threading
import socket
import time

class CommThread(threading.Thread):
    def run(self):
        while True:
            # 接受数据
            data = sock.recv(4096)
            print "收到了%s = %s" % (str(self.addr), str(data)),

    def __init__(self,sock,addr):
        threading.Thread.__init__(self)
        self.sock = sock
        self.addr = addr


# 创建服务器套接字，绑定端口
ss = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
ss.bind(("127.0.0.1" , 8888))
ss.listen(0)

while True:
    sock,addr = ss.accept()
    CommThread(sock,addr).start()
    print "%s链接进来\r\n"%(str(addr)) ,
    time.sleep(1)
