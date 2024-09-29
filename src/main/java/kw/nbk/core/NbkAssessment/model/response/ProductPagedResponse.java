package kw.nbk.core.NbkAssessment.model.response;

import kw.nbk.core.NbkAssessment.model.ProductSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPagedResponse {
    private List<ProductSummary> products;
    private int startIndex;
    private int pageSize;
    private int total;
}
