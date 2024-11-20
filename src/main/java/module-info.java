module edu.badpals.pokebase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;



    opens edu.badpals.pokebase to javafx.fxml;
    opens edu.badpals.pokebase.controller to javafx.fxml;
    opens edu.badpals.pokebase.model to javafx.fxml, javafx.base, com.fasterxml.jackson.databind;
    exports edu.badpals.pokebase;
    exports edu.badpals.pokebase.controller to javafx.fxml;
    exports edu.badpals.pokebase.model to javafx.fxml, com.fasterxml.jackson.databind;
}