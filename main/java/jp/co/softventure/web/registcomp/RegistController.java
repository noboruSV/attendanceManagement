package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistController {
	@ModelAttribute
	public RegistPrintForm setForm() {
		return new RegistPrintForm();
	}
	@RequestMapping("/regist")
	public String regist() {
		return "regist";
	}
	@RequestMapping("/registcomp")
	public String helloprint(RegistPrintForm form) {
		return "registcomp";
	}
}