package com.example.reservation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "selenium")
public class SeleniumProperties {
    private boolean headless = true;
    private Timeout timeout = new Timeout();

    public static class Timeout {
        private int implicit = 10;
        private int explicit = 10;

        public int getImplicit() {
            return implicit;
        }

        public void setImplicit(int implicit) {
            this.implicit = implicit;
        }

        public int getExplicit() {
            return explicit;
        }

        public void setExplicit(int explicit) {
            this.explicit = explicit;
        }
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public Timeout getTimeout() {
        return timeout;
    }

    public void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }
}
