package com.ProjectByElvara.controllers;

import com.ProjectByElvara.configs.PageConfig;
import com.ProjectByElvara.configs.enums.Pages;
import com.ProjectByElvara.dto.ProductDTO;
import com.ProjectByElvara.entities.additionalClasses.Category;
import com.ProjectByElvara.services.ProductService;
import com.ProjectByElvara.services.exceptions.ProductConstraintException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = {"/products", "/"})
    public ModelAndView openProductsPage(@RequestParam(name = "category", required = false) Category category) throws SQLException {
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.PRODUCTS));
        modelAndView.addObject("category", category);
        modelAndView.addObject("products", this.productService.getAllProducts(category));
        return modelAndView;
    }

    @GetMapping(value = "/product")
    public ModelAndView openProductPage(@RequestParam(name = "id") String id,
                                        RedirectAttributes redirectAttributes) throws SQLException {
        ProductDTO product = this.productService.getProductById(Integer.parseInt(id));
        ModelAndView modelAndView;
        if (product == null) {
            modelAndView = new ModelAndView(new RedirectView("/products"));
            redirectAttributes.addFlashAttribute("message", "Товар відсутній в базі");
        } else {
            modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.PRODUCT));
            modelAndView.addObject("product", product);
        }
        return modelAndView;
    }

    @GetMapping(value = "/addProduct")
    public ModelAndView openAddProductPage() {
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.ADD_PRODUCT));
        modelAndView.addObject("newProduct", new ProductDTO());
        return modelAndView;
    }

    @PostMapping(value = "/addProduct")
    public RedirectView addProduct(@ModelAttribute(name = "newProduct")ProductDTO productDTO,
                                   RedirectAttributes redirectAttributes) throws SQLException {
        productDTO = this.productService.addNewProduct(productDTO);
        if (productDTO == null) {
            redirectAttributes.addFlashAttribute("message", "Такий товар вже існує");
            return new RedirectView("/addProduct");
        } else {
            redirectAttributes.addFlashAttribute("product", productDTO);
            redirectAttributes.addFlashAttribute("message", "Товар успішно додано!");
            return new RedirectView("/product?id=" + productDTO.getId());
        }
    }

    @GetMapping(value = "/changeProductStatus")
    public RedirectView changeProductStatus(@RequestParam(name = "id") String id,
                                      RedirectAttributes redirectAttributes) throws SQLException {
        this.productService.changeProductStatus(id);
        redirectAttributes.addFlashAttribute("message", "Статус товару було успішно змінено");
        return new RedirectView("/products");
    }

    @GetMapping(value = "/updateProduct")
    public ModelAndView openProductUpdatePage(@RequestParam(name = "id") String id,
                                              RedirectAttributes redirectAttributes) throws SQLException {
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.UPDATE_PRODUCT));
        ProductDTO productDTO = this.productService.getProductById(Integer.parseInt(id));
        if (productDTO == null) {
            redirectAttributes.addFlashAttribute("message", "Товар більше не існує");
            return new ModelAndView(new RedirectView("/products"));
        }
        modelAndView.addObject("updatedProduct", productDTO);
        return modelAndView;
    }

    @PostMapping(value = "/updateProduct")
    public RedirectView updateProduct(@ModelAttribute(name = "updatedProduct") ProductDTO updatedProduct,
                                      RedirectAttributes redirectAttributes) throws SQLException {
        try {
            this.productService.updateProduct(updatedProduct);
            redirectAttributes.addFlashAttribute("product", updatedProduct);
            redirectAttributes.addFlashAttribute("message", "Дані було оновлено");
            return new RedirectView("/product?id=" + updatedProduct.getId());
        } catch (ProductConstraintException e) {
            redirectAttributes.addFlashAttribute("message", "Такий продукт вже існує!");
            return new RedirectView("/updateProduct?id=" + updatedProduct.getId());
        }
    }

    @PostMapping(value = "/addToCart")
    public RedirectView addProductToCart(@RequestParam(name = "amount") String amount,
                                         @RequestParam(name = "id") String id,
                                         RedirectAttributes redirectAttributes) throws SQLException {
        ProductDTO productDTO = this.productService.addToCart(id, amount);
        if (productDTO == null) {
            redirectAttributes.addFlashAttribute("message", "Максимальна допустима кількість товару вже додана до кошика");
            return new RedirectView("/product?id=" + id);
        }
        redirectAttributes.addFlashAttribute("message", "До кошика додано "
                + productDTO.getProductName() + " " + productDTO.getTrademark() + " " + productDTO.getSize()
                + ". У кошику зараз " + productDTO.getAmount() + " одиниць товару");
        return new RedirectView("/products");
    }

    @GetMapping(value = "/removeFromCart")
    public RedirectView removeFromCart(@RequestParam(name = "productId") String productId,
                                       RedirectAttributes redirectAttributes) {
        ProductDTO productDTO = this.productService.removeFromCart(Integer.parseInt(productId));
        if (productDTO == null) {
            redirectAttributes.addFlashAttribute("message", "Такого товару немає в кошику");
            return new RedirectView("/cart");
        }
        redirectAttributes.addFlashAttribute("message", "Товар було успішно видалено з кошика");
        return new RedirectView("/cart");
    }
}
