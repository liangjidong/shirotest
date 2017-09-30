package shiro.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shiro.common.Response;
import shiro.common.StatusType;
import shiro.entity.Menu;
import shiro.service.MenuService;

import java.util.List;

/**
 * Created by author on 2017/9/25.
 */
@Controller
@RequestMapping("/menu/")
public class MenuController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuService menuService;

    @RequestMapping("getMenus")
    @ResponseBody
    public Object getMenus() {
        try {
            List<Menu> allMenu = menuService.getAllMenu();
            if (allMenu != null) {
                return new Response(StatusType.SUCCESS.getVal(), StatusType.SUCCESS.getMessage(), allMenu);
            }
            return new Response(-100, "菜单数据为空");
        } catch (Exception e) {
            logger.error("invoke MenuController.getMenus error!", e);
            return new Response(StatusType.EXCEPTION.getVal(), StatusType.EXCEPTION.getMessage());
        }
    }
}
