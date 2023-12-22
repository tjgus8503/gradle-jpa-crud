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
@Table(name = "product")
public class Product extends BaseEntity{
    @Id
    private String id;
    private String name;
    private String description;
    private Integer price;
    private Integer stock;
    private String imageUrl;
}
