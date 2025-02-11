package ru.mtuci.bvt_demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class MyFileController {
    private final MyFileRepository fileRepository;

    public MyFileController(MyFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> saveFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }

        try {
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setData(bytes);

            fileRepository.save(fileEntity);

            return ResponseEntity.ok().body("Файл успешно загружен: " + fileName);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable(name = "id") Long id) {
        Optional<FileEntity> optionalFileEntity = fileRepository.findById(id);
        if (optionalFileEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FileEntity fileEntity = optionalFileEntity.get();

        return new ResponseEntity<>(fileEntity.getData(), HttpStatus.OK);
    }
}
