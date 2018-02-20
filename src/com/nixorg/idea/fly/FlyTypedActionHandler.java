package com.nixorg.idea.fly;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.ActionPlan;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.editor.actionSystem.TypedActionHandlerEx;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FlyTypedActionHandler implements TypedActionHandlerEx {

    private final TypedActionHandler originalHandler;
    private final FlyKeyHandler flyKeyHandler;

    public FlyTypedActionHandler(TypedActionHandler originalHandler) {
        this.originalHandler = originalHandler;
        this.flyKeyHandler = FlyKeyHandler.getInstance();
    }

    @Override
    public void beforeExecute(@NotNull Editor editor, char c, @NotNull DataContext context, @NotNull ActionPlan plan) {
        if (originalHandler instanceof TypedActionHandlerEx) {
            //((TypedActionHandlerEx)originalHandler).beforeExecute(editor, c, context, plan);
        }
    }

    @Override
    public void execute(@NotNull Editor editor, char charTyped, @NotNull DataContext dataContext) {
        if(!FlyPlugin.isEnable() || !flyKeyHandler.handleKey(editor, KeyStroke.getKeyStroke(charTyped), dataContext)) {
            originalHandler.execute(editor, charTyped, dataContext);
        }
    }
}
