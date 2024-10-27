package matal.user.domain;

public enum Role {

    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
