package kw.nbk.core.NbkAssessment.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kw.nbk.core.NbkAssessment.model.ProductSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {
    private List<ProductSummary> products;
    private int total;
    private int startIndex;
    private int pageSize;
}
