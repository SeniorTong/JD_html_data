package com.hua.controller;

import com.hua.entity.Goods;
import com.hua.utils.ExcelUtils;
import com.hua.utils.JDHtmlGetDataUtils;
import com.hua.utils.MultipartFileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.util.*;

/**
 * @Author: SeniorTong丶
 * @Date: 2020/11/5 10:44
 * @Version: 1.0
 */
@Controller
public class indexConteoller {

    /**
     *   返回页面
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     *   获取 文件中的数据
     * @param column
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @PostMapping("/getFile")
    @ResponseBody
    public Map<String, Object> getFile(@RequestParam("column") Integer column,
                                       @RequestParam("file") MultipartFile multipartFile) throws Exception {

        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        if (!multipartFile.isEmpty()) {
            File file = MultipartFileUtils.transferToFile(multipartFile);

            list = ExcelUtils.readColumn(file, column - 1);
        }

        map.put("fileName", multipartFile.getOriginalFilename());
        map.put("list", list);
        return map;
    }

    /**
     *    爬取数据后 返回数据
     * @param strList
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/exportInfo")
    public void exportInfo(@RequestParam("fileName") String fileName,
                           @RequestParam("strList") String strList,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {

        String[] filename = fileName.split("\\.");

        String agent = request.getHeader("USER-AGENT").toLowerCase();
        response.setContentType("application/vnd.ms-excel");

        String codedFileName = java.net.URLEncoder.encode("test.xsl", "UTF-8");
        if (agent.contains("firefox")) {
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-disposition",
                    "attachment;filename=" + new String(filename[0].getBytes(), "ISO8859-1") + ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }

        String[] title = {"序号", "商品ID", "商品名称", "商品价格", "商品网址", "商品图片网址", "商品店铺名称"};

        String[] split = strList.split(",");
        List<String> list = Arrays.asList(split);
        String[][] content = new String[list.size()][title.length];

        int i = 0;
        for (String name : list) {

            if (!StringUtil.isBlank(name)) {
                //  只获取第一个商品信息
                System.out.println("--------------开始爬取------------------");
                List<Goods> listGoods = JDHtmlGetDataUtils.downJDProduct(name);
                //每次请求完延时3秒
                Thread.sleep(3000);
                Goods goods = listGoods.get(0);
                content[i][0] = (i + 1) + "";
                content[i][1] = goods.getGoodsId();
                content[i][2] = goods.getGoodsName();
                content[i][3] = goods.getGoodsPrice();
                content[i][4] = goods.getGoodsUrl();
                content[i][5] = goods.getGoodsImgUrl();
                content[i][6] = goods.getGoodsShop();
                System.out.println("--------------爬取结束------------------");
            } else {
                content[i][0] = (i + 1) + "";
                content[i][1] = "";
                content[i][2] = "";
                content[i][3] = "";
                content[i][4] = "";
                content[i][5] = "";
                content[i][6] = "";
            }
            i++;
        }
        // 将数据装载到excel中
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(filename[0], title, content, null);

        //  流写入到文件
        OutputStream ouputStream = null;
        try {
            System.out.println("--------文件输出----------");
            ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
            System.out.println("close------------------------");
        } catch (Exception e) {
            System.out.println("catch------------------------");
            throw new RuntimeException("系统异常");
        }

    }

}
