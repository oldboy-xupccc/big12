package com.chuangdata.framework.logmodel.text;

import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogFields;
import com.chuangdata.framework.logmodel.LogModel;

import java.util.Map;

/**
 * 文本类型的LogModel
 *
 * @author User
 */
public class TextLogModel implements LogModel {

    protected String[] fields;

    protected final Map<String, Integer> fieldsIndexMap;

    public TextLogModel(String[] fields, Map<String, Integer> fieldsIndexMap) {
        this.fields = fields;
        this.fieldsIndexMap = fieldsIndexMap;
    }

    public int getConfigFieldsNumber() {
        return fieldsIndexMap.size();
    }

    public int getActualFieldsNumber() {
        return fields.length;
    }

    protected String getFields(String fieldName) throws FieldNotFoundException {
        if (fieldsIndexMap.containsKey(fieldName)) {
            return fields[fieldsIndexMap.get(fieldName) - 1];
        }
        throw new FieldNotFoundException("Can't find field with name=" + fieldName);
    }

    public String getFlag() throws FieldNotFoundException {
        return getFields(LogFields.FLAG);
    }

    public String getResponseNo() throws FieldNotFoundException {
        return getFields(LogFields.RESPONSE_NO);
    }

    public String getRequestDir() throws FieldNotFoundException {
        return getFields(LogFields.REQUEST_DIR);
    }

    public String getRequestTMAIP() throws FieldNotFoundException {
        return getFields(LogFields.REQUEST_TMAIP);
    }

    public String getResponseTMAIP() throws FieldNotFoundException {
        return getFields(LogFields.RESPONSE_TMAIP);
    }

    public String getReqTMALinkNo() throws FieldNotFoundException {
        return getFields(LogFields.REQ_TMA_LINK_NO);
    }

    public String getRespTMALinkNo() throws FieldNotFoundException {
        return getFields(LogFields.RESP_TMA_LINK_NO);
    }

    public String getClientIP() throws FieldNotFoundException {
        return getFields(LogFields.CLIENT_IP);
    }

    public String getServerIP() throws FieldNotFoundException {
        return getFields(LogFields.SERVER_IP);
    }

    public String getClientPort() throws FieldNotFoundException {
        return getFields(LogFields.CLIENT_PORT);
    }

    public String getServerPort() throws FieldNotFoundException {
        return getFields(LogFields.SERVER_PORT);
    }

    public String getProtocol() throws FieldNotFoundException {
        return getFields(LogFields.PROTOCOL);
    }

    public String getTransactionType() throws FieldNotFoundException {
        return getFields(LogFields.TRANSACTION_TYPE);
    }

    public String getHttpWapTransactionState() throws FieldNotFoundException {
        return getFields(LogFields.HTTP_WAP_TRANSACTION_STATE);
    }

    public String getRequestTimeS() throws FieldNotFoundException {
        return getFields(LogFields.REQUEST_TIME_S);
    }

    public String getResponseTimeS() throws FieldNotFoundException {
        return getFields(LogFields.RESPONSE_TIME_S);
    }

    public String getRequestTimeMs() throws FieldNotFoundException {
        return getFields(LogFields.REQUEST_TIME_MS);
    }

    public String getResponseTimeMs() throws FieldNotFoundException {
        return getFields(LogFields.RESPONSE_TIME_MS);
    }

    public String getReqACK() throws FieldNotFoundException {
        return getFields(LogFields.REQ_ACK);
    }

    public String getRespSEQ() throws FieldNotFoundException {
        return getFields(LogFields.RESP_SEQ);
    }

    public String getReqPktTimeFlag() throws FieldNotFoundException {
        return getFields(LogFields.REQ_PKT_TIME_FLAG);
    }

    public String getReqPktIndex() throws FieldNotFoundException {
        return getFields(LogFields.REQ_PKT_INDEX);
    }

    public String getRespPktTimeFlag() throws FieldNotFoundException {
        return getFields(LogFields.RESP_PKT_TIME_FLAG);
    }

    public String getRespPktIndex() throws FieldNotFoundException {
        return getFields(LogFields.RESP_PKT_INDEX);
    }

    public String getReqPktoffSet() throws FieldNotFoundException {
        return getFields(LogFields.REQ_PKTOFF_SET);
    }

