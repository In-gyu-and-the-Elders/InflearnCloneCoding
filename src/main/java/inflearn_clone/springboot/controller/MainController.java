package inflearn_clone.springboot.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class MainController {
  @GetMapping("/main")
  public void main(Model model) {
    model.addAttribute("msg", "Hello World");
  }
}
