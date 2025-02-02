package com.heony.coffee_order_collector.client;

import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector._common.exception.CustomException;
import com.heony.coffee_order_collector._common.exception.ErrorCodes;
import com.heony.coffee_order_collector._common.util.MyDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    @RequestMapping
    public String first() {
        throw new CustomException(ErrorCodes.NOT_FOUND_PAGE);
    }

    @RequestMapping("/{corp}")
    public String index(@PathVariable String corp, Model model) {

        model.addAttribute("today", LocalDate.now(MyDateUtils.ZONE_GMT_9));

        switch (corp){
            case "mammoth-coffee" -> {
                model.addAttribute("title", "매머드 커피");
                model.addAttribute("corp_brand", Corp.Brand.MAMMOTH_COFFEE);

            }
            case "mammoth-express" -> {
                model.addAttribute("title", "매머드 익스프레스");
                model.addAttribute("corp_brand", Corp.Brand.MAMMOTH_EXPRESS);
            }
            case null, default -> throw new CustomException(ErrorCodes.NOT_FOUND_PAGE);
        }

        return "index";
    }

}


