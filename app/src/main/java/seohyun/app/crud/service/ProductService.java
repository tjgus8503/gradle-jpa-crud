package seohyun.app.crud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import seohyun.app.crud.models.Product;
import seohyun.app.crud.models.Purchase;
import seohyun.app.crud.repository.PurchaseRepository;
import seohyun.app.crud.repository.ProductRepository;
import seohyun.app.crud.utils.ImageFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final ImageFile imageFile;

    @Transactional
    public void CreateProduct(Product req, MultipartFile[] image) throws Exception{
        try{
            UUID uuid = UUID.randomUUID();
            req.setId(uuid.toString());
            if(image != null) {
                List<String> imageUrls = imageFile.CreateImages(image);
                String multiImages = String.join(",", imageUrls);
                req.setImageUrl(multiImages);
            } else {req.setImageUrl(null);}
            productRepository.save(req);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    public List<Product> UpdateStock(List<Purchase> req) throws Exception{
        try{
            List<Product> list = new ArrayList<>();
            for(Purchase purchase : req) {
                Product product = productRepository.getProductByIdAndStock(purchase.getProductId(), purchase.getCount());
                if (product == null) {
                    return null;
                }
                product.setStock(product.getStock() - purchase.getCount());
                list.add(product);
            }
            return productRepository.saveAll(list);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void PurchaseProduct(List<Purchase> req, String decoded) throws Exception{
        try{
            List<Purchase> list = new ArrayList<>();
            for(Purchase purchase : req) {
                UUID uuid =  UUID.randomUUID();
                Purchase value = Purchase.builder()
                    .id(uuid.toString())
                    .userId(decoded)
                    .productId(purchase.getProductId())
                    .count(purchase.getCount())
                    .build();
                list.add(value);
            }
            purchaseRepository.saveAll(list);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    // todo deleteByIdAndUserId
    @Transactional
    public void CancelPurchase(Map<String, String> req) throws Exception{
        try{
            purchaseRepository.deleteById(req.get("id"));
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    public Purchase FindPurchase(String id) throws Exception{
        try{
            return purchaseRepository.findOneById(id);
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public int AddStock(Integer count, String productId) throws Exception{
        try{
            return productRepository.addStock(count, productId);
        } catch(Exception e){
            throw new Exception(e);
        }
    }
}
