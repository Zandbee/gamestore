package org.strokova.gamestore.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author vstrokova, 06.10.2016.
 */
@Embeddable
public class UserApplicationKey implements Serializable{
    @NotNull
    @ManyToOne
    @Column(name = "user_id")
    int userId;

    @NotNull
    @ManyToOne
    @Column(name = "application_id")
    int applicationId;

    protected UserApplicationKey() {}

    public UserApplicationKey(int userId, int applicationId) {
        this.userId = userId;
        this.applicationId = applicationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }
}
