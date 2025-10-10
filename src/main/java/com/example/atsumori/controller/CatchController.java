package com.example.atsumori.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 釣った魚登録の画面表示
    @GetMapping("/fish")
    public String showCatchFishPage(@AuthenticationPrincipal LoginUser user, Model model) {
        System.out.println("ログインユーザーID: " + user.getId());
        List<Creatures> uncaughtFish = catchService.getUncaughtFish(user);
        model.addAttribute("uncaughtFish", uncaughtFish);
        return "catch/catchFish"; // ← catchFish.html に飛ばす
    }

    // 複数の魚を登録する処理
    @PostMapping("/fish/register-multiple")
    public String registerMultipleCaughtFish(@RequestParam("fishIds") List<Long> fishIds,
                                             @AuthenticationPrincipal LoginUser user) {
        catchService.registerMultipleFish(user, fishIds);
        return "redirect:/catch/fish";
    }
}
