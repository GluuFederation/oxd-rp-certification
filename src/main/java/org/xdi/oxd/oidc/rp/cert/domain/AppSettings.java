package org.xdi.oxd.oidc.rp.cert.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AppSettings {
    @Id
    @GeneratedValue
    private Integer id;

    @Column   
    private String oxdId;

    @Column(unique = true)
    private String opHost;

    @Column
    private String testId;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOxdId() {
        return oxdId;
    }

    public void setOxdId(String oxdId) {
        this.oxdId = oxdId;
    }

    public String getOpHost() {
        return opHost;
    }

    public void setOpHost(String opHost) {
        this.opHost = opHost;
    }
    
	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	@Override
	public String toString() {
		return "AppSettings [id=" + id + ", oxdId=" + oxdId + ", opHost=" + opHost + ", testId=" + testId + "]";
	}

}
