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
import seohyun.app.crud.utils.Jwt;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    private final Jwt jwt;
    
    // 상품 등록
    @PostMapping("/createproduct")
    public ResponseEntity<Object> CreateProduct(@RequestHeader String authorization,
    @ModelAttribute Product req, @RequestPart(required = false) MultipartFile[] image) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            jwt.VerifyToken(authorization);
            productService.CreateProduct(req, image);
            map.put("result", "success 등록이 완료되었습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // todo 상품 수정, 삭제

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
    // todo 주문 취소 완료 후 상품 재고 관리
    @PostMapping("/cancelpurchase")
    public ResponseEntity<Object> CancelPurchase(@RequestHeader String authorization,
    @RequestBody Map<String, String> req) throws Exception{
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            productService.CancelPurchase(req, decoded);
            map.put("result", "success 취소가 완료되었습니다.");
            //재고관리
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch(Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
