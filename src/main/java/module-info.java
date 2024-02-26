module edu.andreasgut.neuronalesnetzwerkfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens edu.andreasgut.neuronalesnetzwerkfx to javafx.fxml;
    exports edu.andreasgut.neuronalesnetzwerkfx;
    exports edu.andreasgut.neuronalesnetzwerkfx.core;
}