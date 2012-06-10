package com.pizza.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import com.pizza.domain.PizzaOrder;
import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;




public class PresentationUtils {
	

	// find topping object for given id
	public static Topping getToppingFromToppings(Set<Topping> toppings, Long id)
	{
		for (Topping t: toppings)
			if (t.getId().equals(id))
				return t;
		return null;
	}
	// find PizzaSize object for given id
	public static PizzaSize getSizeFromSizes(List<PizzaSize> sizes, Long id)
	{
		for (PizzaSize s: sizes)
			if (s.getId().equals(id))
				return s;
		return null;
	}
	
	// we shouldn't create a domain object in the presentation layer, but
	// we can reuse one we got from the service layer--find the right one
	// in allToppings and put it in chosenToppings

	public static void addToppingToChosenSet(Long id, List<Topping> chosenToppings, 
			Set<Topping> allToppings)
	{
		Topping chosenTopping = 
			getToppingFromToppings(allToppings, id);
		chosenToppings.add(chosenTopping);
	}
	public static void printReport(List<PizzaOrder> report, PrintStream out)  
	{
		for (PizzaOrder order: report) {
			out.println(order);
			out.println("---------------------");
		}
	}
	// super-simple prompted input from user
	public static String readEntry(BufferedReader in, String prompt) throws IOException {
		System.out.print(prompt + ":");
		return in.readLine().trim();
	}
	
	public static String buildToppingImageUrl(Topping top) {
		String url = "async/getToppingImage.ajax";
		
		if(null != top.getLogo()) {
			return new RedirectBuilder("", url)
						.addParameter("id",top.getId()).buildRedirect();
		}
		
		// return "No Photo Available"
		return "images/IMG_NoPhotoAvailable.png";
		
	}
	
	
}
