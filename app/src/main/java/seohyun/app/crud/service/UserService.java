package seohyun.app.crud.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import seohyun.app.crud.models.User;
import seohyun.app.crud.repository.UserRepository;
import seohyun.app.crud.utils.Bcrypt;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final Bcrypt bcrypt;
    
    public Boolean CheckUserId(User req) throws Exception{
        try{
            return userRepository.existsByUserId(req.getUserId());
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void SignUp(User req) throws Exception{
        try{
            UUID uuid = UUID.randomUUID();
            req.setId(uuid.toString());
            String hashPassword = bcrypt.HashPassword(req.getPassword());
            req.setPassword(hashPassword);
            userRepository.save(req);
        } catch(Exception e){
            throw new Exception(e);
        }
    }
}