    public String getReqPktLen() throws FieldNotFoundException {
        return getFields(LogFields.REQ_PKT_LEN);
    }

    public String getRespPktoffSet() throws FieldNotFoundException {
        return getFields(LogFields.RESP_PKTOFF_SET);
    }

    public String getRespPktLen() throws FieldNotFoundException {
        return getFields(LogFields.RESP_PKT_LEN);
    }

    public String getResponseEndTimeS() throws FieldNotFoundException {
        return getFields(LogFields.RESPONSE_END_TIME_S);
    }

    public String getResponseAckTimeS() throws FieldNotFoundException {
        return getFields(LogFields.RESPONSE_ACK_TIME_S);
    }

    public String getResponseEndTimeMs() throws FieldNotFoundException {
        return getFields(LogFields.RESPONSE_END_TIME_MS);
    }

    public String getResponseAckTimeMs() throws FieldNotFoundException {
        return getFields(LogFields.RESPONSE_ACK_TIME_MS);
    }

    public String getUri() throws FieldNotFoundException {
        return getFields(LogFields.URI);
    }

    public String getHost() throws FieldNotFoundException {
        return getFields(LogFields.HOST);
    }

    public String getUserAgent() throws FieldNotFoundException {
        return getFields(LogFields.USER_AGENT);
    }

    public String getReferer() throws FieldNotFoundException {
        return getFields(LogFields.REFERER);
    }

    public String getHttpContentType() throws FieldNotFoundException {
        return getFields(LogFields.HTTP_CONTENT_TYPE);
    }

    public String getReqContentLen() throws FieldNotFoundException {
        return getFields(LogFields.REQ_CONTENT_LEN);
    }

    public String getRespContentType() throws FieldNotFoundException {
        return getFields(LogFields.RESP_CONTENT_TYPE);
    }

    public String getRespContentLen() throws FieldNotFoundException {
        return getFields(LogFields.RESP_CONTENT_LEN);
    }

    public String getRespHtmlHeader() throws FieldNotFoundException {
        return getFields(LogFields.RESP_HTML_HEADER);
    }

    public String getLength() throws FieldNotFoundException {
        return getFields(LogFields.LENGTH);
    }

    public String getCity() throws FieldNotFoundException {
        return getFields(LogFields.CITY);
    }

    public String getInterface() throws FieldNotFoundException {
        return getFields(LogFields.INTERFACE);
    }

    public String getXDRId() throws FieldNotFoundException {
        return getFields(LogFields.X_DR_ID);
    }

    public String getTMAID() throws FieldNotFoundException {
        return getFields(LogFields.TMAID);
    }

    public String getLinkIndex() throws FieldNotFoundException {
        return getFields(LogFields.LINK_INDEX);
    }

    public String getAppTypeCode() throws FieldNotFoundException {
        return getFields(LogFields.APP_TYPE_CODE);
    }

    public String getProcedureStartTime() throws FieldNotFoundException {
        return getFields(LogFields.PROCEDURE_START_TIME);
    }

    public String getProcedureEndTime() throws FieldNotFoundException {
        return getFields(LogFields.PROCEDURE_END_TIME);
    }

    public String getAppType() throws FieldNotFoundException {
        return getFields(LogFields.APP_TYPE);
    }

    public String getAppSubType() throws FieldNotFoundException {
        return getFields(LogFields.APP_SUB_TYPE);
    }

    public String getAppContent() throws FieldNotFoundException {
        return getFields(LogFields.APP_CONTENT);
    }

    public String getAppStatus() throws FieldNotFoundException {
        return getFields(LogFields.APP_STATUS);
    }

    public String getClientIpv6() throws FieldNotFoundException {
        return getFields(LogFields.CLIENT_IPV6);
    }

    public String getL4protocal() throws FieldNotFoundException {
        return getFields(LogFields.L4PROTOCAL);
    }

    public String getServerIpv6() throws FieldNotFoundException {
        return getFields(LogFields.SERVER_IPV6);
    }

    public String getULData() throws FieldNotFoundException {
        return getFields(LogFields.UL_DATA);
    }

    public String getDLData() throws FieldNotFoundException {
        return getFields(LogFields.DL_DATA);
    }

    public String getULIpPacket() throws FieldNotFoundException {
        return getFields(LogFields.UL_IP_PACKET);
    }

