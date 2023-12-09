package com.smqa.smqa.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smqa.smqa.datatypes.Role;
import lombok.Data;

import java.time.LocalDate;
@Data
public  class UserDetailsDto {
    private final String email;
    private final String name;
    private final String address;
    private final String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate dob;
    private final Role role;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Role getRole() {
        return role;
    }

    public UserDetailsDto(String email, String name, String address, String phone, LocalDate dob, Role role) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
        this.role = role;
    }




}

