package br.com.projetoFluxoCaixa.controller;

import br.com.projetoFluxoCaixa.model.Usuario;
import br.com.projetoFluxoCaixa.repository.UsuarioRepository;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Augusto
 */
@Controller
public class MenuController {

    @Autowired
    UsuarioRepository ur;

    @RequestMapping("/privacidade")
    public String privacidade() {
        return "privacidade";
    }
    
    @RequestMapping("/cadastrarLancamento")
    public String cadastrarLanc() {
        return "cadastrarLancamento";
    }

    @RequestMapping("/privacidadee")
    public String privacidadeNoLogin() {
        return "privacidadee";
    }

    @RequestMapping("/perfil")
    public String perfil() {
        return "perfil";
    }

    @RequestMapping("/editar-perfil")
    public String editarPerfil(RedirectAttributes ra, HttpSession session) {
    	Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
    	if(usuarioLogado == null) {
    		ra.addFlashAttribute("mensagem", "É necessário logar para essa ação.");
   		 	return "redirect:/login";
    	}else {
    		return "editar-perfil";
    	}
        
    }

    @RequestMapping("/salvarPerfil")
    public String salvarPerfil(@RequestParam("senha2") String senha2, @RequestParam("senha1") String senha1, RedirectAttributes ra, HttpSession session, Usuario usuario) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        String senhaaa = usuario.getSenha();
        if (!senhaaa.equals("")) {

            if (senhaaa.equals(usuarioLogado.getSenha())) {
                if (senha2.equals(senha1)) {
                    usuario.setSenha(senha1);

                    ur.save(usuario);
                    session.setAttribute("usuarioLogado", usuario);
                } else {
                    ra.addFlashAttribute("mensagem", "As senhas devem ser iguais.");
                    return "redirect:/editar-perfil";
                }
            } else {
                ra.addFlashAttribute("mensagem", "Senha original está incorreta.");
                return "redirect:/editar-perfil";
            }

        } else {
            usuario.setSenha(usuarioLogado.getSenha());
            ur.save(usuario);
            session.setAttribute("usuarioLogado", usuario);
        }

        return "perfil";
    }
}
