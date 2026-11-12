package br.com.starkstecnologia.control_api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class UsuarioLoginDto implements Serializable {

    @NotBlank
    private String username;

    @Size(max = 12, min = 4)
    private String password;

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
}
