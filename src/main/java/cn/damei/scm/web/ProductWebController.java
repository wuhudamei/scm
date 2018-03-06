package cn.damei.scm.web;

import cn.damei.scm.service.prod.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.damei.scm.entity.prod.Product;

@Controller
@RequestMapping("/product")
public class ProductWebController {
	private final static String pc = "pc";
	private final static String ipad = "ipad";
	private final static String m = "m";
	@Autowired
	private ProductService productService;

	@RequestMapping("{id}/{type}")
	public String detail(@PathVariable Long id, @PathVariable String type, Model model) {
		Product product = productService.getById(id);
		model.addAttribute("product", product);
		if (ipad.equals(type)) {
			return "ipad/detail";
		} else if (m.equals(type)) {
			return "m/detail";
		} else {
			return "pc/detail";
		}
	}

}
