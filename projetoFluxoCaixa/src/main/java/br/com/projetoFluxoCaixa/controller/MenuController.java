package br.com.projetoFluxoCaixa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Augusto
 */
@Controller
public class MenuController {

    @RequestMapping("/privacidade")
    public String privacidade() {
        return "privacidade";
    }
    
    @RequestMapping("/privacidadee")
    public String privacidadeNoLogin() {
        return "privacidadee";
    }
    
    @RequestMapping("/perfil")
    public String perfil() {
        return "perfil";
    }
}
