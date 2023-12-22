package seohyun.app.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import seohyun.app.crud.models.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String>{
    
}
