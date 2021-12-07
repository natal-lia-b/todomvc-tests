package todomvctest;

import org.junit.jupiter.api.Test;
import todomvctest.model.TodoMvc;
import todomvctest.testconfigs.BaseTest;

public class CommonUserWorkflowsTest extends BaseTest {

    private TodoMvc app = new TodoMvc();

    @Test
    public void todoCrudManagement() {
        app.givenOpenedWith("a", "b", "c");
        app.todosShouldBe("a", "b", "c");

        app.edit("b", "b edited");

        app.toggle("b edited");
        app.clearCompleted();
        app.todosShouldBe("a", "c");

        app.cancelEditing("c", "c to be canceled");

        app.delete("c");
        app.todosShouldBe("a");
    }

    @Test
    void filtersTodos() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("b");

        app.filterActive();
        app.todosShouldBe("a", "c");

        app.filterCompleted();
        app.todosShouldBe("b");

        app.filterAll();
        app.todosShouldBe("a", "b", "c");
    }
}