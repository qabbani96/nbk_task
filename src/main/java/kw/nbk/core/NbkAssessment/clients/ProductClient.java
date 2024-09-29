package kw.nbk.core.NbkAssessment.clients;

import kw.nbk.core.NbkAssessment.model.ProductDetail;
import kw.nbk.core.NbkAssessment.model.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productClient", url = "${api.products.url}")
public interface ProductClient {
    @GetMapping("/products")
    ProductResponse getProducts();

    @GetMapping("/product/{id}")
    ProductDetail getProductById(@PathVariable("id") int id);

}
