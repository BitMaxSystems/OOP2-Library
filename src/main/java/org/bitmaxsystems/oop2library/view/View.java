package org.bitmaxsystems.oop2library.view;

public enum View {
    BASE_HOME_VIEW("base-home-view.fxml","Library",600,400),
    ADMINISTRATIVE_HOME_VIEW("administrative-home-view.fxml","Library management",860,430),
    ADMINISTRATIVE_MANAGEMENT_VIEW("administrative-management-view.fxml","Administrative management",858,433),
    ADMINISTRATIVE_INVENTORY_VIEW("administrative-inventory-view.fxml","Inventory",914,493),
    ADMINISTRATIVE_BOOK_REGISTRY_VIEW("administrative-book-registry-view.fxml","Book registry",914,493),
    INVENTORY_COPY_CREATION_VIEW("inventory-copy-creation-view.fxml", "Add Inventory copies", 430, 260),
    BOOK_PARAMETER_MANAGEMENT_VIEW("book-parameter-manegement-view.fxml","Parameter management",600,400),
    BOOK_PARAMETER_CREATION_VIEW("book-parameter-creation-view.fxml","Parameter creation",486,277),
    BOOK_PARAMETER_DETAILS_VIEW("book-parameter-details-view.fxml","Parameter details",486,277),
    BOOK_REGISTRY_CREATION_VIEW("book-registry-creation-view.fxml","Book creation form",467,532),
    BOOK_REGISTRY_MANAGEMENT_VIEW("book-registry-management-view.fxml","Book management form",467,532),
    LOGIN_VIEW("login-view.fxml","Login",600,400),
    NEW_USER_FORM_VIEW("user-form-creation-view.fxml","New User Form",600,787),
    NEW_ADMINISTRATION_USER_FORM_VIEW("administrative-creation-view.fxml","New Administrative User",600,787),
    BASIC_USER_DETAILS_VIEW("basic-user-details-view.fxml","User Details",472,573),
    USER_DETAILS_VIEW("user-details-view.fxml","User Details",472,665),
    USER_FORM_DETAILS_VIEW("user-form-details-view.fxml","User Form Details",600,721),
    USER_FORM_MANAGEMENT_VIEW("user-form-management-view.fxml","User Form Management",1207,442),
    READER_MANAGEMENT_VIEW("reader-management-view.fxml","Reader Management",970,414),
    READER_DETAILS_VIEW("reader-details-view.fxml","Reader Details",1357,1004),
    LIBRARY_HISTORY_VIEW("library-history-view.fxml","Library history",1346,493),
    LIBRARY_HISTORY_CREATION_VIEW("library-history-creation-view.fxml","Lend book",467,595),
    LIBRARY_HISTORY_DETAILS_VIEW("library-history-details-view.fxml","Lending details",467,658),
    ;

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
