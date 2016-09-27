package com.me;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by wangsenyuan on 9/27/16.
 */
@Configuration
@ConfigurationProperties(prefix = "allowed")
public class Allowed {
    private List<String> urls;
    private List<String> applications;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getApplications() {
        return applications;
    }

    public void setApplications(List<String> applications) {
        this.applications = applications;
    }

    public boolean allowUrl(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }

        return urls.contains(getFirstPath(path));
    }

    public boolean allowApplication(String path, String profile) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }

        String app = getFirstPath(path);
        if (!applications.contains(app)) {
            return false;
        }

        return path.startsWith("/" + app + "/" + profile);
    }

    private String getFirstPath(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        int i = path.indexOf("/");
        if (i > 0) {
            return path.substring(0, i);
        }
        return path;
    }
}
