package com.lambdaschool.shoppingcart.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User
        extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;

    @Column(nullable = false,
            unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "user", allowSetters = true)
    private List<UserRoles> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "user",
            allowSetters = true)
    private List<Cart> carts = new ArrayList<>();

    public User()
    {

    }

    public User(long userid, String username, String password, String comments) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.comments = comments;
    }

    public long getUserid()
    {
        return userid;
    }

    public void setUserid(long userid)
    {
        this.userid = userid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setPasswordNoEncrypt(String password) {
        this.password = password;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public List<UserRoles> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoles> roles) {
        this.roles = roles;
    }

    public List<Cart> getCarts()
    {
        return carts;
    }

    public void setCarts(List<Cart> carts)
    {
        this.carts = carts;
    }

    public List<SimpleGrantedAuthority> getAuthority() {
        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();
        for (UserRoles r: this.roles) {
            String myString = "ROLE_" + r.getRole().getName().toUpperCase();
            rtnList.add(new SimpleGrantedAuthority(myString));
        }

        return rtnList;
    }
}
