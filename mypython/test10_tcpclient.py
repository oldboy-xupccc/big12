# -*-coding:utf-8-*-
'''
tcp协议的socker编程，server端
'''
import threading
import socket
import time

# 创建服务器套接字，绑定端口
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("127.0.0.1" , 8888))

i = 1
while True:
    str = "tom%d\r\n" % (i)
    print ("client : " + str),
    sock.send(str)
    time.sleep(1)
    i += 1
