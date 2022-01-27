package todomvctest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import todomvctest.model.TodoMvc;
import todomvctest.testconfigs.BaseTest;

import static com.codeborne.selenide.Condition.hidden;

public class OperationsOnCompletedFilterTest extends BaseTest {

    private TodoMvc app = new TodoMvc();

    @Test
    void editingTodoWithEnter() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("a");
        app.toggle("c");
        app.filterCompleted();

        app.getLabel("a").editWith(Keys.ENTER, "a edited");

        app.todosShouldBe("a edited", "c");
        app.itemsLeftShouldBe(1);
    }

    @Test
    void unCompletingOneOfSeveralCompleted() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("a");
        app.toggle("c");
        app.filterCompleted();

        app.toggle("c");

        app.todosShouldBe("a");
        app.itemsLeftShouldBe(2);
    }

    @Test
    void unCompletingLastCompleted() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("b");
        app.filterCompleted();

        app.toggle("b");

        app.todosShouldBeEmpty();
        app.clearCompletedShouldBe(hidden);
    }

    @Test
    void deletingLastCompletedTodo() {
        app.givenOpenedWith("a");
        app.toggle("a");
        app.filterCompleted();

        app.delete("a");

        app.todosShouldBeEmpty();
    }

    @Test
    void clearCompletedTodo() {
        app.givenOpenedWith("a", "b", "c", "d", "e");
        app.toggle("a");
        app.toggle("c");
        app.toggle("e");
        app.filterCompleted();

        app.clearCompleted();

        app.todosShouldBeEmpty();
        app.itemsLeftShouldBe(2);
    }
}
