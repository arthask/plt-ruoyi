package com.example.pltool.util.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 快速生成
 * </p>
 *
 * @author lanjerry
 * @since 2021-09-16
 */
public class FastAutoGeneratorTest extends BaseGeneratorTest {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/ry_vue?serverTimezone=Asia/Shanghai",
            "root",
            "202312");

    /**
     * 执行 run
     */
    public static void main(String[] args) {
        //基本信息
        String parent = "com.ruoyi.system";   //父包名
        String module = "gencode";   //模块包名

        //数据库表的设置
        List<String> listTable = Arrays.asList("expression","expression_detail","expression_detail_ref");  //设置需要自动代码生成的表名
        List<String> listTableSuffix = Arrays.asList("_b");    //设置 过滤 表的后缀
        List<String> listTablePrefix = Arrays.asList("t_","c_"); //设置 过滤 表的后缀
        // 初始化数据库脚本
//        initDataSource(DATA_SOURCE_CONFIG.build());
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 数据库配置
//            .dataSourceConfig((scanner, builder) -> builder.schema(scanner.apply("ry_vue")))
                // 全局配置
                .globalConfig((scanner, builder) -> builder
                        .outputDir(System.getProperty("user.dir") + "/ruoyi-system/src/main/java")
                        .author("author")
//                        .enableKotlin()
//                        .enableSwagger()
                        .dateType(DateType.TIME_PACK)
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir())
                // 包配置
                .packageConfig((scanner, builder) ->  builder.parent(parent) // 设置父包名
                        .moduleName(module)   //设置模块包名
                        .entity("entity")   //pojo 实体类包名
                        .service("service") //Service 包名
                        .serviceImpl("service.impl") // ***ServiceImpl 包名
                        .mapper("mapper")   //Mapper 包名
                        .xml("mapper.xml")  //Mapper XML 包名
                        //配置 mapper.xml 路径信息：项目的 resources 目录下)
                        .pathInfo(Collections.singletonMap(OutputFile.mapper.xml,
                                System.getProperty("user.dir")+"/ruoyi-system/src/main/resources/mapper/gen")))
                // 策略配置
                .strategyConfig(builder -> {
                    builder
                            .enableCapitalMode()    //开启大写命名
                            .enableSkipView()   //创建实体类的时候跳过视图
                            .addInclude(listTable) // 设置需要生成的数据表名
//                            .addTableSuffix(listTableSuffix) //设置 过滤 表的后缀
//                            .addTablePrefix(listTablePrefix) // 设置 过滤 表的前缀

                            //4.1、实体类策略配置
                            .entityBuilder()
                            .enableChainModel() //开启链式模型
                            //.disableSerialVersionUID()  //默认是开启实体类序列化，可以手动disable使它不序列化。由于项目中需要使用序列化就按照默认开启了
                            .enableTableFieldAnnotation()       // 开启生成实体时生成字段注解
                            .enableLombok()
                            .versionColumnName("version")   //乐观锁字段名(数据库)
                            .versionPropertyName("version") //乐观锁属性名(实体)
                            .logicDeleteColumnName("deleted")   //逻辑删除字段名(数据库)
                            .logicDeletePropertyName("deleteFlag")  //逻辑删除属性名(实体)
                            .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：默认是下划线转驼峰命。这里可以不设置
                            .columnNaming(NamingStrategy.underline_to_camel)    //数据库表字段映射到实体的命名策略：下划线转驼峰命。（默认是和naming一致，所以也可以不设置）
//                            .addTableFills(
//                                    new Column("create_time", FieldFill.INSERT),
//                                    new Column("modify_time", FieldFill.INSERT_UPDATE)
//                            )   //添加表字段填充，"create_time"字段自动填充为插入时间，"modify_time"字段自动填充为插入修改时间
                            .idType(IdType.AUTO)//设置主键自增
                            .enableFileOverride()

                            //4.2、Controller策略配置
//                            .controllerBuilder()
//                            .enableHyphenStyle()    //开启驼峰连转字符
//                            .formatFileName("%sController") //格式化 Controller 类文件名称，%s进行匹配表名，如 UserController
//                            .enableRestStyle()  //开启生成 @RestController 控制器

                            //4.3、service 策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService") //格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                            .formatServiceImplFileName("%sServiceImpl") //格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl
                            .enableFileOverride()
                            //4.4、Mapper策略配置
                            .mapperBuilder()
                            .enableFileOverride()
                            .superClass(BaseMapper.class)   //设置父类
                            .enableBaseResultMap()  //启用 BaseResultMap 生成
                            .enableBaseColumnList() //启用 BaseColumnList
                            .formatMapperFileName("%sMapper")   //格式化 mapper 文件名称
                            .enableMapperAnnotation()       //开启 @Mapper 注解
                            .formatXmlFileName("%s") //格式化Xml文件名称
                            .formatMapperFileName("%sMapper");   //格式化Mapper文件名称


                })
                //5、模板
                .templateEngine(new VelocityTemplateEngine())
                .templateEngine(new FreemarkerTemplateEngine())
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker 或 Enjoy
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                   .templateEngine(new EnjoyTemplateEngine())
                 */
                .execute();
    }
}