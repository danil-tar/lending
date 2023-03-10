package md.sinomach.lending.controller;

import lombok.RequiredArgsConstructor;
import md.sinomach.lending.dao.Product;
import md.sinomach.lending.dao.SubType;
import md.sinomach.lending.dao.Type;
import md.sinomach.lending.dto.ProductInfo;
import md.sinomach.lending.dto.SubTypeMenuInfo;
import md.sinomach.lending.dto.TypeMenuInfo;
import md.sinomach.lending.service.ProductService;
import md.sinomach.lending.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/menu_product")
@RequiredArgsConstructor
public class MenuProductController {
    private final ProductService ProductService;
    private final TypeService typeService;

    @GetMapping("all")
    public Set<TypeMenuInfo> menuProducts() {

        Set<Type> allTypes = typeService.getAllTypes();

        return allTypes.stream()
                .map(MenuProductController::convertType)
                .collect(Collectors.toSet());
    }

    private static ProductInfo convertProduct(Product product) {
        return ProductInfo.builder()
                .name(product.getName())
                .id(product.getId())
                .build();
    }

    private static SubTypeMenuInfo convertSubType(SubType subType) {
        Set<ProductInfo> products = subType.getProducts().stream()
                .map(MenuProductController::convertProduct)
                .collect(Collectors.toSet());

        return SubTypeMenuInfo.builder()
                .name(subType.getName())
                .products(products)
                .build();
    }

    private static TypeMenuInfo convertType(Type type) {
        Set<SubTypeMenuInfo> subTypes = type.getSubTypes().stream()
                .map(MenuProductController::convertSubType)
                .collect(Collectors.toSet());

        return TypeMenuInfo.builder()
                .name(type.getName())
                .subTypes(subTypes)
                .build();
    }
}

