package com.app.webdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.webdemo.model.RegistrationForm;

import jakarta.validation.Valid;


import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.multipart.MultipartFile; 
import org.springframework.web.servlet.mvc.support.RedirectAttributes; 
import java.io.IOException; 
import java.nio.file.Files; 
import java.nio.file.Path; 
import java.nio.file.Paths; 

@Controller
public class WebdemoController {

    // Serve the index.html page
    @GetMapping("/")
    public String home() {
        return "index"; // returns Thymeleaf template index.html
    }

   // Display the registration form
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration"; // returns Thymeleaf template registration.html
    }

    // Handle form submission
    @PostMapping("/register")
    public String handleRegistration(
            @Valid RegistrationForm registrationForm,
            BindingResult bindingResult,
            Model model) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "registration"; // Return to the form with error messages
        }

        // Add form data to the model for the success page
        model.addAttribute("firstName", registrationForm.getFirstName());
        model.addAttribute("lastName", registrationForm.getLastName());
        model.addAttribute("country", registrationForm.getCountry());
        model.addAttribute("dob", registrationForm.getDob());
        model.addAttribute("email", registrationForm.getEmail());
        model.addAttribute("annualIncome", registrationForm.getAnnualIncome());

        return "success"; // returns Thymeleafe template success.html
    }

    private static String UPLOADED_FOLDER = "uploads/"; 

    // 显示文件上传页面 - GET 请求
    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload"; // 返回 upload.html
    }
   
    @PostMapping("/upload") 
    public String singleFileUpload(@RequestParam("file") MultipartFile file, 
                                   RedirectAttributes redirectAttributes) { 
        if (file.isEmpty()) { 
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload"); 
            return "redirect:/uploadStatus"; 
        } 
        try { 
            byte[] bytes = file.getBytes(); 
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename()); 
            Files.write(path, bytes); 
            redirectAttributes.addFlashAttribute("message", 
                    "You successfully uploaded '" + file.getOriginalFilename() + "'"); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return "redirect:/uploadStatus"; 
    } 
    @GetMapping("/uploadStatus") 
    public String uploadStatus() { 
        return "uploadStatus"; 
    } 
}