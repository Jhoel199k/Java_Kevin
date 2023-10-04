package util.filechooser;

import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

public class VistaArchivos extends FileView {

    private final Icon iconoImagen = new ImageIcon(this.getClass().getResource("/resources/picture.png"));

    @Override
    public String getDescription(File f) {
        String descripcion = "";
        String nombreArchivo = f.getName().toLowerCase();
        if (f.isDirectory()) {
            return "Directorio";
        } else {
            if (nombreArchivo.endsWith("jpg") || nombreArchivo.endsWith("jpeg")
                    || nombreArchivo.endsWith("png") || nombreArchivo.endsWith("gif")) {
                descripcion = "Imagen";
            }

            return descripcion;
        }
    }

    @Override
    public Icon getIcon(File f) {

        if (f.isDirectory()) {
            return null;
        } else {

            Icon icono = null;
            String nombreArchivo = f.getName().toLowerCase();
            if (nombreArchivo.endsWith("jpg") || nombreArchivo.endsWith("jpeg")
                    || nombreArchivo.endsWith("png") || nombreArchivo.endsWith("gif")) {
                icono = this.iconoImagen;
            }

            return icono;
        }

    }

}
