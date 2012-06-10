package com.pizza.mvc.admin;

import org.springframework.web.multipart.MultipartFile;

import com.pizza.domain.Topping;

public class ToppingCommand extends Topping {

	MultipartFile imageFile;

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	
}
