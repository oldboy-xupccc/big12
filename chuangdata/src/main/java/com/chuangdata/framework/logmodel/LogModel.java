package com.chuangdata.framework.logmodel;

public interface LogModel {
    public int getConfigFieldsNumber();

    public int getActualFieldsNumber();

    /**
     * Common Fields
     **/
    public String getProtocol() throws FieldNotFoundException;

    public String getClientPort() throws FieldNotFoundException;

    public String getServerIP() throws FieldNotFoundException;

    public String getServerPort() throws FieldNotFoundException;

    public String getRequestDir() throws FieldNotFoundException;

    public String getTransactionType() throws FieldNotFoundException;

    public String getHttpWapTransactionState() throws FieldNotFoundException;

    public String getHost() throws FieldNotFoundException;

    public String getUri() throws FieldNotFoundException;

    public String getUserAgent() throws FieldNotFoundException;

    public String getHttpContentType() throws FieldNotFoundException;

    public String getReferer() throws FieldNotFoundException;

    public String getReqContentLen() throws FieldNotFoundException;

    /**
     * DNS Fields
     **/
    public String getDnsStatusCode() throws FieldNotFoundException;

    /**
     * PPP_HTTP_log Fields
     **/

    public String getFlag() throws FieldNotFoundException;

    public String getResponseNo() throws FieldNotFoundException;

    public String getRequestTMAIP() throws FieldNotFoundException;

    public String getResponseTMAIP() throws FieldNotFoundException;

    public String getReqTMALinkNo() throws FieldNotFoundException;

    public String getRespTMALinkNo() throws FieldNotFoundException;

    public String getClientIP() throws FieldNotFoundException;

    public String getRequestTimeS() throws FieldNotFoundException;

    public String getResponseTimeS() throws FieldNotFoundException;

    public String getRequestTimeMs() throws FieldNotFoundException;

    public String getResponseTimeMs() throws FieldNotFoundException;

    public String getReqACK() throws FieldNotFoundException;

    public String getRespSEQ() throws FieldNotFoundException;

    public String getReqPktTimeFlag() throws FieldNotFoundException;

    public String getReqPktIndex() throws FieldNotFoundException;

    public String getRespPktTimeFlag() throws FieldNotFoundException;

    public String getRespPktIndex() throws FieldNotFoundException;

    public String getReqPktoffSet() throws FieldNotFoundException;

    public String getReqPktLen() throws FieldNotFoundException;

    public String getRespPktoffSet() throws FieldNotFoundException;

    public String getRespPktLen() throws FieldNotFoundException;

    public String getResponseEndTimeS() throws FieldNotFoundException;

    public String getResponseAckTimeS() throws FieldNotFoundException;

    public String getResponseEndTimeMs() throws FieldNotFoundException;

    public String getResponseAckTimeMs() throws FieldNotFoundException;

    public String getRespContentType() throws FieldNotFoundException;

    public String getRespContentLen() throws FieldNotFoundException;

    public String getRespHtmlHeader() throws FieldNotFoundException;

    /**
     * UASG_HTTP_log Fields
     **/

    public String getLength() throws FieldNotFoundException;

    public String getCity() throws FieldNotFoundException;

    public String getInterface() throws FieldNotFoundException;

    public String getXDRId() throws FieldNotFoundException;

    public String getTMAID() throws FieldNotFoundException;

    public String getLinkIndex() throws FieldNotFoundException;

    public String getUlTMAID() throws FieldNotFoundException;

    public String getUlLinkIndex() throws FieldNotFoundException;

    public String getDlTMAID() throws FieldNotFoundException;

    public String getDlLinkIndex() throws FieldNotFoundException;

    public String getAppTypeCode() throws FieldNotFoundException;

    public String getProcedureStartTime() throws FieldNotFoundException;

    public String getProcedureEndTime() throws FieldNotFoundException;

    public String getAppType() throws FieldNotFoundException;

    public String getAppSubType() throws FieldNotFoundException;

    public String getAppContent() throws FieldNotFoundException;

    public String getAppStatus() throws FieldNotFoundException;

