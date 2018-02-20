package org.xdi.oxd.oidc.rp.cert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xdi.oxd.oidc.rp.cert.domain.AppSettings;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Integer> {
    AppSettings findOneByOpHost(String opHost);
}