    public String getDLIpPacket() throws FieldNotFoundException {
        return getFields(LogFields.DL_IP_PACKET);
    }

    public String getULDisordPkts() throws FieldNotFoundException {
        return getFields(LogFields.UL_DISORD_PKTS);
    }

    public String getDLDisordPkts() throws FieldNotFoundException {
        return getFields(LogFields.DL_DISORD_PKTS);
    }

    public String getULRetransPkts() throws FieldNotFoundException {
        return getFields(LogFields.UL_RETRANS_PKTS);
    }

    public String getDLRetransPkts() throws FieldNotFoundException {
        return getFields(LogFields.DL_RETRANS_PKTS);
    }

    public String getTcpResDelayMs() throws FieldNotFoundException {
        return getFields(LogFields.TCP_RES_DELAY_MS);
    }

    public String getTcpAckDelayMs() throws FieldNotFoundException {
        return getFields(LogFields.TCP_ACK_DELAY_MS);
    }

    public String getULIpFragPackets() throws FieldNotFoundException {
        return getFields(LogFields.UL_IP_FRAG_PACKETS);
    }

    public String getDLIpFragPackets() throws FieldNotFoundException {
        return getFields(LogFields.DL_IP_FRAG_PACKETS);
    }

    public String getTcpFirstTranReqDelayMs() throws FieldNotFoundException {
        return getFields(LogFields.TCP_FIRST_TRAN_REQ_DELAY_MS);
    }

    public String getTranReqFirstresPkDelayMs() throws FieldNotFoundException {
        return getFields(LogFields.TRAN_REQ_FIRSTRES_PK_DELAY_MS);
    }

    public String getWinSize() throws FieldNotFoundException {
        return getFields(LogFields.WIN_SIZE);
    }

    public String getMss() throws FieldNotFoundException {
        return getFields(LogFields.MSS);
    }

    public String getTcpSynCounts() throws FieldNotFoundException {
        return getFields(LogFields.TCP_SYN_COUNTS);
    }

    public String getTcpConnStat() throws FieldNotFoundException {
        return getFields(LogFields.TCP_CONN_STAT);
    }

    public String getSuccessFlag() throws FieldNotFoundException {
        return getFields(LogFields.SUCCESS_FLAG);
    }

    public String getHttpVersion() throws FieldNotFoundException {
        return getFields(LogFields.HTTP_VERSION);
    }

    public String getHttpFirstResPkDelayMs() throws FieldNotFoundException {
        return getFields(LogFields.HTTP_FIRST_RES_PK_DELAY_MS);
    }

    public String getHttpLastContentPkDelayMs() throws FieldNotFoundException {
        return getFields(LogFields.HTTP_LAST_CONTENT_PK_DELAY_MS);
    }

    public String getHttpLastACKPkDelayMs() throws FieldNotFoundException {
        return getFields(LogFields.HTTP_LAST_ACK_PK_DELAY_MS);
    }

    public String getXOnlineHost() throws FieldNotFoundException {
        return getFields(LogFields.X_ONLINE_HOST);
    }

    public String getCookie() throws FieldNotFoundException {
        return getFields(LogFields.COOKIE);
    }

    public String getTargetBehavior() throws FieldNotFoundException {
        return getFields(LogFields.TARGET_BEHAVIOR);
    }

    public String getWtpInterruptType() throws FieldNotFoundException {
        return getFields(LogFields.WTP_INTERRUPT_TYPE);
    }

    public String getWtpInteruptType() throws FieldNotFoundException {
        return getFields(LogFields.WTP_INTERUPT_TYPE);
    }

    public String getTitle() throws FieldNotFoundException {
        return getFields(LogFields.TITLE);
    }

    public String getKeyword() throws FieldNotFoundException {
        return getFields(LogFields.KEYWORD);
    }

    public String getBusinessBehaviorId() throws FieldNotFoundException {
        return getFields(LogFields.BUSINESS_BEHAVIOR_ID);
    }

    public String getBusinessCompleteId() throws FieldNotFoundException {
        return getFields(LogFields.BUSINESS_COMPLETE_ID);
    }

    public String getMsBusinessDelay() throws FieldNotFoundException {
        return getFields(LogFields.MS_BUSINESS_DELAY);
    }

