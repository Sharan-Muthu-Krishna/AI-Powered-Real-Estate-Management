// GlobalExceptionHandler.java - src/main/java/com/realestate/management/exception/GlobalExceptionHandler.java
package com.realestate.management.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, 
                                                   RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/";
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(UnauthorizedException ex, 
                                              RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }
    
    @ExceptionHandler(FileStorageException.class)
    public String handleFileStorageException(FileStorageException ex, 
                                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "File upload failed: " + ex.getMessage());
        return "redirect:/admin/dashboard";
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, 
                                                  RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/register";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("error", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }
}