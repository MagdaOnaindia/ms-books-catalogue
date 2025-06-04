package com.unir.products.data;

import com.unir.products.data.utils.Consts;
import com.unir.products.data.utils.SearchCriteria;
import com.unir.products.data.utils.SearchOperation;
import com.unir.products.data.utils.SearchStatement;
import com.unir.products.data.model.Product;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final ProductJpaRepository repository;

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return repository.save(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }

    public List<Product> search(String name, String country, String description, Boolean visible) {
        SearchCriteria<Product> spec = new SearchCriteria<>();

        if (StringUtils.isNotBlank(name)) {
            spec.add(new SearchStatement(Consts.NAME, name, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(country)) {
            spec.add(new SearchStatement(Consts.COUNTRY, country, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(description)) {
            spec.add(new SearchStatement(Consts.DESCRIPTION, description, SearchOperation.MATCH));
        }

        if (visible != null) {
            spec.add(new SearchStatement(Consts.VISIBLE, visible, SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }

}
