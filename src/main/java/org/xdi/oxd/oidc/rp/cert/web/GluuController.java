package org.xdi.oxd.oidc.rp.cert.web;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xdi.oxd.common.response.GetTokensByCodeResponse;
import org.xdi.oxd.common.response.GetUserInfoResponse;
import org.xdi.oxd.oidc.rp.cert.Settings;
import org.xdi.oxd.oidc.rp.cert.security.AuthoritiesConstants;
import org.xdi.oxd.oidc.rp.cert.security.GluuUser;
import org.xdi.oxd.oidc.rp.cert.service.OxdService;

@Controller
@RequestMapping("/gluu")
public class GluuController {
	private static final Logger LOG = Logger.getLogger(GluuController.class);
	
    @Inject
    private OxdService oxdService;

    @Inject
    private Settings settings;
    
    @SuppressWarnings("unused")
	@RequestMapping(path = "/callback", method = RequestMethod.GET)
    public String receiveAuthorizationCode(
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append(String.format("\nReceive Authorization Code\n================================\nCallback URI: %s\n", settings.getCallbackUrl()));
    	sb.append("\nRequest parameters:\n---------------------------------");
    	request.getParameterMap().entrySet().forEach(entry -> {
    		sb.append(String.format("\n%s=%s", entry.getKey(), String.join(",", entry.getValue())));
    	});
    	
    	LOG.debug(sb.toString());
    	
    	if(false) { // TODO: replace with an identifier that points to the relevant test
    		String code = request.getParameter("code");
            String state = request.getParameter("code");
            String error = request.getParameter("error");
            String errorDescription = request.getParameter("error_description");
            
	        if (error != null) {
	            redirectAttributes.addAttribute("error", error);
	            redirectAttributes.addAttribute("error_description", errorDescription);
	            return "redirect:/error";
	        }
	
	        Optional<GetTokensByCodeResponse> tokenResponse = Optional.of(oxdService)
	                .map(c -> c.getTokenByCode(settings.getOxdId(), code,state))
	                .map(c -> c.dataAsResponse(GetTokensByCodeResponse.class));
	        GetUserInfoResponse userInfoResponse = tokenResponse
	                .map(c -> oxdService.getUserInfo(settings.getOxdId(), c.getAccessToken()))
	                .map(c -> c.dataAsResponse(GetUserInfoResponse.class))
	                .orElseThrow(() -> new BadCredentialsException("Can't get user info"));
	
	        Collection<GrantedAuthority> authorities = Arrays
	                .asList(new GrantedAuthority[]{new SimpleGrantedAuthority(AuthoritiesConstants.USER)});
	
	        GluuUser user = new GluuUser(tokenResponse.get().getIdToken(), userInfoResponse.getClaims(), authorities);
	        SecurityContextHolder.getContext()
	                .setAuthentication(new UsernamePasswordAuthenticationToken(user, "", authorities));
    	}
    	
        return "redirect:/home";
    }

    @RequestMapping(path = "/tests/{id}", method = RequestMethod.POST)
    public @ResponseBody String setTestId(@PathVariable("id") String testId) {
        System.out.println("testID=" + testId);
        return testId;
    }
    
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/home";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleAllException(BadCredentialsException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("error", e.getCause());
        redirectAttributes.addAttribute("error_description", e.getMessage());
        return "redirect:/error";
    }
}