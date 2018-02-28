package org.xdi.oxd.oidc.rp.cert.service;

import org.xdi.oxd.common.CommandResponse;
import org.xdi.oxd.oidc.rp.cert.domain.AppSettings;

public interface OxdService {

    //CommandResponse registerSite(String redirectUrl, String postLogoutRedirectUrl);
	AppSettings registerSite(String testId);
    
    CommandResponse updateSite(String oxdId, String redirectUrl);

    CommandResponse getAuthorizationUrl(String oxdId);

    CommandResponse getTokenByCode(String oxdId, String code,String state);

    CommandResponse getUserInfo(String oxdId, String accessToken);

    CommandResponse getLogoutUrl(String oxdId, String idToken);
}
