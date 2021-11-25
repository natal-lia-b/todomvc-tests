package todomvctest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import todomvctest.testconfigs.BaseTest;

public class OperationsOnAllFilterTest extends BaseTest {

    private TodoMvcPage todoMvc = new TodoMvcPage();

    @Test
    void addsTodo() {
        todoMvc.givenAppOpened();

        /* WHEN nothing */

        todoMvc.todosShouldBeEmpty();

        /* WHEN */
        todoMvc.add("a");

        todoMvc.todosShouldBe("a");
        todoMvc.itemsLeftShouldBe(1);
    }

    @Test
    void addsTodos() {
        todoMvc.givenAppOpened();

        todoMvc.add("a", "b", "c");

        todoMvc.todosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void editsTodoWithEnter() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.editWith(Keys.ENTER, "b", "b edited");

        todoMvc.todosShouldBe("a", "b edited", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void editsTodoByFocusChange() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.editWith(Keys.TAB, "b", "b edited");

        todoMvc.todosShouldBe("a", "b edited", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void cancelsEditingTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.cancelEditing("b", "b edited");

        todoMvc.todosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void completesTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.toggle("b");

        todoMvc.completedTodosShouldBe("b");
        todoMvc.activeTodosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void completesAllTodos() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBe("a", "b", "c");
        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    void completeAllTodosWithSomeAlreadyCompleted() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBe("a", "b", "c");
        todoMvc.activeTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    void unCompletesTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");

        todoMvc.toggle("b");

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.activeTodosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void unCompletesAllTodos() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggleAll();

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.activeTodosShouldBe("a", "b", "c");
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void deletesTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.delete("b");

        todoMvc.todosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void deletesTodoByEditingToBlank() {
        todoMvc.givenAppOpenedWith("a", "b", "c");

        todoMvc.editWith(Keys.ENTER, "b", " ");

        todoMvc.todosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void deletesCompletedTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");

        todoMvc.delete("b");

        todoMvc.todosShouldBe("a", "c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void deletesLastTodo() {
        todoMvc.givenAppOpenedWith("a");

        todoMvc.delete("a");

        todoMvc.todosShouldBeEmpty();
    }

    @Test
    void clearCompletedTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c", "d", "e");
        todoMvc.toggle("a");
        todoMvc.toggle("c");
        todoMvc.toggle("e");

        todoMvc.clearCompleted();

        todoMvc.todosShouldBe("b", "d");
        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.activeTodosShouldBe("b", "d");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void clearCompletedAllTodos() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggleAll();

        todoMvc.clearCompleted();

        todoMvc.todosShouldBeEmpty();
    }
}
