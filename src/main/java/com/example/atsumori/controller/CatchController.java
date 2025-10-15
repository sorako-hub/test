package com.example.atsumori.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.atsumori.entity.Creatures;
import com.example.atsumori.entity.LoginUser;
import com.example.atsumori.service.impl.CatchService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/catch")
@RequiredArgsConstructor
public class CatchController {

    private final CatchService catchService;

    // 魚の画面表示
    @GetMapping("/fish")
    public String showCatchFishPage(@AuthenticationPrincipal LoginUser user, Model model) {
        List<Creatures> uncaughtFish = catchService.getUncaughtCreaturesByType(user, "fish");
        model.addAttribute("uncaughtCreatures", uncaughtFish);
        model.addAttribute("type", "魚");
        return "catch/catchFish"; // 共通のHTMLにするならcatchCreatures.htmlなど
    }

    // 虫の画面表示
    @GetMapping("/insect")
    public String showCatchInsectPage(@AuthenticationPrincipal LoginUser user, Model model) {
        List<Creatures> uncaughtInsect = catchService.getUncaughtCreaturesByType(user, "insect");
        model.addAttribute("uncaughtCreatures", uncaughtInsect);
        model.addAttribute("type", "虫");
        return "catch/catchInsect";
    }

    // 海の幸の画面表示
    @GetMapping("/seafood")
    public String showCatchSeafoodPage(@AuthenticationPrincipal LoginUser user, Model model) {
        List<Creatures> uncaughtSeafood = catchService.getUncaughtCreaturesByType(user, "seafood");
        model.addAttribute("uncaughtCreatures", uncaughtSeafood);
        model.addAttribute("type", "海の幸");
        return "catch/catchSeafood";
    }
  

    // 複数登録（魚・虫・海の幸共通）
    @PostMapping("/{type}/register-multiple")
    public String registerMultipleCaughtCreatures(@RequestParam("creatureIds") List<Long> creatureIds,
                                                  @AuthenticationPrincipal LoginUser user,
                                                  @PathVariable String type) {
        catchService.registerMultipleCreatures(user, creatureIds, type);
        return "redirect:/catch/" + type;
    }
}
