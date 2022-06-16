package todomvctest.model;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.$$;

public class Label {

    public SelenideElement element;

    public Label(SelenideElement element) {
        this.element = element;
    }

    public SelenideElement startEditing(String newText) {
        this.element.doubleClick();
        return $$("#todo-list>li").findBy(cssClass("editing")).find(".edit").setValue(newText);
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
