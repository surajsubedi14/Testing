package com.suraj.service;

import java.util.HashMap;
import java.util.Map;

public class CategoryService {

    private Map<Long, String> categoryStore = new HashMap<>();
    private long nextId = 1;

    public String createCategory(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        categoryStore.put(nextId, name);
        return "Category created with ID: " + nextId++;
    }

    public String getCategory(long id) {
        return categoryStore.getOrDefault(id, "Category not found");
    }

    public String updateCategory(long id, String name) {
        if (!categoryStore.containsKey(id)) {
            return "Category not found";
        }
        categoryStore.put(id, name);
        return "Category updated successfully";
    }

    public String deleteCategory(long id) {
        if (categoryStore.remove(id) != null) {
            return "Category deleted successfully";
        }
        return "Category not found";
    }
}
