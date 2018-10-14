package com.chuangdata.framework.logmodel;

public class LogFields {
    /**
     * Common Fields
     **/
    // 协议号
    public static final String PROTOCOL = "protocol";
    public static final String CLIENT_IP = "clientIp";
    public static final String CLIENT_PORT = "clientPort";
    public static final String SERVER_IP = "serverIp";
    public static final String SERVER_PORT = "serverPort";
    // 查询报文流向
    public static final String REQUEST_DIR = "requestDir";
    // 请求方法
    public static final String TRANSACTION_TYPE = "transactionType";
    // 应答状态码
    public static final String HTTP_WAP_TRANSACTION_STATE = "httpWapTransactionState";
    public static final String HOST = "host";
    public static final String URI = "uri";
    public static final String USER_AGENT = "userAgent";
    public static final String HTTP_CONTENT_TYPE = "httpContentType";
    public static final String REFERER = "referer";
    // 请求报文中的content-length
    public static final String REQ_CONTENT_LEN = "reqContentLen";

    /**
     * DNS Fields
     **/
    public static final String DNS_STATUS_CODE = "dnsStatusCode";

    /**
     * PPP_HTTP_log Fields
     **/
    // 标志
    public static final String FLAG = "flag";
    // 匹配次数计数
    public static final String RESPONSE_NO = "responseNo";
    // 查询报文所在TMA的IP
    public static final String REQUEST_TMAIP = "requestTMAIP";
    // 应答报文所在TMA的IP
    public static final String RESPONSE_TMAIP = "responseTMAIP";
    // 查询报文所在TMA的链路标识
    public static final String REQ_TMA_LINK_NO = "reqTMALinkNo";
    // 应答报文所在TMA的链路标识
    public static final String RESP_TMA_LINK_NO = "respTMALinkNo";
    // 查询时间（秒）
    public static final String REQUEST_TIME_S = "requestTimeS";
    // 应答时间（秒）
    public static final String RESPONSE_TIME_S = "responseTimeS";
    // 查询时间（毫秒）
    public static final String REQUEST_TIME_MS = "requestTimeMs";
    // 应答时间（毫秒）
    public static final String RESPONSE_TIME_MS = "responseTimeMs";
    // 请求报文传输层ACK号
    public static final String REQ_ACK = "reqACK";
    // 应答报文传输层SEQ号
    public static final String RESP_SEQ = "respSEQ";
    // 请求报文所在转储文件生成时间与话单文件时间关系标志
    public static final String REQ_PKT_TIME_FLAG = "reqPktTimeFlag";
    // 请求报文所在pcap转储文件在当天的编号
    public static final String REQ_PKT_INDEX = "reqPktIndex";
    // 应答报文所在转储文件生成时间与话单文件时间关系标志
    public static final String RESP_PKT_TIME_FLAG = "respPktTimeFlag";
    // 应答报文所在pcap转储文件在当天的编号
    public static final String RESP_PKT_INDEX = "respPktIndex";
    // 请求报文在pcap转储文件的字节偏移量
    public static final String REQ_PKTOFF_SET = "reqPktoffSet";
    // 请求首报文的字节长度
    public static final String REQ_PKT_LEN = "reqPktLen";
    // 应答报文在pcap转储文件的字节偏移量
    public static final String RESP_PKTOFF_SET = "respPktoffSet";
    // 应答首报文的字节长度
    public static final String RESP_PKT_LEN = "respPktLen";
    // 最后一个HTTP内容包时间秒
    public static final String RESPONSE_END_TIME_S = "responseEndTimeS";
    // 最后一个应答报文对应ACK确认包时间秒
    public static final String RESPONSE_ACK_TIME_S = "responseAckTimeS";
    // 最后一个HTTP内容包时间毫秒
    public static final String RESPONSE_END_TIME_MS = "responseEndTimeMs";
    // 最后一个应答报文对应ACK确认包时间
    public static final String RESPONSE_ACK_TIME_MS = "responseAckTimeMs";
    // 应答报文媒体类型
    public static final String RESP_CONTENT_TYPE = "respContentType";
    // 应答报文首部字段
    public static final String RESP_CONTENT_LEN = "respContentLen";
    // 应答报文html内容
    public static final String RESP_HTML_HEADER = "respHtmlHeader";

    /**
     * UASG_HTTP_log Fields
     **/
    // 用于指示整个XDR所占用字节数
    public static final String LENGTH = "length";
    // 默认填写北京
    public static final String CITY = "city";
    // 接口类型
    public static final String INTERFACE = "interface";
    // DPI设备内唯一的xDR编号
    public static final String X_DR_ID = "xDRId";
    // 在同一个同源镜像组内的TMAID
    public static final String TMAID = "TMAID";
    // 在同一台设备上的链路序号
    public static final String LINK_INDEX = "linkIndex";

    public static final String UL_TMAID = "ulTMAID";

    public static final String UL_LINK_INDEX = "ulLinkIndex";

    public static final String DL_TMAID = "dlTMAID";

