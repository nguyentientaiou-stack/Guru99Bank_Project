package model;

public class Customer {

  private final String customerName;
  private final String gender;
  private final String dob;
  private final String address;
  private final String city;
  private final String state;
  private final String pin;
  private final String telephone;
  private final String email;
  private final String password;

  public Customer(String customerName, String gender, String dob, String address, String city, String state, String pin, String telephone, String email, String password) {
    this.customerName = customerName;
    this.gender = gender;
    this.dob = dob;
    this.address = address;
    this.city = city;
    this.state = state;
    this.pin = pin;
    this.telephone = telephone;
    this.email = email;
    this.password = password;
  }

  public String getCustomerName() {
    return customerName;
  }

  public String getGender() {
    return gender;
  }

  public String getDob() {
    return dob;
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getPin() {
    return pin;
  }

  public String getTelephone() {
    return telephone;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public static final class Builder {

    private String customerName;
    private String gender;
    private String dob;
    private String address;
    private String city;
    private String state;
    private String pin;
    private String telephone;
    private String email;
    private String password;

    public Builder() {
    }

    public Builder aCustomer() {
      return new Builder();
    }

    public Builder withCustomerName(String customerName) {
      this.customerName = customerName;
      return this;
    }

    public Builder withGender(String gender) {
      this.gender = gender;
      return this;
    }

    public Builder withEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder withDOB(String dob) {
      this.dob = dob;
      return this;
    }

    public Builder withAddress(String address) {
      this.address = address;
      return this;
    }

    public Builder withCity(String city) {
      this.city = city;
      return this;
    }

    public Builder withState(String state) {
      this.state = state;
      return this;
    }

    public Builder withPin(String pin) {
      this.pin = pin;
      return this;
    }

    public Builder withTelephone(String telephone) {
      this.telephone = telephone;
      return this;
    }

    public Builder withPassword(String password) {
      this.password = password;
      return this;
    }

    public Customer build() {
      return new Customer(customerName, gender, dob, address, city, state, pin, telephone, email, password);
    }
  }
}
