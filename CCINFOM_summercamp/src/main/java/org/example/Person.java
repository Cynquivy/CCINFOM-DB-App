package org.example;

public class Person {
    private int personId;
    private String personType; // camper or employee
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String registrationDate;
    private String hireDate;
    private String role;

    // Constructors, getters, setters
    public Person() {}
    public Person(int id, String type, String fn, String ln, String email, String phone) {
        this.personId = id; this.personType = type; this.firstName = fn; this.lastName = ln; this.email = email; this.phone = phone;
    }

    public int getPersonId() { return personId; }
    public void setPersonId(int personId) { this.personId = personId; }
    public String getPersonType() { return personType; }
    public void setPersonType(String personType) { this.personType = personType; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() { return personId + " - " + firstName + " " + lastName; }
}
