-- 创建无限级菜单的table
CREATE TABLE sys_menu (
  id     INT         NOT NULL  AUTO_INCREMENT,
  name   VARCHAR(64) NOT NULL,
  url    VARCHAR(255),
  pid    INT         NOT NULL  DEFAULT 0,
  isLeaf TINYINT     NOT NULL  DEFAULT 1
  COMMENT '0表示不是叶子，1表示是叶子',
  status TINYINT     NOT NULL  DEFAULT 1
  COMMENT '0表示禁用，1表示启用',
  seq    TINYINT     NOT NULL  DEFAULT 0
  COMMENT '同级中的顺序，0-n，从上到下',
  PRIMARY KEY (id)
);