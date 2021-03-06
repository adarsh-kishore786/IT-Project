import java.io.*;

/**
  * Person.java
  *
  * An abstract class which has all the login
  * credentials common to Admin and Customer.
  * It is the parent class of Admin.java and
  * Customer.java
  */
public abstract class Person implements Serializable
{
    protected String m_name;
    protected int m_age;
    protected String m_username;
    protected String m_password;

    Person() {}

    Person(String name, int age, String username, String password)
    {
        m_name = name;
        m_age = age;
        m_username = username;
        m_password = password;
    }

    // getter functions
    public String getName() { return m_name; }
    public int getAge() { return m_age; }

    public String getUsername() { return m_username; }
    public String getPassword() { return m_password; }

    // toString() to print it
    @Override
    public String toString()
    {
        return ("\nName     : " + m_name + "\nAge      : " + m_age + "\nUser Name: " + m_username + "\n");
    }
}
