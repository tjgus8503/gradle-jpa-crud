package seohyun.app.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import seohyun.app.crud.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Boolean existsByUserId(String userId);
    User findOneByUserId(String userId);
}
