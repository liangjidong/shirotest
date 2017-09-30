package shiro.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import shiro.entity.Menu;

import java.util.List;

/**
 * Created by author on 2017/9/25.
 */
public class MenuDaoImpl extends JdbcDaoSupport implements MenuDao {

    @Override
    public List<Menu> selectAll() {
        String sql = "select * from sys_menu";
        List<Menu> menus = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Menu.class));
        if (menus.size() == 0)
            return null;
        return menus;
    }
}
