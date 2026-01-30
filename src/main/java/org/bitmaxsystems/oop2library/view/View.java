package org.bitmaxsystems.oop2library.view;

public enum View {
    MAIN_VIEW("base-dialog-view.fxml","Main window",600,400),
    LOGIN("login-view.fxml","Login",600,400),
    NEW_USER_FORM("user-form-view.fxml","New User Form",600,787),
    BASIC_USER_DETAILS("basic-user-details-view.fxml","User Details",472,665);

    private String path;
    private String title;
    private int width;
    private int height;

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
