package org.mokusakura.bilive.core.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author MokuSakura
 */
public class DanmakuServerInfo {

    @JSONField(name = "host_list")
    private HostListItem[] hostList;

    @JSONField(name = "token")
    private String token;

    public HostListItem[] getHostList() {
        return hostList;
    }

    public DanmakuServerInfo setHostList(HostListItem[] hostList) {
        this.hostList = hostList;
        return this;
    }

    public String getToken() {
        return token;
    }

    public DanmakuServerInfo setToken(String token) {
        this.token = token;
        return this;
    }

    public static class HostListItem {
        @JSONField(name = "host")
        private String host;

        @JSONField(name = "port")
        private Integer port;

        public String getHost() {
            return host;
        }

        public HostListItem setHost(String host) {
            this.host = host;
            return this;
        }

        public Integer getPort() {
            return port;
        }

        public HostListItem setPort(Integer port) {
            this.port = port;
            return this;
        }

        @Override
        public String toString() {
            return "HostListItem{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }
}
