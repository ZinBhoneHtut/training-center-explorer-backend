package com.zbh.tce.entity.audit;

import com.zbh.tce.common.utils.SecurityUtils;
import com.zbh.tce.exception.BadRequestException;
import org.springframework.util.ObjectUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * 
 * @author ZinBhoneHtut
 *
 */
public class AuditListener {

	@PrePersist
	public void setCreated(Object obj) {
		if (obj instanceof IAudit) {

			IAudit iAudit = (IAudit) obj;
			Audit audit = iAudit.getAudit();

			if (ObjectUtils.isEmpty(audit)) {
				audit = new Audit();
				iAudit.setAudit(audit);
			}

			audit.setCreatedDate(new Date());
			audit.setCreatedBy(getUsername());
		}
	}

	@PreUpdate
	public void setUpdated(Object obj) {
		if (obj instanceof IAudit) {
			IAudit iAudit = (IAudit) obj;
			Audit audit = iAudit.getAudit();
			if (ObjectUtils.isEmpty(audit)) {
				audit = new Audit();
				iAudit.setAudit(audit);
			}
			audit.setUpdatedDate(new Date());
			audit.setUpdatedBy(getUsername());
		}
	}

	private static String getUsername() {
		try {
			return SecurityUtils.getCurrentUsername();
		} catch (BadRequestException badRequestException) {
			return "System";
		}
	}
}
