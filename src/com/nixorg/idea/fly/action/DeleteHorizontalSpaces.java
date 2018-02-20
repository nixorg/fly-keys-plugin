package com.nixorg.idea.fly.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

public class DeleteHorizontalSpaces extends AnAction {

    public int skipCharsForward(CharSequence buf ,int point, String skipChars) {
        for (int i = point; i < buf.length(); ++i) {
            if (0 > skipChars.indexOf((int)buf.charAt(i))) {
                return i;
            }
        }
        return buf.length();
    }

    public int skipCharsBackward(CharSequence buf ,int point, String skipChars) {
        for (int i = point - 1; i >= 0; --i) {
            if (0 > skipChars.indexOf((int)buf.charAt(i))) {
                return i + 1;
            }
        }
        return 0;
    }

    @Override
    public void actionPerformed(AnActionEvent e)
    {
        Project project = e.getData(CommonDataKeys.PROJECT);
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        CaretModel caretModel = editor.getCaretModel();

        final Document document = editor.getDocument();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int offset = caretModel.getOffset();
                CharSequence charsSequence = document.getCharsSequence();
                document.deleteString(skipCharsBackward(charsSequence, offset, " \t"), skipCharsForward(charsSequence, offset," \t"));
            }
        };

        WriteCommandAction.runWriteCommandAction(project, runnable);
    }
}
