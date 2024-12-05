/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senacpi.estetica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author crist
 */

@Controller
public class ControllerEstetica {
    
    @GetMapping("/tela-inicio")
    public String mostraIndex() {
        return "index";
    }
    
    @GetMapping("/inserir-paciente")
    public String mostraCadastro() {
        return "cadastro";
    }
    
    @GetMapping("/listar-paciente")
    public String mostraPaciente() {
        return "lista";
    }
    
    
    
    
}
