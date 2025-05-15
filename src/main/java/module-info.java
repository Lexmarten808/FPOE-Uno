module com.example.fpoeuno {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    exports com.example.fpoeuno.views;
    exports com.example.fpoeuno.models;
   //exports com.example.fpoeuno; // si usas Main aquí
    // Abre el paquete de controladores para permitir acceso por reflexión
    opens com.example.fpoeuno.controllers to javafx.fxml;
}