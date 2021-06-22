package com.davalosh.taco.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import com.davalosh.taco.model.Taco;
import com.davalosh.taco.data.IngredientRepository;
import com.davalosh.taco.data.TacoRepository;
import com.davalosh.taco.model.Ingredient;
import com.davalosh.taco.model.Ingredient.Type;

import com.davalosh.taco.model.Order;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
  
  private final IngredientRepository ingredientRepo;
  private TacoRepository designRepo;
  
  private List<Ingredient> ingredients;
  
  @ModelAttribute(name = "order")
  public Order order() {
    return new Order();
  }
  
  @ModelAttribute(name = "taco")
  public Taco taco() {
    return new Taco();
  }
  
  @ModelAttribute
  public void addIngredientsToModel(Model model) {
    if(ingredients == null) {
	  ingredients = new ArrayList<>();
      ingredientRepo.findAll().forEach(i -> ingredients.add(i));
	}
    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
	  model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));      
    }
  }

	
  @Autowired
  public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
    this.ingredientRepo = ingredientRepo;
	this.designRepo = designRepo;
  }
	
  /*
  @ModelAttribute
  public void addIngredientsToModel(Model model) {
    List<Ingredient> ingredients = Arrays.asList(
      new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
	  new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
	  new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
	  new Ingredient("CARN", "Carnitas", Type.PROTEIN),
	  new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
	  new Ingredient("LETC", "Lettuce", Type.VEGGIES),
	  new Ingredient("CHED", "Cheddar", Type.CHEESE),
	  new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
	  new Ingredient("SLSA", "Salsa", Type.SAUCE),
	  new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
	);
		
	Type[] types = Ingredient.Type.values();
	for (Type type : types) {
	  model.addAttribute(type.toString().toLowerCase(),
		filterByType(ingredients, type));
    }
  }
  */
  
  @GetMapping
  public String showDesignForm(Model model) {
	log.info("Design form Get");
	
	printModelAttributes(model);
    return "design";
  }
  
  @PostMapping
  public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order, Model model) {
	log.info("Design form Post");
    if (errors.hasErrors()) {
      printModelAttributes(model);
      return "design";
    }

    Taco saved = designRepo.save(design);
    order.addDesign(saved);
    
    printModelAttributes(model);
    return "redirect:/orders/current";
  }
  
  private void printModelAttributes(Model model) {
	  log.info("Model Attributes");
	  model.asMap().forEach((x,y) -> log.info(x + "-" + y));
  }

  private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
	return ingredients.stream().filter(ingredient -> ingredient.getType().equals(type))
			.collect(Collectors.toList());
  }

}