package com.suraj.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService();
    }

    @AfterEach
    void tearDown() {
        categoryService = null;
    }

    @Test
    void createCategory_ShouldCreateNewCategory() {
        String result = categoryService.createCategory("Electronics");
        assertNotNull(result);
        assertTrue(result.contains("Category created with ID:"));
    }

    @Test
    void createCategory_WithEmptyName_ShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory("");
        });
        assertEquals("Category name cannot be null or empty", exception.getMessage());
    }

    @Test
    void getCategory_ShouldReturnCategoryName() {
        categoryService.createCategory("Books");
        String result = categoryService.getCategory(1);
        assertEquals("Books", result);
    }

    @Test
    void getCategory_WithNonExistentId_ShouldReturnNotFoundMessage() {
        String result = categoryService.getCategory(999);
        assertEquals("Category not found", result);
    }

    @Test
    void updateCategory_ShouldUpdateExistingCategory() {
        categoryService.createCategory("Fashion");
        String result = categoryService.updateCategory(1, "Clothing");
        assertEquals("Category updated successfully", result);
        assertEquals("Clothing", categoryService.getCategory(1));
    }

    @Test
    void updateCategory_WithNonExistentId_ShouldReturnNotFoundMessage() {
        String result = categoryService.updateCategory(999, "New Category");
        assertEquals("Category not found", result);
    }

    @Test
    void deleteCategory_ShouldDeleteExistingCategory() {
        categoryService.createCategory("Appliances");
        String result = categoryService.deleteCategory(1);
        assertEquals("Category deleted successfully", result);
        assertEquals("Category not found", categoryService.getCategory(1));
    }

    @Test
    void deleteCategory_WithNonExistentId_ShouldReturnNotFoundMessage() {
        String result = categoryService.deleteCategory(999);
        assertEquals("Category not found", result);
    }
}
