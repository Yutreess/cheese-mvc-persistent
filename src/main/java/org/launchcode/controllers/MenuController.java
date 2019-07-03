package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value="menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;


    @Autowired
    private CheeseDao cheeseDao;

    // Index
    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("title", "Menu");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }

    // Display Add Menu Form
    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    // Process Add Menu Form
    @RequestMapping(value="add", method = RequestMethod.POST)
    public String add(Model model,
                      @ModelAttribute @Valid  Menu newMenu,
                      Errors errors) {
        // If the form has errors, re-render form
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }

    // View Menu
    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model,
                           @PathVariable int id) {
        model.addAttribute("menu", menuDao.findOne(id));
        model.addAttribute("title", "Menu: " + menuDao.findOne(id).getName());

        return "menu/view";
    }

    // Display Add Menu Item Form
    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model,
                          @PathVariable int menuId) {
        Menu menu = menuDao.findOne(menuId);
        AddMenuItemForm addItemForm = new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("form", addItemForm);
        model.addAttribute("title", "Add item to menu: " +
                                                menuDao.findOne(menuId).getName());

        return "menu/add-item";
    }

    // Process Add Menu Item Form
    @RequestMapping(value="add-item", method = RequestMethod.POST)
    public String addItem(Model model,
                          @ModelAttribute @Valid AddMenuItemForm form,
                          int menuId,
                          int cheeseId,
                          Errors errors) {
        if (errors.hasErrors()) {
            return "menu/add-item/";
        }

        Cheese item = cheeseDao.findOne(cheeseId);
        Menu menu = menuDao.findOne(menuId);
        menu.addItem(item);
        menuDao.save(menuDao.findOne(menuId));

        return "redirect:/menu/view/" + menuId;
    }
}
