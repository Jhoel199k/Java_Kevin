package util.filechooser;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FiltraExtensionArchivos extends FileFilter {

    private String description = "Archivos Permitidos";
    private String[] extensiones;
    private String[] extImg = {"jpg", "jpeg", "png", "gif"};

    public FiltraExtensionArchivos(String extension) {
        this(new String[]{extension});
    }

    public FiltraExtensionArchivos(String[] extensiones) {
        this.extensiones = extensiones;
        generarDescripcion();
    }

    public FiltraExtensionArchivos(String description, String[] extensiones) {
        this.description = description;
        this.extensiones = extensiones;
        generarDescripcion();
    }

    public FiltraExtensionArchivos() {
        this.description = "Imagenes Permitidas";
        this.extensiones = extImg;
        generarDescripcion();
    }

    private void generarDescripcion() {
        for (String extensione : extensiones) {
            description += ", " + extensione;
        }
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            for (String extensione : extensiones) {
                if (f.getName().endsWith(extensione)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
