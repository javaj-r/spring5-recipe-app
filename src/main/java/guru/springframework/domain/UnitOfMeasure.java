package guru.springframework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class UnitOfMeasure extends BaseEntity {

    private String description;

}
