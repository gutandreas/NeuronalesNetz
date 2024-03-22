module edu.andreasgut.neuronalesnetzwerkfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens edu.andreasgut.neuronalesnetzwerkfx to javafx.fxml;
    exports edu.andreasgut.neuronalesnetzwerkfx;
    exports edu.andreasgut.neuronalesnetzwerkfx.core;
}