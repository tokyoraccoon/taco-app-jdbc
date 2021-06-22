package com.davalosh.taco.data;

import com.davalosh.taco.model.Ingredient;

public interface IngredientRepository {

	  Iterable<Ingredient> findAll();
	  Ingredient findOne(String id);
	  Ingredient save(Ingredient ingredient);

}