    public String getClientIpv6() throws FieldNotFoundException;

    public String getL4protocal() throws FieldNotFoundException;

    public String getServerIpv6() throws FieldNotFoundException;

    public String getULData() throws FieldNotFoundException;

    public String getDLData() throws FieldNotFoundException;

    public String getULIpPacket() throws FieldNotFoundException;

    public String getDLIpPacket() throws FieldNotFoundException;

    public String getULDisordPkts() throws FieldNotFoundException;

    public String getDLDisordPkts() throws FieldNotFoundException;

    public String getULRetransPkts() throws FieldNotFoundException;

    public String getDLRetransPkts() throws FieldNotFoundException;

    public String getTcpResDelayMs() throws FieldNotFoundException;

    public String getTcpAckDelayMs() throws FieldNotFoundException;

    public String getULIpFragPackets() throws FieldNotFoundException;

    public String getDLIpFragPackets() throws FieldNotFoundException;

    public String getTcpFirstTranReqDelayMs() throws FieldNotFoundException;

    public String getTranReqFirstresPkDelayMs() throws FieldNotFoundException;

    public String getWinSize() throws FieldNotFoundException;

    public String getMss() throws FieldNotFoundException;

    public String getTcpSynCounts() throws FieldNotFoundException;

    public String getTcpConnStat() throws FieldNotFoundException;

    public String getSuccessFlag() throws FieldNotFoundException;

    public String getHttpVersion() throws FieldNotFoundException;

    public String getHttpFirstResPkDelayMs() throws FieldNotFoundException;

    public String getHttpLastContentPkDelayMs() throws FieldNotFoundException;

    public String getHttpLastACKPkDelayMs() throws FieldNotFoundException;

    public String getXOnlineHost() throws FieldNotFoundException;

    public String getCookie() throws FieldNotFoundException;

    public String getTargetBehavior() throws FieldNotFoundException;

    public String getWtpInterruptType() throws FieldNotFoundException;

    public String getWtpInteruptType() throws FieldNotFoundException;

    public String getTitle() throws FieldNotFoundException;

    public String getKeyword() throws FieldNotFoundException;

    public String getBusinessBehaviorId() throws FieldNotFoundException;

    public String getBusinessCompleteId() throws FieldNotFoundException;

    public String getMsBusinessDelay() throws FieldNotFoundException;

    public String getExplorerTool() throws FieldNotFoundException;

    public String getPortalAppCollection() throws FieldNotFoundException;

    /**
     * Mobile Fields
     */
    public String getNai() throws FieldNotFoundException;

    public String getTac() throws FieldNotFoundException;

    public String getCi() throws FieldNotFoundException;

    public String getUserZone() throws FieldNotFoundException;

    public String getBsId() throws FieldNotFoundException;

    public String getMsisdn() throws FieldNotFoundException;

    public String getImei() throws FieldNotFoundException;

    public String getMeid() throws FieldNotFoundException;

    public String getImsi() throws FieldNotFoundException;

    public String getMachineIpAddType() throws FieldNotFoundException;

    public String getGgsnIp() throws FieldNotFoundException;

    public String getSgsnIp() throws FieldNotFoundException;

    public String getGgsnPort() throws FieldNotFoundException;

    public String getSgsnPort() throws FieldNotFoundException;

    public String getSgsnGtpTEID() throws FieldNotFoundException;

    public String getGgsnGtpTEID() throws FieldNotFoundException;

    public String getRat() throws FieldNotFoundException;

    public String getApn() throws FieldNotFoundException;

    /**
     * UASG_Game_log Fields
     **/

    public String getHttpGameFlag() throws FieldNotFoundException;

    public String getGameUserAccount() throws FieldNotFoundException;

    public String getLoginStartTime() throws FieldNotFoundException;

    public String getLoginEndTime() throws FieldNotFoundException;

    public String getCommunicationMethod() throws FieldNotFoundException;

    public String getPaymentFlag() throws FieldNotFoundException;

    public String getLoginFlag() throws FieldNotFoundException;

    public String getProcedureStatus() throws FieldNotFoundException;

}
