package adsyf.renewables.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomeController {
    @Autowired
    HomeService homeService;
    @GetMapping("/calculate")
    public String greeting() {
        return homeService.calculate();
    }
}
