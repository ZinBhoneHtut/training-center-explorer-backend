package com.zbh.tce.entity.audit;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author ZinBhoneHtut
 *
 */
@Data
@Embeddable
public class Audit  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;
    
    @PrePersist
    protected void onCreate() {
    	createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
    	updatedDate = new Date();
    }
}