package seohyun.app.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import seohyun.app.crud.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>{
    
    Product findOneById(String id);
    
    @Query(value = "select * from product where id = :productId and stock >= :count", nativeQuery = true)
    Product getProductByIdAndStock(@Param("productId") String productId, @Param("count") Integer count);

    @Modifying(clearAutomatically = true)
    @Query(value = "update product set stock = stock + :count where id = :productId", nativeQuery = true)
    int addStock(@Param("count") Integer count, @Param("productId") String productId);
}
