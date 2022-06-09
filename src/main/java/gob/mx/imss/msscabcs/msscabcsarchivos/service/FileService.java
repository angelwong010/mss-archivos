package gob.mx.imss.msscabcs.msscabcsarchivos.service;

import gob.mx.imss.msscabcs.msscabcsarchivos.api.SoporteDocumentalController;
import gob.mx.imss.msscabcs.msscabcsarchivos.exceptions.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class FileService {

    private Logger log = LoggerFactory.getLogger(SoporteDocumentalController.class);

    private final Path fileStorageLocation;

    public FileService(@Value("${file.directory}") String filePath) {
        this.fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("No se pudo crear el directorio donde se almacenarán los archivos.", ex);
        };
    }

    public List<String> insertaSoporteDocumental(MultipartFile[] files, Long solicitud, Long step, String desagrupador) {
        final String methodName = "uploadMultipleFiles";
        log.info("On {}.", methodName);
        List<String> urls = new ArrayList<>();
        Arrays.asList(files).forEach(file -> {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try{
                if (!isValidFile(fileName))
                    throw new FileStorageException("El nombre del archivo contiene una secuencia invalida. " + fileName);

                Path solicitudPath = Paths.get(fileStorageLocation.toString() + "/" + solicitud);
                if (!Files.exists(solicitudPath))
                    Files.createDirectory(solicitudPath);

                Path stepPath = Paths.get(solicitudPath.toString() + "/" + step);
                if (!Files.exists(stepPath))
                    Files.createDirectory(stepPath);

                File directory=new File(stepPath.toString()+"/");
                int numberFile=directory.list().length+1;
                String newFileNameToCreate = buildFileNameToCreate(solicitud, step, desagrupador, (long) numberFile) + getExtensionFile(fileName);
                String targetPath = newFileNameToCreate;


                Path targetLocation = stepPath.resolve(targetPath);

                Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
                //Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                log.info("Archivo creado exitosamente");
                urls.add(targetLocation.toString());
            }catch (IOException ex) {
                log.error("-- .:: No se pudo subir el archivo ::. -- " + ex.getMessage());
                throw new FileStorageException("No se pudo subir el archivo " + fileName + ". Intente nuevamente", ex);
            }
        });
        return urls;
    }

    public ByteArrayResource getFileByURL(final String url) {
        try {
            Path path = Paths.get(url).toAbsolutePath();
            if (Files.exists(path))
                return new ByteArrayResource(Files.readAllBytes(path)){
                    @Override
                    public String getFilename() {
                        return path.getFileName().toString();
                    };
                };
            else
                throw new FileNotFoundException("Archivo " + url + " no encontrado");
        }catch(IOException ex){
            log.error("Ocurrió un error al leer el archivo: {}", url, ex);
            throw new FileStorageException("No se pudo leer el archivo " + url + ". Intente nuevamente", ex);
        }
    }

    public void deleteFile(final String url) {
        try {
            Files.delete(Paths.get(url));
        } catch (IOException ex) {
            log.error("Ocurrió un error al elimnar el archivo: {}", url, ex);
            throw new FileStorageException("No se pudo elimnar el archivo " + url + ". Intente nuevamente", ex);
        }
    }

    private boolean isValidFile(String fileName) {
        if(fileName.contains("..") || !fileName.contains(".pdf"))
            return false;
        return true;
    }

    private String buildFileNameToCreate(final Long solicitud, final Long step, final String desagrupador,final Long numberFile) {
        return new StringBuilder()
                .append(solicitud).append("_")
                .append(step).append("_")
                .append(desagrupador).append("_")
                .append(numberFile).toString();
    }

    public String getExtensionFile(String filename) {
        return "."+filename.substring(filename.lastIndexOf(".")+1);
    }
}