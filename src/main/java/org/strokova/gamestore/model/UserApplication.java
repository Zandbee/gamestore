package org.strokova.gamestore.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author vstrokova, 06.10.2016.
 */
@Entity
@Table(name = "user_application")
public class UserApplication {
    @EmbeddedId
    private UserApplicationKey userApplicationKey;

    protected UserApplication() {}

    public UserApplication(UserApplicationKey userApplicationKey) {
        this.userApplicationKey = userApplicationKey;
    }

    public UserApplicationKey getUserApplicationKey() {
        return userApplicationKey;
    }

    public void setUserApplicationKey(UserApplicationKey userApplicationKey) {
        this.userApplicationKey = userApplicationKey;
    }
}
