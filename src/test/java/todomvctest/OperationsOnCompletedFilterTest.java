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

        app.editWith(Keys.ENTER, "a", "a edited");

        app.completedTodosShouldBe("a edited", "c");
        app.itemsLeftShouldBe(1);
    }

    @Test
    void activatingOneOfSeveralCompleted() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("a");
        app.toggle("c");
        app.filterCompleted();

        app.toggle("c");

        app.completedTodosShouldBe("a");
        app.itemsLeftShouldBe(2);
    }

    @Test
    void activatingLastCompleted() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("b");
        app.filterCompleted();

        app.toggle("b");

        app.completedTodosShouldBeEmpty();
        app.clearCompletedShouldBe(hidden);
    }

    @Test
    void completeAllTodosWithSomeAlreadyCompleted() {
        app.givenOpenedWith("a", "b", "c", "d", "e");
        app.toggle("a");
        app.toggle("c");
        app.toggle("e");
        app.filterCompleted();

        app.toggleAll();

        app.todosShouldBe("a", "b", "c", "d", "e");
        app.itemsLeftShouldBe(0);
    }

    @Test
    void unCompletingTodo() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("b");
        app.filterCompleted();

        app.toggle("b");

        app.completedTodosShouldBeEmpty();
        app.itemsLeftShouldBe(3);
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

        app.completedTodosShouldBeEmpty();
        app.itemsLeftShouldBe(2);
    }
}
