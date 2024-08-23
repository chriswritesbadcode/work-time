package state;

public class AppState {
    private int id;
    private String userName;
    private String fullName;
    private boolean isWorking;

    private static AppState instance;

    private AppState() {
        this.id = 0;
        this.userName = null;
        this.fullName = null;
        this.isWorking = false;
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

    public int getUserId() {
        return id;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean getIsWorking() {
        return isWorking;
    }

    public void setIsWorking(boolean value) {
        this.isWorking = value;
    }
}
