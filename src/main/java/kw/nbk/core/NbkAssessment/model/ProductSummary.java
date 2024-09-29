package kw.nbk.core.NbkAssessment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSummary {

    private int id;
    private String title;
    private String description;
    private double price;
    private String brand;
    private String thumbnail;
}
