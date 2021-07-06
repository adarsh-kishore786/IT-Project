public abstract class Person {

    private String name;
    private int age;
    private String username;
    private String password;

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
    public String toString() {
        return ("\nName: "+name+"\nAge: "+age+"\nUser Name: "+username+"\n");
    }   
}