package shiro.service;

import org.springframework.stereotype.Service;
import shiro.dao.MenuDao;
import shiro.entity.Menu;

import java.util.List;

/**
 * Created by author on 2017/9/25.
 */
public class MenuServiceImpl implements MenuService {
    private MenuDao menuDao;

    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    public List<Menu> getAllMenu() {
        return menuDao.selectAll();
    }
}
