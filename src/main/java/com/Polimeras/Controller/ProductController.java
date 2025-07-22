package com.Polimeras.Controller;

import com.Polimeras.Entity.Category;
import com.Polimeras.Entity.Products;
import com.Polimeras.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pure‑REST controller: JSON lists, JSON single item, and raw image bytes.
 * No security annotations, so the public site can load data without login.
 */
@CrossOrigin                     // <- keep/remove if you serve pages from another port
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Products> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{category}")
    public List<Products> byCategory(@PathVariable Category category) {
        return productService.getCategoryService(category);
    }

    @GetMapping("/products/VEGETABLES")
    public List<Products> category(){
        List<Products> category = productService.getCategoryService();
        System.out.println(category);
        return category;
    }

    /** GET /api/products/search?keyword=tomato */
    @GetMapping("/search")
    public List<Products> search(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    /* ---------- SINGLE ITEM ---------- */

    /** GET /api/products/{id} */
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Products> one(@PathVariable long id) {
        return ResponseEntity.ok(productService.get(id));
    }

    /** GET /api/products/{id}/image  (binary) */
    @GetMapping(
            value    = "/{id:[0-9]+}/image",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> image(@PathVariable long id) {
        return productService.getProductById(id)
                .map(p -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, p.getImgType())
                        .body(p.getImgUrl()))
                .orElse(ResponseEntity.notFound().build());
    }
}
