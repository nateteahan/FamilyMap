package request;

/**
 * Send appropriate data to the RegisterService class
 */
public class RegisterRequest {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     * Constructor for RegisterRequest
     *
     * @param username  created userName of the person registering
     * @param password  password for the current user
     * @param email_address email address of the current user
     * @param firstName first name of the individual trying to register
     * @param lastName  last name of the individual trying to register
     * @param gender    gender of the individual who is registering
     */
    public RegisterRequest(String username, String password, String email_address, String firstName, String lastName, String gender) {
        setUsername(username);
        setPassword(password);
        setEmail_address(email_address);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
    }

    public boolean isValidRequest(RegisterRequest r) {

        if (r.getUsername().equals("") || r.getPassword().equals("")) {
            return false;
        }
        if (r.getFirstName().equals("") || r.getLastName().equals("")) {
            return false;
        }
        if (r.getEmail_address().equals("") || r.getGender().equals("")) {
            return false;
        }
        return true;
    }

    public boolean checkValidGender(RegisterRequest r) {
      if (r.getGender().equals("m")) {
          return true;
      }
      if (r.getGender().equals("f")) {
          return true;
      }
        return false;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail_address() {
        return email;
    }

    public void setEmail_address(String email_address) {
        this.email = email_address;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
