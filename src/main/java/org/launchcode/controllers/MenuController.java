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

    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("title", "Menu");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String add(Model model,
                      @Valid  Menu newMenu,
                      Errors errors) {
        // If the form has errors, re-render form
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model,
                           @PathVariable int id) {
        model.addAttribute("menu", menuDao.findOne(id));
        model.addAttribute("title", "Menu: " + menuDao.findOne(id).getName());

        return "menu/view";
    }

    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model,
                          @PathVariable int menuId) {
        //model.addAttribute("menu", menuDao.findOne(menuId));
        model.addAttribute("form", new AddMenuItemForm(
                                                    menuDao.findOne(menuId),
                                                    cheeseDao.findAll()
        ));
        model.addAttribute("title", "Add item to menu: " +
                                                menuDao.findOne(menuId).getName());

        return "menu/add-item";
    }

    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.POST)
    public String addItem(Model model,
                          @Valid  AddMenuItemForm newMenuItem,
                          @PathVariable int menuId,
                          Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add item to menu: " +
                    menuDao.findOne(menuId).getName());
            return "menu/add-item/" + menuId;
        }

        Cheese item = cheeseDao.findOne(newMenuItem.getCheeseId());
        menuDao.findOne(menuId).addItem(item);

        menuDao.save(menuDao.findOne(menuId));

        return "redirect:../view/" + menuId;
    }
}
