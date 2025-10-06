package com.example.atsumori.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.atsumori.entity.Creatures;
import com.example.atsumori.service.AtsumoriService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/atsumori")
@RequiredArgsConstructor
public class AtsumoriController {

    private final AtsumoriService atsumoriService;

    // 生き物一覧表示
    @GetMapping
    public String list(Model model) {
        model.addAttribute("creatures", atsumoriService.findAllCreatures());
        model.addAttribute("title", "生き物一覧");
        return "creatures/list"; // このテンプレート名に合わせてください
    }

 // 名前検索
    @GetMapping("/searchByName")
    public String searchByName(@RequestParam String name, Model model, RedirectAttributes redirectAttributes) {
        Creatures creature = atsumoriService.findByNameCreature(name);
        if (creature != null) {
            model.addAttribute("creatures", List.of(creature));
            model.addAttribute("title", "名前検索結果");
            model.addAttribute("searchMessage", name + " ： 検索結果"); // 追加
            return "creatures/list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "対象データがありません");
            return "redirect:/atsumori";
        }
    }

    // 月・半球検索
    @GetMapping("/search")
    public String searchByMonthAndHemisphere(@RequestParam int month, @RequestParam String hemisphere, Model model) {
        if (!"北半球".equals(hemisphere) && !"南半球".equals(hemisphere)) {
            model.addAttribute("errorMessage", "半球は必ず「北半球」か「南半球」を指定してください");
            model.addAttribute("creatures", List.of());
            model.addAttribute("title", "検索結果（エラー）");
            return "creatures/list";
        }

        List<Creatures> creatures = atsumoriService.findByAppearance(month, hemisphere);
        model.addAttribute("creatures", creatures);
        model.addAttribute("title", "検索結果");
        model.addAttribute("searchMessage", month + "月 " + hemisphere + "に出現 ： 検索結果"); // 追加
        return "creatures/list";
    }
}

