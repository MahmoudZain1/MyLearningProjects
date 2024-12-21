package com.test.vaildtion;

import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.Period;

@Controller
public class ControllerOfForm {

    @InitBinder
    public void aVoid(WebDataBinder webDataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class , stringTrimmerEditor);
    }

    private int CalcAge(LocalDate birthDate){
        return Period.between(birthDate , LocalDate.now()).getYears();
    }

    @GetMapping("/Show")
    public String ShowForm(Model model){
        model.addAttribute("User" , new PersonalInformation());
        return "file";
    }

    @PostMapping("/ProcessForm")
    public String Process(
            @Valid @ModelAttribute("User")PersonalInformation personalInformation,
            BindingResult bindingResult , Model model)
    {
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
                System.out.println("Field: " + ((FieldError) error).getField());
                System.out.println("Rejected Value: " + ((FieldError) error).getRejectedValue());
            });
            model.addAttribute("User", personalInformation);
            return "file";
        }else{
            LocalDate birthDate = personalInformation.getDateOfBirth();
            int age = CalcAge(birthDate);
            model.addAttribute("Age", age);
            System.out.println("Processing successful");
            model.addAttribute("User" , personalInformation);
            return "Done";
        }
    }

}
