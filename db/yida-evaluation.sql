-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Table `yde_evaluation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `yde_evaluation` (
  `id` CHAR(1) NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(64) NULL COMMENT '新建者',
  `create_date` DATETIME NULL COMMENT '新建时间',
  `update_by` VARCHAR(64) NULL COMMENT '更新者',
  `update_date` DATETIME NULL COMMENT '更新时间',
  `status` CHAR(1) NULL COMMENT '状态',
  `remarks` VARCHAR(500) NULL,
  `name` VARCHAR(100) NULL COMMENT '测评名称',
  `module_id` BIGINT NULL COMMENT '所属测评模块',
  `duration` INT NULL COMMENT '测评时长',
  `background` VARCHAR(500) NULL COMMENT '测评背景内容',
  `is_part` TINYINT NULL COMMENT '是否分部分',
  `is_limited_once` TINYINT NULL,
  `is_enabled` TINYINT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  COMMENT = '测评设置';


-- -----------------------------------------------------
-- Table `yde_module`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `yde_module` (
  `id` CHAR(1) NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(64) NULL COMMENT '新建者',
  `create_date` DATETIME NULL COMMENT '新建时间',
  `update_by` VARCHAR(64) NULL COMMENT '更新者',
  `update_date` DATETIME NULL COMMENT '更新时间',
  `status` CHAR(1) NULL COMMENT '状态',
  `remarks` VARCHAR(500) NULL,
  `module_name` VARCHAR(100) NULL COMMENT '测评模块',
  `start_time` DATETIME NULL COMMENT '开始时间',
  `end_time` DATETIME NULL COMMENT '结束时间',
  `is_evaluation_limited` TINYINT NULL COMMENT '测评套题限制',
  `introduction` VARCHAR(500) NULL COMMENT '测评模块介绍',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  COMMENT = '测评模块';;


-- -----------------------------------------------------
-- Table `yde_question`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `yde_question` (
  `id` CHAR(1) NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(64) NULL COMMENT '新建者',
  `create_date` DATETIME NULL COMMENT '新建时间',
  `update_by` VARCHAR(64) NULL COMMENT '更新者',
  `update_date` DATETIME NULL COMMENT '更新时间',
  `status` CHAR(1) NULL COMMENT '状态',
  `remarks` VARCHAR(500) NULL,
  `part_id` BIGINT NULL COMMENT '所属部分',
  `no` INT NULL COMMENT '题号',
  `title` VARCHAR(100) NULL COMMENT '题目',
  `option_type` TINYINT NULL COMMENT '选项类型 1:选项 2:填写',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  COMMENT = '题库设置';;


-- -----------------------------------------------------
-- Table `yde_rule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `yde_rule` (
  `id` CHAR(1) NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(64) NULL COMMENT '新建者',
  `create_date` DATETIME NULL COMMENT '新建时间',
  `update_by` VARCHAR(64) NULL COMMENT '更新者',
  `update_date` DATETIME NULL COMMENT '更新时间',
  `status` CHAR(1) NULL COMMENT '状态',
  `remarks` VARCHAR(500) NULL,
  `rule_type` INT NULL COMMENT '规则类型 1: 叠加统计 2: 分批统计',
  `result_type` INT NULL COMMENT '结果展现 1: 文字描述 2: 饼图 3: 雷达图 4: 仪表盘',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  COMMENT = '评分规则';


-- -----------------------------------------------------
-- Table `yde_layout`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `yde_layout` (
  `id` CHAR(1) NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(64) NULL COMMENT '新建者',
  `create_date` DATETIME NULL COMMENT '新建时间',
  `update_by` VARCHAR(64) NULL COMMENT '更新者',
  `update_date` DATETIME NULL COMMENT '更新时间',
  `status` CHAR(1) NULL COMMENT '状态',
  `remarks` VARCHAR(500) NULL,
  `layout_type` INT NULL COMMENT '版式类型',
  `module_name_1` VARCHAR(50) NULL,
  `module_name_2` VARCHAR(50) NULL,
  `module_name_3` VARCHAR(50) NULL,
  `module_name_4` VARCHAR(50) NULL,
  `module_name_5` VARCHAR(50) NULL,
  `module_name_6` VARCHAR(50) NULL,
  `module_name_7` VARCHAR(50) NULL,
  `module_name_8` VARCHAR(50) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  COMMENT = '展示模式';


-- -----------------------------------------------------
-- Table `yde_part`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `yde_part` (
  `id` CHAR(1) NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(64) NULL COMMENT '新建者',
  `create_date` DATETIME NULL COMMENT '新建时间',
  `update_by` VARCHAR(64) NULL COMMENT '更新者',
  `update_date` DATETIME NULL COMMENT '更新时间',
  `status` CHAR(1) NULL COMMENT '状态',
  `remarks` VARCHAR(500) NULL COMMENT '背景内容',
  `section` VARCHAR(45) NULL COMMENT '部分名称（第一部分，第二部分）',
  `content` VARCHAR(255) NULL,
  `evaluation_id` INT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  COMMENT = '部分';

-- -----------------------------------------------------
-- Table `yde_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `yde_option` (
  `id` CHAR(1) NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(64) NULL COMMENT '新建者',
  `create_date` DATETIME NULL COMMENT '新建时间',
  `update_by` VARCHAR(64) NULL COMMENT '更新者',
  `update_date` DATETIME NULL COMMENT '更新时间',
  `status` CHAR(1) NULL COMMENT '状态',
  `remarks` VARCHAR(500) NULL,
  `question_id` BIGINT NULL COMMENT '题目ID',
  `points` INT NULL COMMENT '分值',
  `option_name` INT NULL COMMENT '选项名称，例如A，B…',
  `option_type` INT NULL COMMENT '答案类型(1: 选项，2:填写)',
  `content` VARCHAR(255) NULL COMMENT '选项内容，或者标准答案',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  COMMENT = '选项';


-- -----------------------------------------------------
-- Table `yds_module_evaluation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `yds_module_evaluation` (
  `id` BIGINT NOT NULL,
  `module_id` BIGINT NULL COMMENT '模块ID',
  `evaluation_id` BIGINT NULL COMMENT '测评ID',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

create or replace view v_evaluation_modules as
 select a.user_id as user_id, count(distinct b.id) as evaluation_modules  from yde_evaluation_record a, yde_evaluation b
where b.is_enabled and b.status='0' and a.evaluation_id=b.id group by a.user_id;

create or replace view v_evaluation_times as
  select a.user_id as user_id, count(a.evaluation_id) as evaluation_times  from yde_evaluation_record a, yde_evaluation b where b.is_enabled and b.status='0' and a.evaluation_id=b.id  group by a.user_id;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
