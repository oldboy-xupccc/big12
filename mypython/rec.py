# -*-coding:utf-8-*-
import random
import math

'''
rmse : 均方根误差
        sqrt(sum((R(ui) - R'(ui))^2))
rmse = -----------------------------
            |T|
'''
def rmse(test):

    pass

'''
召回率:
T(u) : {1:[2,3,4] , 2:[3,4,5]}
R(u) : [(3,0.9) , (5,0.6) , (6,0.4)]
            SUM(|R(u) & T(u)|)
recall = ----------------------
            SUM(|T(u)|)
'''
def recall():
    commSum = 0
    testSum = 0
    # 测试字典
    T = getTestDataDict()
    userItemsDict , itemUsersDict= getDict()
    # 对所有用户进行推荐
    for u in userItemsDict.keys():
        # 推荐列表[(2:0.6),(5:04),(6:0.2)]
        Ru = recommItemCF(u , 5)
        #计算命中的个数
        comon = set(dict(Ru).keys()).intersection(set(T[u])).__len__()
        commSum += comon
        testSum = testSum + T[u].__len__()
    return float(commSum) / testSum

'''
准确度
            SUM(|R(u) & T(u)|)
precision = ------------------
            SUM(|R(u)|)
'''
def precision():
    commSum = 0
    recommSum = 0
    # 测试字典
    T = getTestDataDict()
    userItemsDict, itemUsersDict = getDict()
    # 对所有用户进行推荐
    for u in userItemsDict.keys():
        # 推荐列表[(2:0.6),(5:04),(6:0.2)]
        Ru = recommItemCF(u, 5)
        # 计算命中的个数
        comon = set(dict(Ru).keys()).intersection(set(T[u])).__len__()
        commSum += comon
        recommSum = recommSum + Ru.__len__()
    return float(commSum) / recommSum

'''
切割数据集
'''
def splitData(splitLine):
    fin = open("D:\\ml\\data\\recdata\\ratings.dat")
    trainFile = open("D:\\ml\\data\\recdata\\train.txt" , "ab")
    testFile = open("D:\\ml\\data\\recdata\\test.txt" , "ab")
    while True:
        line = fin.readline()
        if line != '':
            r = random.randint(1, 100)
            if r < splitLine :
                trainFile.write(str(line));
            else:
                testFile.write(str(line));
        else:
            break ;

    trainFile.close()
    testFile.close()


'''
获得训练数据集
'''
def getTrainData():
    return open("D:\\ml\\data\\recdata\\train.txt")

'''
获得测试集合
'''
def getTestDataDict():
    d = {}
    f = open("d:\\\\ml\\data\\recdata\\test.txt")
    while True :
        line = f.readline()
        if line != '':
            arr = line.split("::")
            uid = int(arr[0])
            iid = int(arr[1])
            if uid not in d:
                d[uid] = []
            d[uid].append(iid)
        else :
            break;
    return list

'''
jaccard相似度算法:
        N(u) & N(v)
sim = ----------------
        N(u) | N(v)
'''
def simJaccard(itemsA , itemsB):
    setA = set(itemsA)
    setB = set(itemsB)
    # 共同交互商品数
    common = setA.intersection(setB)
    if common.__len__() == 0:
        return 0.0
    # 两个用户交互的总的商品
    all = setA.union(setB)
    return float(common.__len__()) / all.__len__()


'''
余弦相似度:
         N(u) & N(v)
sim = ----------------
        sqrt(|N(u)| |N(v)|)
'''
def simCos(itemsA , itemsB):
    setA = set(itemsA)
    setB = set(itemsB)
    # 共同交互商品数
    common = setA.intersection(setB)
    if common.__len__() == 0:
        return 0.0
    #
    import math
    bot = math.sqrt(setA.__len__() * setB.__len__())
    return float(common.__len__()) / bot


