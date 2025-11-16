package com.app.webdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.webdemo.model.RegistrationForm;
import com.app.webdemo.service.CustomUserDetailsService;

import jakarta.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class WebdemoController {

    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    
    // 修正构造函数名与类名一致
    public WebdemoController(CustomUserDetailsService userDetailsService, 
                           AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    private static String UPLOADED_FOLDER = "uploads/";

    // 主页
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // 显示注册表单 - 使用 RegistrationForm 对象
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration";
    }

    // 处理注册表单提交 - 使用 RegistrationForm 对象
    @PostMapping("/registration")
    public String handleRegistration(
            @Valid RegistrationForm registrationForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        // 添加到模型用于成功页面显示
        model.addAttribute("firstName", registrationForm.getFirstName());
        model.addAttribute("lastName", registrationForm.getLastName());
        model.addAttribute("country", registrationForm.getCountry());
        model.addAttribute("dob", registrationForm.getDob());
        model.addAttribute("email", registrationForm.getEmail());
        model.addAttribute("annualIncome", registrationForm.getAnnualIncome());

        return "success";
    }

    // 简单的用户注册（用于不同的注册流程）
    @GetMapping("/register")
    public String showSimpleRegisterForm() {
        return "register";
    }

    // 处理简单用户注册
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        
        try {
            userDetailsService.registerUser(username, password);
            
            // 自动登录
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            return "redirect:/greet?success";
            
        } catch (Exception e) {
            return "redirect:/register?error";
        }
    }

    // 文件上传页面
    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }

    // 处理文件上传
    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择要上传的文件");
            return "redirect:/uploadStatus";
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.createDirectories(path.getParent()); // 创建目录如果不存在
            Files.write(path, bytes);
            redirectAttributes.addFlashAttribute("message",
                    "文件 '" + file.getOriginalFilename() + "' 上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "文件上传失败");
        }
        return "redirect:/uploadStatus";
    }

    // 上传状态页面
    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    // 问候页面 - 显示当前用户
    @GetMapping("/greet")
    public String greet(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Username from context: " + username);
        model.addAttribute("username", username);
        return "greet";
    }

    // 登录页面
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}