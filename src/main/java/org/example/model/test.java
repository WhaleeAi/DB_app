package org.example.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class test {

    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn != null) {
            System.out.println("Подключение к базе данных успешно!");
        } else {
            System.out.println("Не удалось подключиться к базе данных.");
        }

        List<Product> products = Products.getInstance().getAllProducts();
        if (products != null && !products.isEmpty()) {
            for (Product p : products) {
                System.out.println("ID: " + p.getId() + ", Название: " + p.getName());
            }
        } else {
            System.out.println("Продукты не найдены или произошла ошибка.");
        }


            Product newProduct = new Product();
            newProduct.setName("Тестовый продукт");
            newProduct.setRetailPrice(99.99);
            newProduct.setWholesalePrice(99.99);
            newProduct.setDescription("Это тестовый продукт.");

            Products.getInstance().addOrUpdateProduct(newProduct);

            List<Product> productss = Products.getInstance().getAllProducts();
            boolean found = false;
            for (Product p : productss) {
                if (p.getName().equals("Тестовый продукт")) {
                    found = true;
                    System.out.println("Продукт добавлен с ID: " + p.getId());
                    break;
                }
            }
            if (!found) {
                System.out.println("Продукт не был добавлен.");
            }

    }
}