'''
john breese相似度算法：
    该算法对商品的流行度进行惩罚，越流行的商品，对相似度计算的贡献越低。
'''
def simJohnbreese(u, v):
    # 相似度
    sum = 0.0
    userItemsDict , itemUsersDict = getDict()
    setA = set(userItemsDict[u])
    setB = set(userItemsDict[v])
    common = setA.intersection(setB)

    # 循环交集
    for i in common:
        # 商品的交互用户数==商品i的流行度
        iusers = itemUsersDict[i].__len__()
        sum = sum + (1.0 / math.log10(iusers + 1))

    return sum / math.sqrt(setA.__len__() * setB.__len__())


'''
得到user和商品列表的字典
{
1->[1,2,3,4]
2->[2,3,4,5]
...
}
getUserItems = {}
'''
def getDict():
    # 用户->商品之间的字典
    userItemsDict = {}
    # 商品->用户之间的字典
    itemUsersDict = {}

    # 加载训练数据
    file = getTrainData()
    while True :
        line = file.readline()
        if line != '':
            arr = line.split("::")
            uid = int(arr[0])
            iid = int(arr[1])
            # userItemDict
            if uid in userItemsDict.keys():
                userItemsDict[uid].append(iid)
            else :
                userItemsDict[uid] = [iid]

            # itemUsersDict
            if iid in itemUsersDict.keys():
                itemUsersDict[iid].append(uid)
            else:
                itemUsersDict[iid] = [uid]
        else:
            break ;
    return userItemsDict ,itemUsersDict

'''
计算item和用户列表的字典
{
a->[1,2,3,4]
b->[2,3,4]
c->[3,4]
}
'''

'''
计算用户相似度矩阵:
simMatrix =
'''
def userSimMatrix():
    # 得到用户和商品列表
    userItemsDict,itemUsersDict = getDict()
    # 字典
    M = {}
    for u,itemsU in userItemsDict.items():
        for v,itemsV in userItemsDict.items():
            if u < v:
                if u not in M.keys():
                    M[u] = {}
                if v not in M.keys():
                    M[v] = {}
                # 计算相似度
                ratio = simJohnbreese(u,v)
                M[u][v] = ratio
                M[v][u] = ratio

    # 对M中每个用的相似度字典按照相似度倒排序
    M2 = {}
    for k,v in M.items():
        M2[k] = sorted(v.items(), key=lambda e: e[1], reverse=True)
    return M2

'''
商品相似度矩阵：
itemSimMatrix
'''
def itemSimMatrix():
    # 得到字典
    userItemsDict, itemUsersDict = getDict()
    #
    M = {}
    for i, usersI in itemUsersDict.items():
        for j, usersJ in itemUsersDict.items():
            if i < j:
                if i not in M.keys():
                    M[i] = {}
                if j not in M.keys():
                    M[j] = {}
                # 计算相似度
                ratio = simCos(usersI, usersJ)
                M[i][j] = ratio
                M[j][i] = ratio

    # 对M中每个用的相似度字典按照相似度倒排序
    M2 = {}
    for k, v in M.items():
        M2[k] = sorted(v.items(), key=lambda e: e[1], reverse=True)
    return M2


'''
计算用户u对商品i的偏好值
    前提是用户u没有交互过商品i。
    Wuv是用户u和v之间的相似度。
    Rvi是用户v对商品i的偏好值，如果隐式反馈，始终为1.
    计算过程:
    1.找出和u最相似的K个用户
    2.在这K个用户中找出交互了商品i的用户
    3.用户u与每个用户的相似度 * 用户v和商品i进行累加求和.
'''
def preferUserItemByUserCF(u,i , K):
    #偏好值
    pref = 0.0
    #找出最相似的K个用户[(2,0.9),(3,0.7),(4,0.5)]
    kUsers = userSimMatrix()[u][:K]
    #用户商品列表字典{1->[1,2,3] , 2->[2,3,4,5]}
    userItemsDict,itemUsersDict = getDict()

    for t in kUsers:
        if i in userItemsDict[t[0]]:
            pref += t[1] * 1
    return pref


