package za.co.philani.customer.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Document(collection = "customers")
@Builder
public class Customer implements Serializable{

    @Id
    @NotBlank(message = "customer number is required")
    private String custNo;
    @NotBlank(message = "customer name is required")
    private String name;
    @NotBlank(message = "customer surname is required")
    private String surname;
}
