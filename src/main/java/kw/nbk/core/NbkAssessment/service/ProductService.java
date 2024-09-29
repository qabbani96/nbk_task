package kw.nbk.core.NbkAssessment.service;

import kw.nbk.core.NbkAssessment.model.ProductDetail;
import kw.nbk.core.NbkAssessment.model.ProductSummary;
import kw.nbk.core.NbkAssessment.model.request.SearchRequest;
import kw.nbk.core.NbkAssessment.model.response.ProductPagedResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    ProductPagedResponse getProducts(int pageIndex, int pageSize);
    ProductDetail getProductById(int id );
    ResponseEntity<Resource> downloadImage(String imageUrl);
    List<ProductSummary> searchProducts(String searchKey);
}
