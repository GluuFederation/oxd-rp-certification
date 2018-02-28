package org.xdi.oxd.oidc.rp.cert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xdi.oxd.oidc.rp.cert.domain.AppSettings;

@Component
public class Settings {
	private final static String DEFAULT_TEST_ID = "rp-response_type-code/";
	
    @Value("${oxd.server.op-host}")
    private String opHost;
    @Value("${oxd.client.callback-uri}")
    private String callbackUrl;
    @Value("${oxd.client.post-logout-uri}")
    private String postLogoutUrl;
     
    @Value("${oxd.server.scopes}")
    private String scopes;
    @Value("${oxd.server.acr-values}")
    private String acrValues;
    @Value("${oxd.server.host}")
    private String host;
    @Value("${oxd.server.port}")
    private int port;
    @Value("${oxd.server.email}")
    private String email;
    
    private AppSettings appSettings;
    private String currentTestId = DEFAULT_TEST_ID;
    
	public String getOpHost() {
		return opHost;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public String getPostLogoutUrl() {
		return postLogoutUrl;
	}

	public String getScopes() {
		return scopes;
	}

	public String getAcrValues() {
		return acrValues;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getEmail() {
		return email;
	}

	public AppSettings getAppSettings() {
		return appSettings;
	}

	public void setAppSettings(AppSettings appSettings) {
		this.appSettings = appSettings;
	}

	public String getCurrentTestId() {
		return currentTestId;
	}

	public void setCurrentTestId(String currentTestId) {
		this.currentTestId = currentTestId;
	}

	@Override
	public String toString() {
		return "Settings [opHost=" + opHost + ", callbackUrl=" + callbackUrl + ", postLogoutUrl=" + postLogoutUrl
				+ ", scopes=" + scopes + ", acrValues=" + acrValues + ", host=" + host + ", port=" + port + ", email="
				+ email + ", currentTestId=" + currentTestId + "]";
	}	
	
	
}
