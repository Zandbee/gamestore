package org.strokova.gamestore.model;

import javax.persistence.*;

/**
 * author: Veronika, 9/4/2016.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username; // max = 45
    private String password; // max = 45
    @Column(name="role_id")
    private Integer roleId;

    protected User() {}

    public User(int id, String username, String password, int roleId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
