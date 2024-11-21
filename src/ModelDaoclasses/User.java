package ModelDaoclasses;

public class User {
    private int userId;
    private String name;
    private String contactInfo;

    public User(){

    }

    public User( int userId, String name, String contactInfo){
        this.userId = userId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

}
