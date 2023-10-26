package applicationPackage;

public class User {
    private int userId;
    private String username;
    private String hashPassword;


    public User(
        int userId,
        String username,
        String hashPassword
    ) {
        this.userId = userId;
        this.username = username;
        this.hashPassword = hashPassword;
    }

    /**
     * Get the value of userId.
     * @return the userId
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Get the value of username.
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the value of hashPassword.
     * @return the hashPassword
     */
    public String getHashPassword() {
        return this.hashPassword;
    }
}
