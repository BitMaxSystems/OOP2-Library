package org.bitmaxsystems.oop2library.view;

public enum View {
    MAIN_VIEW("hello-view.fxml","Main window",600,400),
    LOGIN("login-view.fxml","Login",600,400);

    private String path;
    private String title;
    private int width;
    private int length;

    View (String path, String title, int width, int length)
    {
        this.path = "/org/bitmaxsystems/oop2library/"+path;
        this.title = title;
        this.width = width;
        this.length = length;

    }

    public int getLength() {
        return length;
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
