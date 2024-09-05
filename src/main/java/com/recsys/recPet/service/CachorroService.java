package com.recsys.recPet.service;

import com.recsys.recPet.animals.Cachorro;
import com.recsys.recPet.repository.CachorroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class CachorroService {
    @Autowired
    private CachorroRepository cachorroRepository;

    @Value("${file.path}")
    private String filePath;

    public List<Cachorro> findAll() {
        return cachorroRepository.findAll();
    }

    public Cachorro findById(Long id) {
        return cachorroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado"));
    }

    public Cachorro save(Cachorro cachorro) {
        return cachorroRepository.save(cachorro);
    }

    public Cachorro update(@NotNull Cachorro cachorro) {
        Optional<Cachorro> cachorroOptional = cachorroRepository.findById(cachorro.getId());
        if (cachorroOptional.isPresent()) {
            return cachorroRepository.save(cachorro);
        } else {
            throw new EntityNotFoundException("Animal não encontrado");
        }
    }

    public void delete(Long id) {
        Cachorro cachorro = findById(id);
        cachorroRepository.delete(cachorro);
    }

    public String uploadFile(MultipartFile file) {

        // Substituir o Time pelo id do usuário
        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path destPath = Paths.get(filePath + "//" + fileName);
            file.transferTo(destPath);

            return destPath.toString();
        } catch (IOException e) {
            System.err.println("Error saving the file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /* MODELO SALVANDO COM ARQUIVO TEMPORARIO ********************************************
    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        String uploadDir = "C:\\Users\\Lucas\\Documents\\Projeto\\RecPet Projeto\\Upload image\\";

        try {
            Path destPath = Paths.get(uploadDir + fileName);
            Files.copy(fileObj.toPath(), destPath);

            return fileName;
        } catch (IOException e) {
            System.err.println("Error saving the file: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fileObj.delete();
        }
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.err.println("Error converting multipartFile to file: " + e.getMessage());
            e.printStackTrace();
        }
        return convertedFile;
    }

*/

}
