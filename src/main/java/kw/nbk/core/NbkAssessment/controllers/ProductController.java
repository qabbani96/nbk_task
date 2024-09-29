package kw.nbk.core.NbkAssessment.controllers;

import kw.nbk.core.NbkAssessment.model.ProductDetail;
import kw.nbk.core.NbkAssessment.model.ProductSummary;
import kw.nbk.core.NbkAssessment.model.request.SearchRequest;
import kw.nbk.core.NbkAssessment.model.response.ProductPagedResponse;
import kw.nbk.core.NbkAssessment.service.ProductService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductPagedResponse> getProducts(
            @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        ProductPagedResponse response = productService.getProducts(pageIndex, pageSize);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getProductById(@PathVariable("id") int id ){
        ProductDetail productDetail = productService.getProductById(id);
        return ResponseEntity.ok().body(productDetail);
    }

    @GetMapping("/image")
    public ResponseEntity<Resource> getImage(@RequestParam("z") String fileUrl) {
        return productService.downloadImage(fileUrl);
    }

    @PostMapping("/search")
    public ResponseEntity<List<ProductSummary>> productSearch(@RequestBody SearchRequest searchRequest){
        List<ProductSummary> products = productService.searchProducts(searchRequest.getSearchKey());
        return ResponseEntity.ok(products);
    }
}
