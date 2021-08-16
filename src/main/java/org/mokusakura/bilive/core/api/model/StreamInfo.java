package org.mokusakura.bilive.core.api.model;

import com.alibaba.fastjson.annotation.JSONField;

public class StreamInfo {
    @JSONField(name = "live_status")
    private Integer liveStatus;

    @JSONField(name = "encrypted")
    private Boolean encrypted;

    @JSONField(name = "playurl_info")
    private PlayurlInfoClass playurlInfo;

    public Integer getLiveStatus() {
        return liveStatus;
    }

    public StreamInfo setLiveStatus(Integer liveStatus) {
        this.liveStatus = liveStatus;
        return this;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public StreamInfo setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
        return this;
    }

    public PlayurlInfoClass getPlayurlInfo() {
        return playurlInfo;
    }

    public StreamInfo setPlayurlInfo(PlayurlInfoClass playurlInfo) {
        this.playurlInfo = playurlInfo;
        return this;
    }

    public static class PlayurlInfoClass {
        @JSONField(name = "playurl")
        private PlayurlClass playurl;

        public PlayurlClass getPlayurl() {
            return playurl;
        }

        public PlayurlInfoClass setPlayurl(PlayurlClass playurl) {
            this.playurl = playurl;
            return this;
        }
    }

    public static class PlayurlClass {
        @JSONField(name = "stream")
        private StreamItem[] streams;

        public StreamItem[] getStreams() {
            return streams;
        }

        public PlayurlClass setStreams(StreamItem[] streams) {
            this.streams = streams;
            return this;
        }
    }

    public static class StreamItem {
        @JSONField(name = "protocol_name")
        private String protocolName;

        @JSONField(name = "format")
        private FormatItem[] formats;

        public String getProtocolName() {
            return protocolName;
        }

        public StreamItem setProtocolName(String protocolName) {
            this.protocolName = protocolName;
            return this;
        }

        public FormatItem[] getFormats() {
            return formats;
        }

        public StreamItem setFormats(FormatItem[] formats) {
            this.formats = formats;
            return this;
        }
    }

    public static class FormatItem {
        @JSONField(name = "format_name")
        private String formatName;

        @JSONField(name = "codec")
        private CodecItem[] codecs;

        public String getFormatName() {
            return formatName;
        }

        public FormatItem setFormatName(String formatName) {
            this.formatName = formatName;
            return this;
        }

        public CodecItem[] getCodecs() {
            return codecs;
        }

        public FormatItem setCodecs(CodecItem[] codecs) {
            this.codecs = codecs;
            return this;
        }
    }

    public static class CodecItem {
        @JSONField(name = "codec_name")
        private String codecName;

        @JSONField(name = "base_url")
        private String baseUrl;

        @JSONField(name = "current_qn")
        private Integer currentQn;

        @JSONField(name = "accept_qn")
        private Integer[] acceptQn;

        @JSONField(name = "url_info")
        private UrlInfoItem[] urlInfos;

        public String getCodecName() {
            return codecName;
        }

        public CodecItem setCodecName(String codecName) {
            this.codecName = codecName;
            return this;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public CodecItem setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Integer getCurrentQn() {
            return currentQn;
        }

        public CodecItem setCurrentQn(Integer currentQn) {
            this.currentQn = currentQn;
            return this;
        }

        public Integer[] getAcceptQn() {
            return acceptQn;
        }

        public CodecItem setAcceptQn(Integer[] acceptQn) {
            this.acceptQn = acceptQn;
            return this;
        }

        public UrlInfoItem[] getUrlInfos() {
            return urlInfos;
        }

        public CodecItem setUrlInfos(UrlInfoItem[] urlInfos) {
            this.urlInfos = urlInfos;
            return this;
        }
    }

    public static class UrlInfoItem {
        @JSONField(name = "host")
        private String host;

        @JSONField(name = "extra")
        private String extra;

        public String getHost() {
            return host;
        }

        public UrlInfoItem setHost(String host) {
            this.host = host;
            return this;
        }

        public String getExtra() {
            return extra;
        }

        public UrlInfoItem setExtra(String extra) {
            this.extra = extra;
            return this;
        }
    }
}
