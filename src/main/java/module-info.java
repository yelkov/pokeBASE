module edu.badpals.pokebase.pokebase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens edu.badpals.pokebase to javafx.fxml;
    exports edu.badpals.pokebase;
}