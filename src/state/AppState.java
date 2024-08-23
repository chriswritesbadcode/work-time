package state;

public class AppState {
    private String id;
    private String userName;
    private String fullName;

    private static AppState instance;

    private AppState() {
        this.id = null;
        this.userName = null;
        this.fullName = null;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return id;
    }

    public void setUserId(int id) {
        this.id = Integer.toString(id);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
