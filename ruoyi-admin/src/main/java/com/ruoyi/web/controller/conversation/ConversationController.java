package com.ruoyi.web.controller.conversation;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 对话Controller
 * 
 * @author ruoyi
 * @date 2024-02-02
 */
@RestController
@RequestMapping("/conversation")
public class ConversationController extends BaseController
{
   private List<String> replyTextList = Arrays.asList(
           "Excuse me Ms Wong I have a question.",
            "I don't know this word. Can you teach me?",
            "Oh I get it.Thank you Ms Wong.");
    /**
     * 获取对话回复
     */
    @GetMapping(value = "/getReplayInfo/{index}")
    public AjaxResult getReplayInfo(@PathVariable("index") int index) {
        if (index >= replyTextList.size()) {
            return AjaxResult.success("conversation is ended");
        }
        return AjaxResult.success(replyTextList.get(index));
    }
}
