package com.healbe.healbe_example_andorid.pojo.tools;

public class AuthenticateResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String jwtToken;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getJwtToken() {
        return jwtToken;
    }
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public String toString() {
        return "AuthenticateResponse{" +
                "\n id= " + id +
                ",\n firstName= '" + firstName + '\'' +
                ",\n lastName= '" + lastName + '\'' +
                ",\n email= '" + email + '\'' +
                ",\n token= '" + jwtToken + '\'' +
                '}';
    }
}
