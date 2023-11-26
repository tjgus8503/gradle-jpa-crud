package seohyun.app.crud.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import seohyun.app.crud.models.User;
import seohyun.app.crud.repository.UserRepository;
import seohyun.app.crud.utils.Bcrypt;
import seohyun.app.crud.utils.ImageFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final Bcrypt bcrypt;
    private final ImageFile imageFile;
    
    public Boolean CheckUserId(User req) throws Exception{
        try{
            return userRepository.existsByUserId(req.getUserId());
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void SignUp(User req, MultipartFile image) throws Exception{
        try{
            UUID uuid = UUID.randomUUID();
            req.setId(uuid.toString());
            String hashPassword = bcrypt.HashPassword(req.getPassword());
            req.setPassword(hashPassword);
            if (image != null) {
                String imageUrl = imageFile.CreateImage(image);
                req.setImageUrl(imageUrl);
            } req.setImageUrl(null);
            userRepository.save(req);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    public User FindUserId(String userId) throws Exception{
        try{
            return userRepository.findOneByUserId(userId);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void UpdateUser(User req, String decoded) throws Exception{
        try{
            User findUserId = FindUserId(decoded);
            req.setId(findUserId.getId());
            req.setUserId(findUserId.getUserId());
            req.setPassword(findUserId.getPassword());
            req.setImageUrl(findUserId.getImageUrl());
            userRepository.save(req);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void UpdatePassword(User req) throws Exception{
        try{
            userRepository.save(req);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    // deleteBy_ 부분부터.
    @Transactional
    public void UnRegister() throws Exception{
        try{
            userRepository.deleteById(null);
        } catch(Exception e){
            throw new Exception(e);
        }
    }
}
