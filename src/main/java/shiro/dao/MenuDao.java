package shiro.dao;

import shiro.entity.Menu;

import java.util.List;

/**
 * Created by author on 2017/9/25.
 */
public interface MenuDao {
    List<Menu> selectAll();
}
