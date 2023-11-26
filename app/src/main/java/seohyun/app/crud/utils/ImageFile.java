package seohyun.app.crud.utils;

import java.io.File;
import java.io.IOException;
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
}