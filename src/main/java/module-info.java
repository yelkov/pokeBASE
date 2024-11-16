module edu.badpals.pokebase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens edu.badpals.pokebase to javafx.fxml;
    opens edu.badpals.pokebase.controller to javafx.fxml;
    exports edu.badpals.pokebase;
    exports edu.badpals.pokebase.controller to javafx.fxml;
}