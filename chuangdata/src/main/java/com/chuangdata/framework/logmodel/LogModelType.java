package com.chuangdata.framework.logmodel;

/**
 * The logModel type we currently supported.
 *
 * @author luxiaofeng
 */
public enum LogModelType {

    UNIFIED_DPI_MOBILE_TEXT_LOG_2_0("unified_dpi_mobile_text_log_2.0", "/config/unified_dpi_mobile_text_log_2.0.json", LogFormatType.TEXT),
    UNIFIED_DPI_MOBILE_HTTP_TEXT_LOG_2_0("unified_dpi_mobile_http_text_log_2.0", "/config/unified_dpi_mobile_http_text_log_2.0.json", LogFormatType.TEXT),
    PPP_HTTP_TEXT_LOG("ppp_http_text_log", "/config/ppp_http_text_log.json", LogFormatType.TEXT),
    UASG_HTTP_TEXT_LOG("uasg_http_text_log", "/config/uasg_http_text_log.json", LogFormatType.TEXT), // fixed network userurl
    UASG_TEXT_LOG("uasg_text_log", "/config/uasg_text_log.json", LogFormatType.TEXT), // fixed network userflow
    UASG_HTTP_TEXT_LOG_1_0_1("uasg_http_text_log_1.0.1", "/config/uasg_http_text_log_1.0.1.json", LogFormatType.TEXT),
    UASG_TEXT_LOG_1_0_1("uasg_text_log_1.0.1", "/config/uasg_text_log_1.0.1.json", LogFormatType.TEXT),
    UASG_MOBILE_HTTP_TEXT_LOG_1_0_1("uasg_mobile_http_text_log_1.0.1", "/config/uasg_mobile_http_text_log_1.0.1.json", LogFormatType.TEXT),
    UASG_MOBILE_TEXT_LOG_1_0_1("uasg_mobile_text_log_1.0.1", "/config/uasg_mobile_text_log_1.0.1.json", LogFormatType.TEXT),
    UASG_GAME_TEXT_LOG_1_0_1("uasg_game_text_log_1.0.1", "/config/uasg_game_text_log_1.0.1.json", LogFormatType.TEXT);

    private String name;
    private String configFilePath;
    private LogFormatType logFormatType;

    private LogModelType(String name, String configFilePath, LogFormatType logFormatType) {
        this.name = name;
        this.configFilePath = configFilePath;
        this.logFormatType = logFormatType;
    }

    public String getName() {
        return this.name;
    }

    public String getConfigFilePath() {
        return this.configFilePath;
    }

    public LogFormatType getLogFormatType() {
        return this.logFormatType;
    }

    public static LogModelType getLogModelType(String name) throws UnsupportedLogModelNameException {
        for (LogModelType type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        throw new UnsupportedLogModelNameException("Unsupported log model name: " + name);
    }
}
