package me.dolia.pmm.controller;

import me.dolia.pmm.entity.User;
import me.dolia.pmm.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class IndexController {

	@Inject
	private UserService userService;

	@ModelAttribute("user")
	public User createUser() {
		return new User();
	}

	@RequestMapping("/index")
	public String index() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:/app";
		}
		return "index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signin() {
		return "signin";
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public String doSignin(@Valid @ModelAttribute("user") User user, BindingResult result,
			RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "signin";
		}
		userService.save(user);
		attr.addFlashAttribute("success", true);
		return "redirect:/signin";
	}

	@RequestMapping("/profile")
	public String profile() {
		return "profile";
	}

	@RequestMapping("/profile/delete_profile")
	public String deleteProfile(HttpServletRequest request, Principal principal, RedirectAttributes attr)
			throws ServletException {
		String email = principal.getName();
		userService.deleteByEmail(email);
		request.logout();
		attr.addFlashAttribute("message", "delete_profile_success");
		return "redirect:/";
	}

	/*
	 * Service method for jQuery validation plugin - to check whether email is
	 * available or not
	 */
	@RequestMapping("/signin/available_email")
	@ResponseBody
	public String availableEmail(@RequestParam String email) {
		Boolean available = userService.findOneByEmail(email) == null;
		return available.toString();
	}
	
	
	@RequestMapping("/404")
	public String pageNotFound() {
		return "404";
	}
	
	@RequestMapping("/error")
	public String errorPage() {
		return "error";
	}
}
