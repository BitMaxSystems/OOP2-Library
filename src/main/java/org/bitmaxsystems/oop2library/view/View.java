package org.bitmaxsystems.oop2library.view;

public enum View {
    BASE_MAIN_VIEW("base-dialog-view.fxml","Library",600,400),
    ADMIN_MAIN_VIEW("administrative-base-view.fxml","Library management",860,430),
    ADMINISTRATIVE_MANAGEMENT_VIEW("administrative-management-view.fxml","Administrative management",858,433),
    LOGIN("login-view.fxml","Login",600,400),
    NEW_USER_FORM("user-form-view.fxml","New User Form",600,787),
    NEW_ADMINISTRATION_USER_FORM("administrative-creation-view.fxml","New Administrative User",600,787),
    BASIC_USER_DETAILS("basic-user-details-view.fxml","User Details",472,573),
    USER_DETAILS("user-details-view.fxml","User Details",472,665);

    private final String path;
    private final String title;
    private final int width;
    private final int height;

    View (String path, String title, int width, int height)
    {
        this.path = "/org/bitmaxsystems/oop2library/"+path;
        this.title = title;
        this.width = width;
        this.height = height;

    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }
}
