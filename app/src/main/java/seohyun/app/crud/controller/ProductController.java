package seohyun.app.crud.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import seohyun.app.crud.models.Product;
import seohyun.app.crud.models.Purchase;
import seohyun.app.crud.service.ProductService;
import seohyun.app.crud.utils.ImageFile;
import seohyun.app.crud.utils.Jwt;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    private final Jwt jwt;
    private final ImageFile imageFile;
    
    // 상품 등록
    @PostMapping("/createproduct")
    public ResponseEntity<Object> CreateProduct(@RequestHeader String authorization,
    @ModelAttribute Product req, @RequestPart(required = false) MultipartFile[] image) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            productService.CreateProduct(req, image, decoded);
            map.put("result", "success 등록이 완료되었습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // todo 상품 삭제

    // 상품 수정(상품을 등록한 본인만 수정 가능)
    // todo 수정할 상품이 null일 경우 추가
    @PostMapping("/updateproduct")
    public ResponseEntity<Object> UpdateProduct(@RequestHeader String authorization,
    @ModelAttribute Product req, @RequestPart(required = false) MultipartFile[] image) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            Product getProduct = productService.GetProduct(req.getId());
            System.out.println("here1!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+getProduct);
            if (!decoded.equals(getProduct.getUserId())) {
                map.put("result", "failed 수정 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            req.setUserId(decoded);
            productService.UpdateProduct(req, image);
            map.put("result", "success 수정이 완료되었습니다.");
            System.out.println("here2!!!!!!!!!!!!!!!!!!!"+getProduct.getImageUrl());
            // todo
            // 스레드 코드 직전 getProduct.getImageUrl()을 콘솔에 찍어보면 수정 전 기존 이미지가 아닌 
            // 수정 후 이미지 url이 찍힌다.
            // 회원탈퇴, 상품주문 취소때와는 다르게 상품 수정때는 왜 수정 후 데이터로 찍힘?

            // new Thread() {
            //     public void run(){
            //         try{
            //             imageFile.DeleteImages(getProduct.getImageUrl());
            //         } catch(Exception e){
            //             e.printStackTrace();
            //         }
            //     }
            // }.start();
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 상품 주문
    @PostMapping("/purchaseproduct")
    public ResponseEntity<Object> PurchaseProduct(@RequestHeader String authorization,
    @RequestBody List<Purchase> req) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            List<Product> result = productService.UpdateStock(req);
            if (result == null){
                map.put("result", "failed 해당 상품이 존재하지 않거나 재고가 부족합니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            productService.PurchaseProduct(req, decoded);
            map.put("result", "success 주문이 완료되었습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 상품 주문 취소
    @PostMapping("/cancelpurchase")
    public ResponseEntity<Object> CancelPurchase(@RequestHeader String authorization,
    @RequestBody Map<String, String> req) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            jwt.VerifyToken(authorization);
            Purchase findPurchase = productService.FindPurchase(req.get("id"));
            productService.CancelPurchase(req);
            map.put("result", "success 취소가 완료되었습니다.");
            productService.AddStock(findPurchase.getCount(), findPurchase.getProductId());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
