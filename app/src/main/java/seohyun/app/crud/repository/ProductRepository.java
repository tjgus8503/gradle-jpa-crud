package seohyun.app.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import seohyun.app.crud.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>{
    
    @Query(value = "select * from product where id = :productId and stock >= :count", nativeQuery = true)
    Product getProductByIdAndStock(@Param("productId") String productId, @Param("count") Integer count);
}
