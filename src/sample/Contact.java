package sample;

public class Contact {
    String firstName;
    String lastName;
    String phoneNumber;
    String email;

    public Contact(String firstName, String lastName, String phoneNumber, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setFirstName(String fn){
        this.firstName = fn;
    }

    public void setLastName(String ln){
        this.lastName = ln;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getFirstName(){ return firstName; }

    public String getLastName(){ return lastName; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getEmail() { return email; }

    @Override
    public String toString() {
        return firstName + " " + lastName + "\n" + phoneNumber + "\n" + email;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;

        if(this.getClass() != obj.getClass())
            return false;

        Contact contact = (Contact) obj;

        return (this.firstName.equals(contact.firstName) && this.lastName.equals(contact.lastName) && this.phoneNumber.equals(contact.phoneNumber) && this.email.equals(contact.email));
    }
}
