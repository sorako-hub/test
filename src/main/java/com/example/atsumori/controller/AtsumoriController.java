package com.example.atsumori.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{type}")
    public String listByType(@PathVariable String type, Model model) {
        if (!isValidType(type)) {
            return errorResult(model, "不正なタイプです", "図鑑（エラー）");
        }

        List<Creatures> creatures = atsumoriService.findCreaturesByType(type);
        model.addAttribute("creatures", creatures);
        model.addAttribute("title", getDisplayName(type) + "図鑑");
        return "creatures/" + type;
    }

    // 名前検索（タイプ別）
    @GetMapping("/{type}/searchByName")
    public String searchByNameWithType(@PathVariable String type, @RequestParam String name, Model model, RedirectAttributes redirectAttributes) {
        if (!isValidType(type)) {
            redirectAttributes.addFlashAttribute("errorMessage", "不正なタイプです");
            return "redirect:/atsumori/" + type;
        }

        Creatures creature = atsumoriService.findByNameAndType(name, type);
        if (creature != null) {
            model.addAttribute("creatures", List.of(creature));
            model.addAttribute("title", getDisplayName(type) + "名前検索結果");
            model.addAttribute("searchMessage", name + " ： 検索結果（" + getDisplayName(type) + "）");
            return "creatures/" + type;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "対象データがありません");
            return "redirect:/atsumori/" + type;
        }
    }

    // 月と半球検索（タイプ別）
    @GetMapping("/{type}/search")
    public String searchByMonthAndHemisphereWithType(@PathVariable String type, @RequestParam int month, @RequestParam String hemisphere, Model model) {
        if (!isValidType(type)) {
            return errorResult(model, "不正なタイプです", "検索結果（エラー）");
        }

        if (!isValidHemisphere(hemisphere)) {
            return errorResult(model, "半球は必ず「北半球」か「南半球」を指定してください", getDisplayName(type) + "検索結果（エラー）");
        }

        List<Creatures> creatures = atsumoriService.findByAppearanceAndType(month, hemisphere, type);
        model.addAttribute("creatures", creatures);
        model.addAttribute("title", getDisplayName(type) + "出現検索結果");
        model.addAttribute("searchMessage", month + "月 " + hemisphere + " に出現する " + getDisplayName(type));
        return "creatures/" + type;
    }

    // 取得済み一覧（タイプ別 / ログイン必要）
    @GetMapping("/{type}/caught")
    public String caughtCreaturesList(@PathVariable String type, HttpSession session, Model model) {
        if (!isValidType(type)) {
            return errorResult(model, "不正なタイプです", "取得済み一覧（エラー）");
        }

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return unauthorizedResult(model, "取得済み" + getDisplayName(type) + "一覧");
        }

        List<Creatures> caughtCreatures = atsumoriService.findCaughtCreaturesByType(userId, type);
        model.addAttribute("creatures", caughtCreatures);
        model.addAttribute("title", "取得済み" + getDisplayName(type) + "一覧");
        model.addAttribute("searchMessage", "取得済みの " + getDisplayName(type) + " 一覧");
        return "creatures/" + type;
    }

    // 未取得一覧（タイプ別 / ログイン必要）
    @GetMapping("/{type}/uncaught")
    public String uncaughtCreaturesList(@PathVariable String type, HttpSession session, Model model) {
        if (!isValidType(type)) {
            return errorResult(model, "不正なタイプです", "未取得一覧（エラー）");
        }

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return unauthorizedResult(model, "未取得" + getDisplayName(type) + "一覧");
        }

        List<Creatures> uncaughtCreatures = atsumoriService.findUncaughtCreaturesByType(userId, type);
        model.addAttribute("creatures", uncaughtCreatures);
        model.addAttribute("title", "未取得" + getDisplayName(type) + "一覧");
        model.addAttribute("searchMessage", "まだ取得していない " + getDisplayName(type) + " 一覧");
        return "creatures/" + type;
    }

    // 月・半球で取得済み検索（タイプ別）
    @GetMapping("/{type}/caughtByMonthAndHemisphere")
    public String caughtByMonthAndHemisphere(@PathVariable String type, @RequestParam int month, @RequestParam String hemisphere, HttpSession session, Model model) {
        if (!isValidType(type)) {
            return errorResult(model, "不正なタイプです", "取得済み検索（エラー）");
        }

        if (!isValidHemisphere(hemisphere)) {
            return errorResult(model, "半球は必ず「北半球」か「南半球」を指定してください", "取得済み" + getDisplayName(type) + "検索結果（エラー）");
        }

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return unauthorizedResult(model, "取得済み" + getDisplayName(type) + "検索結果");
        }

        List<Creatures> caughtCreatures = atsumoriService.findCaughtCreaturesByAppearance(userId, month, hemisphere, type);
        model.addAttribute("creatures", caughtCreatures);
        model.addAttribute("title", "取得済み" + getDisplayName(type) + "検索結果");
        model.addAttribute("searchMessage", month + "月 " + hemisphere + " に取得済みの " + getDisplayName(type));
        return "creatures/" + type;
    }

    // 月・半球で未取得検索（タイプ別）
    @GetMapping("/{type}/uncaughtByMonthAndHemisphere")
    public String uncaughtByMonthAndHemisphere(@PathVariable String type, @RequestParam int month, @RequestParam String hemisphere, HttpSession session, Model model) {
        if (!isValidType(type)) {
            return errorResult(model, "不正なタイプです", "未取得検索（エラー）");
        }

        if (!isValidHemisphere(hemisphere)) {
            return errorResult(model, "半球は必ず「北半球」か「南半球」を指定してください", "未取得" + getDisplayName(type) + "検索結果（エラー）");
        }

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return unauthorizedResult(model, "未取得" + getDisplayName(type) + "検索結果");
        }

        List<Creatures> uncaughtCreatures = atsumoriService.findUncaughtCreaturesByAppearance(userId, month, hemisphere, type);
        model.addAttribute("creatures", uncaughtCreatures);
        model.addAttribute("title", "未取得" + getDisplayName(type) + "検索結果");
        model.addAttribute("searchMessage", month + "月 " + hemisphere + " にまだ取得していない " + getDisplayName(type));
        return "creatures/" + type;
    }

    // -------------------------
    // 共通メソッド
    // -------------------------

    private Long getUserIdFromSession(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        } else if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        return null;
    }

    private boolean isValidHemisphere(String hemisphere) {
        return "北半球".equals(hemisphere) || "南半球".equals(hemisphere);
    }

    private boolean isValidType(String type) {
        if (type == null) return false;
        String normalized = type.trim().toLowerCase();
        return List.of("fish", "insect", "seafood").contains(normalized);
    }

    private String getDisplayName(String type) {
        if (type == null) return "生き物";
        return switch (type.trim().toLowerCase()) {
            case "fish" -> "魚";
            case "insect" -> "虫";
            case "seafood" -> "海の幸";
            default -> "生き物";
        };
    }

    private String unauthorizedResult(Model model, String title) {
        model.addAttribute("creatures", List.of());
        model.addAttribute("title", title);
        model.addAttribute("searchMessage", "ユーザーが認証されていません");
        return "creatures/error";  // error専用テンプレート推奨
    }

    private String errorResult(Model model, String errorMessage, String title) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("creatures", List.of());
        model.addAttribute("title", title);
        return "creatures/error";    // error専用テンプレート推奨
    }
}
