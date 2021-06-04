package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.order.OrderChangeDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ProductFullDTO addItem(@RequestBody OrderChangeDTO orderChangeDTO) {
        return cartService.addItem(orderChangeDTO);
    }

    @PutMapping("/remove")
    public void removeItem(@RequestBody OrderChangeDTO orderChangeDTO) {
        cartService.removeItem(orderChangeDTO);
    }

    @PutMapping("/buy")
    public void buyItems(@RequestBody Integer userId) {
        cartService.buyItems(userId);
    }
}
