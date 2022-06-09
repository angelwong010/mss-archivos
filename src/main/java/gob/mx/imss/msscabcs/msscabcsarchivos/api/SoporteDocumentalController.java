package gob.mx.imss.msscabcs.msscabcsarchivos.api;

import gob.mx.imss.msscabcs.msscabcsarchivos.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class SoporteDocumentalController {
    private Logger log = LoggerFactory.getLogger(SoporteDocumentalController.class);

    private final FileService fileService;

    public SoporteDocumentalController(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<?> uploadMultipleFiles(
            @RequestParam(value = "solicitud") Long solicitud,
            @RequestParam(value = "paso") Long step,
            @RequestParam(value = "desagrupador") String desagrupador,
            @RequestPart("files") MultipartFile[] files) {
        final String methodName = "uploadMultipleFiles";
        log.info("On {}. Solicitud: {}, Paso: {}, Desagrupador: {}",methodName,
                solicitud, step, desagrupador);
        return ResponseEntity.ok(fileService.insertaSoporteDocumental(files, solicitud, step, desagrupador));
    }

    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "urlFile") String urlFile){
        Resource fileToDownload = fileService.getFileByURL(urlFile);
        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,  "attachment;filename=\"" + fileToDownload.getFilename() + "\"")
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileToDownload);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFile(@RequestParam(value = "urlFile") String urlFile){
        fileService.deleteFile(urlFile);
        return ResponseEntity.noContent().build();
    }

}
