package seohyun.app.crud.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import seohyun.app.crud.models.User;
import seohyun.app.crud.service.UserService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    
    @GetMapping("/hello")
    public ResponseEntity<Object> Hello() throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            map.put("result", "hello");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Object> SignUp(@RequestBody User req) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            Boolean checkUserId = userService.CheckUserId(req);
            if (checkUserId == true) {
                map.put("result", "failed 이미 존재하는 아이디 입니다. 다른 아이디를 입력해주세요.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            userService.SignUp(req);
            map.put("result", "회원가입이 완료되었습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
