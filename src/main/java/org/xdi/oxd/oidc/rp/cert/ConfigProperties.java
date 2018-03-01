package org.xdi.oxd.oidc.rp.cert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperties {
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

	@Override
	public String toString() {
		return "Settings [opHost=" + opHost + ", callbackUrl=" + callbackUrl + ", postLogoutUrl=" + postLogoutUrl
				+ ", scopes=" + scopes + ", acrValues=" + acrValues + ", host=" + host + ", port=" + port + ", email="
				+ email + "]";
	}
}
