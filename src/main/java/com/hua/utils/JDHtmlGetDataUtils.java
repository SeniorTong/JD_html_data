package com.hua.utils;

import com.hua.entity.Goods;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SeniorTong丶
 * @Date: 2020/11/5 9:23
 * @Version: 1.0
 */
public class JDHtmlGetDataUtils {

    public static List<Goods> downJDProduct(String keyword) throws IOException, URISyntaxException {

        List<Goods> list = new ArrayList<>();
        //  需要爬取商品信息的网站地址
//        String url = "https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8";
//        HttpGet httpGet = new HttpGet(url);
        //        HttpClient httpclient    = new DefaultHttpClient();

        // 动态模拟请求数据
        CloseableHttpClient httpclient = HttpClients.createDefault();


        URL url = new URL("https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8");
        URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);

        HttpGet httpGet = new HttpGet(uri);

        // 模拟浏览器浏览（user-agent的值可以通过浏览器浏览，查看发出请求的头文件获取）
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        httpclient.execute(httpGet);
        // 获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        try {
            HttpEntity entity = response.getEntity();
            // 如果状态响应码为200，则获取html实体内容或者json文件
            if (statusCode == 200) {
                String html = EntityUtils.toString(entity, Consts.UTF_8);
                // 提取HTML得到商品信息结果
                Document doc = Jsoup.parse(html);
                // 通过浏览器查看商品页面的源代码，找到信息所在的div标签，再对其进行一步一步地解析,这都需要对html代码进行分析了
                Elements ulList = doc.select("#J_goodsList");
                Elements liList = ulList.select(".gl-item");
                // 循环liList的数据（具体获取的数据值还得看doc的页面源代码来获取，可能稍有变动）
                for (Element item : liList) {

                    Goods goods = new Goods();
                    // 商品ID
                    String id = item.attr("data-sku");
                    goods.setGoodsId(id);
                    // 商品名称
                    String name = item.select(".p-name").select("em").text();
                    goods.setGoodsName(name);
                    // 商品价格
                    String price = item.select(".p-price").select("i").text();
                    goods.setGoodsPrice(price);
                    // 商品网址
                    String goodsUrl = item.select(".p-name").select("a").attr("href");
                    goods.setGoodsUrl(goodsUrl);
                    // 商品图片网址
                    String imgUrl = item.select(".p-img").select("a").select("img").attr("data-lazy-img");
                    goods.setGoodsImgUrl(imgUrl);
                    // 商品店铺
                    String goodsShop = item.select(".p-shop").select("span").select("a").attr("title");

                    System.out.println(goodsUrl);
                    goods.setGoodsShop(goodsShop);
                    list.add(goods);
                    break;
                }
                // 消耗掉实体
                EntityUtils.consume(response.getEntity());
            } else {
                // 消耗掉实体
                EntityUtils.consume(response.getEntity());
            }
        } finally {
            response.close();
        }

        return list;
    }


    public static void downJDProductCe(String keyword) throws IOException {

        //  需要爬取商品信息的网站地址
        String url = "https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8";
        HttpGet httpGet = new HttpGet(url);
        // 动态模拟请求数据

        CloseableHttpClient httpclient = HttpClients.createDefault();

        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        httpclient.execute(httpGet);
        // 获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        try {
            HttpEntity entity = response.getEntity();
            // 如果状态响应码为200，则获取html实体内容或者json文件
            if (statusCode == 200) {
                String html = EntityUtils.toString(entity, Consts.UTF_8);
                // 提取HTML得到商品信息结果
                Document doc = Jsoup.parse(html);
                System.out.println(doc);
                // 通过浏览器查看商品页面的源代码，找到信息所在的div标签，再对其进行一步一步地解析,这都需要对html代码进行分析了
                Elements ulList = doc.select("#J_goodsList");
                Elements liList = ulList.select(".gl-item");
                // 循环liList的数据（具体获取的数据值还得看doc的页面源代码来获取，可能稍有变动）
                for (Element item : liList) {

                    Goods goods = new Goods();
                    // 商品ID
                    String id = item.attr("data-sku");
                    goods.setGoodsId(id);
                    // 商品名称
                    String name = item.select(".p-name").select("em").text();
                    goods.setGoodsName(name);
                    // 商品价格
                    String price = item.select(".p-price").select("i").text();
                    goods.setGoodsPrice(price);
                    // 商品网址
                    String goodsUrl = item.select(".p-name").select("a").attr("href");
                    goods.setGoodsUrl(goodsUrl);
                    // 商品图片网址
                    String imgUrl = item.select(".p-img").select("a").select("img").attr("data-lazy-img");
                    goods.setGoodsImgUrl(imgUrl);
                    // 商品店铺
                    String goodsShop = item.select(".p-shop").select("span").select("a").attr("title");

                    System.out.println(goodsUrl);
                    goods.setGoodsShop(goodsShop);
                }
                // 消耗掉实体
                EntityUtils.consume(response.getEntity());
            } else {
                // 消耗掉实体
                EntityUtils.consume(response.getEntity());
            }
        } finally {
            response.close();
        }
    }


    public static void downJDProductCeShi(String keyword) throws IOException {

        //  需要爬取商品信息的网站地址
        String url = "https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8";
        // 提取HTML得到商品信息结果
//        Document doc = Jsoup.connect(url).get();
        Document doc = Jsoup.parse(new URL(url), 30000);
        System.out.println(doc);
        Elements ulList = doc.select("#J_goodsList");
        Elements liList = ulList.select(".gl-item");

        for (Element item : liList) {

            // 商品ID
            String id = item.attr("data-sku");
            // 商品名称
            String name = item.select(".p-name").select("em").text();
            // 商品价格
            String price = item.select(".p-price").select("i").text();
            // 商品网址
            String goodsUrl = item.select(".p-name").select("a").attr("href");
            // 商品图片网址
            String imgUrl = item.select(".p-img").select("a").select("img").attr("data-lazy-img");
            // 商品店铺
            String goodsShop = item.select(".p-shop").select("span").select("a").attr("title");

            System.out.println(goodsUrl);
        }
    }

    public static void main(String[] args) throws IOException {

        downJDProductCe("java");
//        downJDProductCeShi("java");
    }

}
