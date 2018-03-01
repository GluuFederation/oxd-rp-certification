package org.xdi.oxd.oidc.rp.cert.web;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xdi.oxd.common.response.LogoutResponse;
import org.xdi.oxd.oidc.rp.cert.ConfigProperties;
import org.xdi.oxd.oidc.rp.cert.security.GluuUser;
import org.xdi.oxd.oidc.rp.cert.service.OxdService;

@Controller
public class ApplicationController {

    @Inject
    private OxdService oxdService;

    @Inject
    private ConfigProperties settings;

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public String user() {
        return "fragments/user";
    }

    @RequestMapping(path = {"/", "/home"}, method = RequestMethod.GET)
    public String home() {
        return "fragments/home";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String loginError(Model model, @ModelAttribute("error") String error, @ModelAttribute("error_description") String errorDescription) {
        model.addAttribute("error", error);
        model.addAttribute("error_description", errorDescription);
        return "fragments/error";
    }

    @ModelAttribute("isLoggedIn")
    public Boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken))
            return true;
        else
            return false;
    }

    @ModelAttribute("user")
    public GluuUser getUser() {
        if (!isLoggedIn())
            return null;
        return (GluuUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /*
    @ModelAttribute("logoutUrl")
    public String getLogoutUrl() {
        if (!isLoggedIn())
            return null;
        GluuUser user = getUser();
        return Optional.of(oxdService).map(c -> c.getLogoutUrl(settings.getAppSettings().getOxdId(), user.getIdToken()))
                .map(c -> c.dataAsResponse(LogoutResponse.class)).map(LogoutResponse::getUri).orElse(null);
    }
    */
}