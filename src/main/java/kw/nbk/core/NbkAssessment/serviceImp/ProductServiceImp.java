package kw.nbk.core.NbkAssessment.serviceImp;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import kw.nbk.core.NbkAssessment.clients.ProductClient;
import kw.nbk.core.NbkAssessment.model.ProductDetail;
import kw.nbk.core.NbkAssessment.model.ProductSummary;
import kw.nbk.core.NbkAssessment.model.response.ProductPagedResponse;
import kw.nbk.core.NbkAssessment.model.response.ProductResponse;
import kw.nbk.core.NbkAssessment.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImp implements ProductService {

    private final Integer CACHE_SIZE;
    private final String CACHE_NAME;
    private final Integer defaultPageSize;
    private final ProductClient productClient;
    private final HazelcastInstance hazelcastInstance;

    public ProductServiceImp(@Value("${cache_size}") Integer CACHE_SIZE,
                             @Value("${cache_name}") String CACHE_NAME,
                             @Value("${paging.limit}") Integer defaultPageSize, ProductClient productClient, HazelcastInstance hazelcastInstance) {
        this.CACHE_SIZE = CACHE_SIZE;
        this.CACHE_NAME = CACHE_NAME;
        this.defaultPageSize = defaultPageSize;
        this.productClient = productClient;
        this.hazelcastInstance = hazelcastInstance;
    }


    @Override
    public ProductPagedResponse getProducts(int pageIndex, int pageSize) {
        log.info(String.format("getProducts request %s , %s",pageIndex , pageSize));

        pageSize = pageSize <= 0 ? defaultPageSize : pageSize;
        IMap<Integer, List<ProductSummary>> productCache = hazelcastInstance.getMap(CACHE_NAME);

        int startIndex = pageIndex * pageSize;
        int cacheKey   = startIndex / CACHE_SIZE;

        List<ProductSummary> products = productCache.get(cacheKey);
        if(products == null){
            ProductResponse productResponse = productClient.getProducts();
            products = productResponse.getProducts().subList(0 , Math.min(CACHE_SIZE , productResponse.getProducts().size()));
            productCache.set(cacheKey , new ArrayList<>(products));
        }

        int toIndex = Math.min(startIndex + pageSize  , products.size());
        if(startIndex > toIndex)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , String.format("The Page request not found %s",startIndex));
        List<ProductSummary> pagedProducts = products.subList(startIndex, toIndex);
        return new ProductPagedResponse(pagedProducts, startIndex, pageSize, products.size());
    }

    @Override
    public ProductDetail getProductById(int id) {
        log.info(String.format("getProductById request %s",id));
        IMap<Integer, ProductDetail> productCache = hazelcastInstance.getMap(CACHE_NAME);
        ProductDetail product = productCache.get(id);

        if(product == null){
            product = productClient.getProductById(id);
            productCache.put(id , product);
        }
        return product;
    }

    @Override
    public ResponseEntity<Resource> downloadImage(String imageUrl) {
        try{
            log.info("Request Start with URL: {}", imageUrl);
            URL url = new URL(imageUrl);
            RestTemplate restTemplate = new RestTemplate();
            byte [] imageBytes = restTemplate.getForObject(imageUrl , byte[].class);

            ByteArrayResource resource = new ByteArrayResource(imageBytes);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg");
            return new ResponseEntity<>(resource , httpHeaders , HttpStatus.OK);

        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<ProductSummary> searchProducts(String searchKey) {
        log.info(String.format("searchProducts request : %s" , searchKey));
        IMap<Integer, List<ProductSummary>> productsCache = hazelcastInstance.getMap("productsCache");

        return productsCache.values().stream()
              .flatMap(List::   stream)
                .filter(product -> containsIgnoreCase(product.getTitle(), searchKey)
                        || containsIgnoreCase(product.getDescription(), searchKey)
                        || containsIgnoreCase(product.getBrand(), searchKey))
                .collect(Collectors.toList());

    }

    private boolean containsIgnoreCase(String field, String searchKey) {
        return field != null && field.toLowerCase().contains(searchKey.toLowerCase());
    }
}
