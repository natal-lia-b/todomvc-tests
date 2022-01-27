package todomvctest.model;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static todomvctest.model.TodoMvc.todos;

public class Label {

    private SelenideElement label;

    public Label(String labelText) {
        this.label = todos.findBy(exactText(labelText));
    }

    public SelenideElement startEditing(String newText) {
        this.label.doubleClick();
        return todos.findBy(cssClass("editing")).find(".edit").setValue(newText);
    }

    public Label editWith(CharSequence key, String newText) {
        this.startEditing(newText).sendKeys(key);
        return this;
    }

    public Label edit(String newText) {
        this.editWith(Keys.ENTER, newText);
        return this;
    }

    public Label cancelEditing(String newText) {
        this.editWith(Keys.ESCAPE, newText);
        return this;
    }

}
