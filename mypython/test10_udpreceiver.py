# -*-coding:utf-8-*-
'''
tcp协议的socker编程，server端
'''
import threading
import socket
import time

# 创建UDP接收方
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind(("192.168.13.4" , 9999))

i = 1
while True:
    data = sock.recv(4096)
    print str(data)
