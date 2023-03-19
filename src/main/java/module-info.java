module mukan.minesweeperfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens mukan.minesweeperfx to javafx.fxml;
    exports mukan.minesweeperfx;
}