package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.exception.ProductNotFoundException;

public interface ProductService {

    ProductFullDTO findById(Integer id) throws ProductNotFoundException;

    Iterable<ProductFullDTO> findAll();

    ProductFullDTO create(ProductCreateDTO productCreateDTO);

    ProductFullDTO update(ProductUpdateDTO productUpdateDTO, boolean force);

    void delete(Integer productId);
}
