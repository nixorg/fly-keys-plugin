package com.nixorg.idea.fly.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;

public class ForwardPunctuation extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        int offset = editor.getCaretModel().getOffset();
        CharSequence charsSequence = editor.getDocument().getCharsSequence();

        String punctuations = "!?\"'#$%&*+.,:;<=>@^`|~]+";
        while (offset < charsSequence.length()) {
            if (punctuations.indexOf(charsSequence.charAt(offset)) > 0) {
                offset++;
                break;
            }
            offset++;
        }
        editor.getCaretModel().moveToOffset(offset);
    }
}
