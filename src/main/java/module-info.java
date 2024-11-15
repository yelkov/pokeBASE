module edu.badpals.pokebase.pokebase {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.badpals.pokebase.pokebase to javafx.fxml;
    exports edu.badpals.pokebase.pokebase;
}