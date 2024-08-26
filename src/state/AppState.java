package state;

public class AppState {
    private int id;
    private String userName;
    private String fullName;
    private boolean isWorking;
    private String role;

    private static AppState instance;

    private AppState() {
        this.id = 0;
        this.userName = null;
        this.fullName = null;
        this.isWorking = false;
        this.role = "";
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public static AppState resetInstance() {
        instance = new AppState();

        return instance;
    }

    // USER NAME
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // USER ID
    public int getUserId() {
        return id;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    // FULL NAME
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // ISOWRKING
    public boolean getIsWorking() {
        return isWorking;
    }

    public void setIsWorking(boolean value) {
        this.isWorking = value;
    }

    // ROLE
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
