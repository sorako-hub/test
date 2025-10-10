package com.example.atsumori.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

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
        return "creatures/list";
    }

    // 名前検索
    @GetMapping("/searchByName")
    public String searchByName(@RequestParam String name, Model model, RedirectAttributes redirectAttributes) {
        Creatures creature = atsumoriService.findByNameCreature(name);
        if (creature != null) {
            model.addAttribute("creatures", List.of(creature));
            model.addAttribute("title", "名前検索結果");
            model.addAttribute("searchMessage", name + " ： 検索結果");
            return "creatures/list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "対象データがありません");
            return "redirect:/atsumori";
        }
    }

    // 月・半球検索（ログイン不要）
    @GetMapping("/search")
    public String searchByMonthAndHemisphere(@RequestParam int month, @RequestParam String hemisphere, Model model) {
        if (!isValidHemisphere(hemisphere)) {
            return errorResult(model, "半球は必ず「北半球」か「南半球」を指定してください", "検索結果（エラー）");
        }

        List<Creatures> creatures = atsumoriService.findByAppearance(month, hemisphere);
        model.addAttribute("creatures", creatures);
        model.addAttribute("title", "検索結果");
        model.addAttribute("searchMessage", month + "月 " + hemisphere + "に出現 ： 検索結果");
        return "creatures/list";
    }

    // 釣った魚一覧（ログイン必要）
    @GetMapping("/caughtFish")
    public String caughtFishList(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return unauthorizedResult(model, "釣った魚一覧");
        }

        List<Creatures> caughtFish = atsumoriService.findCaughtFishByUserId(userId);
        model.addAttribute("creatures", caughtFish);
        model.addAttribute("title", "釣った魚一覧");
        model.addAttribute("searchMessage", "釣った魚一覧");
        return "creatures/list";
    }

    // まだ釣っていない魚一覧（ログイン必要）
    @GetMapping("/uncaughtFish")
    public String uncaughtFishList(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return unauthorizedResult(model, "まだ釣っていない魚一覧");
        }

        List<Creatures> uncaughtFish = atsumoriService.findUncaughtFishByUserId(userId);
        model.addAttribute("creatures", uncaughtFish);
        model.addAttribute("title", "まだ釣っていない魚一覧");
        model.addAttribute("searchMessage", "まだ釣っていない魚一覧");
        return "creatures/list";
    }

    // 月・半球で釣った魚検索（ログイン必要）
    @GetMapping("/caughtFishByMonthAndHemisphere")
    public String caughtFishByMonthAndHemisphere(@RequestParam int month, @RequestParam String hemisphere, HttpSession session, Model model) {
        if (!isValidHemisphere(hemisphere)) {
            return errorResult(model, "半球は必ず「北半球」か「南半球」を指定してください", "釣った魚検索結果（エラー）");
        }

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return unauthorizedResult(model, "釣った魚検索結果");
        }

        List<Creatures> caughtFish = atsumoriService.findCaughtFishByAppearance(userId, month, hemisphere);
        model.addAttribute("creatures", caughtFish);
        model.addAttribute("title", "釣った魚検索結果");
        model.addAttribute("searchMessage", month + "月 " + hemisphere + "に釣った魚一覧");
        return "creatures/list";
    }

    // 月・半球でまだ釣っていない魚検索（ログイン必要）
    @GetMapping("/uncaughtFishByMonthAndHemisphere")
    public String uncaughtFishByMonthAndHemisphere(@RequestParam int month, @RequestParam String hemisphere, HttpSession session, Model model) {
        if (!isValidHemisphere(hemisphere)) {
            return errorResult(model, "半球は必ず「北半球」か「南半球」を指定してください", "まだ釣っていない魚検索結果（エラー）");
        }

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return unauthorizedResult(model, "まだ釣っていない魚検索結果");
        }

        List<Creatures> uncaughtFish = atsumoriService.findUncaughtFishByAppearance(userId, month, hemisphere);
        model.addAttribute("creatures", uncaughtFish);
        model.addAttribute("title", "まだ釣っていない魚検索結果");
        model.addAttribute("searchMessage", month + "月 " + hemisphere + "にまだ釣っていない魚一覧");
        return "creatures/list";
    }

    // ---------------------
    // 🔽 共通メソッド
    // ---------------------

    // セッションから userId を取得（なければ null）
    private Long getUserIdFromSession(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        } else if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        return null;
    }


    // 北半球・南半球のバリデーション
    private boolean isValidHemisphere(String hemisphere) {
        return "北半球".equals(hemisphere) || "南半球".equals(hemisphere);
    }

    // 未認証時のエラー表示
    private String unauthorizedResult(Model model, String title) {
        model.addAttribute("creatures", List.of());
        model.addAttribute("title", title);
        model.addAttribute("searchMessage", "ユーザーが認証されていません");
        return "creatures/list";
    }

    // 汎用的なエラーレスポンス
    private String errorResult(Model model, String errorMessage, String title) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("creatures", List.of());
        model.addAttribute("title", title);
        return "creatures/list";
    }
}
