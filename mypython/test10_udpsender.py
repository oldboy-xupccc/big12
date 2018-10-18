# -*-coding:utf-8-*-
'''
tcp协议的socker编程，server端
'''
import threading
import socket
import time

# 创建UDP发送方
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind(("192.168.13.4" , 8888))

i = 1
while True:
    str0 = "tom" + str(i)
    sock.sendto(str0, ("192.168.13.255", 9999))
    print "发送了: " + str0
    i += 1
    time.sleep(1)

list = [1,2,3,4,5]

























