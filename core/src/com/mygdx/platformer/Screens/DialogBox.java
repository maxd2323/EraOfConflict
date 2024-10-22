package com.mygdx.platformer.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.platformer.Platformer;

public class DialogBox {

    private Stage stage;
    private Label textField;

    public DialogBox(Stage stage) {
        this.stage = stage;

        createDialogBox();
    }

    public void createDialogBox() {
        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Label textField = new Label("Lorem lipsus", style);
        textField.setWidth(Platformer.getvWidth());
        textField.setWrap(true);
        textField.setPosition(20,-100);
        textField.setFillParent(true);

        this.textField = textField;
        stage.addActor(this.textField);
    }

    public void setText(String text) {
        this.textField.setText(text);
    }

    public void appendText(String text) {
        this.textField.setText(textField.getText() + text);
    }

}
