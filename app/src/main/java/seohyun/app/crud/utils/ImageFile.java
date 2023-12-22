package seohyun.app.crud.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageFile {
    
    // 단일 이미지
    public String CreateImage(MultipartFile image) throws IllegalStateException, IOException {
        String uploadPath = "/Users/parkseohyun/desktop/project/project/crud/app/src/main/java/seohyun/app/crud/imageUpload/";
        String originalFileName = image.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String savedFileName = uuid.toString() + "_" + originalFileName;
        File file = new File(uploadPath + savedFileName);
        image.transferTo(file);
        return uploadPath + savedFileName;
    }

    public void DeleteImage(String priorImage) throws Exception{
        try{
            if (priorImage != null) {
                Path filePath = Paths.get(priorImage);
                Files.delete(filePath);
            }
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    public List<String> CreateImages(MultipartFile[] image) throws IllegalStateException, IOException {
        List<String> list = new ArrayList<>();
        for(MultipartFile images : image) {
            String uploadPath = "/Users/parkseohyun/desktop/project/project/crud/app/src/main/java/seohyun/app/crud/imageUpload/";
            String originalFileName = images.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String savedFileName = uuid.toString() + "_" + originalFileName;
            File file = new File(uploadPath + savedFileName);
            images.transferTo(file);
            list.add(uploadPath + savedFileName);
        }
        return list;
    }
}