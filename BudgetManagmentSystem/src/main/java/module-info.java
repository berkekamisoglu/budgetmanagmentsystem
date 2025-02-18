module org.openjfx {
    requires javafx.controls;
    exports org.openjfx;
    requires java.sql;
    requires javafx.graphics;
    opens org.openjfx.Pages to javafx.base; // Bu satırı ekleyin 

}
