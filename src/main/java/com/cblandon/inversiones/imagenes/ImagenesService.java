package com.cblandon.inversiones.imagenes;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class ImagenesService {

    private final Path rutaImagenes = Paths.get("imagenes");

    public String guardarArchivo(MultipartFile archivo) throws Exception {

        if (archivo.isEmpty()) {
            throw new Exception("archivo enviado vacio");
        }

        String nombreArchivo = StringUtils.cleanPath(Objects.requireNonNull(archivo.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(nombreArchivo);
        String archivoGuardado = System.currentTimeMillis() + "." + extension;

        if (nombreArchivo.contains("..")) {
            throw new Exception("Nombre de archivo incorrecto");
        }

        try (InputStream inputStream = archivo.getInputStream()) {

            Files.copy(inputStream, this.rutaImagenes.resolve(archivoGuardado), StandardCopyOption.REPLACE_EXISTING);
            return archivoGuardado;

        } catch (IOException ex) {
            throw new Exception("Error al almacenar archivo");
        }
    }

    public boolean eliminarArchivo(String nombreArchivo) {
        try {
            Path file = rutaImagenes.resolve(nombreArchivo);
            if (Files.exists(file)) {
                Files.delete(file);
                return true;
            } else
                return false;
        } catch (IOException ex) {
            return false;
        }
    }

    public Resource cargarArchivo(String nombreArchivo) {
        try {
            Path file = rutaImagenes.resolve(nombreArchivo);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.err.println("error IO");
        }
        return null;
    }
}
