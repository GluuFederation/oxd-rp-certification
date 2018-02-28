package org.xdi.oxd.oidc.rp.cert.service;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.xdi.oxd.client.CommandClient;
import org.xdi.oxd.common.Command;
import org.xdi.oxd.common.CommandResponse;
import org.xdi.oxd.common.CommandType;
import org.xdi.oxd.common.ResponseStatus;
import org.xdi.oxd.common.params.GetAuthorizationUrlParams;
import org.xdi.oxd.common.params.GetLogoutUrlParams;
import org.xdi.oxd.common.params.GetTokensByCodeParams;
import org.xdi.oxd.common.params.GetUserInfoParams;
import org.xdi.oxd.common.params.RegisterSiteParams;
import org.xdi.oxd.common.params.UpdateSiteParams;
import org.xdi.oxd.common.response.RegisterSiteResponse;
import org.xdi.oxd.oidc.rp.cert.Settings;
import org.xdi.oxd.oidc.rp.cert.domain.AppSettings;
import org.xdi.oxd.oidc.rp.cert.repository.AppSettingsRepository;

@Component
public class OxdServiceImpl implements OxdService {
    private static final Logger logger = LoggerFactory.getLogger(OxdServiceImpl.class);

    @Autowired
    private Settings settings;
    
    @Autowired
    private AppSettingsRepository appSettingsRepository;
    
    private CommandClient client;
    
    @PostConstruct
    public void initIt() {
        try {
            client = new CommandClient(settings.getHost(), settings.getPort());
        } catch (IOException e) {
            logger.error("oxd client did not initialized properly with: {host: '" + settings.getHost() 
            	+ "', port: '" + settings.getPort() + "'}", e);
        }
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        CommandClient.closeQuietly(client);
    }
    
    @EventListener({ContextRefreshedEvent.class})
    private void onContextStarted() {
    	settings.setAppSettings(registerSite(settings.getCurrentTestId()));
    	System.out.println("0.this.appSettings=" + settings.getAppSettings());
    }
    
    public AppSettings registerSite(String testId) {
        AppSettings appSettings = appSettingsRepository.findOneByOpHost(settings.getOpHost());
        System.out.println("OxdServiceImpl::getting appSettings=" + appSettings);
        if (appSettings != null) {
        	System.out.println("OxdServiceImpl::deleting appSettings=" + appSettings);
        	appSettingsRepository.delete(appSettings); 
        }

        settings.setCurrentTestId(testId);
        
        System.out.println("settings=" + settings);
        /// from old register
        final RegisterSiteParams commandParams = new RegisterSiteParams();
        commandParams.setOpHost(settings.getOpHost() + testId);
        commandParams.setAuthorizationRedirectUri(settings.getCallbackUrl());
        commandParams.setPostLogoutRedirectUri(settings.getPostLogoutUrl());
        commandParams.setRedirectUris(Arrays.asList(settings.getCallbackUrl()));
        commandParams.setAcrValues(Arrays.asList(settings.getAcrValues().split(",")));
        commandParams.setScope(Arrays.asList(settings.getScopes().split(",")));
        commandParams.setGrantType(Arrays.asList("authorization_code"));
        commandParams.setResponseTypes(Arrays.asList("code"));
        commandParams.setContacts(Arrays.asList(settings.getEmail()));
        
        final Command command = new Command(CommandType.REGISTER_SITE).setParamsObject(commandParams);

        CommandResponse commandResponse = client.send(command);
        /// end from old register
        
        if (commandResponse.getStatus().equals(ResponseStatus.ERROR))
            throw new RuntimeException("Can not register site: {callbackUrl: '" + settings.getCallbackUrl() + 
            		"', postLogoutUrl: '" + settings.getPostLogoutUrl() + "'}. Plese see the oxd-server.log");

        RegisterSiteResponse response = commandResponse.dataAsResponse(RegisterSiteResponse.class);
        String oxdId = response.getOxdId();

        appSettings = new AppSettings();
        appSettings.setOxdId(oxdId);
        appSettings.setOpHost(settings.getOpHost());
        appSettingsRepository.save(appSettings);    
        
        return appSettings;
    }
    
    @Override
    public CommandResponse updateSite(String oxdId, String redirectUrl) {
        final UpdateSiteParams commandParams = new UpdateSiteParams();
        commandParams.setOxdId(oxdId);
        commandParams.setAuthorizationRedirectUri(redirectUrl);

        final Command command = new Command(CommandType.UPDATE_SITE).setParamsObject(commandParams);

        return client.send(command);
    }

    @Override
    public CommandResponse getAuthorizationUrl(String oxdId) {
        final GetAuthorizationUrlParams commandParams = new GetAuthorizationUrlParams();
        commandParams.setScope(Arrays.asList(settings.getScopes().split(",")));
        commandParams.setAcrValues(Arrays.asList(settings.getAcrValues().split(",")));
        commandParams.setOxdId(oxdId);
        final Command command = new Command(CommandType.GET_AUTHORIZATION_URL).setParamsObject(commandParams);

        return client.send(command);
    }

    @Override
    public CommandResponse getTokenByCode(String oxdId, String code,String state) {
        final GetTokensByCodeParams commandParams = new GetTokensByCodeParams();
        commandParams.setOxdId(oxdId);
        commandParams.setCode(code);
        commandParams.setState(state);
        final Command command = new Command(CommandType.GET_TOKENS_BY_CODE).setParamsObject(commandParams);

        return client.send(command);
    }

    @Override
    public CommandResponse getUserInfo(String oxdId, String accessToken) {
        GetUserInfoParams params = new GetUserInfoParams();
        params.setOxdId(oxdId);
        params.setAccessToken(accessToken);

        final Command command = new Command(CommandType.GET_USER_INFO).setParamsObject(params);

        return client.send(command);
    }

    @Override
    public CommandResponse getLogoutUrl(String oxdId, String idToken) {
        GetLogoutUrlParams params = new GetLogoutUrlParams();
        params.setOxdId(oxdId);
        params.setIdTokenHint(idToken);
        final Command command = new Command(CommandType.GET_LOGOUT_URI).setParamsObject(params);

        return client.send(command);
    }
}
