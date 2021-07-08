import java.io.*;

public abstract class Person implements Serializable {

    private String name;
    private int age;
    private String username;
    private String password;
    
    Person(String pname, int page, String pusername, String ppassword) {
        name=pname;
        age=page;
        username=pusername;
        password=ppassword;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    @Override
    public String toString() {
        return ("\nName: "+name+"\nAge: "+age+"\nUser Name: "+username+"\n");
    }
}
