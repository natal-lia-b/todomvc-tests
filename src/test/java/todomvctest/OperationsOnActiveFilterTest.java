package todomvctest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import todomvctest.testconfigs.BaseTest;

public class OperationsOnActiveFilterTest extends BaseTest {

    private TodoMvcPage todoMvc = new TodoMvcPage();

    @Test
    void addsTodos() {
        todoMvc.givenAppOpened();
        todoMvc.add("a");
        todoMvc.filterActive();

        todoMvc.add("b", "c");

        todoMvc.activeTodosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void editsTodoWithEnter() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.editWith(Keys.ENTER, "b", "b edited");

        todoMvc.activeTodosShouldBe("a", "b edited", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void editsTodoByFocusChange() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.editWith(Keys.TAB, "b", "b edited");

        todoMvc.activeTodosShouldBe("a", "b edited", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void cancelsEditingTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.cancelEditing("b", "b edited");

        todoMvc.activeTodosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void completesTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.toggle("b");

        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void completesAllTodos() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.toggleAll();

        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    void completeAllTodosWithSomeAlreadyCompleted() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.filterActive();

        todoMvc.toggleAll();

        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    void deletesTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.filterActive();

        todoMvc.delete("b");

        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void deletesLastTodo() {
        todoMvc.givenAppOpenedWith("a");
        todoMvc.filterActive();

        todoMvc.delete("a");

        todoMvc.todosShouldBeEmpty();
    }

    @Test
    void deletesLastActiveTodo() {
        todoMvc.givenAppOpenedWith("a", "b");
        todoMvc.toggle("a");
        todoMvc.filterActive();

        todoMvc.delete("b");

        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }
}