'''
计算用户u对商品i的偏好值
    前提是用户u没有交互过商品i。
    计算过程:
    1.找出和商品j最相似的K个商品
    2.在这K个商品中找到用户u交互的商品i
    3.用户u与每个商品i的偏好(这里始终为) * (商品j与商品i的相似度)累加求和.
'''
def preferUserItemByItemCF(u, j, K):
    # 偏好值
    pref = 0.0
    # 找出与商品j最相似的K个商品
    kItems = itemSimMatrix()[j][:K]
    # 用户商品列表字典{1->[1,2,3] , 2->[2,3,4,5]}
    userItemsDict, itemUsersDict = getDict()

    for t in kItems:
        if t[0] in userItemsDict[u]:
            pref += t[1]
    return pref

'''
recommUserCF： 基于用户的协同过滤
    1.找出K个和u最相似的用户
    2.在这K个用户中，找到每个用户的交互商品
    3.滤除用户u交互的商品
    4.将商品添加到推荐列表
    5.对推荐列表中的每个商品累加求偏好值
    6.得到最终的推荐列表
'''
def recommUserCF(u , K):
    # 推荐字典[]
    recommDict = {}
    # 得到最相似的K个用户
    kUsers = userSimMatrix()[u][:K]
    # 得到用户商品列表
    userItemsDict,itemUsersDict = getDict()

    for v, simV in kUsers:
        vitems = userItemsDict[v]
        for vi in vitems:
            if vi not in userItemsDict[u]:
                if vi not in recommDict.keys():
                    recommDict[vi] = 0.0
                recommDict[vi] = recommDict[vi] + simV
    return sorted(recommDict.items() , key = lambda e : e[1] ,reverse=True)


'''
recommItemCF: 基于商品的推荐列表
    过程:
    1.找出用户u的历史商品记录
    2.遍历历史记录，找出每个商品的最相似的K个商品
    3.滤除用户u交互过的商品.
    4.将剩余的商品放到推荐列表
    5.推荐列表的偏好值累加求和。
'''
def recommItemCF(u,K):
    # 推荐列表
    W = {}
    # 商品相似度矩阵
    M = itemSimMatrix()
    #
    userItemsDict , itemUsersDict = getDict()

    # 找到每个用户的历史商品
    for i in userItemsDict[u]:
        itemsK = M[i][:K]
        for j in itemsK:
            if j[0] not in userItemsDict[u]:
                if j[0] not in W.keys():
                    W[j[0]] = 0.0
                W[j[0]] = W[j[0]] + j[1]

    return sorted(W.items() , key=lambda e:e[1] ,reverse=True)

'''
矩阵归一化：按照最大值归一
normalizeMatrix()
'''
def normalMatrix(m):
    M = {}
    for k,v in m.items():
        M[k] = []
        mx = v[0][1]
        for t in v:
            M[k].append((t[0] , float(t[1]) / mx))
    return M

'''
冷启动 ：新用户或新商品
    1.用户冷启动
    2.商品冷启动
    3.系统冷启动


UserCF              和   ItemCF对比
---------------------------------------------------------
社会化，小群体热点         个性化强,反映个人以往的兴趣传承
新闻类推荐适合             用户数 < 商品数·
itemCF计算量大

用户冷启动时推荐无法实时    长尾物品丰富以及用户个性化强烈
相似度计算是隔断时间进行    用户冷启动只要有交互行为，就能推荐其上商品。

商品冷启动，一旦有用户访问，无法在线进行模型更新，无法向新用户推荐。
即可推荐给其他用户。

推荐解释性不强             有历史记录做参考，有较强的解释信
'''

'''
哈利波特问题解决办法
    1.惩罚过度流行商品
                N(i) & N(j)
        sim = -----------------  a = [0.5 ,1] a = 0.5就是余弦相似度
                  1-a      a
                N(i)  * N(j)

    2.不计算特别流行的商品
'''


'''
隐语义模型:LFM(latent factor model)
    冥冥之中只有定数。
    矩阵分解。

'''
if __name__ == '__main__':
    # simMatrix = userSimMatrix()
    # userItemDict = getUserItemsDict()
    # print  preferUserItem(1 , 8 ,2)
    # list = recommUserCF(1, 2)
    # print list
    # sim = simJohnbreese(1,2)
    # print sim

    # pre = preferUserItemByItemCF(1 , 4 , 5)
    # print pre
    # m = itemSimMatrix()
    # M = normalMatrix(m)
    # print M

    list = getTestDataDict()
    print list
