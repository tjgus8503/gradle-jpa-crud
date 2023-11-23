package seohyun.app.crud.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import seohyun.app.crud.models.User;
import seohyun.app.crud.service.UserService;
import seohyun.app.crud.utils.Bcrypt;
import seohyun.app.crud.utils.Jwt;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final Bcrypt bcrypt;
    private final Jwt jwt;
    
    @GetMapping("/hello")
    public ResponseEntity<Object> Hello() throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            map.put("result", "hello");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 회원가입 
    @PostMapping("/signup")
    public ResponseEntity<Object> SignUp(
        @ModelAttribute User req, @RequestPart(required = false) MultipartFile image) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            Boolean checkUserId = userService.CheckUserId(req);
            if (checkUserId == true){
                map.put("result", "failed 이미 존재하는 아이디 입니다. 다른 아이디를 입력해주세요.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            userService.SignUp(req, image);
            map.put("result", "success 회원가입이 완료되었습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<Object> SignIn(@RequestBody User req) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            User findUserId = userService.FindUserId(req.getUserId());
            if (findUserId == null){
                map.put("result", "failed 해당 아이디가 존재하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Boolean comparePassword = bcrypt.CompareHash(req.getPassword(), findUserId.getPassword());
            if (comparePassword == false){
                map.put("result", "failed 비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            String authorization = jwt.CreateToken(req.getUserId());
            map.put("authorization", authorization);
            map.put("result", "success 로그인 성공.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 회원정보 수정(이름, 이메일)
    @PostMapping("/updateuser")
    public ResponseEntity<Object> UpdateUser(
        @RequestHeader String authorization, @RequestBody User req
    ) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            userService.UpdateUser(req, decoded);
            map.put("result", "success 수정이 완료되었습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
    // 회원정보 수정(이미지)
    // 비밀번호 수정
    // 회원탈퇴
    // 회원정보 조회(마이페이지)
}