    public String getExplorerTool() throws FieldNotFoundException {
        return getFields(LogFields.EXPLORER_TOOL);
    }

    public String getPortalAppCollection() throws FieldNotFoundException {
        return getFields(LogFields.PORTAL_APP_COLLECTION);
    }

    public String getDnsStatusCode() throws FieldNotFoundException {
        return getFields(LogFields.DNS_STATUS_CODE);
    }

    /**
     * Mobile Fields
     */
    public String getNai() throws FieldNotFoundException {
        return getFields(LogFields.NAI);
    }

    public String getTac() throws FieldNotFoundException {
        return getFields(LogFields.TAC);
    }

    public String getCi() throws FieldNotFoundException {
        return getFields(LogFields.CI);
    }

    public String getUserZone() throws FieldNotFoundException {
        return getFields(LogFields.USER_ZONE);
    }

    public String getBsId() throws FieldNotFoundException {
        return getFields(LogFields.BSID);
    }

    public String getMsisdn() throws FieldNotFoundException {
        return getFields(LogFields.MSISDN);
    }

    public String getImei() throws FieldNotFoundException {
        return getFields(LogFields.IMEI);
    }

    public String getMeid() throws FieldNotFoundException {
        return getFields(LogFields.MEID);
    }

    public String getImsi() throws FieldNotFoundException {
        return getFields(LogFields.IMSI);
    }

    @Override
    public String getUlTMAID() throws FieldNotFoundException {
        return getFields(LogFields.UL_TMAID);
    }

    @Override
    public String getUlLinkIndex() throws FieldNotFoundException {
        return getFields(LogFields.UL_LINK_INDEX);
    }

    @Override
    public String getDlTMAID() throws FieldNotFoundException {
        return getFields(LogFields.DL_TMAID);
    }

    @Override
    public String getDlLinkIndex() throws FieldNotFoundException {
        return getFields(LogFields.DL_LINK_INDEX);
    }

    @Override
    public String getMachineIpAddType() throws FieldNotFoundException {
        return getFields(LogFields.MACHINE_IP_ADD_TYPE);
    }

    @Override
    public String getGgsnIp() throws FieldNotFoundException {
        return getFields(LogFields.GGSN_IP);
    }

    @Override
    public String getSgsnIp() throws FieldNotFoundException {
        return getFields(LogFields.SGSN_IP);
    }

    @Override
    public String getGgsnPort() throws FieldNotFoundException {
        return getFields(LogFields.GGSN_PORT);
    }

    @Override
    public String getSgsnPort() throws FieldNotFoundException {
        return getFields(LogFields.SGSN_PORT);
    }

    @Override
    public String getSgsnGtpTEID() throws FieldNotFoundException {
        return getFields(LogFields.SGSN_GTP_TEID);
    }

    @Override
    public String getGgsnGtpTEID() throws FieldNotFoundException {
        return getFields(LogFields.GGSN_GTP_TEID);
    }

    @Override
    public String getRat() throws FieldNotFoundException {
        return getFields(LogFields.RAT);
    }

    @Override
    public String getApn() throws FieldNotFoundException {
        return getFields(LogFields.APN);
    }

    @Override
    public String getHttpGameFlag() throws FieldNotFoundException {
        return getFields(LogFields.HTTP_GAME_FLAG);
    }

    @Override
    public String getGameUserAccount() throws FieldNotFoundException {
        return getFields(LogFields.GAME_USER_ACCOUNT);
    }

    @Override
    public String getLoginStartTime() throws FieldNotFoundException {
        return getFields(LogFields.LOGIN_START_TIME);

    }

    @Override
    public String getLoginEndTime() throws FieldNotFoundException {
        return getFields(LogFields.LOGIN_END_TIME);
    }

    @Override
    public String getCommunicationMethod() throws FieldNotFoundException {
        return getFields(LogFields.COMMUNICATION_METHOD);
    }

    @Override
    public String getPaymentFlag() throws FieldNotFoundException {
        return getFields(LogFields.PAYMENT_FLAG);
    }

    @Override
    public String getLoginFlag() throws FieldNotFoundException {
        return getFields(LogFields.LOGIN_FLAG);
    }

    @Override
    public String getProcedureStatus() throws FieldNotFoundException {
        return getFields(LogFields.PROCEDURE_STATUS);
    }
}