    public static final String DL_LINK_INDEX = "dlLinkIndex";
    // 业务类型编码
    public static final String APP_TYPE_CODE = "appTypeCode";
    // TCP/UDP流开始时间
    public static final String PROCEDURE_START_TIME = "procedureStartTime";
    // TCP/UDP流结束时间
    public static final String PROCEDURE_END_TIME = "procedureEndTime";
    // 应用大类
    public static final String APP_TYPE = "appType";
    // 应用小类
    public static final String APP_SUB_TYPE = "appSubType";
    // 应用小类的内容细分
    public static final String APP_CONTENT = "appContent";
    // 标识业务是否成功
    public static final String APP_STATUS = "appStatus";
    // 终端用户的IPv6地址
    public static final String CLIENT_IPV6 = "clientIpv6";
    // L4协议类型
    public static final String L4PROTOCAL = "l4protocal";
    // 访问服务器的IPv6地址
    public static final String SERVER_IPV6 = "serverIpv6";
    // 上行流量
    public static final String UL_DATA = "ULData";
    // 下行流量
    public static final String DL_DATA = "DLData";
    // 上行IP包数
    public static final String UL_IP_PACKET = "ULIpPacket";
    // 下行IP包数
    public static final String DL_IP_PACKET = "DLIpPacket";
    // 上行TCP乱序报文数
    public static final String UL_DISORD_PKTS = "ULDisordPkts";
    // 下行TCP乱序报文数
    public static final String DL_DISORD_PKTS = "DLDisordPkts";
    // 上行TCP重传报文数
    public static final String UL_RETRANS_PKTS = "ULRetransPkts";
    // 下行TCP重传报文数
    public static final String DL_RETRANS_PKTS = "DLRetransPkts";
    // TCP建链响应时延
    public static final String TCP_RES_DELAY_MS = "tcpResDelayMs";
    // TCP建链确认时延
    public static final String TCP_ACK_DELAY_MS = "tcpAckDelayMs";
    // 分片报文数
    public static final String UL_IP_FRAG_PACKETS = "ULIpFragPackets";
    // 分片报文数
    public static final String DL_IP_FRAG_PACKETS = "DLIpFragPackets";
    // TCP建链成功到第一条事务请求的时延
    public static final String TCP_FIRST_TRAN_REQ_DELAY_MS = "tcpFirstTranReqDelayMs";
    // 第一条事务请求到其第一个响应包时延
    public static final String TRAN_REQ_FIRSTRES_PK_DELAY_MS = "tranReqFirstresPkDelayMs";
    // 窗口大小
    public static final String WIN_SIZE = "winSize";
    // MSS尺寸
    public static final String MSS = "mss";
    // TCPSYN的次数
    public static final String TCP_SYN_COUNTS = "tcpSynCounts";
    // TCP连接状态指示
    public static final String TCP_CONN_STAT = "tcpConnStat";
    // 会话是否结束
    public static final String SUCCESS_FLAG = "successFlag";
    // HTTP版本
    public static final String HTTP_VERSION = "httpVersion";
    // 第一个响应包reply200OK相对于get命令的时延
    public static final String HTTP_FIRST_RES_PK_DELAY_MS = "httpFirstResPkDelayMs";
    // 最后一个包相对于get命令的时延
    public static final String HTTP_LAST_CONTENT_PK_DELAY_MS = "httpLastContentPkDelayMs";
    // 最后一个包的ACK相对于get命令的时延
    public static final String HTTP_LAST_ACK_PK_DELAY_MS = "httpLastACKPkDelayMs";
    // 针对wap代理上网的私有头部字段
    public static final String X_ONLINE_HOST = "xOnlineHost";
    // 协议中Cookie字段
    public static final String COOKIE = "cookie";
    // 目标行为
    public static final String TARGET_BEHAVIOR = "targetBehavior";
    // WTP层的失败类型
    public static final String WTP_INTERRUPT_TYPE = "wtpInterruptType";
    // WTP层失败原因
    public static final String WTP_INTERUPT_TYPE = "wtpInteruptType";
    // 网站名称
    public static final String TITLE = "title";
    // 网站关键字
    public static final String KEYWORD = "keyword";
    // 业务行为标识
    public static final String BUSINESS_BEHAVIOR_ID = "businessBehaviorId";
    // 业务完成标识
    public static final String BUSINESS_COMPLETE_ID = "businessCompleteId";
    // 登陆、访问响应或者刷新时延
    public static final String MS_BUSINESS_DELAY = "msBusinessDelay";
    // 详见附录F“浏览工具”
    public static final String EXPLORER_TOOL = "explorerTool";
    // 详见附录G“门户应用集合”
    public static final String PORTAL_APP_COLLECTION = "portalAppCollection";

    /**
     * Mobile Fields
     */
    public static final String NAI = "nai";

    public static final String TAC = "tac";

    public static final String CI = "ci";

    public static final String USER_ZONE = "userZone";

    public static final String BSID = "bsid";

    public static final String MSISDN = "msisdn";

    public static final String MEID = "meid";

    public static final String IMEI = "imei";

    public static final String IMSI = "imsi";

    public static final String MACHINE_IP_ADD_TYPE = "machineIpAddType";

    public static final String GGSN_IP = "ggsnIp";

    public static final String SGSN_IP = "sgsnIp";

    public static final String GGSN_PORT = "ggsnPort";

    public static final String SGSN_PORT = "sgsnPort";

    public static final String SGSN_GTP_TEID = "sgsnGtpTEID";

    public static final String GGSN_GTP_TEID = "ggsnGtpTEID";

    public static final String APN = "apn";

    public static final String RAT = "rat";

    public static final String HTTP_GAME_FLAG = "httpGameFlag";

    public static final String GAME_USER_ACCOUNT = "gameUserAccount";

    public static final String LOGIN_START_TIME = "loginStartTime";

    public static final String LOGIN_END_TIME = "loginEndTime";

    public static final String COMMUNICATION_METHOD = "communicationMethod";

    public static final String PAYMENT_FLAG = "paymentFlag";

    public static final String LOGIN_FLAG = "loginFlag";

    public static final String PROCEDURE_STATUS = "procedureStatus";
}
