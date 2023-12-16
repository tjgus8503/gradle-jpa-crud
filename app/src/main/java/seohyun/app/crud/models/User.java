package seohyun.app.crud.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity{
    @Id
    private String id;
    private String username;
    private String userId;
    private String password;
    private String email;
    private String imageUrl;
}
