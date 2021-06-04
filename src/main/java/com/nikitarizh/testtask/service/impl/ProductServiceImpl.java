package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.ProductIsInCartException;
import com.nikitarizh.testtask.exception.ProductNotFoundException;
import com.nikitarizh.testtask.repository.ProductRepository;
import com.nikitarizh.testtask.service.MailService;
import com.nikitarizh.testtask.service.ProductService;
import com.nikitarizh.testtask.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nikitarizh.testtask.mapper.ProductMapper.PRODUCT_MAPPER;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final MailService mailService;
    private final TagService tagService;

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductFullDTO findById(Integer id) throws ProductNotFoundException {
        return PRODUCT_MAPPER.mapToFullDTO(
                productRepository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException(id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductFullDTO> findAll() {
        return productRepository.findAll().stream()
                .map(PRODUCT_MAPPER::mapToFullDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductFullDTO create(ProductCreateDTO productCreateDTO) {
        Product newProduct = productRepository.save(PRODUCT_MAPPER.mapToEntity(productCreateDTO));
        newProduct.setTags(tagService.findAllByIds(productCreateDTO.getTagIds()));
        return PRODUCT_MAPPER.mapToFullDTO(newProduct);
    }

    @Override
    @Transactional
    public ProductFullDTO update(ProductUpdateDTO productUpdateDTO, boolean force) {
        Product productToUpdate = productRepository.findById(productUpdateDTO.getId())
                .orElseThrow(() -> new ProductNotFoundException(productUpdateDTO.getId()));

        if (!force && productToUpdate.getOrderedBy().size() > 0) {
            throw new ProductIsInCartException(productToUpdate);
        }

        for (User buyer : productToUpdate.getOrderedBy()) {
            mailService.sendProductUpdateNotification(buyer, productToUpdate, productUpdateDTO);
        }

        productToUpdate.setDescription(productUpdateDTO.getDescription());
        productToUpdate.setTags(tagService.findAllByIds(productUpdateDTO.getTagIds()));

        return PRODUCT_MAPPER.mapToFullDTO(productToUpdate);
    }

    @Override
    @Transactional
    public void delete(Integer productId) {
        productRepository.deleteById(productId);
    }
}
