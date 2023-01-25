package com.microservice.erp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/version")
public class VersionInfoController {

    @Value("${app.version}")
    private String version;

    @Value("${app.build.name}")
    private String buildName;

    @Value("${app.build.version}")
    private String buildVersion;

    @Value("${app.ios.version}")
    private String iosVersion;

    @Value("${app.ios.build}")
    private String iosBuild;

    @Value("${app.ios.force.update}")
    private Boolean iosForceUpdate;

    @Value("${app.android.version}")
    private String androidVersion;

    @Value("${app.android.version.code}")
    private String androidVersionCode;

    @Value("${app.android.force.update}")
    private Boolean androidForceUpdate;

    @GetMapping
    public ResponseEntity<Map> version() {
        Map<String, String> version = new HashMap<>();
        version.put("app.version", this.version);
        version.put("app.build.name", buildName);
        version.put("app.build.version", buildVersion);

        version.put("app.ios.version", iosVersion);
        version.put("app.ios.build", iosBuild);
        version.put("app.ios.force.update", iosForceUpdate.toString());

        version.put("app.android.version", androidVersion);
        version.put("app.android.version.code", androidVersionCode);
        version.put("app.android.force.update", androidForceUpdate.toString());
        return ResponseEntity.ok(version);
    }
}
