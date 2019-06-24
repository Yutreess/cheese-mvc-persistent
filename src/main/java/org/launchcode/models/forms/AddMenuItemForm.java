package org.launchcode.models.forms;


import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;

import javax.validation.constraints.NotNull;

public class AddMenuItemForm {

    // Form Rendering Fields
    private Menu menu;

    private Iterable<Cheese> cheeses;

    // Form Processing Fields
    @NotNull
    private int menuId;

    @NotNull
    private int cheeseId;

    // Constructors
    public AddMenuItemForm() {}

    public AddMenuItemForm(Menu menu,
                           Iterable<Cheese> cheeses) {
        this.menu = menu;
        this.cheeses = cheeses;
    }

    // Getters
    public Menu getMenu() {
        return menu;
    }

    public Iterable<Cheese> getCheeses() {
        return cheeses;
    }

    public int getMenuId() {
        return menuId;
    }

    public int getCheeseId() {
        return cheeseId;
    }
}
